package rothkoff.baruch.cars.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.controller.B;
import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.view.MainActivity;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class MyUploadService extends Service {

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    /**
     * Intent Extras
     **/
    public static final String EXTRA_CAR = "extra_car";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String EXTRA_DETAILS = "extra_details";
    private static final String TAG = "MyUploadService";
    private static final int NOTIF_ID_DOWNLOAD = 0;
    private int mNumTasks = 0;
    // [START declare_ref]
    private StorageReference mStorageRef;
    // [END declare_ref]

    /*public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            String details = intent.getStringExtra(EXTRA_DETAILS);
            Car car = intent.getParcelableExtra(EXTRA_CAR);
            uploadFromUri(fileUri, details, car);
        }

        return START_REDELIVER_INTENT;
    }
    // [END upload_from_uri]

    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri, final String details, final Car car) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // [START_EXCLUDE]
        taskStarted();
        showUploadProgressNotification();
        // [END_EXCLUDE]

        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference photoRef = mStorageRef.child(B.Keys.IMAGES)
                .child(fileUri.getLastPathSegment());
        // [END get_child_ref]

        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri:onSuccess");

                        // Get the public download URL
                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                        // [START_EXCLUDE]
                        broadcastUploadFinished(downloadUri, fileUri);
                        showUploadFinishedNotification(downloadUri, fileUri);
                        car.addPhoto(
                                fileUri.getLastPathSegment().substring(0, fileUri.getLastPathSegment().length() - 4),
                                details);
                        //photosAdapter.addPhoto(photoName, NO_DETAILS);
                        taskCompleted();
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        // [START_EXCLUDE]
                        broadcastUploadFinished(null, fileUri);
                        showUploadFinishedNotification(null, fileUri);
                        taskCompleted();
                        // [END_EXCLUDE]
                    }
                });
    }

    /**
     * Broadcast finished upload (success or failure).
     *
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        // Make Intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Make PendingIntent for notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Set message and icon based on success or failure
        boolean success = downloadUrl != null;
        String message = success ? "Upload finished" : "Upload failed";
        int icon = success ? R.drawable.ic_check : R.drawable.ic_error;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD, builder.build());
    }

    /**
     * Show notification with an indeterminate upload progress bar.
     */
    private void showUploadProgressNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_file_upload)
                .setContentTitle(getString(R.string.upload_photo))
                .setContentText("Uploading...")
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD, builder.build());
    }

    public void taskStarted() {
        changeNumberOfTasks(1);
    }

    public void taskCompleted() {
        changeNumberOfTasks(-1);
    }

    private synchronized void changeNumberOfTasks(int delta) {
        Log.d(TAG, "changeNumberOfTasks:" + mNumTasks + ":" + delta);
        mNumTasks += delta;

        // If there are no tasks left, stop the service
        if (mNumTasks <= 0) {
            Log.d(TAG, "stopping");
            stopSelf();
        }
    }
}

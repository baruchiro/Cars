package rothkoff.baruch.cars.view;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.controller.B;
import rothkoff.baruch.cars.model.Car;

/**
 * Created by baruc on 01/12/2016.
 */

public class UploadService extends IntentService {

    public static final String EXTRA_URI = "UploadService.ExtraURI";
    public static final String EXTRA_CAR = "UploadService.ExtraCAR";

    public UploadService(){
        super("UploadService");
    }
    public UploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri f = Uri.fromFile(new File(intent.getStringExtra(EXTRA_URI)));
                //new File(URI.create());
        Car c = (Car) intent.getSerializableExtra(EXTRA_CAR);
        SavePhoto(f,c);

    }

    public void SavePhoto(Uri file, final Car car) {
                final String photoName = file.getLastPathSegment().substring(0, file.getLastPathSegment().length() - 4);

        StorageReference photoRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(B.Constants.FIREBASE_STORAGE_URL)
                .child(B.Keys.IMAGES).child(file.getLastPathSegment());

        UploadTask uploadTask = photoRef.putFile(file);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getString(R.string.upload_photo));
        builder.setContentText("gggggg");
        builder.setSmallIcon(R.drawable.com_facebook_button_icon);
        builder.setProgress(0,0,false);

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mNotificationManager.notify(5543,builder.build());

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.error_when_updating_details, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override//H
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();

                car.addPhoto(photoName, "no");

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                builder.setProgress((int) taskSnapshot.getTotalByteCount(),
                        (int) taskSnapshot.getBytesTransferred(),
                        false);
                mNotificationManager.notify(5543,builder.build());
            }
        });
    }

    private void Noti(){

    }
}

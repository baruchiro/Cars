package rothkoff.baruch.cars;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView tvDetails;
    private ProgressBar progressBar;
    private TextView tvNotFound;
    private File photoFile;
    private Context context;

    public PhotoViewHolder(View itemView) {
        super(itemView);

        this.imageView = (ImageView) itemView.findViewById(R.id.item_photo_imageview);
        this.tvDetails = (TextView) itemView.findViewById(R.id.item_photo_details);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.item_photo_progress);
        this.tvNotFound = (TextView) itemView.findViewById(R.id.item_photo_nophoto);
    }

    public void Init(Context context, StorageReference storageReference, String details) {
        Log.d(MainActivity.LOG_NAME, "PhotoViewHolder Init reference=" + storageReference.toString());

        this.context = context;

        File storageDir = context.getDir("Pictures", 0);
        storageDir = getStorageDir();
        photoFile = new File(Environment.getExternalStorageDirectory().toString(), storageReference.getName());
        Log.d(MainActivity.LOG_NAME, "PhotoViewHolder Init photoFile=" + photoFile.getAbsolutePath());

        photoFile = new File(storageDir, storageReference.getName());
        Log.d(MainActivity.LOG_NAME, "PhotoViewHolder Init photoFile=" + photoFile.getAbsolutePath());

        if (photoFile.exists()) {
            Log.i(MainActivity.LOG_NAME, "Photo exists. path: " + photoFile.getAbsolutePath());
            ShowPhoto();
        }
        else {
            Log.i(MainActivity.LOG_NAME, "Photo NOT exists. path: " + photoFile.getAbsolutePath());
            storageReference.getFile(photoFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.i(MainActivity.LOG_NAME, "Success Download photo: " + photoFile.getAbsolutePath());
                            ShowPhoto();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(MainActivity.LOG_NAME, "ERROR Download photo: " + photoFile.getAbsolutePath());
                            Log.e(MainActivity.LOG_NAME, e.getMessage());
                            NoPhoto();
                        }
                    });
        }

        tvDetails.setText(details);
    }

    private File getStorageDir() {
        Log.d(MainActivity.LOG_NAME + "KITKAT", android.os.Build.VERSION.SDK_INT + "--" + android.os.Build.VERSION_CODES.KITKAT);
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT) {
            Log.d(MainActivity.LOG_NAME + "KITKAT", "getStorageDir KITKAT");

            return context.getDir("Pictures", 0);


        } else {
            Log.d(MainActivity.LOG_NAME, "getStorageDir NOT KITKAT");
            //return context.getDir("Pictures",0);
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
    }

    private void ShowPhoto() {
        if (photoFile.exists()) {
            Glide.with(imageView.getContext())
                    .load(photoFile)
                    .crossFade()
                    .into(imageView);

            /*Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);*/

            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
        } else NoPhoto();
    }

    private void NoPhoto() {
        photoFile.delete();
        progressBar.setVisibility(View.GONE);
        tvNotFound.setVisibility(View.VISIBLE);
    }
}

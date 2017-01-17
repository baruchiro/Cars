package rothkoff.baruch.cars;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class PhotoHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView tvDetails;
    private ProgressBar progressBar;
    private TextView tvNotFound;
    private File photoFile;
    private Context context;

    public PhotoHolder(View itemView) {
        super(itemView);

        this.imageView = (ImageView) itemView.findViewById(R.id.item_photo_imageview);
        this.tvDetails = (TextView) itemView.findViewById(R.id.item_photo_details);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.item_photo_progress);
        this.tvNotFound = (TextView) itemView.findViewById(R.id.item_photo_nophoto);
    }

    public void Init(Context context, StorageReference storageReference, String details) {

        this.context = context;

        File storageDir = context.getDir("Pictures", 0);
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        photoFile = new File(Environment.getExternalStorageDirectory(), storageReference.getName());
        photoFile = new File(storageDir, storageReference.getName());

        if (photoFile.exists())
            ShowPhoto();

        else {
            storageReference.getFile(photoFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            ShowPhoto();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            NoPhoto();
                        }
                    });
        }

        tvDetails.setText(details);
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

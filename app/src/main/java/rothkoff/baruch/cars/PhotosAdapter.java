package rothkoff.baruch.cars;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotosAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private Context context;
    private List<String> details;
    private List<String> photos;
    private List<View> views;

    public PhotosAdapter(Context context, Map<String, String> photos) {
        this.context = context;
        this.views = new ArrayList<>();

        if (photos != null) {
            this.details = new ArrayList<>(photos.values());
            this.photos = new ArrayList<>();
            for (Map.Entry<String, String> entry : photos.entrySet())
                this.photos.add(entry.getKey());
        } else {
            this.details = new ArrayList<>();
            this.photos = new ArrayList<>();
        }

        Log.i(MainActivity.LOG_NAME, String.valueOf("PhotosAdapter constructor. size: "
                + (photos == null ? null : photos.size())));
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        views.add(itemView);
        Log.i(MainActivity.LOG_NAME, "PhotosAdapter onCreateViewHolder views.size= " + views.size());
        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReferenceFromUrl(B.Constants.FIREBASE_STORAGE_URL)
                .child("images")
                .child(photos.get(position) + ".jpg");

        holder.Init(context, reference, details.get(position));

        Log.i(MainActivity.LOG_NAME, "PhotoAdapter onBindViewHolder position= " + position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    public void addPhoto(String photoName, String details) {
        Log.i(MainActivity.LOG_NAME, "PhotosAdapter addPhoto name:" + photoName + " details:" + details);
        if (!this.photos.contains(photoName)) {
            this.details.add(details);
            this.photos.add(photoName);

            notifyDataSetChanged();
        }
    }

    public List<View> getViews() {
        return views;
    }

    //public List<View> getAllViews() {
    //    return views;
    //}
}

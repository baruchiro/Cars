package rothkoff.baruch.cars;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendPicturesFragment extends MyFragment implements View.OnClickListener {

    private final int REQUEST_IMAGE_CAPTURE = 546;
    private File currentPhotoFile;
    private List<Uri> photos;

    private LinearLayout llPictures;
    private Button btnTake;
    private Button btnSend;

    public SendPicturesFragment() {
        // Required empty public constructor
    }

    public static SendPicturesFragment newInstance() {
        SendPicturesFragment fragment = new SendPicturesFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_pictures, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        llPictures = (LinearLayout)view.findViewById(R.id.frag_sendpictures_linearpictures);
        btnTake = (Button)view.findViewById(R.id.frag_sendpictures_btn_take);
        btnSend = (Button)view.findViewById(R.id.frag_sendpictures_btn_send);

        currentPhotoFile = null;
        photos = new ArrayList<>();
    }

    private void BehaviorMembers() {
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btnSend.setOnClickListener(this);
    }
    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.send_pictures);
    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_sendpictures).setChecked(true);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            currentPhotoFile = null;
            try {
                createImageFile();
            }catch (IOException ex){

            }

            if (currentPhotoFile !=null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),"rothkoff.baruch.cars",currentPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == RESULT_OK){
            addPicture();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addPicture() {
        Uri u = FileProvider.getUriForFile(getContext(),
                "rothkoff.baruch.cars",
                currentPhotoFile);

        photos.add(u);

        ImageView imageView = new ImageView(getContext());
        imageView.setImageURI(u);
        llPictures.addView(imageView);
    }

    private void createImageFile() throws IOException{
        String timeStemp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStemp+"_";
        File stogageDir =getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        currentPhotoFile = File.createTempFile(imageFileName,".jpg",stogageDir);
    }

    @Override
    public void onClick(View view) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(B.Constants.FIREBASE_STORAGE_URL).child(B.Keys.IMAGES);

        for (Uri u : photos){
            StorageReference sr = storageRef.child(u.getLastPathSegment());
            UploadTask uploadTask = sr.putFile(u);
        }
    }
}

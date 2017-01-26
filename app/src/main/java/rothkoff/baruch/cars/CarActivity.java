package rothkoff.baruch.cars;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class CarActivity extends AppCompatActivity
        implements ValueEventListener, ChildEventListener {

    public static final String EXTRA_RENT = "rothkoff.baruch.cars.CarActivity.rent";
    public static final String EXTRA_CAR = "rothkoff.baruch.cars.CarActivity.Car";
    private static final int REQUEST_TAKE_PHOTO = 22;
    private static int pageNumber = 1;
    private String NO_DETAILS;
    private ProgressDialog progressDialog;
    private Car car;
    private Rent rent;
    private LinearLayout content;
    private Button btnAddPhoto;
    private Button btnShowSign;
    private Button btnActivate;
    private Button btnClearSign;
    private RecyclerView photosRecycler;
    private PhotosAdapter photosAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout llDetailCard;
    private LinearLayout llSignArea;
    private CarHolder carHolder;
    private RentHolder rentHolder;
    private SignatureView signatureView;
    private LinkedList<File> photosFiles;
    private File tempPhotoFile;
    private boolean makotListener;
    private boolean ifFromLink;

    public CarActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.activity_car_toolbar);
        setSupportActionBar(myToolbar);

        InitMembers();

        if (!isFromLink()) {
            this.car = getIntent().getParcelableExtra(EXTRA_CAR);
            this.rent = getIntent().getParcelableExtra(EXTRA_RENT);
            BehaviorMembers();
        }
    }

    /**
     * Check if this Activity lounch from out Link.
     *
     * @return if from link return <b>true</b>. else <b>false</b>
     */
    private boolean isFromLink() {
        Intent intent = getIntent();
        Uri link = intent.getData();

        if (link == null)
            return false;

        List<String> params = link.getPathSegments();
        if (params.size() < 2)
            return false;

        String type = params.get(0); // "car" / "rent"
        String number = params.get(1); // "1234"

        if (type.equals(B.Keys.CARS)) {
            FirebaseDatabase.getInstance().getReference(B.Keys.CARS)
                    .child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    car = dataSnapshot.getValue(Car.class);
                    BehaviorMembers();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }

        if (type.equals(B.Keys.RENTS)) {
            FirebaseDatabase.getInstance().getReference(B.Keys.RENTS).child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rent = dataSnapshot.getValue(Rent.class);
                    BehaviorMembers();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }

        return false;
    }

    private void InitMembers() {
        NO_DETAILS = getString(R.string.no_details);
        photosFiles = new LinkedList<>();

        content = (LinearLayout) findViewById(R.id.activity_car);

        llDetailCard = (LinearLayout) findViewById(R.id.activity_car_detailscard);
        llSignArea = (LinearLayout) findViewById(R.id.activity_car_signArea);

        this.signatureView = new SignatureView(this);

        carHolder = new CarHolder(LayoutInflater.from(this).inflate(R.layout.item_car, llDetailCard, false));
        rentHolder = new RentHolder(LayoutInflater.from(this).inflate(R.layout.item_rent, llDetailCard, false));

        progressDialog = new ProgressDialog(this);

        photosRecycler = (RecyclerView) findViewById(R.id.activity_car_recycler);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        this.btnAddPhoto = (Button) findViewById(R.id.activity_car_addPhoto);
        this.btnShowSign = (Button) findViewById(R.id.activity_car_showSignBtn);
        this.btnActivate = (Button) findViewById(R.id.activity_car_activateBtn);
        this.btnClearSign = (Button) findViewById(R.id.activity_car_sign_clearBtn);

        makotListener = true;
    }

    private void BehaviorMembers() {

        if (rent != null) {//show rentHolder
            BehaviorRentMembers();

            //if rent in this day show sign AND show add photo
            long thisTime = B.getLongWithOnlyDate(new Date().getTime());
            if (thisTime >= rent.getDateStart() && thisTime <= rent.getDateEnd())
                BehaviorSignMembers();
        } else if (car != null)//show carHolder
            BehaviorCarMembers();
        else {
            goToMainActivity();
        }

        if (B.customer != null && B.customer.isManager())//show add photo
            BehaviorManagerMembers();
    }

    private void goToMainActivity() {
        Toast.makeText(this, R.string.not_found_data, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void BehaviorSignMembers() {

        this.btnAddPhoto.setVisibility(View.VISIBLE);
        this.btnShowSign.setVisibility(View.VISIBLE);

        this.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        this.btnShowSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSignArea.setVisibility(
                        llSignArea.getVisibility() == View.VISIBLE ?
                                View.GONE :
                                View.VISIBLE);
            }
        });

        this.btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activate();
            }
        });
        this.btnClearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearSignature();
            }
        });

        llSignArea.addView(signatureView);

    }

    private void BehaviorManagerMembers() {
        this.btnAddPhoto.setVisibility(View.VISIBLE);
        this.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void BehaviorCarMembers() {
        carHolder.Init(this, null, this.car, true);
        llDetailCard.addView(carHolder.itemView);

        if (makotListener) {
            FirebaseDatabase.getInstance().getReference(B.Keys.CARS).child(car.getCarNumber())
                    .addListenerForSingleValueEvent(this);
            Log.d("BARRUCH","Listener to makot from car Func");
            makotListener = false;
        }

        BuildRecycler();
    }

    private void BehaviorRentMembers() {
        rentHolder.Init(this, null, this.rent, true);
        llDetailCard.addView(rentHolder.itemView);

        progressDialog.setTitle(R.string.search_for_car);
        progressDialog.show();

        if (makotListener) {
            FirebaseDatabase.getInstance().getReference(B.Keys.CARS).child(this.rent.getCarNumber())
                    .addListenerForSingleValueEvent(this);
            makotListener = false;
            Log.d("BARRUCH","Listener to makot from rent Func.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {//after show camera

            switch (resultCode) {
                case Activity.RESULT_OK://after taking picture
                    photosFiles.offer(tempPhotoFile);
                    dispatchTakePictureIntent();//take more picture
                    break;
                case Activity.RESULT_CANCELED://after close picture
                    //this.progressDialog = new ProgressDialog(this);
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                    while (photosFiles.peek() != null) {

                        // Start MyUploadService to upload the file, so that the file is uploaded
                        // even if this Activity is killed or put in the background
                        startService(new Intent(this, MyUploadService.class)
                                .putExtra(MyUploadService.EXTRA_FILE_URI, Uri.fromFile(photosFiles.poll()))
                                .putExtra(MyUploadService.EXTRA_CAR, car)
                                .putExtra(MyUploadService.EXTRA_DETAILS, getString(R.string.no_details))
                                .setAction(MyUploadService.ACTION_UPLOAD));

                    /*Intent intent = new Intent(this, UploadService.class);
                    intent.putExtra(UploadService.EXTRA_URI, photosFiles.poll().getAbsolutePath());
                    intent.putExtra(UploadService.EXTRA_CAR, car);
                    startService(intent);*/
                    }
                    //SavePhoto();
                    break;
                case Activity.RESULT_FIRST_USER:
                    Toast.makeText(this, "RESULT_FIRST_USER", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                    break;
            }

        } else super.onActivityResult(requestCode, resultCode, data);
    }

    //Activate showed Rent
    private void Activate() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.create_contract));
            progressDialog.setMax(3);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

            //create PDF
            PDFCreator pdfCreator = new PDFCreator();
            pdfCreator.AddToDocument(llDetailCard);
            for (View v : photosAdapter.getViews()) {
                pdfCreator.AddToDocument(v);
            }
            pdfCreator.AddToDocument(signatureView);

            File pdfContract = pdfCreator.getFile();
            if (pdfContract != null) {

                progressDialog.setProgress(1);
                progressDialog.setMessage(getString(R.string.upload_contract));

                //upload pdf
                if (uploadPDFContract(pdfContract)) {
                    progressDialog.setProgress(2);
                    progressDialog.setMessage(getString(R.string.make_rent_activeated));

                    //activate rent
                    if (makeRentActivated()) {
                        progressDialog.dismiss();

                        //share this contract
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfContract));
                        startActivity(Intent.createChooser(intent, getString(R.string.send_file_to)));

                        //end with this Activity
                        this.finish();


                    } else showSnackbar(R.string.cant_make_activated);//if not activate rent in db
                } else showSnackbar(R.string.cant_upload_contract);//if not upload pdf
            } else showSnackbar(R.string.cant_create_contract);//if not create pdf
        }

        //for Android low level if
        else Toast.makeText(this, R.string.apilevel_low, Toast.LENGTH_LONG).show();

    }

    private boolean uploadPDFContract(File pdfContract) {
        Uri uriFile = Uri.fromFile(pdfContract);

        StorageReference pdfRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(B.Constants.FIREBASE_STORAGE_URL)
                .child(B.Keys.PDF)
                .child(uriFile.getLastPathSegment());

        UploadTask uploadTask = pdfRef.putFile(uriFile);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            }
        });

        return true;
    }

    private boolean makeRentActivated() {
        this.rent.setActivated(true);
        return this.rent.Update();
    }

    private void BuildRecycler() {
        photosRecycler.setLayoutManager(layoutManager);

        photosAdapter = new PhotosAdapter(this, this.car.getMakot());
        photosRecycler.setAdapter(photosAdapter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.car = dataSnapshot.getValue(Car.class);
        BuildRecycler();

        Log.d("BARRUCH","Listener to makot from onDataChange Func");

        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (photosAdapter != null) {
            photosAdapter.addPhoto(dataSnapshot.getKey(), dataSnapshot.getValue(String.class));
            Log.d("BARRUCH","add photo from onChild Added");
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        File storageDir = getDir("Pictures", 0);
        File ttt = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(Environment.getExternalStorageDirectory(), timeStamp + ".jpg");
        File imageXXX = new File(ttt, timeStamp + ".jpg");


                /*File.createTempFile(
                timeStamp,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );*/
        return imageXXX;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            tempPhotoFile = null;
            try {
                tempPhotoFile = createImageFile();
            } catch (IOException ex) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"baruchiro@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Error Log");

                String text = ex.getMessage() + "\n\n\n" +
                        ex.toString() + "\n\n\n";
                for (StackTraceElement element : ex.getStackTrace())
                    text += element.toString();

                i.putExtra(Intent.EXTRA_TEXT, text);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException eror) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
            // Continue only if the File was successfully created
            if (tempPhotoFile != null) {
                Uri photoURI = Uri.fromFile(tempPhotoFile);
                /*Uri photoURI = FileProvider.getUriForFile(this,
                        "rothkoff.baruch.cars.fileProvider",
                        tempPhotoFile);*/
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        } else Toast.makeText(this, R.string.not_have_camera, Toast.LENGTH_LONG).show();
    }

    public void SavePhoto() {

        if (photosFiles.size() > 0) {
            final Uri file = Uri.fromFile(photosFiles.poll());

            final String photoName = file.getLastPathSegment().substring(0, file.getLastPathSegment().length() - 4);

            StorageReference photoRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(B.Constants.FIREBASE_STORAGE_URL)
                    .child(B.Keys.IMAGES).child(file.getLastPathSegment());

            UploadTask uploadTask = photoRef.putFile(file);

            progressDialog.setMessage(
                    getString(R.string.remined) + ": " +
                            photosFiles.size() + 1 +
                            " " + getString(R.string.pictures));
            if (!progressDialog.isShowing()) progressDialog.show();

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CarActivity.this, R.string.error_when_updating_details, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override//H
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CarActivity.this, R.string.success, Toast.LENGTH_SHORT).show();

                    car.addPhoto(photoName, NO_DETAILS);

                    photosAdapter.addPhoto(photoName, NO_DETAILS);

                    SavePhoto();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMax((int) taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress((int) taskSnapshot.getBytesTransferred());
                }
            });
        } else {
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    private void showSnackbar(@StringRes int message) {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        Snackbar
                .make(signatureView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Activate();
                    }
                })
                .show();
    }

    private class SignatureView extends View {

        // set the stroke width
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final RectF dirtyRect = new RectF();
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;

        public SignatureView(Context context) {

            super(context);

            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);

            // set the bg color as white
            this.setBackgroundColor(Color.WHITE);

            // width and height should cover the screen
            this.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        }

        /**
         * Get signature
         *
         * @return
         */
        protected Bitmap getSignature() {

            Bitmap signatureBitmap = null;

            // set the signature bitmap
            if (signatureBitmap == null) {
                signatureBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
            }

            // important for saving signature
            final Canvas canvas = new Canvas(signatureBitmap);
            this.draw(canvas);

            return signatureBitmap;
        }

        /**
         * clear signature canvas
         */
        private void clearSignature() {
            path.reset();
            this.invalidate();
        }

        // all touch events during the drawing
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(this.path, this.paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    path.moveTo(eventX, eventY);

                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);

                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:

                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class PDFCreator {

        private PdfDocument document;

        PDFCreator() {
            this.document = new PdfDocument();
        }

        public File getFile() {
            try {

                String filename = rent.getCarNumber() + "_" + rent.getUid() + ".pdf";


                File file = new File(getFilesDir(), filename);

                //Create File
                file.createNewFile();

                // openFileOutput(filename, MODE_WORLD_READABLE);
                FileOutputStream outputStream = new FileOutputStream(file);
                document.writeTo(outputStream);
                outputStream.close();

                // close the document
                document.close();

                return file;
            } catch (IOException ex) {
                return null;
            }
        }

        private void AddToDocument(View viewDrew) {

            //create page info
            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(viewDrew.getWidth(), viewDrew.getHeight(), pageNumber).create();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);

            viewDrew.setDrawingCacheEnabled(true);
            //draw view
            viewDrew.draw(page.getCanvas());

            viewDrew.setDrawingCacheEnabled(false);
            //close page
            document.finishPage(page);

            pageNumber++;
        }
    }
}
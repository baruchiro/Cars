package rothkoff.baruch.cars;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rothkoff.baruch.cars.cardetails.CarView;

public class CarActivity extends Activity {

    public static final String EXTRA_RENT_ID = "rothkoff.baruch.cars.CarActivity.rentID";
    public static final String EXTRA_CAR_NUMBER = "rothkoff.baruch.cars.CarActivity.CarNumber";

    private ProgressDialog progressDialog;
    private Car car;
    private Rent rent;
    private boolean rentMode;

    private CarView carView;

    public CarActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        InitMembers();
        BehaviorMembers();
    }

    private void getCarDetails() {
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        progressDialog.show();

        String carNumber = getIntent().getStringExtra(EXTRA_CAR_NUMBER);
        String rentID = getIntent().getStringExtra(EXTRA_RENT_ID);

        if (rentID != null) {
            progressDialog.setMessage(getString(R.string.search_for_rent));

            DatabaseReference rentRef = FirebaseDatabase.getInstance().getReference(B.Keys.RENTS).child(rentID);
            rentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CarActivity.this.rent = dataSnapshot.getValue(Rent.class);

                    progressDialog.setMessage(getString(R.string.search_for_car));
                    setCar(CarActivity.this.rent.getCarNumber());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (carNumber != null) setCar(carNumber);
        else {
            if (progressDialog.isShowing())progressDialog.dismiss();
            Toast.makeText(this,R.string.not_found_data,Toast.LENGTH_SHORT).show();
            this.finish();
        }
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void setCar(String carNumber){
        FirebaseDatabase.getInstance().getReference(B.Keys.CARS).child(carNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CarActivity.this.car = dataSnapshot.getValue(Car.class);

                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void InitMembers() {
        carView = new CarView(this,(ViewGroup) findViewById(R.id.activity_car_carview));
        progressDialog = new ProgressDialog(this);

        getCarDetails();
    }

    private void BehaviorMembers() {
    }
}

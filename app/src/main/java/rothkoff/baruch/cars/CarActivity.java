package rothkoff.baruch.cars;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import rothkoff.baruch.cars.cardetails.CarView;

public class CarActivity extends Activity {

    public final String EXTRA_CAR_NUMBER = "rothkoff.baruch.cars.CarActivity.CarNumber";

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
        progressDialog.setMessage(getString(R.string.search_for_car));
        progressDialog.show();

        //DatabaseReference carRef = FirebaseDatabase.getInstance().getReference()
                getIntent().getStringExtra(EXTRA_CAR_NUMBER);
    }

    private void InitMembers() {
        carView = new CarView(this,(ViewGroup) findViewById(R.id.activity_car_carview));
        progressDialog = new ProgressDialog(this);

        getCarDetails();
    }

    private void BehaviorMembers() {

        if (car == null){
            Toast.makeText(this,R.string.error_select_car,Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}

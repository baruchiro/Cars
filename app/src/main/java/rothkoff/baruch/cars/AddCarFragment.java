package rothkoff.baruch.cars;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarFragment extends MyFragment implements DatabaseReference.CompletionListener {

    //private ForCustomerFragments mainActivity;
    private ProgressDialog updatingDialog;

    private EditText etBrand, etColor, etParkLocation, etCarNumber;
    private Switch sYoungDriver;
    private FloatingActionButton fab;
    private TextView errorFields;

    private String brand;
    private String color;
    private String parkLocation;
    private String carNumber;
    private boolean youngDriver;

    public AddCarFragment() {
    }

    public static AddCarFragment newInstance() {
        AddCarFragment fragment = new AddCarFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        etBrand = (EditText) view.findViewById(R.id.frag_addcar_edit_brand);
        etColor = (EditText) view.findViewById(R.id.frag_addcar_edit_color);
        etParkLocation = (EditText) view.findViewById(R.id.frag_addcar_edit_parklocation);
        etCarNumber = (EditText) view.findViewById(R.id.frag_addcar_edit_carnumber);
        sYoungDriver = (Switch)view.findViewById(R.id.frag_addcar_isyoung);

        errorFields = (TextView) view.findViewById(R.id.frag_addcar_error);

        fab = (FloatingActionButton) view.findViewById(R.id.frag_addcar_fab);

        updatingDialog = new ProgressDialog(getContext());
    }

    private void BehaviorMembers() {
        errorFields.setTextColor(COLOR_ERROR);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()) SaveCarDetails();
            }
        });
        updatingDialog.setMessage(getString(R.string.send_data_to_server));

        sYoungDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                youngDriver = b;
            }
        });
        sYoungDriver.setChecked(false);
    }

    private void SaveCarDetails() {
        updatingDialog.show();

        Car car = new Car(carNumber, brand,color);
        car.setParkLocation(parkLocation);
        car.setYoung(youngDriver);

        FirebaseDatabase.getInstance().getReference(B.Keys.CARS).child(carNumber)
                .updateChildren(car.getMapForUpdate(), this);
        updatingDialog.setMessage(getString(R.string.wait_to_ok_from_server));
    }

    private boolean Validate() {
        String errorMessage = "";
        brand = etBrand.getText().toString();
        color = etColor.getText().toString();
        parkLocation = etParkLocation.getText().toString();
        carNumber = etCarNumber.getText().toString();

        if (brand.matches("")) {
            errorMessage += getString(R.string.error_brand) + "<br/>";
            etBrand.setHighlightColor(COLOR_ERROR);
        }
        if (color.matches("")) {
            errorMessage += getString(R.string.error_color) + "<br/>";
            etColor.setHighlightColor(COLOR_ERROR);
        }
        if (parkLocation.matches("")) {
            errorMessage += getString(R.string.error_parklocation) + "<br/>";
            etParkLocation.setHighlightColor(COLOR_ERROR);
        }
        if (carNumber.matches("")) {
            errorMessage += getString(R.string.error_carnumber) + "<br/>";
            etCarNumber.setHighlightColor(COLOR_ERROR);
        }

        if (errorMessage.equals("")) {
            errorFields.setVisibility(View.GONE);
            etBrand.setHighlightColor(COLOR_HEADING);
            etColor.setHighlightColor(COLOR_HEADING);
            etParkLocation.setHighlightColor(COLOR_HEADING);
            etCarNumber.setHighlightColor(COLOR_HEADING);
            return true;
        }

        errorFields.setVisibility(View.VISIBLE);
        errorFields.setText(Html.fromHtml(errorMessage));
        return false;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError == null) {
            mainActivity.ReplaceFragment(MyAccountFragment.newInstance());
            Toast.makeText(getContext(),R.string.success,Toast.LENGTH_LONG).show();
            updatingDialog.dismiss();
        } else {
            updatingDialog.dismiss();
            Snackbar.make(fab, R.string.error_when_updating_details, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Validate()) SaveCarDetails();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.addcar_title);
    }
}

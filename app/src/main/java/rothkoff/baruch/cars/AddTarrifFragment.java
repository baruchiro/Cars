package rothkoff.baruch.cars;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTarrifFragment extends MyFragment implements DatabaseReference.CompletionListener {
    public static final String NEW_TARRIF = "NEW_TARRIF";

    private ProgressDialog updatingDialog;

    private EditText etName, etPrice, etSeatCount, etEngineCapacity, etYoungPrice;
    private FloatingActionButton fab;
    private TextView errorFields;

    private String uid;
    private String name;
    private double price;
    private int seatCount;
    private int engineCapacity;
    private double youngPrice;

    public AddTarrifFragment() {
    }

    public static AddTarrifFragment newInstance(@NonNull String uid) {
        AddTarrifFragment fragment = new AddTarrifFragment();

        Bundle args = new Bundle();
        args.putString(B.Keys.UID, uid);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tarrif, container, false);

        InitMembers(view);
        BehavoirMembers();

        return view;
    }

    private void InitMembers(View view) {
        etName = (EditText) view.findViewById(R.id.frag_addtarrif_edit_name);
        etPrice = (EditText) view.findViewById(R.id.frag_addtarrif_edit_price);
        etSeatCount = (EditText) view.findViewById(R.id.frag_addtarrif_edit_seatcount);
        etEngineCapacity = (EditText) view.findViewById(R.id.frag_addtarrif_edit_enginecapacity);
        etYoungPrice = (EditText) view.findViewById(R.id.frag_addtarrif_edit_youngPrice);

        errorFields = (TextView) view.findViewById(R.id.frag_addtarrif_error);

        fab = (FloatingActionButton) view.findViewById(R.id.frag_addtarrif_fab);

        updatingDialog = new ProgressDialog(getContext());

        if (getArguments().getString(B.Keys.UID) != null)
            uid = getArguments().getString(B.Keys.UID);
        else
            throw new NullPointerException("Argument 'UID' not found. use static method 'newInstance' to create this fragment");
    }

    private void BehavoirMembers() {
        errorFields.setTextColor(COLOR_ERROR);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatingDialog.show();
                if (Validate()) SaveTarrifDetails();
                else updatingDialog.dismiss();
            }
        });
        updatingDialog.setMessage(getString(R.string.send_data_to_server));
    }

    private void SaveTarrifDetails() {

        Tarrif tarrif = new Tarrif(name, price, seatCount, engineCapacity, youngPrice);

        if (uid.equals(NEW_TARRIF)) {
            uid = FirebaseDatabase.getInstance().getReference(B.Keys.TARIFFS).push().getKey();
            tarrif.setUid(uid);
        }
        FirebaseDatabase.getInstance().getReference(B.Keys.TARIFFS).child(tarrif.getUid())
                .updateChildren(tarrif.getMapForUpdate(), this);
        updatingDialog.setMessage(getString(R.string.wait_to_ok_from_server));
    }

    private boolean Validate() {
        String errorMessage = "";
        try {
            name = etName.getText().toString();

            price = -1;
            if (!etPrice.getText().toString().matches("") && etPrice.getText().toString().trim().length() > 0)
                price = Double.parseDouble(etPrice.getText().toString());

            seatCount = -1;
            if (!etSeatCount.getText().toString().matches("") && etSeatCount.getText().toString().trim().length() > 0)
                seatCount = Integer.parseInt(etSeatCount.getText().toString());

            engineCapacity = -1;
            if (!etEngineCapacity.getText().toString().matches("") && etEngineCapacity.getText().toString().trim().length() > 0)
                engineCapacity = Integer.parseInt(etEngineCapacity.getText().toString());

            youngPrice = -1;
            if (!etYoungPrice.getText().toString().matches("") && etYoungPrice.getText().toString().trim().length() > 0)
                youngPrice = Double.parseDouble(etYoungPrice.getText().toString());


            if (name.matches("")) {
                errorMessage += getString(R.string.error_name) + "<br/>";
                etName.setHighlightColor(COLOR_ERROR);
            }
            if (price <= 0) {
                errorMessage += getString(R.string.error_price) + "<br/>";
                etPrice.setHighlightColor(COLOR_ERROR);
            }
            if (seatCount <= 0) {
                errorMessage += getString(R.string.error_seatcount) + "<br/>";
                etSeatCount.setHighlightColor(COLOR_ERROR);
            }
            if (engineCapacity <= 0) {
                errorMessage += getString(R.string.error_enginecapacity) + "<br/>";
                etEngineCapacity.setHighlightColor(COLOR_ERROR);
            }
            if (youngPrice < 0) {
                errorMessage += getString(R.string.error_youngprice) + "<br/>";
                etYoungPrice.setHighlightColor(COLOR_ERROR);
            }

            if (errorMessage.equals("")) {
                errorFields.setVisibility(View.GONE);
                etName.setHighlightColor(COLOR_HEADING);
                etPrice.setHighlightColor(COLOR_HEADING);
                etSeatCount.setHighlightColor(COLOR_HEADING);
                etEngineCapacity.setHighlightColor(COLOR_HEADING);
                etYoungPrice.setHighlightColor(COLOR_HEADING);
                return true;
            }

            errorFields.setVisibility(View.VISIBLE);
            errorFields.setText(Html.fromHtml(errorMessage));
            return false;

        } catch (NumberFormatException e) {
            Snackbar.make(fab, R.string.error_with_some_numbers, Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError == null) {
            mainActivity.ReplaceFragment(ManageDBFragment.newInstance());
            Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
            updatingDialog.dismiss();
        } else {
            updatingDialog.dismiss();
            Snackbar.make(fab, R.string.error_when_updating_details, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Validate()) SaveTarrifDetails();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.addtarrif_title);
    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_managedb).setChecked(true);
    }
}

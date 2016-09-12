package rothkoff.baruch.cars;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerDetailsEditFragment extends MyFragment
        implements View.OnClickListener, DatabaseReference.CompletionListener,
        DatePickerDialog.OnDateSetListener {

    private ProgressDialog updatingDialog;
    private static int COLOR_ERROR;
    private static int COLOR_HEADING;

    private EditText etFirstName, etLastName, etDateOfBirth, etID;
    private FloatingActionButton fab;
    private TextView errorFields;

    private String errorMessage;
    private String firstName;
    private String lastName;
    private String IDnumber;
    private Calendar dateOfBirth;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;

    public CustomerDetailsEditFragment() {
        // Required empty public constructor
    }

    public static CustomerDetailsEditFragment newInstance() {
        CustomerDetailsEditFragment fragment = new CustomerDetailsEditFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_details_edit, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        etFirstName = (EditText) view.findViewById(R.id.frag_details_edit_firstname);
        etLastName = (EditText) view.findViewById(R.id.frag_details_edit_lastname);
        etDateOfBirth = (EditText) view.findViewById(R.id.frag_details_edit_dateofbirth);
        etID = (EditText) view.findViewById(R.id.frag_details_edit_idnumber);
        dateOfBirth = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        datePickerDialog =new DatePickerDialog(getContext(),this,0,0,0);

        errorFields = (TextView) view.findViewById(R.id.frag_details_error);

        fab = (FloatingActionButton) view.findViewById(R.id.frag_details_fab);

        updatingDialog = new ProgressDialog(getContext());
    }

    private void BehaviorMembers() {
        if (B.customer.getDateOfBirth()==0L) dateOfBirth.add(Calendar.YEAR,-18);
        else dateOfBirth.setTimeInMillis(B.customer.getDateOfBirth());

        DateMembers(dateOfBirth.get(Calendar.YEAR),
                dateOfBirth.get(Calendar.MONTH),
                dateOfBirth.get(Calendar.DAY_OF_MONTH));

        etFirstName.setText(B.customer.getFirstName());
        etLastName.setText(B.customer.getLastName());
        etID.setText(B.customer.getIDnumber());

        etDateOfBirth.setText(simpleDateFormat.format(dateOfBirth.getTime()));
        etDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (!datePickerDialog.isShowing()) datePickerDialog.show();
                } else {
                    if (datePickerDialog.isShowing()) datePickerDialog.dismiss();
                }
            }
        });

        errorFields.setTextColor(COLOR_ERROR);

        fab.setOnClickListener(this);

        updatingDialog.setMessage(getString(R.string.send_data_to_server));
    }

    private void DateMembers(int year,int month,int day){
        datePickerDialog.updateDate(year, month, day);
        dateOfBirth.set(year, month, day);
        etDateOfBirth.setText(simpleDateFormat.format(dateOfBirth.getTime()));
    }

    @Override
    public void onClick(View view) {
        if (Validate()) SaveCustomerDetails();
    }

    private void SaveCustomerDetails() {
        updatingDialog.show();

        B.customer.setFirstName(firstName);
        B.customer.setLastName(lastName);
        B.customer.setDateOfBirth(dateOfBirth.getTimeInMillis());
        B.customer.setIDnumber(IDnumber);

        FirebaseDatabase.getInstance().getReference(B.Keys.CUSTOMERS).child(B.customer.getUid())
                .updateChildren(B.customer.getMapForUpdate(), this);
        updatingDialog.setMessage(getString(R.string.wait_to_ok_from_server));
    }

    private boolean Validate() {
        errorMessage = "";
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        IDnumber = etID.getText().toString();
        Calendar age18 = Calendar.getInstance();
        age18.add(Calendar.YEAR,-18);

        if (firstName.matches("")) {
            errorMessage += getString(R.string.error_firstname) + "<br/>";
            etFirstName.setHighlightColor(COLOR_ERROR);
        }
        if (lastName.matches("")) {
            errorMessage += getString(R.string.error_lastname) + "<br/>";
            etLastName.setHighlightColor(COLOR_ERROR);
        }
        if (dateOfBirth.compareTo(age18)>0){
            errorMessage+=getString(R.string.error_age)+"<br/>";
            etDateOfBirth.setHighlightColor(COLOR_ERROR);
        }
        if (IDnumber.matches("")) {
            errorMessage += getString(R.string.error_idnumber) + "<br/>";
            etID.setHighlightColor(COLOR_ERROR);
        }

        if (errorMessage.equals("")) {
            errorFields.setVisibility(View.GONE);
            etFirstName.setHighlightColor(COLOR_HEADING);
            etLastName.setHighlightColor(COLOR_HEADING);
            etID.setHighlightColor(COLOR_HEADING);
            etDateOfBirth.setHighlightColor(COLOR_HEADING);
            return true;
        }

        errorFields.setVisibility(View.VISIBLE);
        errorFields.setText(Html.fromHtml(errorMessage));
        return false;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError == null) {
            mainActivity.ReplaceFragment(CustomerMainFragment.newInstance());
            updatingDialog.dismiss();
        } else {
            updatingDialog.dismiss();
            Snackbar.make(fab, R.string.error_when_updating_details, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Validate()) SaveCustomerDetails();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        DateMembers(i,i1,i2);
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.customer_details_edit);
    }
}

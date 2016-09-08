package rothkoff.baruch.cars;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerDetailsEditFragment extends Fragment implements View.OnClickListener, DatabaseReference.CompletionListener {
private ForCustomerFragments mainActivity;
    private ProgressDialog updatingDialog;

    private Customer customer;

    private EditText etFirstName,etLastName,etAge;
    private FloatingActionButton fab;
    private TextView errorFields;
    private String errorMessage;
    private String firstName;
    private String lastName;
    private int age;

    public CustomerDetailsEditFragment() {
        // Required empty public constructor
    }

    public static CustomerDetailsEditFragment getInstance(Customer customer){
        CustomerDetailsEditFragment fragment = new CustomerDetailsEditFragment();

        fragment.customer = customer;

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForCustomerFragments) {
            mainActivity = (ForCustomerFragments) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_details_edit, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view){
        etFirstName = (EditText)view.findViewById(R.id.frag_details_edit_firstname);
        etLastName = (EditText)view.findViewById(R.id.frag_details_edit_lastname);
        etAge = (EditText)view.findViewById(R.id.frag_details_edit_age);

        errorFields = (TextView)view.findViewById(R.id.frag_details_error);

        fab = (FloatingActionButton)view.findViewById(R.id.frag_details_fab);

        updatingDialog = new ProgressDialog(getContext());
    }

    private void BehaviorMembers(){
        etFirstName.setText(customer.getFirstName());
        etLastName.setText(customer.getLastName());
        etAge.setText(String.valueOf(customer.getDateOfBirth()));

        errorFields.setTextColor(getResources().getColor(R.color.errorColor));

        fab.setOnClickListener(this);

        updatingDialog.setTitle(R.string.updating_details);
    }

    @Override
    public void onClick(View view) {
        if(Validate())SaveCustomerDetails();
    }

    private void SaveCustomerDetails() {
        updatingDialog.show();

        B.customer.setFirstName(firstName);
        B.customer.setLastName(lastName);
        B.customer.setDateOfBirth(age);

        FirebaseDatabase.getInstance().getReference(B.Keys.CUSTOMERS).child(B.customer.getUid())
                .updateChildren(B.customer.getMapForUpdate(),this);
    }

    private boolean Validate(){
        errorMessage = "";
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        age = Integer.parseInt(etAge.getText().toString());

        if (firstName.matches("")) {
            errorMessage += getString(R.string.error_firstname) + "<br/>";
            etFirstName.setHighlightColor(getResources().getColor(R.color.errorColor));
        }
        if (lastName.matches("")) {
            errorMessage += getString(R.string.error_lastname) + "<br/>";
            etLastName.setHighlightColor(getResources().getColor(R.color.errorColor));
        }
        if (age<18) {
            errorMessage += getString(R.string.error_age) + "<br/>";
            etAge.setHighlightColor(getResources().getColor(R.color.errorColor));
        }

        if (errorMessage.equals("")){
            errorFields.setVisibility(View.GONE);
            etFirstName.setHighlightColor(getResources().getColor(R.color.headingColor));
            etLastName.setHighlightColor(getResources().getColor(R.color.headingColor));
            etAge.setHighlightColor(getResources().getColor(R.color.headingColor));
            return true;
        }

        errorFields.setVisibility(View.VISIBLE);
        errorFields.setText(Html.fromHtml(errorMessage));
        return false;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError == null) {
            //mainActivity.ReplaceFragment();
            updatingDialog.dismiss();
        }else {
            updatingDialog.dismiss();
            Snackbar.make(fab,R.string.error_when_updating_details,Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Validate())SaveCustomerDetails();
                        }
                    })
                    .show();
        }
    }
//pref.edit().putBoolean(B.Constants.FIRST_LAUNCH, false).apply();
}

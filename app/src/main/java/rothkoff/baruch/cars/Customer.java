package rothkoff.baruch.cars;

import android.content.res.Resources;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String uid;
    private String IDnumber = "";
    private String firstName = "";
    private String lastName = "";
    private long dateOfBirth = 0L;
    private boolean manager = false;
    private Map<String,Rent> rents = new HashMap<>();

    public Customer() {

    }

    public Customer(String uid) {
        this.uid = uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public void setRents(Map<String, Rent> rents) {
        this.rents = rents;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Map<String, Object> getMapForUpdate() {
        Map<String, Object> m = new HashMap<>();

        m.put(B.Keys.UID, uid);
        m.put(B.Keys.ID_NUMBER, IDnumber);
        m.put(B.Keys.FIRST_NAME, firstName);
        m.put(B.Keys.LAST_NAME, lastName);
        m.put(B.Keys.DATE_OF_BIRTH, dateOfBirth);

        return m;
    }

    public boolean isDetailMissing(){
        return firstName.matches("")||
                lastName.matches("")||
                IDnumber.matches("")||
                dateOfBirth == 0L;
    }

    public static Map<String, String> getMapForView(Customer customer,Resources resources) {
        Map<String, String> mapForView = new HashMap<>();

        mapForView.put(resources.getString(R.string.full_name), customer.getFullName());
        mapForView.put(resources.getString(R.string.age), String.valueOf(customer.getAge()));
        mapForView.put(resources.getString(R.string.IDnumber), customer.IDnumber);

        return mapForView;
    }

    public Map<String, Rent> getRents() {
        return rents;
    }

    public int getAge() {
        long now = new Date().getTime();
        int age = (int) ((now - dateOfBirth) / B.Constants.YEAR_IN_MILISECONDS);
        return age;
    }

    public String getFullName() {
        return firstName+" "+lastName;
    }

    public void addRent(Rent rent) {
        rent.setCustomerName(getFullName());
        rent.setCustomerUid(uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(B.Keys.CUSTOMERS).child(uid).child(B.Keys.RENTS).push();
        rent.setUid(ref.getKey());
        ref.setValue(rent);

        FirebaseDatabase.getInstance().getReference(B.Keys.RENTS).child(rent.getUid()).setValue(rent);
    }
}

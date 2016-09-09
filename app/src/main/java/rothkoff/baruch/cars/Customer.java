package rothkoff.baruch.cars;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String uid;
    private String IDnumber = "";
    private String firstName = "";
    private String lastName = "";
    private long dateOfBirth = 0L;
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
}

package rothkoff.baruch.cars.model;

import android.content.res.Resources;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.model.Rent;

public class Customer {
    private String uid;
    private String IDnumber = "";
    private String firstName = "";
    private String lastName = "";
    private long dateOfBirth = 0L;
    private boolean manager = false;
    private List<Rent> rents = new LinkedList<>();

    public Customer() {

    }

    public Customer(String uid) {
        this.uid = uid;
    }

    public Map<String, String> getMapForView(Resources resources) {
        Map<String, String> mapForView = new HashMap<>();

        mapForView.put(resources.getString(R.string.full_name), getFullName());
        mapForView.put(resources.getString(R.string.age), String.valueOf(getAge()));
        mapForView.put(resources.getString(R.string.IDnumber), IDnumber);

        return mapForView;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
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

    public boolean isDetailMissing() {
        return firstName.matches("") ||
                lastName.matches("") ||
                IDnumber.matches("") ||
                dateOfBirth == 0L;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(Map<String, Rent> rents) {
        for (Rent rent : rents.values())
            insertToRents(rent);
        getNextRents();
    }

    public List<Rent> getNextRents(){
        long now = B.getLongWithOnlyDate(System.currentTimeMillis());
        List<Rent> r = new LinkedList<>(rents);

        while (r.size()>0&&r.get(0).getDateStart()<now)
            r.remove(0);

        return r;
    }

    private void insertToRents(Rent rent) {
        if (rents.size() == 0) rents.add(rent);
        else {
            int i = 0;
            long dateStart = rent.getDateStart();

            while (rents.size() > i &&
                    rents.get(i).getDateStart() < dateStart) {
                i++;
            }

            rents.add(i, rent);
        }
    }

    public int getAge() {
        long now = new Date().getTime();
        int age = (int) ((now - dateOfBirth) / B.Constants.YEAR_IN_MILISECONDS);
        return age;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

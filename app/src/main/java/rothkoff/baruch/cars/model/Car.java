package rothkoff.baruch.cars.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.Rent;
import rothkoff.baruch.cars.Tarrif;

public class Car implements Parcelable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
    private String carNumber;
    private String brand;
    private String color;
    private boolean isYoung;
    private Map<String, String> makot;
    private String parkLocation = "";
    private String tariffUid;
    private Map<String, Rent> rents;

    public Car() {
    }

    public Car(String carNumber, String brand, String color, Tarrif tarrif) {
        this.carNumber = carNumber;
        this.brand = brand;
        this.color = color;
        this.tariffUid = tarrif.getUid();
        makot = new HashMap<>();
    }

    public Car(Parcel parcel) {
        this.carNumber = parcel.readString();
        this.brand = parcel.readString();
        this.color = parcel.readString();
        this.isYoung = Boolean.valueOf(parcel.readString());
        this.parkLocation = parcel.readString();
        this.tariffUid = parcel.readString();

        //read the size of Map makot, and the next object must be key and value- key and value...
        this.makot = new HashMap<>();
        int size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            String key = parcel.readString();
            String value = parcel.readString();
            this.makot.put(key, value);
        }

        //read the size of Map rents, and the next object must be key and value- key and value...
        this.rents = new HashMap<>();
        size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            String key = parcel.readString();
            Rent rent = parcel.readParcelable(Rent.class.getClassLoader());
            this.rents.put(key, rent);
        }
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getIsYoung() {
        return isYoung;
    }

    public void setIsYoung(boolean isYoung) {
        this.isYoung = isYoung;
    }

    public Map<String, String> getMakot() {
        return makot;
    }

    public void setMakot(Map<String, String> makot) {
        this.makot = makot;
    }

    public String getParkLocation() {
        return parkLocation;
    }

    public void setParkLocation(String parkLocation) {
        this.parkLocation = parkLocation;
    }

    public String getTariffUid() {
        return tariffUid;
    }

    public void setTariffUid(String tariffUid) {
        this.tariffUid = tariffUid;
    }

    public Map<String, Object> getMapForUpdate() {
        Map<String, Object> m = new HashMap<>();

        m.put(B.Keys.CAR_NUMBER, carNumber);
        m.put(B.Keys.BRAND, brand);
        m.put(B.Keys.COLOR, color);
        m.put(B.Keys.IS_YOUNG, isYoung);
        m.put(B.Keys.PARK_LOCATION, parkLocation);
        m.put(B.Keys.TARIFF_UID, tariffUid);

        return m;
    }

    //TODO check this
    public boolean availableInDates(Calendar dateStart, Calendar dateEnd) {
        dateStart = B.getCalenderWithOnlyDate(dateStart);
        dateEnd = B.getCalenderWithOnlyDate(dateEnd);

        for (long time = dateStart.getTimeInMillis(); time <= dateEnd.getTimeInMillis(); time += B.Constants.DAY_IN_MILISECONDS) {
            if (rents != null)
                for (Rent rent : rents.values())
                    if (time >= rent.getDateStart() && time <= rent.getDateEnd()) return false;
        }
        return true;
    }

    public Map<String, Rent> getRents() {
        return rents;
    }

    public void setRents(Map<String, Rent> rents) {
        this.rents = rents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.carNumber);
        parcel.writeString(this.brand);
        parcel.writeString(this.color);
        parcel.writeString(String.valueOf(this.isYoung));
        parcel.writeString(this.parkLocation);
        parcel.writeString(this.tariffUid);

        //enter the size of map that say to us how many object next for the restore the map
        //after the size- insert key and value, key and value...
        if (this.makot == null) parcel.writeInt(0);
        else {
            parcel.writeInt(this.makot.size());
            for (Map.Entry<String, String> entry : this.makot.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        //enter the size of map that say to us how many object next for the restore the map
        //after the size- insert key and value, key and value...
        if (this.rents == null) parcel.writeInt(0);
        else {
            parcel.writeInt(this.rents.size());
            for (Map.Entry<String, Rent> entry : this.rents.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeParcelable(entry.getValue(), i);
            }
        }

    }

    public void addPhoto(String photoName, String details) {

        if (this.makot == null) this.makot = new HashMap<>();

        this.makot.put(photoName,details);

        FirebaseDatabase.getInstance().getReference(B.Keys.CARS)
                .child(this.carNumber).child(B.Keys.MAKOT)
                .child(photoName).setValue(details);
    }
}
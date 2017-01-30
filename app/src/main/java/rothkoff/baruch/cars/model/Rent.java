package rothkoff.baruch.cars.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rothkoff.baruch.cars.B;

public class Rent implements Parcelable {
    public static final Creator<Rent> CREATOR = new Creator<Rent>() {
        @Override
        public Rent createFromParcel(Parcel in) {
            return new Rent(in);
        }

        @Override
        public Rent[] newArray(int size) {
            return new Rent[size];
        }
    };
    private long dateStart;
    private long dateEnd;
    private String uid;
    private String customerUid;
    private String customerName;
    private String carNumber;
    private String carBrand;
    private String carColor;
    private double totalPrice;
    private boolean activated = false;

    public Rent() {
    }

    public Rent(long dateStart, long dateEnd, Car selectedCar, double totalPrice) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.carNumber = selectedCar.getCarNumber();
        this.carBrand = selectedCar.getBrand();
        this.carColor = selectedCar.getColor();
        this.totalPrice = totalPrice;
    }

    protected Rent(Parcel in) {
        dateStart = in.readLong();
        dateEnd = in.readLong();
        uid = in.readString();
        customerUid = in.readString();
        customerName = in.readString();
        carNumber = in.readString();
        carBrand = in.readString();
        carColor = in.readString();
        totalPrice = in.readDouble();
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean AddToDB(Customer customer) {
        setCustomerName(customer.getFullName());
        setCustomerUid(customer.getUid());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rentRef = firebaseDatabase.getReference(B.Keys.RENTS).push();

        uid = rentRef.getKey();

        DatabaseReference customerRef = firebaseDatabase.getReference(B.Keys.CUSTOMERS).child(this.customerUid).child(B.Keys.RENTS).child(uid);
        DatabaseReference carRef = firebaseDatabase.getReference(B.Keys.CARS).child(carNumber).child(B.Keys.RENTS).child(uid);

        rentRef.setValue(this);
        customerRef.setValue(this);
        carRef.setValue(this);

        return true;
    }

    public boolean Update() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rentRef = firebaseDatabase.getReference(B.Keys.RENTS).child(this.uid);

        DatabaseReference customerRef = firebaseDatabase.getReference(B.Keys.CUSTOMERS).child(this.customerUid).child(B.Keys.RENTS).child(uid);
        DatabaseReference carRef = firebaseDatabase.getReference(B.Keys.CARS).child(carNumber).child(B.Keys.RENTS).child(uid);

        rentRef.setValue(this);
        customerRef.setValue(this);
        carRef.setValue(this);

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.dateStart);
        parcel.writeLong(this.dateEnd);
        parcel.writeString(this.uid);
        parcel.writeString(this.customerUid);
        parcel.writeString(this.customerName);
        parcel.writeString(this.carNumber);
        parcel.writeString(this.carBrand);
        parcel.writeString(this.carColor);
        parcel.writeDouble(this.totalPrice);
    }
}

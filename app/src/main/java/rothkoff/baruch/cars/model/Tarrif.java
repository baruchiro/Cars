package rothkoff.baruch.cars.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import rothkoff.baruch.cars.B;

public class Tarrif implements Parcelable {

    public static final Creator<Tarrif> CREATOR = new Creator<Tarrif>() {
        @Override
        public Tarrif createFromParcel(Parcel in) {
            return new Tarrif(in);
        }

        @Override
        public Tarrif[] newArray(int size) {
            return new Tarrif[size];
        }
    };

    private String uid;
    private String name;
    private double price;
    private int seatCount;
    private int engineCapacity;
    private double youngPrice;

    public Tarrif() {
    }

    public Tarrif(String name, double price, int seatCount, int engineCapacity, double youngPrice) {
        this.name = name;
        this.price = price;
        this.seatCount = seatCount;
        this.engineCapacity = engineCapacity;
        this.youngPrice = youngPrice;
    }

    public Tarrif(Parcel in) {
        uid = in.readString();
        name = in.readString();
        price = in.readDouble();
        seatCount = in.readInt();
        engineCapacity = in.readInt();
        youngPrice = in.readDouble();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getCalculatePrice(Calendar dateStart, Calendar dateEnd, boolean isYoung) {
        double priceFinall = 0;

        priceFinall += price * B.getNumberOfDays(dateStart, dateEnd);
        if (isYoung) priceFinall += youngPrice;

        return priceFinall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public double getYoungPrice() {
        return youngPrice;
    }

    public void setYoungPrice(int youngPrice) {
        this.youngPrice = youngPrice;
    }

    public Map<String, Object> getMapForUpdate() {
        Map<String, Object> m = new HashMap<>();

        m.put(B.Keys.UID, uid);
        m.put(B.Keys.NAME, name);
        m.put(B.Keys.PRICE, price);
        m.put(B.Keys.SEAT_COUNT, seatCount);
        m.put(B.Keys.ENGINE_CAPACITY, engineCapacity);
        m.put(B.Keys.YOUNG_PRICE, youngPrice);

        return m;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(seatCount);
        dest.writeInt(engineCapacity);
        dest.writeDouble(youngPrice);
    }
}

package rothkoff.baruch.cars;

import java.util.HashMap;
import java.util.Map;

public class Tarrif {
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;
    public static final int ALL = 0;
    private String uid;
    private String name;
    private double price;
    private int seatCount;
    private int engineCapacity;
    private double youngPrice;

    public Tarrif(){}

    public Tarrif(String name, double price, int seatCount, int engineCapacity, double youngPrice) {
        this.name = name;
        this.price = price;
        this.seatCount = seatCount;
        this.engineCapacity = engineCapacity;
        this.youngPrice = youngPrice;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
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

        m.put(B.Keys.NAME, name);
        m.put(B.Keys.PRICE, price);
        m.put(B.Keys.SEAT_COUNT, seatCount);
        m.put(B.Keys.ENGINE_CAPACITY, engineCapacity);
        m.put(B.Keys.YOUNG_PRICE, youngPrice);

        return m;
    }
}

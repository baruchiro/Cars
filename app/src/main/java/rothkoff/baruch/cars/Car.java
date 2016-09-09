package rothkoff.baruch.cars;

import java.util.HashMap;
import java.util.Map;

public class Car {
    private String carNumber;
    private String brand;
    private String color;
    private boolean isYoung = false;
    private int size;
    private Map<String,Maka> makot;
    private String parkLocation = "";
    private int tariffID;

    public Car(){
    }

    public Car(String carNumber, String brand, String color) {
        this.carNumber = carNumber;
        this.brand = brand;
        this.color = color;
        makot = new HashMap<>();
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

    public boolean isYoung() {
        return isYoung;
    }

    public void setYoung(boolean young) {
        isYoung = young;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, Maka> getMakot() {
        return makot;
    }

    public void setMakot(Map<String, Maka> makot) {
        this.makot = makot;
    }

    public String getParkLocation() {
        return parkLocation;
    }

    public void setParkLocation(String parkLocation) {
        this.parkLocation = parkLocation;
    }

    public int getTariffID() {
        return tariffID;
    }

    public void setTariffID(int tariffID) {
        this.tariffID = tariffID;
    }

    public Map<String, Object> getMapForUpdate() {
        Map<String, Object> m = new HashMap<>();

        m.put(B.Keys.CAR_NUMBER, carNumber);
        m.put(B.Keys.BRAND, brand);
        m.put(B.Keys.COLOR, color);
        m.put(B.Keys.IS_YOUNG, isYoung);
        m.put(B.Keys.SIZE, size);
        m.put(B.Keys.PARK_LOCATION, parkLocation);
        m.put(B.Keys._TARIFF_ID, tariffID);

        return m;
    }

    private class Maka{
        private String location;
        private String details;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}

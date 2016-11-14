package rothkoff.baruch.cars;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Car {
    private String carNumber;
    private String brand;
    private String color;
    private boolean isYoung;
    private Map<String,Maka> makot;
    private String parkLocation = "";
    private String tariffUid;
    private Map<String,Rent> rents;

    public Car(){
    }

    public Car(String carNumber, String brand, String color,Tarrif tarrif) {
        this.carNumber = carNumber;
        this.brand = brand;
        this.color = color;
        this.tariffUid = tarrif.getUid();
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

    public boolean getIsYoung() {
        return isYoung;
    }

    public void setIsYoung(boolean isYoung) {
        this.isYoung = isYoung;
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

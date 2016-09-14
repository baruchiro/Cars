package rothkoff.baruch.cars;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Car {
    private String carNumber;
    private String brand;
    private String color;
    private boolean isYoung = false;
    private Map<String,Maka> makot;
    private String parkLocation = "";
    private String tariffUid;
    private Map<String,Long> rentDates;
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

    public Map<String, Long> getRentDates() {
        return rentDates;
    }

    public void setRentDates(Map<String, Long> rentDates) {
        this.rentDates = rentDates;
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

    public double getPrice(Customer customer,Tarrif tarrif) {
        if (customer.getAge()<24){
            if (!isYoung)return 0;
            return tarrif.getPrice()+tarrif.getYoungPrice();
        }return tarrif.getPrice();
    }
    public boolean availableInDate(Calendar dateStart) {

        for (long l : rentDates.values()){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            if (B.CompareWithYearMonthDay(c,dateStart))return false;
        }
        return true;
    }

    public boolean availableInDates(Calendar dateStart, Calendar dateEnd) {
        dateStart = B.getCalenderWithOnlyDate(dateStart);
        dateEnd = B.getCalenderWithOnlyDate(dateEnd);

        while (dateStart.compareTo(dateEnd)<=0){
            if (!availableInDate(dateStart)) return false;
            dateStart.add(Calendar.DATE,1);
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

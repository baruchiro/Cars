package rothkoff.baruch.cars;

public class Rent {
    private long dateStart;
    private long dateEnd;
    private String uid;
    private String customerUid;
    private String customerName;
    private String carNumber;
    private String carBrand;
    private String carColor;
    private double totalPrice;

    public Rent(){}

    public Rent(long dateStart, long dateEnd, Car selectedCar, double totalPrice) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.carNumber = selectedCar.getCarNumber();
        this.carBrand = selectedCar.getBrand();
        this.carColor = selectedCar.getColor();
        this.totalPrice = totalPrice;
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

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}

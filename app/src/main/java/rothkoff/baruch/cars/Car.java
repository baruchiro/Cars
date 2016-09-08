package rothkoff.baruch.cars;

import java.util.Map;

public class Car {
    private String carNumber;
    private String type;
    private String color;
    private int ageMin;
    private int size;
    private Map<String,Maka> makot;
    private String parkLocation;
    private int tariffID;

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
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

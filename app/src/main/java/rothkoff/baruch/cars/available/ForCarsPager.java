package rothkoff.baruch.cars.available;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.Tarrif;

public interface ForCarsPager {
    Calendar getDateStart();

    Calendar getDateEnd();

    boolean isOneDay();

    Tarrif getTarrif(String uid);

    void setSelectedCar(Car car);
}

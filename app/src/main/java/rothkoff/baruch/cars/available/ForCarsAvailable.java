package rothkoff.baruch.cars.available;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.Tarrif;

public interface ForCarsAvailable {
    Calendar getDateStart();
    Calendar getDateEnd();
    boolean isOneDay();
    void setSelectedCar(CarHolder holder, Car car);

    Tarrif getTarrif(String uid);
}

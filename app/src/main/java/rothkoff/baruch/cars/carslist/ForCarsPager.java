package rothkoff.baruch.cars.carslist;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.Tarrif;

public interface ForCarsPager {

    Tarrif getTarrif(String uid);
    void setSelectedCar(Car car);

    Calendar getDateStart();

    Calendar getDateEnd();
}

package rothkoff.baruch.cars.view.carslist;

import java.util.Calendar;

import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Tarrif;

public interface ForCarsPager {

    Tarrif getTarrif(String uid);
    void setSelectedCar(Car car);

    Calendar getDateStart();

    Calendar getDateEnd();
}

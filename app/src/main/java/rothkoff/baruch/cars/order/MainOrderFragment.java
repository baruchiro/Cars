package rothkoff.baruch.cars.order;

import android.content.Context;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.ForUseMainActivity;
import rothkoff.baruch.cars.MyFragment;
import rothkoff.baruch.cars.available.ForCarsPager;

public abstract class MainOrderFragment extends MyFragment implements ForCarsPager {

    protected static Calendar dateStart,dateEnd;
    protected static Car selectedCar;
    protected static boolean isOneDay;
    private double totalPrice;

    public Car getSelectedCar() {
        return selectedCar;
    }

    public abstract int getPageTitle();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForUseMainActivity) {
            mainActivity = (ForUseMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }

        dateStart = Calendar.getInstance();
        dateEnd = Calendar.getInstance();
        isOneDay = true;
    }

    public Calendar getDateStart() {
        return dateStart;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public boolean isOneDay() {
        return isOneDay;
    }

    public void setSelectedCar(Car car) {
        selectedCar = car;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

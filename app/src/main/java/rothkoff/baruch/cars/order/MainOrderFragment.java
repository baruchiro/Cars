package rothkoff.baruch.cars.order;

import android.content.Context;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.ForUseMainActivity;
import rothkoff.baruch.cars.MyFragment;
import rothkoff.baruch.cars.available.ForCarsPager;

public abstract class MainOrderFragment extends MyFragment implements ForCarsPager {

    private static Car selectedCar;
    private static Calendar dateStart;
    private static Calendar dateEnd;

    public abstract int getPageTitle();
    public abstract void Refresh();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForUseMainActivity) {
            mainActivity = (ForUseMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }

        if (dateStart == null) {
            dateStart = Calendar.getInstance();
            dateStart.add(Calendar.DATE, 1);
        }
        if (dateEnd==null){
            dateEnd = Calendar.getInstance();
            dateEnd.add(Calendar.DATE, 1);
        }
    }

    protected boolean setDateStart(int year, int month, int day) {
        dateStart.set(year, month, day);

        if (dateStart.compareTo(dateEnd) > 0) {
            dateEnd.set(year, month, day);
            return true;
        }
        return false;
    }

    public Calendar getDateStart(){
        return dateStart;
    }

    protected boolean setDateEnd(int year, int month, int day) {
        dateEnd.set(year, month, day);

        if (dateStart.compareTo(dateEnd) > 0) {
            dateStart.set(year, month, day);
            return true;
        }
        return false;
    }

    public Calendar getDateEnd(){
        return dateEnd;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }
    public void setSelectedCar(Car car) {
        selectedCar = car;
        Refresh();
    }

    public boolean isAllDone() {
        if (selectedCar!=null) return true;
        return false;
    }
}

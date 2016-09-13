package rothkoff.baruch.cars.order;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.ForCustomerFragments;

public abstract class MainOrderFragment extends Fragment {
    protected ForCustomerFragments mainActivity;

    protected static Calendar dateStart,dateEnd;
    protected static Car selectedCar;
    protected static boolean isOneDay;

    public abstract int getPageTitle();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForCustomerFragments) {
            mainActivity = (ForCustomerFragments) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }
    }
}

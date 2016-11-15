package rothkoff.baruch.cars.cardetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarViewFragment extends Fragment {

    private Car car;

    public CarViewFragment() {
        // Required empty public constructor
    }

    public static CarViewFragment newInstance(Car car){
        CarViewFragment fragment = new CarViewFragment();

        fragment.car = car;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_view, container, false);
    }

}

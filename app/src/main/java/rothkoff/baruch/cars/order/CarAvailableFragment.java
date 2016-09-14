package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.available.CarsAvailablePagerAdapter;
import rothkoff.baruch.cars.available.ForCarsAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarAvailableFragment extends MainOrderFragment implements ForCarsAvailable {

    private ViewPager pager;
    private CarsAvailablePagerAdapter adapter;
    private TabLayout tabLayout;

    public CarAvailableFragment() {
        // Required empty public constructor
    }

    public static CarAvailableFragment newInstance() {
        CarAvailableFragment fragment = new CarAvailableFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_car_available, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        pager = (ViewPager) view.findViewById(R.id.frag_order_caravail_pager);
        adapter = new CarsAvailablePagerAdapter(getChildFragmentManager(),this, mainActivity.getTarrifUids());
        pager.setAdapter(adapter);
        tabLayout = (TabLayout) view.findViewById(R.id.frag_order_caravail_tablayout);
    }
    private void BehaviorMembers(){
        tabLayout.setupWithViewPager(pager,true);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_caravail_title;
    }

    @Override
    public Calendar getDateStart() {
        return dateStart;
    }

    @Override
    public Calendar getDateEnd() {
        return dateEnd;
    }

    @Override
    public boolean isOneDay() {
        return isOneDay;
    }

    @Override
    public void setSelectedCar(CarHolder holder,Car car) {
        selectedCar = car;
        holder.MakeChecked();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.order_caravail_title);
    }
}

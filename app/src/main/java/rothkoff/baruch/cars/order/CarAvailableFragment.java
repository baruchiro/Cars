package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.carslist.CarsPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarAvailableFragment extends MainOrderFragment {

    private ViewPager viewPager;
    private CarsPagerAdapter pagerAdapter;
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
        viewPager = (ViewPager) view.findViewById(R.id.frag_order_caravail_pager);
        pagerAdapter = new CarsPagerAdapter(getChildFragmentManager(), this, mainActivity.getTarrifUids());
        tabLayout = (TabLayout) view.findViewById(R.id.frag_order_caravail_tablayout);
    }

    private void BehaviorMembers() {
        tabLayout.setupWithViewPager(viewPager, true);

        pagerAdapter.setCustomer(B.customer);
        pagerAdapter.UpdateDates();

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    @Override
    public int getPageTitle() {
        return R.string.order_caravail_title;
    }

    @Override
    public void Refresh() {
        pagerAdapter.UpdateDates();
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.order_caravail_title);
    }
}

package rothkoff.baruch.cars.order;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.ForCustomerFragments;
import rothkoff.baruch.cars.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarAvailableFragment extends MainOrderFragment {

    private ForCustomerFragments mainActivity;
    private Car selectedCar;
    private ViewPager pager;
    private TabLayout tabLayout;

    public CarAvailableFragment() {
        // Required empty public constructor
    }

    public static CarAvailableFragment newInstance() {
        CarAvailableFragment fragment = new CarAvailableFragment();

        return fragment;
    }

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
        pager.setAdapter(new PagerAdapter(getChildFragmentManager(), mainActivity.getTarrifUids()));
        tabLayout = (TabLayout) view.findViewById(R.id.frag_order_caravail_tablayout);
    }
    private void BehaviorMembers(){
        tabLayout.setupWithViewPager(pager,true);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_caravail_title;
    }

    private class PagerAdapter extends FragmentPagerAdapter{

        private List<CarsListFragment> fragments;
        private List<String> tarrifsUid;

        public PagerAdapter(FragmentManager fm, List<String> tarrifsUid) {
            super(fm);

            this.tarrifsUid = tarrifsUid;

            fragments = new ArrayList<>();
            for (String s : tarrifsUid)
                fragments.add(CarsListFragment.newInstance(s));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mainActivity.getTarrifName(tarrifsUid.get(position));
        }
    }
}

package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.order.DatesFragment;
import rothkoff.baruch.cars.order.MainOrderFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends MyFragment{

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.frag_order_viewpager);
        pagerAdapter = new PageAdapter(getChildFragmentManager());
    }

    private void BehaviorMembers() {
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.order_title);
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private List<MainOrderFragment> fragments;

        public PageAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(DatesFragment.newInstance());
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
            return getString(fragments.get(position).getPageTitle());
        }
    }
}

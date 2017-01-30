package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Rent;
import rothkoff.baruch.cars.order.CarAvailableFragment;
import rothkoff.baruch.cars.order.DatesFragment;
import rothkoff.baruch.cars.order.MainOrderFragment;
import rothkoff.baruch.cars.order.OrderDetailsFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends MyFragment implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private PageAdapter pagerAdapter;
    private Button btnNext,btnPrevious,btnSend;

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
        btnNext = (Button)view.findViewById(R.id.frag_order_next);
        btnPrevious = (Button)view.findViewById(R.id.frag_order_previous);
        btnSend = (Button)view.findViewById(R.id.frag_order_send);

        viewPager = (ViewPager) view.findViewById(R.id.frag_order_viewpager);
        pagerAdapter = new PageAdapter(getChildFragmentManager());
    }

    private void BehaviorMembers() {
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(pagerAdapter.getCount());

        btnSend.setVisibility(View.GONE);
        btnPrevious.setEnabled(false);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerAdapter.CreateRent();
            }
        });
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.order_title);
    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_order).setChecked(true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = pagerAdapter.getCount();
        MainOrderFragment fragment = (MainOrderFragment) pagerAdapter.getItem(position);

        position += 1;

        btnNext.setVisibility(position == 1 ? View.GONE : View.VISIBLE);
        btnPrevious.setEnabled(position != count);
        btnSend.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(fragment.isAllDone());

        fragment.Refresh();
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class PageAdapter extends FragmentPagerAdapter {

        private List<MainOrderFragment> fragments;

        public PageAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(OrderDetailsFragment.newInstance());
            fragments.add(CarAvailableFragment.newInstance());
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

        public void CreateRent() {
            MainOrderFragment fragment = fragments.get(1);
            Car selectedCar = fragment.getSelectedCar();

            Rent rent = new Rent(fragment.getDateStart().getTimeInMillis(), fragment.getDateEnd().getTimeInMillis(), selectedCar, 0);

            if (rent.AddToDB(B.customer)) mainActivity.ReplaceFragment(MyAccountFragment.newInstance());
        }
    }
}

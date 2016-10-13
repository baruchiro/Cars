package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import rothkoff.baruch.cars.carslist.CarsPagerAdapter;
import rothkoff.baruch.cars.carslist.ForCarsPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageDBFragment extends MyFragment implements ForCarsPager {

    private Button btnAddCar,btnAddTarrif;

    private ViewPager pager;
    private CarsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    public ManageDBFragment() {
        // Required empty public constructor
    }

    public static ManageDBFragment newInstance() {
        ManageDBFragment fragment = new ManageDBFragment();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_db, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }
    private void InitMembers(View view) {
        btnAddCar = (Button) view.findViewById(R.id.frag_manage_btn_addcar);
        btnAddTarrif = (Button) view.findViewById(R.id.frag_manage_btn_addtarrif);

        pager = (ViewPager) view.findViewById(R.id.frag_manage_pager);
        pagerAdapter = new CarsPagerAdapter(getChildFragmentManager(),
                this, mainActivity.getTarrifUids());

        tabLayout = (TabLayout) view.findViewById(R.id.frag_manage_tablayout);
    }

    private void BehaviorMembers() {
        pager.setOffscreenPageLimit(pagerAdapter.getCount());
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager,true);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.ReplaceFragment(AddCarFragment.newInstance());
            }
        });
        btnAddTarrif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.ReplaceFragment(AddTarrifFragment.newInstance(AddTarrifFragment.NEW_TARRIF));
            }
        });
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_managedb).setChecked(true);
    }

    @Override
    public Tarrif getTarrif(String uid) {
        return mainActivity.getTarrifByUid(uid);
    }

    @Override
    public void setSelectedCar(Car car) {

    }

    @Override
    public Calendar getDateStart() {
        return null;
    }

    @Override
    public Calendar getDateEnd() {
        return null;
    }
}

package rothkoff.baruch.cars.carslist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.CarViewHolder;
import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Customer;

public class CarsPagerAdapter extends FragmentPagerAdapter implements CarViewHolder.OnClickListener {

    private ForCarsPager mainFragment;
    private List<CarsListFragment> fragments;
    private List<String> tarrifsUid;

    /**
     * <p>create custom PagerAdapter for show Cars in One Fragment and Tab for Tarrif</p>
     *
     * <p>Fragment or Activity that want to show this ViewPager
     * , <b>must implement "ForCarsAvailable"</b>.
     * mainFragment Parameter should be 'this' (Fragment or Activity).</p>
     *
     * @param fm Fragment Manager (Child)
     * @param mainFragment Object implement ForCarsAvailable
     * @param tarrifsUid The uid of Tarrifs that you want to show
     */
    public CarsPagerAdapter(FragmentManager fm,
                            ForCarsPager mainFragment,
                            List<String> tarrifsUid) {
        super(fm);

        this.tarrifsUid = tarrifsUid;
        this.mainFragment = mainFragment;

        fragments = new ArrayList<>();
        for (String uid : tarrifsUid) {
            CarsListFragment fragment = CarsListFragment.newInstance(this, mainFragment.getTarrif(uid));

            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {

        CarsListFragment fr = fragments.get(position);
        return fr;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mainFragment.getTarrif(tarrifsUid.get(position)).getName();
    }

    public void setCustomer(Customer customer){
        for (CarsListFragment fragment:fragments)
            fragment.setCustomer(customer);
    }

    @Override
    public void onClick(Car car, CarViewHolder holder) {
        mainFragment.setSelectedCar(car);
        for (CarsListFragment fragment : fragments)
            fragment.setChecked(car);
    }

    public void UpdateDates() {
        for (CarsListFragment fragment:fragments)
            fragment.UpdateDates(mainFragment.getDateStart(),mainFragment.getDateEnd());
    }
}

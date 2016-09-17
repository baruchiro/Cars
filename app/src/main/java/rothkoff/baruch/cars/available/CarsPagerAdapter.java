package rothkoff.baruch.cars.available;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.Customer;

public class CarsPagerAdapter extends FragmentPagerAdapter {

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
            CarsListFragment fragment = CarsListFragment.newInstance(mainFragment,mainFragment.getTarrif(uid));

            if (mainFragment.getDateStart() != null) {
                if (mainFragment.isOneDay()) fragment.ShowAvailableInDate(mainFragment.getDateStart());
                else
                    fragment.ShowAvailableInDates(mainFragment.getDateStart(), mainFragment.getDateEnd());
            }

            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        CarsListFragment fr = fragments.get(position);
        if (mainFragment.getDateStart() == null)
            return fr;
        if (mainFragment.isOneDay()) fr.ShowAvailableInDate(mainFragment.getDateStart());
        else fr.ShowAvailableInDates(mainFragment.getDateStart(), mainFragment.getDateEnd());

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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (CarsListFragment f : fragments)
            f.notifyDataSetChanged();
    }

    public void setCustomer(Customer customer){
        for (CarsListFragment fragment:fragments)
            fragment.setCustomer(customer);
    }
}

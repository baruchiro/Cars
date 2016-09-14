package rothkoff.baruch.cars.available;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.B;

public class CarsAvailablePagerAdapter extends FragmentPagerAdapter {

    private ForCarsAvailable mainFragment;
    private List<CarsListFragment> fragments;
    private List<String> tarrifsUid;

    public CarsAvailablePagerAdapter(FragmentManager fm,
                                     ForCarsAvailable mainFragment,
                                     List<String> tarrifsUid) {
        super(fm);

        this.tarrifsUid = tarrifsUid;
        this.mainFragment = mainFragment;

        fragments = new ArrayList<>();
        for (String s : tarrifsUid)
            fragments.add(CarsListFragment.newInstance(mainFragment,s, B.customer));
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
}

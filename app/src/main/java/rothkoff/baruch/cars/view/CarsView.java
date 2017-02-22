package rothkoff.baruch.cars.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rothkoff.baruch.cars.CarsListFragment;
import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Tarrif;


public class CarsView extends ViewPager {

    private FragmentManager fragmentManager;
    private CarsAdapter pagerAdapter;
    private LinkedList<Car> cars;
    private LinkedList<Tarrif> tarrifs;

    public CarsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (context instanceof AppCompatActivity)
            this.fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        else throw new RuntimeException(context.toString() + " Must implement AppCompatActivity");

    }

    public void init(@Nullable List<Car> cars, @Nullable List<Tarrif> tarrifs) {
        if (cars == null) this.cars = new LinkedList<>();
        else
            this.cars = new LinkedList<>(cars);

        if (tarrifs == null) this.tarrifs = new LinkedList<>();
        else
            this.tarrifs = new LinkedList<>(tarrifs);


        pagerAdapter = new CarsAdapter(fragmentManager);
        setAdapter(pagerAdapter);
    }


    public class CarsAdapter extends FragmentPagerAdapter {

        public CarsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return CarsListFragment.newInstance(
                    tarrifs.size() == 0 ? null : tarrifs.get(position),
                    cars.size() == 0 ? null : new ArrayList<>(cars));
        }

        @Override
        public int getCount() {
            if (tarrifs.size() == 0) return 1;
            return tarrifs.size();
        }
    }


    /*public static class CarsListFragment extends Fragment {

        private Tarrif fragmentTarrif;

        static CarsListFragment newInstance(int tarrifPosition) {
            CarsListFragment fragment = new CarsListFragment();

            Bundle args = new Bundle();
            args.putInt("tarrifPosition", tarrifPosition);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                fragmentTarrif = tarrifs.get(getArguments().getInt("tarrifPosition"));
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return new CarsRecycler(getContext(),null,fragmentTarrif.getUid());
        }


    }*/


}


package rothkoff.baruch.cars.design;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.model.Tarrif;

public class CarsByTarrif extends LinearLayout implements ChildEventListener {

    private List<Tarrif> tarrifs;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public CarsByTarrif(Context context, @Nullable List<Tarrif> tarrifs) {
        super(context);

        this.tarrifs = new ArrayList<>();
        if (tarrifs != null)
            this.tarrifs = tarrifs;
        else
            FirebaseDatabase.getInstance().getReference(B.Keys.TARIFFS).addChildEventListener(this);

        addView(LayoutInflater.from(context).inflate(R.layout.view_cars_by_tarrif, this, true));

        tabLayout = (TabLayout) findViewById(R.id.view_carsbytarrif_tablayout);
        viewPager = (ViewPager) findViewById(R.id.view_carsbytarrif_viewpager);

        ViewsAdapter adapter = new ViewsAdapter();
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        tarrifs.add(dataSnapshot.getValue(Tarrif.class));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    private class ViewsAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            CarRecycler carRecycler = new CarRecycler(getContext(), null, tarrifs.get(position));
            collection.addView(carRecycler);
            return carRecycler;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return tarrifs.size();
        }
    }
}

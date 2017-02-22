package rothkoff.baruch.cars;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;

import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Tarrif;

public class CarsListFragment extends Fragment implements ChildEventListener {

    private Tarrif tarrif;
    private ArrayList<Car> fragmentCars;

    public static CarsListFragment newInstance(Tarrif tarrif, @Nullable ArrayList<Car> cars) {
        CarsListFragment fragment = new CarsListFragment();

        Bundle args = new Bundle();
        args.putParcelable("tarrif", tarrif);
        args.putParcelableArrayList("fragmentCars", cars);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tarrif = getArguments().getParcelable("tarrif");
            fragmentCars = getArguments().getParcelableArrayList("fragmentCars");

            if (fragmentCars == null) {
                fragmentCars = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference(B.Keys.CARS).addChildEventListener(this);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (tarrif == null)
            return new CarsRecycler(getContext(), null);

        return new CarsRecycler(getContext(), null, tarrif.getUid());
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        fragmentCars.add(dataSnapshot.getValue(Car.class));
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


    private class CarsRecycler extends RecyclerView {

        private LinkedList<Car> recyclerCars;
        private CarsListAdapter recyclerAdapter;
        private String recyclerTarrifUID;

        public CarsRecycler(Context context, AttributeSet attrs) {
            super(context, attrs);

            this.recyclerCars = new LinkedList<>(fragmentCars);

            recyclerAdapter = new CarsListAdapter();
            setAdapter(recyclerAdapter);

            setHasFixedSize(false);

            LayoutManager layoutManager = new LinearLayoutManager(context, VERTICAL, false);
            setLayoutManager(layoutManager);
        }

        /**
         * Constractor for build recycler with one Tarrif
         *
         * @param context
         * @param attrs
         * @param tarrifUID
         */
        public CarsRecycler(Context context, @Nullable AttributeSet attrs, String tarrifUID) {
            super(context, attrs);

            recyclerTarrifUID = tarrifUID;

            buildList();

            recyclerAdapter = new CarsListAdapter();
            setAdapter(recyclerAdapter);

            setHasFixedSize(false);

            LayoutManager layoutManager = new LinearLayoutManager(context, VERTICAL, false);
            setLayoutManager(layoutManager);
        }

        private void buildList() {
            recyclerCars = new LinkedList<>();
            for (Car c : fragmentCars) {
                if (c.getTariffUid().equals(recyclerTarrifUID)) {
                    recyclerCars.add(c);
                }
            }
        }

        private class CarsListAdapter extends RecyclerView.Adapter<CarViewHolder> {


            @Override
            public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.item_car, parent, false);
                return new CarViewHolder(v);
            }

            @Override
            public int getItemCount() {
                return recyclerCars.size();
            }

            @Override
            public void onBindViewHolder(CarViewHolder holder, int position) {
                holder.init(recyclerCars.get(position));
            }
        }

        private class CarViewHolder extends RecyclerView.ViewHolder {

            private Car car;
            private TextView tvBrand, tvColor, tvYoung, tvNumber;

            public CarViewHolder(View itemView) {
                super(itemView);
                tvColor = (TextView) itemView.findViewById(R.id.item_car_color);
                tvBrand = (TextView) itemView.findViewById(R.id.item_car_brand);
                tvYoung = (TextView) itemView.findViewById(R.id.item_car_young);
                tvNumber = (TextView) itemView.findViewById(R.id.item_car_number);
            }

            public void init(Car car) {
                this.car = car;
                tvColor.setText(car.getColor());
                tvBrand.setText(car.getBrand());
                tvNumber.setText(car.getCarNumber());
                if (car.getIsYoung()) tvYoung.setVisibility(View.VISIBLE);
            }
        }
    }


}

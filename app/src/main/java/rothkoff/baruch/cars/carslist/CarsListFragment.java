package rothkoff.baruch.cars.carslist;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.CarViewHolder;
import rothkoff.baruch.cars.ForUseMainActivity;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Customer;
import rothkoff.baruch.cars.model.Tarrif;

public class CarsListFragment extends Fragment {

    private TextView tvSeatCount, tvEngine, tvPrice;

    private RecyclerView recyclerView;
    private CarsListAdapter listAdapter;
    private TextView emptyView;
    private Tarrif tarrif;
    private ForUseMainActivity mainActivity;
    private CarsPagerAdapter parentPagerAdapter;
    private Customer customer;
    private Calendar dateStart;
    private Calendar dateEnd;

    public CarsListFragment() {
        // Required empty public constructor
    }

    public static CarsListFragment newInstance(CarsPagerAdapter parentPagerAdapter, Tarrif tarrif) {
        CarsListFragment carsListFragment = new CarsListFragment();

        carsListFragment.tarrif = tarrif;
        carsListFragment.parentPagerAdapter = parentPagerAdapter;

        return carsListFragment;
    }

    public static CarsListFragment newInstance(CarsPagerAdapter parentPagerAdapter, Tarrif tarrif, Customer customer) {
        CarsListFragment carsListFragment = CarsListFragment.newInstance(parentPagerAdapter, tarrif);

        carsListFragment.customer = customer;

        return carsListFragment;
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForUseMainActivity) {
            mainActivity = (ForUseMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForUseMainActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars_list, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        tvSeatCount = (TextView) view.findViewById(R.id.frag_carslist_seacount);
        tvEngine = (TextView) view.findViewById(R.id.frag_carslist_engine);
        tvPrice = (TextView) view.findViewById(R.id.frag_carslist_price);

        recyclerView = (RecyclerView) view.findViewById(R.id.frag_carslist_recycler);
        listAdapter = new CarsListAdapter();

        emptyView = (TextView) view.findViewById(R.id.frag_carslist_emptylist);
    }

    private void BehaviorMembers() {
        if (tarrif != null) {
            tvSeatCount.setText(String.valueOf(tarrif.getSeatCount()));
            tvEngine.setText(String.valueOf(tarrif.getEngineCapacity()));

            CalculateTotalPrice();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.Bind();

        recyclerView.setAdapter(listAdapter);
    }

    private void CalculateTotalPrice() {
        if (dateStart != null && dateEnd != null) {
            tvPrice.setText(String.valueOf(tarrif.getCalculatePrice(dateStart, dateEnd, customer.getAge() < B.Constants.YOUNG_AGE)) + " " + getString(R.string.NIS));
        } else {
            tvPrice.setText(String.valueOf(tarrif.getPrice()) + " " + getString(R.string.NIS));
        }
    }

    public void OnDataChange() {
        emptyView.setVisibility(listAdapter.cars.size() == 0 ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(listAdapter.cars.size() == 0 ? View.GONE : View.VISIBLE);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setChecked(Car car) {
        if (listAdapter != null) {
            listAdapter.setSelectedCar(car);
            listAdapter.Bind();
        } else
            throw new NullPointerException(this.toString() + " is not load. you need to call to 'setOffscreenPageLimit' method from ViewPger object");
    }

    public void UpdateDates(Calendar dateStart, Calendar dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;

        if (mainActivity != null) CalculateTotalPrice();

        if (listAdapter != null)
            listAdapter.Bind();
    }

    private class CarsListAdapter extends RecyclerView.Adapter<CarViewHolder>
            implements ValueEventListener {

        private List<Car> cars;
        private DatabaseReference carsRef;
        private Car selectedCar;

        /**
         * <p>Constractor</p>
         * <p><b>Don't forget to call the 'Bind' method!!</b></p>
         */
        public CarsListAdapter() {

            cars = new ArrayList<>();
            carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);
        }

        @Override
        public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_car, parent, false);
            return new CarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CarViewHolder holder, int position) {
            Car c = cars.get(position);
            holder.Init(mainActivity.getContext(), parentPagerAdapter, c);

            if (selectedCar != null)
                holder.MakeChecked(c.getCarNumber().equals(selectedCar.getCarNumber()));
        }

        @Override
        public int getItemCount() {
            return cars.size();
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            this.cars = new ArrayList<>();

            for (DataSnapshot d : dataSnapshot.getChildren()) {
                Car car = d.getValue(Car.class);

                //if car is in this tarrif
                //TODO Error?
                boolean isInTarrif = tarrif == null || car.getTariffUid().equals(tarrif.getUid());

                //if custome old from YOUNG_AGE or Car is young
                boolean isYoung = customer == null;
                if (!isYoung) {
                    isYoung = customer.getAge() >= B.Constants.YOUNG_AGE || car.getIsYoung();
                }

                //car avalibale in dates
                boolean isDates = dateStart == null;
                if (!isDates) {
                    isDates = car.availableInDates(dateStart, dateEnd);
                }

                //check all
                if (isInTarrif && isYoung && isDates)
                    cars.add(car);
            }

            notifyMyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        public Car getCarInPosition(int position) {
            return cars.get(position);
        }

        private void notifyMyDataSetChanged() {
            notifyDataSetChanged();
            CarsListFragment.this.OnDataChange();
        }

        /**
         * <p>You must call this method if you want that this Adapter load his data</p>
         * <p>So call this after you end setup the Adapter</p>
         */
        private void Bind() {
            carsRef.addListenerForSingleValueEvent(this);
        }

        public void setSelectedCar(Car selectedCar) {
            this.selectedCar = selectedCar;
        }
    }
}
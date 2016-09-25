package rothkoff.baruch.cars.available;


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
import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.Customer;
import rothkoff.baruch.cars.ForUseMainActivity;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.Tarrif;

public class CarsListFragment extends Fragment {

    private TextView tvSeatCount,tvEngine,tvPrice,tvYoungPrice;

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

    public static CarsListFragment newInstance(CarsPagerAdapter parentPagerAdapter,Tarrif tarrif) {
        CarsListFragment carsListFragment = new CarsListFragment();

        carsListFragment.tarrif = tarrif;
        carsListFragment.parentPagerAdapter = parentPagerAdapter;

        return carsListFragment;
    }

    public static CarsListFragment newInstance(CarsPagerAdapter parentPagerAdapter,Tarrif tarrif, Customer customer) {
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
        View view = inflater.inflate(R.layout.fragment_cars_list,container,false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view){
        tvSeatCount = (TextView)view.findViewById(R.id.frag_carslist_seacount);
        tvEngine = (TextView)view.findViewById(R.id.frag_carslist_engine);
        tvPrice = (TextView)view.findViewById(R.id.frag_carslist_price);
        tvYoungPrice = (TextView)view.findViewById(R.id.frag_carslist_youngprice);

        recyclerView =(RecyclerView)view.findViewById(R.id.frag_carslist_recycler);
        listAdapter = new CarsListAdapter();

        emptyView = (TextView)view.findViewById(R.id.frag_carslist_emptylist);
    }

    private void BehaviorMembers() {
        if (tarrif != null) {
            tvSeatCount.setText(String.valueOf(tarrif.getSeatCount()));
            tvEngine.setText(String.valueOf(tarrif.getEngineCapacity()));
            tvPrice.setText(String.valueOf(tarrif.getPrice()) + " " + getString(R.string.NIS));
            tvYoungPrice.setText(String.valueOf(tarrif.getYoungPrice()) + " " + getString(R.string.NIS));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.Bind();

        recyclerView.setAdapter(listAdapter);
    }

    public void OnDataChange() {
        emptyView.setVisibility(listAdapter.cars.size() == 0 ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(listAdapter.cars.size() == 0 ? View.GONE : View.VISIBLE);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setChecked(Car car) {
        if (listAdapter!=null) {
            listAdapter.setSelectedCar(car);
            listAdapter.Bind();
        }else throw new NullPointerException(this.toString() + " is not load. you need to call to 'setOffscreenPageLimit' method from ViewPger object");
    }

    public void UpdateDates(Calendar dateStart, Calendar dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        listAdapter.Bind();
    }

    private class CarsListAdapter extends RecyclerView.Adapter<CarHolder>
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
        public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_car, parent, false);
            return new CarHolder(view);
        }

        @Override
        public void onBindViewHolder(CarHolder holder, int position) {
            Car c = cars.get(position);
            holder.Init(mainActivity, parentPagerAdapter, c);
            if (customer != null)
                holder.ShowPrice(customer);

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

                boolean isInTarrif = tarrif == null || car.getTariffUid().equals(tarrif.getUid());

                boolean isCustomer = customer == null ||
                        car.getPrice(customer, mainActivity.getTarrifByUid(car.getTariffUid())) != 0;

                boolean isDates = dateStart == null;
                if (!isDates){
                    isDates = car.availableInDates(dateStart,dateEnd);
                }


                if (isInTarrif && isCustomer && isDates)
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

        public void notifyMyDataSetChanged() {
            notifyDataSetChanged();
            CarsListFragment.this.OnDataChange();
        }

        /**
         * <p>You must call this method if you want that this Adapter load his data</p>
         * <p>So call this after you end setup the Adapter</p>
         */
        public void Bind() {
            carsRef.addListenerForSingleValueEvent(this);
        }

        public void setSelectedCar(Car selectedCar) {
            this.selectedCar = selectedCar;
        }
    }
}
package rothkoff.baruch.cars.available;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.Customer;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.Tarrif;

public class CarsListFragment extends Fragment implements View.OnClickListener, CarsListAdapter.OnDataChangeListener {

    private TextView tvSeatCount,tvEngine,tvPrice,tvYoungPrice;

    private RecyclerView recyclerView;
    private CarsListAdapter adapter;
    private TextView emptyView;
    private Tarrif tarrif;
    private ForCarsPager parentFragment;
    private Customer customer;
    private Calendar dateStart;
    private Calendar dateEnd;

    public CarsListFragment() {
        // Required empty public constructor
    }

    public static CarsListFragment newInstance(ForCarsPager fragment, Tarrif tarrif) {
        CarsListFragment carsListFragment = new CarsListFragment();

        carsListFragment.tarrif = tarrif;
        carsListFragment.parentFragment = fragment;

        return carsListFragment;
    }

    public static CarsListFragment newInstance(ForCarsPager fragment, Tarrif tarrif, Customer customer) {
        CarsListFragment carsListFragment = CarsListFragment.newInstance(fragment, tarrif);

        carsListFragment.customer = customer;

        return carsListFragment;
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
        adapter = new CarsListAdapter(getContext(),this);

        emptyView = (TextView)view.findViewById(R.id.frag_carslist_emptylist);
    }

    private void BehaviorMembers() {
        if (tarrif!=null){
            tvSeatCount.setText(String.valueOf(tarrif.getSeatCount()));
            tvEngine.setText(String.valueOf(tarrif.getEngineCapacity()));
            tvPrice.setText(String.valueOf(tarrif.getPrice())+" "+getString(R.string.NIS));
            tvYoungPrice.setText(String.valueOf(tarrif.getYoungPrice())+" "+getString(R.string.NIS));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter.setOnDataChangeListener(this);
        UpdateAdapter();

        recyclerView.setAdapter(adapter);
    }

    private void UpdateAdapter() {
        if (adapter != null) {
            adapter.setTarrif(tarrif);
            adapter.setCustomer(customer);
            adapter.setDateStart(dateStart);
            adapter.setDateEnd(dateEnd);

            adapter.Bind();
        }
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);

        CarHolder holder = (CarHolder) recyclerView.findViewHolderForAdapterPosition(position);
        Car car = adapter.getCarInPosition(position);
        parentFragment.setSelectedCar(holder,car);
    }

    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyMyDataSetChanged();
        else
            throw new NullPointerException(this.toString() + " is not load. you need to call to 'setOffscreenPageLimit' method from ViewPger object");
    }

    @Override
    public void OnDataChange(List<Car> data) {
        emptyView.setVisibility(data.size()==0?View.VISIBLE:View.GONE);
        recyclerView.setVisibility(data.size()==0?View.GONE:View.VISIBLE);
    }

    public void ShowAvailableInDate(Calendar dateStart) {
        this.dateStart = dateStart;
        UpdateAdapter();
    }

    public void ShowAvailableInDates(Calendar dateStart, Calendar dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        UpdateAdapter();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        UpdateAdapter();
    }
}

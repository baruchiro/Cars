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
import rothkoff.baruch.cars.CarsAvailableAdapter;
import rothkoff.baruch.cars.Customer;
import rothkoff.baruch.cars.R;

public class CarsListFragment extends Fragment implements View.OnClickListener, CarsAvailableAdapter.OnDataChangeListener {

    private RecyclerView recyclerView;
    private CarsAvailableAdapter adapter;
    private TextView emptyView;
    private String tarrifUid;
    private ForCarsAvailable parentFragment;
    private Customer customer;
    private Calendar dateStart;
    private Calendar dateEnd;

    public CarsListFragment() {
        // Required empty public constructor
    }

    public static CarsListFragment newInstance(ForCarsAvailable fragment,String tarrifUid) {
        CarsListFragment carsListFragment = new CarsListFragment();

        carsListFragment.tarrifUid = tarrifUid;
        carsListFragment.parentFragment = fragment;

        return carsListFragment;
    }

    public static CarsListFragment newInstance(ForCarsAvailable fragment, String tarrifUid, Customer customer) {
        CarsListFragment carsListFragment = CarsListFragment.newInstance(fragment, tarrifUid);

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
        recyclerView =(RecyclerView)view.findViewById(R.id.frag_carslist_recycler);
        adapter = new CarsAvailableAdapter(getContext(),this,tarrifUid);

        emptyView = (TextView)view.findViewById(R.id.frag_carslist_emptylist);
    }

    private void BehaviorMembers() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter.setOnDataChangeListener(this);
        UpdateAdapter();

        recyclerView.setAdapter(adapter);
    }

    private void UpdateAdapter(){
        if (adapter!=null) {
            if (customer != null) adapter.showPrices(customer);
            if (dateStart != null) {
                if (dateEnd != null) adapter.ShowAvailableInDates(dateStart, dateEnd);
                else adapter.ShowAvailableInDate(dateStart);
            }
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnDataChange(List<Car> data) {
        emptyView.setVisibility(data.size()==0?View.VISIBLE:View.GONE);
        recyclerView.setVisibility(data.size()==0?View.GONE:View.VISIBLE);
    }

    public void ShowAvailableInDate(Calendar dateStart) {
        this.dateStart = dateStart;
    }

    public void ShowAvailableInDates(Calendar dateStart, Calendar dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}

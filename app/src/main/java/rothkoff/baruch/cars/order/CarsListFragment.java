package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.CarsAvailableAdapter;

public class CarsListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private CarsAvailableAdapter adapter;
    private String tarrifUid;
    private ForTarrifsListsFragment parentFragment;

    public CarsListFragment() {
        // Required empty public constructor
    }

    public static CarsListFragment newInstance(ForTarrifsListsFragment fragment,String tarrifUid) {
        CarsListFragment carsListFragment = new CarsListFragment();

        carsListFragment.tarrifUid = tarrifUid;
        carsListFragment.parentFragment = fragment;

        return carsListFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        adapter = new CarsAvailableAdapter(getContext(),this,tarrifUid);
        recyclerView.setAdapter(adapter);


        return recyclerView;
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

    public interface ForTarrifsListsFragment{
        void setSelectedCar(CarHolder holder,Car car);
    }
}

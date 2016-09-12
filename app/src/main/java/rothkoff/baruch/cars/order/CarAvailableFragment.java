package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import rothkoff.baruch.cars.CarsAvailableAdapter;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.Tarrif;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarAvailableFragment extends MainOrderFragment {

    private Button btnSmall,btnMedium,btnLarge;
    private RecyclerView recyclerView;
    private CarsAvailableAdapter adapter;

    public CarAvailableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_car_available, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view){
        btnSmall = (Button)view.findViewById(R.id.frag_ordercaravail_btn_small);
        btnMedium = (Button)view.findViewById(R.id.frag_ordercaravail_btn_medium);
        btnLarge = (Button)view.findViewById(R.id.frag_ordercaravail_btn_large);

        recyclerView = (RecyclerView)view.findViewById(R.id.frag_ordercaravail_recycle);
        adapter = new CarsAvailableAdapter(getContext());
    }
    private void BehaviorMembers(){
        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClick(Tarrif.SMALL);
            }
        });
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClick(Tarrif.MEDIUM);
            }
        });
        btnLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClick(Tarrif.LARGE);
            }
        });

        adapter.setTariffToShow(Tarrif.MEDIUM);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true));
        recyclerView.setAdapter(adapter);
    }

    private void OnClick(int tariff) {
        adapter.setTariffToShow(tariff);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_caravail_title;
    }
}

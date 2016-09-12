package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.CarsAvailableAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarsAvailableAdapter adapter;
    private String tarrifUid;

    public CarsListFragment() {
        // Required empty public constructor
    }

    public static CarsListFragment newInstance(String tarrifUid) {
        CarsListFragment fragment = new CarsListFragment();
        fragment.tarrifUid = tarrifUid;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true));

        adapter = new CarsAvailableAdapter(getContext(),tarrifUid);
        recyclerView.setAdapter(adapter);


        return recyclerView;
    }


}

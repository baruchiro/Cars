package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends MainOrderFragment {


    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_details;
    }

    @Override
    public void Refresh() {

    }

    private void BehaviorMembers() {

    }

    @Override
    public void setTitle() {

    }
}

package rothkoff.baruch.cars.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.view.CustomersView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCustomerFragment extends MainOrderFragment {

    private CustomersView customersView;

    public SelectCustomerFragment() {
        // Required empty public constructor
    }

    public static MainOrderFragment newInstance() {
        SelectCustomerFragment fragment = new SelectCustomerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_customer, container, false);

        customersView = (CustomersView) view.findViewById(R.id.frag_selectust_myrecycler);

        return view;
    }

    @Override
    public int getPageTitle() {
        return R.string.select_customer_title;
    }

    @Override
    public void Refresh() {

    }

    @Override
    public void setTitle() {

    }
}

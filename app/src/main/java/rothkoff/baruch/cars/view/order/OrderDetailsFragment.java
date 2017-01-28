package rothkoff.baruch.cars.view.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends MainOrderFragment {

    private TextView tvDates,tvCar;
    private SimpleDateFormat formatter;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(){
        OrderDetailsFragment fragment = new OrderDetailsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view =  inflater.inflate(R.layout.fragment_order_details, container, false);

        InitMembers(view);
        BehaviorMembers();
        Refresh();

        return view;
    }

    private void InitMembers(View view) {
        formatter = new SimpleDateFormat("dd/MM/yyyy");

        tvDates = (TextView)view.findViewById(R.id.frag_orderdetails_dates);
        tvCar = (TextView)view.findViewById(R.id.frag_orderdetails_car);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_details;
    }

    @Override
    public void Refresh() {
        if (B.CompareWithYearMonthDay(getDateStart(),getDateEnd())) tvDates.setText(formatter.format(getDateStart().getTime()));
        else tvDates.setText(formatter.format(getDateStart().getTime())+" - "+formatter.format(getDateEnd().getTime()));

        if (getSelectedCar()!=null) tvCar.setText(getSelectedCar().getBrand()+" "+getSelectedCar().getColor()+" "+getSelectedCar().getParkLocation());
    }

    private void BehaviorMembers() {

    }

    @Override
    public void setTitle() {

    }
}

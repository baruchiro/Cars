package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RentViewHolder extends RecyclerView.ViewHolder {

    private Rent rent;
    private TextView tvCustomer, tvStartDate, tvEndDate, tvCar;
    //private Context context;

    public RentViewHolder(View itemView) {
        super(itemView);

        tvCustomer = (TextView) itemView.findViewById(R.id.item_rent_customer);
        tvStartDate = (TextView) itemView.findViewById(R.id.item_rent_start_date);
        tvEndDate = (TextView) itemView.findViewById(R.id.item_rent_end_date);
        tvCar = (TextView) itemView.findViewById(R.id.item_rent_car);
    }

    public void setRent(Context context, Rent rent,boolean showCustomerName) {
        this.rent = rent;
        //this.context = context;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        tvStartDate.setText(formatter.format(new Date(rent.getDateStart())));

        if ((Long) rent.getDateEnd() != null)
            tvEndDate.setText(formatter.format(new Date(rent.getDateEnd())));

        tvCar.setText(rent.getCarBrand() + " " + rent.getCarColor() + ". " + context.getResources().getString(R.string.car_number) + ": " + rent.getCarNumber());

        if (showCustomerName) {
            tvCustomer.setText(rent.getCustomerName());
            tvCustomer.setVisibility(View.VISIBLE);
        }
    }
}

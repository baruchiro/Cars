package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RentViewHolder extends RecyclerView.ViewHolder {

    private Rent rent;
    private TextView tvCustomer, tvDate, tvCar;
    private Context context;

    public RentViewHolder(View itemView) {
        super(itemView);

        tvCustomer = (TextView) itemView.findViewById(R.id.item_rent_customer);
        tvDate = (TextView) itemView.findViewById(R.id.item_rent_date);
        tvCar = (TextView) itemView.findViewById(R.id.item_rent_car);
    }

    public void setRent(Context context,Rent rent) {
        this.rent = rent;
        this.context =context;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String temp = formatter.format(new Date(rent.getDateStart()));
        if ((Long) rent.getDateEnd() != null)
            temp += " - " + formatter.format(new Date(rent.getDateEnd()));

        tvDate.setText(temp);

        temp = rent.getCarBrand()+" "+rent.getCarColor()+context.getResources().getString(R.string.car_number)+": "+rent.getCarNumber();
        tvCar.setText(temp);
    }
    public void setRent(Context context,Rent rent, boolean showCustomerName) {
        setRent(context,rent);
        if (showCustomerName){
            tvCustomer.setText(rent.getCustomerName());
        }
    }
}

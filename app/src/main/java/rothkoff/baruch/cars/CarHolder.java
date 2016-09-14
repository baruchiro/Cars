package rothkoff.baruch.cars;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class CarHolder extends RecyclerView.ViewHolder {
    private View view;
    private ForUseMainActivity context;
    //private boolean checked;
    private Car car;
    private TextView etBrand, etColor, etPrice;

    public CarHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        etColor = (TextView) view.findViewById(R.id.item_car_color);
        etBrand = (TextView) view.findViewById(R.id.item_car_type);
        etPrice = (TextView)view.findViewById(R.id.iten_car_price);
    }

    public void Init(ForUseMainActivity context, View.OnClickListener listener, Car car) {
        this.context = context;
        this.car = car;
        etColor.setText(car.getColor());
        etBrand.setText(car.getBrand());

        view.setOnClickListener(listener);
    }

    public double ShowPrice(Customer customer){
        Tarrif t = context.getTarrifByUid(car.getTariffUid());
        double price = car.getPrice(customer,t);
            etPrice.setText(price + " " + context.getString(R.string.NIS));
            etPrice.setVisibility(View.VISIBLE);
        return price;
    }

    public void MakeChecked() {
        view.setBackgroundColor(Color.GRAY);
    }
}

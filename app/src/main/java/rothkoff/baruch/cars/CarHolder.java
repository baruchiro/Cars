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
    private TextView tvBrand, tvColor, tvPrice, tvYoung;

    public CarHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        tvColor = (TextView) view.findViewById(R.id.item_car_color);
        tvBrand = (TextView) view.findViewById(R.id.item_car_type);
        tvPrice = (TextView) view.findViewById(R.id.item_car_price);
        tvYoung = (TextView) view.findViewById(R.id.item_car_young);
    }

    public void Init(ForUseMainActivity context, View.OnClickListener listener, Car car) {
        this.context = context;
        this.car = car;
        tvColor.setText(car.getColor());
        tvBrand.setText(car.getBrand());
        if (car.getIsYoung()) tvYoung.setVisibility(View.VISIBLE);

        view.setOnClickListener(listener);
    }

    public double ShowPrice(Customer customer){
        Tarrif t = context.getTarrifByUid(car.getTariffUid());
        double price = car.getPrice(customer,t);
            tvPrice.setText(price + " " + context.getString(R.string.NIS));
            tvPrice.setVisibility(View.VISIBLE);
        return price;
    }

    public void MakeChecked() {
        view.setBackgroundColor(Color.GRAY);
    }
}

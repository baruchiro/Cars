package rothkoff.baruch.cars;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class CarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View view;
    private ForUseMainActivity context;
    private OnClickListener listener;
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

    public void Init(ForUseMainActivity context, OnClickListener listener, Car car) {
        this.context = context;
        this.car = car;
        this.listener = listener;
        tvColor.setText(car.getColor());
        tvBrand.setText(car.getBrand());
        if (car.getIsYoung()) tvYoung.setVisibility(View.VISIBLE);

        view.setOnClickListener(this);
    }

    public double ShowPrice(Customer customer){
        Tarrif t = context.getTarrifByUid(car.getTariffUid());
        double price = car.getPrice(customer,t);
            tvPrice.setText(price + " " + context.getString(R.string.NIS));
            tvPrice.setVisibility(View.VISIBLE);
        return price;
    }

    public void MakeChecked(boolean checked) {
        if (checked) {
            view.setBackgroundColor(Color.GRAY);
        }else view.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        if (listener!=null) listener.onClick(car,this);
    }

    public interface OnClickListener{
        void onClick(Car car,CarHolder holder);
    }
}

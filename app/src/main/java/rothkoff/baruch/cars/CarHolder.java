package rothkoff.baruch.cars;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class CarHolder extends RecyclerView.ViewHolder {
    private View view;
    private Car car;
    TextView carTypeTxt, carColorTxt;

    public CarHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        carColorTxt = (TextView)view.findViewById(R.id.item_car_color);
        carTypeTxt = (TextView)view.findViewById(R.id.item_car_type);
    }

    public void setCar(Car car) {
        this.car = car;
        carColorTxt.setText(car.getColor());
        carTypeTxt.setText(car.getBrand());
    }
}

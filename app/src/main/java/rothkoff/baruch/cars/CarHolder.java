package rothkoff.baruch.cars;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class CarHolder extends RecyclerView.ViewHolder {
    private View view;
    private boolean checked;
    private Car car;
    private TextView carTypeTxt, carColorTxt;

    public CarHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        carColorTxt = (TextView) view.findViewById(R.id.item_car_color);
        carTypeTxt = (TextView) view.findViewById(R.id.item_car_type);
    }

    public void Init(View.OnClickListener listener, Car car, String tariffToShow) {
        Init(listener,car);

        if ( tariffToShow.equals(car.getTariffUid()))
            view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }
    public void Init(View.OnClickListener listener, Car car){
        this.car = car;
        carColorTxt.setText(car.getColor());
        carTypeTxt.setText(car.getBrand());

        view.setOnClickListener(listener);
    }

    public void MakeChecked() {
        view.setBackgroundColor(Color.GRAY);
    }
}

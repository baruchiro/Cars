package rothkoff.baruch.cars.cardetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rothkoff.baruch.cars.R;

public class CarView{

    private Context context;
    private View view;

    public CarView(Context context,ViewGroup parent) {
        this.context = context;
        this.view = LayoutInflater.from(this.context).inflate(R.layout.item_car, parent, true);
    }
}

package rothkoff.baruch.cars.design;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;
import rothkoff.baruch.cars.XCarViewHolder;
import rothkoff.baruch.cars.model.Car;
import rothkoff.baruch.cars.model.Tarrif;
import rothkoff.baruch.cars.view.CarsView;

/**
 * this Class is View (RecyclerView) that you can add to your Activity.
 * the Recycler show list of cars.
 * you can set listener to listen of Car selected Event.
 */
public class CarRecycler extends RecyclerView implements ChildEventListener {

    private List<Car> cars;
    private Tarrif tarrif;
    private CarsAdapter adapter;

    /**
     * Constractor
     *
     * @param context
     * @param cars    list of cars that you want to show. if Null, the recycler retrive list from Firebase by Tarrif.
     * @param tarrif  if Null, Recycler show all cars.
     */
    public CarRecycler(Context context, @Nullable List<Car> cars, @Nullable Tarrif tarrif) {
        super(context);
        this.tarrif = tarrif;
        adapter = new CarsAdapter();

        if (cars == null) {         //if cars Null- Build the list
            this.cars = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference(B.Keys.CARS).addChildEventListener(this);
        } else {                    //if cars not Null- get all cars or make this by Tarrif
            if (this.tarrif != null) {
                for (Car car : cars) {
                    if (car.getTariffUid().equals(tarrif.getUid()))
                        this.cars.add(car);
                }
            } else {
                this.cars = cars;
            }
        }

        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        setAdapter(adapter);
    }


    public void setSelectedCar(String carID) {
    }

    public void setSelectedCar(Car car) {
    }

    public Car getSelectedCar() {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Car c = dataSnapshot.getValue(Car.class);
        if (tarrif == null) {
            cars.add(c);
        } else {
            if (c.getTariffUid().equals(tarrif.getUid()))
                cars.add(c);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private class CarsAdapter extends RecyclerView.Adapter<CarViewHolder> {

        @Override
        public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_car, parent, false);
            return new CarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CarViewHolder holder, int position) {
            holder.Init(cars.get(position));
        }

        @Override
        public int getItemCount() {
            return cars.size();
        }
    }

    public class CarViewHolder extends ViewHolder {

        private Car car;
        private TextView tvBrand, tvColor, tvYoung, tvNumber;

        public CarViewHolder(View itemView) {
            super(itemView);
            tvColor = (TextView) itemView.findViewById(R.id.item_car_color);
            tvBrand = (TextView) itemView.findViewById(R.id.item_car_brand);
            tvYoung = (TextView) itemView.findViewById(R.id.item_car_young);
            tvNumber = (TextView) itemView.findViewById(R.id.item_car_number);
        }

        public void Init(Car car) {
            this.car = car;
            tvColor.setText(car.getColor());
            tvBrand.setText(car.getBrand());
            tvNumber.setText(car.getCarNumber());
            if (car.getIsYoung()) tvYoung.setVisibility(View.VISIBLE);
        }
    }
}

package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarsAvailableAdapter extends RecyclerView.Adapter<CarHolder> implements ValueEventListener {

    private Context context;
    private List<Car> cars;
    private DatabaseReference carsRef;
    private int tariffToShow;

    public CarsAvailableAdapter(Context context) {
        this.context = context;
        tariffToShow = 0;
        cars = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);

        carsRef.addListenerForSingleValueEvent(this);
    }
    public CarsAvailableAdapter(Context context,int tariffToShow){
        this.context = context;
        this.tariffToShow = tariffToShow;
        cars = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);

        carsRef.addListenerForSingleValueEvent(this);
    }
    @Override
    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car,parent,false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(CarHolder holder, int position) {
        holder.setCar(cars.get(position),tariffToShow);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d : dataSnapshot.getChildren()){
            cars.add(d.getValue(Car.class));
        }
        notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setTariffToShow(int tariffToShow) {
        this.tariffToShow = tariffToShow;
        notifyDataSetChanged();
    }
}

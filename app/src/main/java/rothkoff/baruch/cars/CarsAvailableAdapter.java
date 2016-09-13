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
import java.util.Calendar;
import java.util.List;

public class CarsAvailableAdapter extends RecyclerView.Adapter<CarHolder>
        implements ValueEventListener {

    private ForCustomerFragments context;
    private List<Car> cars;
    private DatabaseReference carsRef;
    private String tariffToShow;
    private View.OnClickListener onClickListener;
    Car selectedCar= null;
    private Customer customer;
    private OnDataChangeListener onDataChangeListener;
    private Calendar dateStart;
    private Calendar dateEnd;

    public CarsAvailableAdapter(Context context, View.OnClickListener onClickListener) {
        if (context instanceof  ForCustomerFragments) this.context = (ForCustomerFragments) context;
        else throw new RuntimeException(context.toString()
                + " must implement ForCustomerFragments");
        this.onClickListener = onClickListener;
        cars = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);

        carsRef.addListenerForSingleValueEvent(this);
    }

    public CarsAvailableAdapter(Context context, View.OnClickListener onClickListener,String tariffToShow){
        if (context instanceof  ForCustomerFragments) this.context = (ForCustomerFragments) context;
        else throw new RuntimeException(context.toString()
                + " must implement ForCustomerFragments");
        this.onClickListener = onClickListener;
        this.tariffToShow = tariffToShow;
        cars = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);

        carsRef.addListenerForSingleValueEvent(this);
    }
    @Override
    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_car,parent,false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(CarHolder holder, int position) {
        holder.Init(context, onClickListener, cars.get(position));
        if (customer != null)
            holder.ShowPrice(customer);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (tariffToShow == null) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                Car car = d.getValue(Car.class);
                if (dateStart != null) {
                    if (dateEnd != null) {
                        if (car.availableInDates(dateStart, dateEnd)) cars.add(car);
                    } else if (car.availableInDate(dateStart)) cars.add(car);
                } else
                    cars.add(car);
            }
        } else {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                Car car = d.getValue(Car.class);
                if (tariffToShow.equals(car.getTariffUid()))
                    if (car.getPrice(customer, context.getTarrifByUid(car.getTariffUid())) != 0)
                    cars.add(car);
            }
        }

        notifyMyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public Car getCarInPosition(int position) {
        return cars.get(position);
    }

    public void showPrices(Customer customer) {
        this.customer = customer;

        carsRef.addListenerForSingleValueEvent(this);
    }

    public void setOnDataChangeListener(OnDataChangeListener listener){
        this.onDataChangeListener = listener;
    }

    public void notifyMyDataSetChanged(){
        notifyDataSetChanged();
        if (onDataChangeListener!=null)onDataChangeListener.OnDataChange(cars);
    }

    public void ShowAvailableInDate(Calendar dateStart) {
        this.dateStart = dateStart;
        carsRef.addListenerForSingleValueEvent(this);
    }

    public void ShowAvailableInDates(Calendar dateStart, Calendar dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        carsRef.addListenerForSingleValueEvent(this);
    }

    public interface OnDataChangeListener{
        void OnDataChange(List<Car> data);
    }
}

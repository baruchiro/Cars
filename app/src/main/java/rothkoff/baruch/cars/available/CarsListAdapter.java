package rothkoff.baruch.cars.available;

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

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.Car;
import rothkoff.baruch.cars.CarHolder;
import rothkoff.baruch.cars.Customer;
import rothkoff.baruch.cars.ForUseMainActivity;
import rothkoff.baruch.cars.R;

public class CarsListAdapter extends RecyclerView.Adapter<CarHolder>
        implements ValueEventListener {

    private ForUseMainActivity context;
    private List<Car> cars;
    private DatabaseReference carsRef;
    private String tariffToShow;
    private View.OnClickListener onClickListener;
    private Car selectedCar= null;
    private Customer customer;
    private OnDataChangeListener onDataChangeListener;
    private Calendar dateStart;
    private Calendar dateEnd;
    private boolean firstBind = true;

    /**
     * <p>Constractor</p>
     * <p><b>Don't forget to call the 'Bind' method!!</b></p>
     * @param context
     * @param onClickListener
     */
    public CarsListAdapter(Context context, View.OnClickListener onClickListener) {
        if (context instanceof ForUseMainActivity) this.context = (ForUseMainActivity) context;
        else throw new RuntimeException(context.toString()
                + " must implement ForCustomerFragments");

        this.onClickListener = onClickListener;
        cars = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);
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
        this.cars = new ArrayList<>();

        for (DataSnapshot d : dataSnapshot.getChildren()){
            Car car = d.getValue(Car.class);

            boolean isInTarrif = tariffToShow==null||car.getTariffUid().equals(tariffToShow);
            boolean isInDates = dateStart==null;
            if (!isInDates)
                if (dateEnd != null) {
                    isInDates = car.availableInDates(dateStart, dateEnd);
                } else isInDates=car.availableInDate(dateStart);
            boolean isCustomer = customer == null ||
                    car.getPrice(customer, context.getTarrifByUid(car.getTariffUid())) != 0;

            if (isInTarrif&&isInDates&&isCustomer)
                cars.add(car);
            }

        firstBind = false;
        notifyMyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public Car getCarInPosition(int position) {
        return cars.get(position);
    }

    public void setOnDataChangeListener(OnDataChangeListener listener){
        this.onDataChangeListener = listener;
    }

    public void notifyMyDataSetChanged() {
        notifyDataSetChanged();
        if (onDataChangeListener != null) onDataChangeListener.OnDataChange(cars);
    }

    /**
     * <p font="bold">You must call this method if you want that this Adapter load his data</p>
     * <p>So call this after you end setup the Adapter</p>
     */
    public void Bind(){
        carsRef.addListenerForSingleValueEvent(this);
    }
    public void setTariffToShow(String uid){
        this.tariffToShow = uid;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDateStart(Calendar dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }

    public interface OnDataChangeListener{
        void OnDataChange(List<Car> data);
    }
}

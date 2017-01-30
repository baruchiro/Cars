package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

import rothkoff.baruch.cars.model.Customer;


public class CustomersView extends RecyclerView {

    private LinkedList<Customer> customers;
    private String selectedCustomerID;
    private CustomerAdapter adapter;
    private OnClickListener onItemClickListener;

    public CustomersView(Context context, AttributeSet attrs) {
        super(context, attrs);

        customers = new LinkedList<>();
        adapter = new CustomerAdapter();
        setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference(B.Keys.CUSTOMERS).addChildEventListener(adapter);

        setHasFixedSize(false);

        LayoutManager layoutManager = new LinearLayoutManager(context, VERTICAL, false);
        setLayoutManager(layoutManager);

    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.onItemClickListener = listener;
        adapter.notifyDataSetChanged();
    }

    public interface OnClickListener extends View.OnClickListener {
        void onClick(CustomerViewHolder holder, Customer customer);
    }

    private class CustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder> implements ChildEventListener {


        @Override
        public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_customer, parent, false);
            return new CustomerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomerViewHolder holder, int position) {
            holder.init(customers.get(position));
        }

        @Override
        public int getItemCount() {
            return customers.size();
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            customers.add(dataSnapshot.getValue(Customer.class));
            Log.d(MainActivity.LOG_NAME + "/" + "CustomerAdapter", "add customer to adapter: " + dataSnapshot.getKey());
            notifyItemInserted(customers.size());
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
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtAge;
        private Customer customer;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            txtAge = (TextView) itemView.findViewById(R.id.item_customer_age);
            txtName = (TextView) itemView.findViewById(R.id.item_customer_name);

            Log.d(MainActivity.LOG_NAME + "/" + "CustomerVH", this.toString() + "-CREATED");
        }

        public void init(Customer customer) {
            if (customer == null) {
                this.itemView.setVisibility(GONE);
                Log.d(MainActivity.LOG_NAME + "/" + "CustomerVH", "init with null");
            } else {
                this.customer = customer;
                txtName.setText(customer.getFullName());
                txtAge.setText(String.valueOf(customer.getAge()));

                Log.d(MainActivity.LOG_NAME + "/" + "CustomerVH", "init with " + customer.getFullName());

                if (onItemClickListener != null) {
                    this.itemView.setOnClickListener(onItemClickListener);
                    Log.d(MainActivity.LOG_NAME + "/" + "CustomerVH", "listener=" + onItemClickListener.toString());
                }
            }


        }
    }
}

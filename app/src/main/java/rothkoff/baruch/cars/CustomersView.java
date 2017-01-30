package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import rothkoff.baruch.cars.model.Customer;


public class CustomersView extends RecyclerView {

    private Map<String, Customer> customers;
    private String selectedCustomerID;

    public CustomersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private class CustomerAdapter extends RecyclerView.Adapter<customerViewHolder> {

        @Override
        public customerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return null;
        }

        @Override
        public void onBindViewHolder(customerViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class customerViewHolder extends RecyclerView.ViewHolder {


        public customerViewHolder(View itemView) {
            super(itemView);
        }
    }
}

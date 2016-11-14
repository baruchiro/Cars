package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RentsAdapter extends RecyclerView.Adapter<RentViewHolder> {

    private Context context;
    private List<Rent> rents;
    private boolean showCustomersNames;

    public RentsAdapter(Context context, Collection<Rent> rents, boolean showCustomersNames) {
        this.rents = new ArrayList<>(rents);
        this.showCustomersNames = showCustomersNames;
        this.context = context;
    }

    @Override
    public RentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_rent, parent, false);
        return new RentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RentViewHolder holder, int position) {
        holder.setRent(context, rents.get(position),showCustomersNames);
    }

    @Override
    public int getItemCount() {
        return rents.size();
    }

    public void addRent(Rent rent) {
        rents.add(rent);
        notifyDataSetChanged();
    }
}

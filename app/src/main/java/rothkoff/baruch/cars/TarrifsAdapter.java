package rothkoff.baruch.cars;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TarrifsAdapter extends ArrayAdapter<Tarrif> {
    private List<Tarrif> tarrifs;

    public TarrifsAdapter(Context context, int resource, List<Tarrif> objects) {
        super(context, resource, objects);
        this.tarrifs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ((TextView)view.findViewById(android.R.id.text1)).setText(tarrifs.get(position).getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        ((TextView)view.findViewById(android.R.id.text1)).setText(tarrifs.get(position).getName());
        return view;
    }

    @Override
    public Tarrif getItem(int position) {
        return super.getItem(position);
    }
}

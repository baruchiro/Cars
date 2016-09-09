package rothkoff.baruch.cars;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerMainFragment extends Fragment {
    private ForCustomerFragments mainActivity;

    private LinearLayout llMain;

    public static CustomerMainFragment newInstance() {
        CustomerMainFragment fragment = new CustomerMainFragment();

        return fragment;
    }

    public CustomerMainFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForCustomerFragments) {
            mainActivity = (ForCustomerFragments) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (B.customer.isDetailMissing())
            mainActivity.ReplaceFragment(CustomerDetailsEditFragment.newInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_main, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view){
        llMain = (LinearLayout)view.findViewById(R.id.frag_customer_main_main);
    }
    private void BehaviorMembers(){
        for (Map.Entry<String,String> entry: Customer.getMapForView(B.customer,getResources()).entrySet()) {
            TextView tv = new TextView(getContext());
            tv.setText(entry.getKey()+": "+entry.getValue());
            llMain.addView(tv);
        }
    }
}

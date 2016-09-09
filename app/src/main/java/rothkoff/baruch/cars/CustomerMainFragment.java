package rothkoff.baruch.cars;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerMainFragment extends Fragment {
    private ForCustomerFragments mainActivity;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_main, container, false);
    }
}

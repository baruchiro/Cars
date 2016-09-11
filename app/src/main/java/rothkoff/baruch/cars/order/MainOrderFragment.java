package rothkoff.baruch.cars.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class MainOrderFragment extends Fragment {
    protected ForOrderFragments mainFragment;
    public abstract int getPageTitle();

    public MainOrderFragment(Fragment mainFragment){
        if (mainFragment instanceof ForOrderFragments) this.mainFragment=(ForOrderFragments) mainFragment;
        else throw new RuntimeException(mainFragment.toString()
                + " must implement ForCustomerFragments");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mainFragment==null)throw new RuntimeException(mainFragment.toString()
                + " must implement ForCustomerFragments");
    }

    public interface ForOrderFragments{
    }
}

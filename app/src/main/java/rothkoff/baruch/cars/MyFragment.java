package rothkoff.baruch.cars;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

public abstract class MyFragment extends Fragment {

    public static int COLOR_ERROR;
    public static int COLOR_HEADING;
    protected ForUseMainActivity mainActivity;
    public abstract void setTitle();
    public abstract void setDrawerMenuItemChecked(Menu menu);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ForUseMainActivity) {
            mainActivity = (ForUseMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ForCustomerFragments");
        }

        COLOR_ERROR = ContextCompat.getColor(context, R.color.errorColor);
        COLOR_HEADING = ContextCompat.getColor(context, R.color.colorPrimary);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        setDrawerMenuItemChecked(mainActivity.getNavigationViewMenu());
    }

    public Tarrif getTarrif(String uid) {
        return mainActivity.getTarrifByUid(uid);
    }
}

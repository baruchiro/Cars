package rothkoff.baruch.cars;

import android.support.v4.app.Fragment;

import java.util.List;

public interface ForCustomerFragments {
    void ReplaceFragment(Fragment... fragments);

    List<Tarrif> getTarrif();
    List<String> getTarrifUids();

    String getTarrifName(String uid);
}

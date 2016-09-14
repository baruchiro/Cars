package rothkoff.baruch.cars;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

public interface ForUseMainActivity {
    void ReplaceFragment(Fragment... fragments);
    List<Tarrif> getTarrifsList();
    List<String> getTarrifUids();
    String getTarrifName(String uid);
    Tarrif getTarrifByUid(String uid);

    String getString(int resID);

    Context getContext();
}

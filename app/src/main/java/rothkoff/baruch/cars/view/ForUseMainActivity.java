package rothkoff.baruch.cars.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Menu;

import java.util.List;

import rothkoff.baruch.cars.model.Tarrif;

public interface ForUseMainActivity {
    void ReplaceFragment(Fragment... fragments);

    List<Tarrif> getTarrifsList();
    List<String> getTarrifUids();
    String getTarrifName(String uid);
    Tarrif getTarrifByUid(String uid);

    String getString(int resID);

    Context getContext();

    Menu getNavigationViewMenu();
}

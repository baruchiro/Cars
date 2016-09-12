package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageDBFragment extends MyFragment {

    private Button btnAddCar,btnAddTarrif;

    public ManageDBFragment() {
        // Required empty public constructor
    }

    public static ManageDBFragment newInstance() {
        ManageDBFragment fragment = new ManageDBFragment();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_db, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }
    private void InitMembers(View view) {
btnAddCar = (Button)view.findViewById(R.id.frag_manage_btn_addcar);
        btnAddTarrif = (Button)view.findViewById(R.id.frag_manage_btn_addtarrif);
    }
    private void BehaviorMembers() {
btnAddCar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mainActivity.ReplaceFragment(AddCarFragment.newInstance());
    }
});
        btnAddTarrif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.ReplaceFragment(AddTarrifFragment.newInstance(AddTarrifFragment.NEW_TARRIF));
            }
        });
    }

    @Override
    public void setTitle() {

    }


}

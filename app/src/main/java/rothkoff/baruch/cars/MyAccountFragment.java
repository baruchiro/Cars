package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends MyFragment {

    private LinearLayout llMain;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RentsAdapter rentsAdapter;
    private ScrollView scrollView;

    public MyAccountFragment() {
    }

    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();

        return fragment;
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.customer_main_title);
    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_myaccount).setChecked(true);
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
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view){
        llMain = (LinearLayout)view.findViewById(R.id.frag_customer_main_main);

        scrollView = (ScrollView)view.findViewById(R.id.frag_customer_main_scroll);
        recyclerView = new RecyclerView(getContext());
        rentsAdapter = new RentsAdapter(getContext(),B.customer.getRents().values(),false);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,true);
    }
    private void BehaviorMembers() {
        for (Map.Entry<String, String> entry : B.customer.getMapForView(getResources()).entrySet()) {
            TextView tv = new TextView(getContext());
            tv.setText(entry.getKey() + ": " + entry.getValue());
            llMain.addView(tv);
        }

        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rentsAdapter);

        scrollView.addView(recyclerView);
    }
}

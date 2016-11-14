package rothkoff.baruch.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NextRentsFragment extends MyFragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RentsAdapter rentsAdapter;
    private LinearLayout mainLinearLayout;

    public NextRentsFragment() {
        // Required empty public constructor
    }

    public static NextRentsFragment newInstance() {
        NextRentsFragment fragment = new NextRentsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_rents, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        mainLinearLayout = (LinearLayout) view.findViewById(R.id.frag_nextrent_main);
        FirebaseDatabase.getInstance().getReference(B.Keys.RENTS).orderByChild(B.Keys.DATE_START).startAt(B.getLongWithOnlyDate(System.currentTimeMillis())).addChildEventListener(new RentsChildEventListener());

        recyclerView = new RecyclerView(getContext());
        rentsAdapter = new RentsAdapter(getContext(), new ArrayList<Rent>(), true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
    }

    private void BehaviorMembers() {
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rentsAdapter);

        mainLinearLayout.addView(recyclerView);
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(R.string.next_rents);
    }

    @Override
    public void setDrawerMenuItemChecked(Menu menu) {
        menu.findItem(R.id.nav_nextrents).setChecked(true);
    }

    private class RentsChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Rent r = dataSnapshot.getValue(Rent.class);
            //if (r.getDateStart() >= B.getLongWithOnlyDate(System.currentTimeMillis()))
                rentsAdapter.addRent(dataSnapshot.getValue(Rent.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}

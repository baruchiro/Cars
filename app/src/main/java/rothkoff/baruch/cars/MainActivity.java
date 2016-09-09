package rothkoff.baruch.cars;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthStateListener,ForCustomerFragments {

    private Button connectBtn;
    private RecyclerView recycleCars;
    private FirebaseRecyclerAdapter<Car,CarHolder> adapterCars;
    private final int RC_SIGN_IN = 22;
    private AuthStateListener authStateListener;
    private DatabaseReference refCars;
    //private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private LinearLayout layoutNoUser;

    private boolean authFlag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        InitDrawer();
        InitMembers();
        InitBeaviors();

        /*if (FirebaseAuth.getInstance().getCurrentUser()==null)UserLogout();
        else getUserFreshDetails(FirebaseAuth.getInstance());*/
    }

    private void InitDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void InitMembers() {
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        connectBtn = (Button) findViewById(R.id.main_btn_connect);
        recycleCars = (RecyclerView) findViewById(R.id.main_recycle);
        refCars = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        layoutNoUser = (LinearLayout)findViewById(R.id.main_layout_nouser);
    }

    private void InitBeaviors() {
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        FirebaseAuth.getInstance().addAuthStateListener(this);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(
                                        AuthUI.EMAIL_PROVIDER,
                                        AuthUI.GOOGLE_PROVIDER)
                                .build(),
                        RC_SIGN_IN);
            }
        });

        adapterCars = new FirebaseRecyclerAdapter<Car, CarHolder>(Car.class,R.layout.item_car,CarHolder.class,refCars) {
            @Override
            protected void populateViewHolder(CarHolder viewHolder, Car model, int position) {
                viewHolder.setCar(model);
            }
        };

        recycleCars.setHasFixedSize(true);
        recycleCars.setLayoutManager(new LinearLayoutManager(this));
        recycleCars.setAdapter(adapterCars);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            if (authFlag) {
                getUserFreshDetails(firebaseAuth);
                authFlag = false;
            }
        } else UserLogout();
    }

    public void getUserFreshDetails(FirebaseAuth firebaseAuth){
        if (!progressDialog.isShowing()) progressDialog.show();

        DatabaseReference customerRef = FirebaseDatabase.getInstance()
                .getReference(B.Keys.CUSTOMERS).child(firebaseAuth.getCurrentUser().getUid());
        customerRef.keepSynced(true);

        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    B.customer = dataSnapshot.getValue(Customer.class);
                } else {
                    B.customer = new Customer(dataSnapshot.getKey());
                }
                UserLogin();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,R.string.error_connect,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UserLogin() {
        layoutNoUser.setVisibility(View.GONE);
        if (B.customer.isDetailMissing())ReplaceFragment(CustomerDetailsEditFragment.newInstance());
        else ReplaceFragment(CustomerMainFragment.newInstance());
        progressDialog.dismiss();
    }

    private void UserLogout() {
        B.customer = null;
        layoutNoUser.setVisibility(View.VISIBLE);
        ReplaceFragment(null);
        progressDialog.dismiss();
    }

    @Override
    public void ReplaceFragment(Fragment... fragments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.getFragments() != null)
            for (Fragment f : fragmentManager.getFragments())
                transaction.remove(f);

        for (Fragment f : fragments)
            transaction.add(R.id.main_fragment, f);
        transaction.commit();
    }
}

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

    private RecyclerView recycleCars;
    private FirebaseRecyclerAdapter<Car, CarHolder> adapterCars;
    private final int RC_SIGN_IN = 22;
    private AuthStateListener authStateListener;
    private DatabaseReference refCars;
    //private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private LinearLayout layoutNoUser;

    private boolean authFlag = true;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void InitMembers() {
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        recycleCars = (RecyclerView) findViewById(R.id.main_recycle);
        refCars = FirebaseDatabase.getInstance().getReference(B.Keys.CARS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        layoutNoUser = (LinearLayout) findViewById(R.id.main_layout_nouser);
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
                adapterCars = new FirebaseRecyclerAdapter<Car, CarHolder>(Car.class, R.layout.item_car, CarHolder.class, refCars) {
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

        switch (id) {
            case R.id.nav_connect:
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(
                                        AuthUI.EMAIL_PROVIDER,
                                        AuthUI.GOOGLE_PROVIDER)
                                .build(),
                        RC_SIGN_IN);
                break;
            case R.id.nav_order:
                ReplaceFragment(OrderFragment.newInstance());
                break;
            case R.id.nav_account:
                break;
            case R.id.nav_managedb:
                ReplaceFragment(AddCarFragment.newInstance());
                break;
            /*case R.id.nav_nextorders:
                break;

            case R.id.nav_send:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_view:
                break;*/
            default:
                Toast.makeText(this,R.string.menuitem_unavilable,Toast.LENGTH_SHORT).show();
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

    public void getUserFreshDetails(FirebaseAuth firebaseAuth) {
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
                Toast.makeText(MainActivity.this, R.string.error_connect, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UserLogin() {
        layoutNoUser.setVisibility(View.GONE);
        ShowMenuItems(true,B.customer.isManager());
        if (B.customer.isDetailMissing())
            ReplaceFragment(CustomerDetailsEditFragment.newInstance());
        else ReplaceFragment(CustomerMainFragment.newInstance());

        navigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
        progressDialog.dismiss();
    }

    private void UserLogout() {
        B.customer = null;
        ShowMenuItems(false, false);
        layoutNoUser.setVisibility(View.VISIBLE);
        ReplaceFragment(null);
        progressDialog.dismiss();
    }

    private void ShowMenuItems(boolean connected,boolean manager) {
        //Hide connect MenuItem
        navigationView.getMenu().findItem(R.id.nav_connect).setVisible(!connected);

        //Show Order now & My account MenuItems
        navigationView.getMenu().findItem(R.id.nav_order).setVisible(connected);
        navigationView.getMenu().findItem(R.id.nav_account).setVisible(connected);

        //Show Next order & Manage DB if customer is Manager
        navigationView.getMenu().findItem(R.id.nav_nextorders).setVisible(manager);
        navigationView.getMenu().findItem(R.id.nav_managedb).setVisible(manager);
    }

    @Override
    public void ReplaceFragment(Fragment... fragments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //TODO: bad programming
        if (fragmentManager.getFragments() != null)
            for (Fragment f : fragmentManager.getFragments())
                if (f != null) transaction.remove(f);
        setTitle(R.string.app_name);

        if (fragments != null)
            for (Fragment f : fragments)
                transaction.add(R.id.main_fragment, f);
        transaction.commit();
    }
}

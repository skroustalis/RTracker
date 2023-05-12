package sokrous.rtracker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import sokrous.rtracker.R;
import sokrous.rtracker.fragment.DashboardFragment;
import sokrous.rtracker.fragment.MapsFragment;
import sokrous.rtracker.fragment.RoutesFragment;
import sokrous.rtracker.model.User;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

   DrawerLayout drawerLayout;
   private FragmentManager fragmentManager;
   private NavigationView navigationView;
   private DashboardFragment dashboardFragment;

   private Bundle bundle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);
        Intent intent = getIntent();
        User loggedInUser = (User) intent.getSerializableExtra("LoggedInUser");
        System.out.println("Intent: "+loggedInUser);
        fragmentManager = getSupportFragmentManager();

        setTitle("Dashboard");

        dashboardFragment = new DashboardFragment();
        bundle = new Bundle();
        bundle.putSerializable("LoggedInUser",loggedInUser);
        System.out.println("Bundle: "+ bundle);
        dashboardFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, dashboardFragment).commit();

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager.addOnBackStackChangedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (findViewById(R.id.fragmentContainer) != null) {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    DashboardFragment newDashboardFragment = new DashboardFragment();
                    newDashboardFragment.setArguments(bundle);
                    setTitle("Dashboard");
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, newDashboardFragment).addToBackStack(null).commit();
                    break;
                case R.id.nav_map:
                    setTitle("Map");
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new MapsFragment()).addToBackStack(null).commit();
                    break;
                case R.id.nav_walk:
                case R.id.nav_bike:
                    setTitle("Routes");
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new RoutesFragment()).addToBackStack(null).commit();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
    }

    //This Closes the drawer layout with the back button when is open
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }


    @Override
    public void onBackStackChanged() {
        Fragment currentFragment;
        currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof DashboardFragment){
            setTitle("Dashboard");
        } else if (currentFragment instanceof MapsFragment) {
            setTitle("Maps");
        } else if (currentFragment instanceof RoutesFragment) {
            setTitle("Routes");
        }
    }
}
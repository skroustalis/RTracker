package sokrous.rtracker;


import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

import sokrous.rtracker.databinding.ActivityDashboardBinding;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocatedActivityTitle( "Dashboard");
    }

}
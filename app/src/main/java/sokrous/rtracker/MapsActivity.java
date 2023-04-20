package sokrous.rtracker;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import sokrous.rtracker.databinding.ActivityMapsBinding;

public class MapsActivity extends DrawerBaseActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private LatLng previousLatLng;

    private GoogleMap mMap;

    private LocationManager locationManager;

    private Marker locationMarker;

    private boolean isDrawingEnable = false;

    private ImageButton startButton;
    private ImageButton stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sokrous.rtracker.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocatedActivityTitle("Map");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.cancelPermissionRequest();
                    }
                }).check();

        // Find the start button
        startButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);

        // Set a click listener on the start button
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == startButton.getId()){
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            isDrawingEnable = true;
            startLocationUpdates();
        } else {
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
            isDrawingEnable = false;
            stopLocationUpdates();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationMarker == null) {
            locationMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        } else {
            locationMarker.setPosition(latlng);
            // Add a line between the previous and current locations
            if (previousLatLng != null && isDrawingEnable) {
                mMap.addPolyline(new PolylineOptions()
                        .add(previousLatLng, latlng)
                        .width(10)
                        .color(0xffefbb5e));
            }
            previousLatLng = latlng;
        }

    }


}



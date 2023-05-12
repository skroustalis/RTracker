package sokrous.rtracker.fragment;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sokrous.rtracker.R;
import sokrous.rtracker.model.Point;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private LatLng previousLatLng;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Marker locationMarker;
    private boolean isDrawingEnable = false;
    private ImageButton startButton;
    private ImageButton stopButton;
    private Chronometer chronometer;
    private List<Point> mPointList;
    private int mRecordId = 0;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FragmentActivity fragmentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_maps, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mPointList = new ArrayList<>();
        fragmentActivity = requireActivity();


        fragmentActivity.setTitle("Map");

        db = FirebaseFirestore.getInstance();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        Dexter.withContext(requireActivity())
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

        // Find the start stop chronometer
        startButton = view.findViewById(R.id.startBtn);
        stopButton = view.findViewById(R.id.stopBtn);
        chronometer = view.findViewById(R.id.chronometer);

        // Set a click listener on the start,stop button
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == startButton.getId()) {
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isDrawingEnable = true;
        } else if (v.getId() == stopButton.getId()) {
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            isDrawingEnable = false;
            showAlertDialog();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
    }

//    public void stopLocationUpdates() {
//        locationManager.removeUpdates(this);
//    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
        // Create a new Record object for the location update
        Point point = new Point(mRecordId, geoPoint);

        if (locationMarker == null) {
            locationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            locationMarker.setPosition(latLng);
            // Add a line between the previous and current locations
            if (previousLatLng != null && isDrawingEnable) {
                mMap.addPolyline(new PolylineOptions()
                        .add(previousLatLng, latLng)
                        .width(10)
                        .color(0xffefbb5e));
                // Add the Record object to the list
                mPointList.add(point);
                // Increment the record ID counter
                mRecordId++;
            }
            previousLatLng = latLng;
        }
    }

    private void saveRecordList(List<Point> pointList) {
        String uid = currentUser.getUid();
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userName = documentSnapshot.getString("userName");
                        Log.d(TAG, "User name: " + userName);

                        // Create a new record document in the "records" collection with the user name and route
                        Map<String, Object> pointData = new HashMap<>();
                        pointData.put("user", userName);
                        pointData.put("route", pointList);

                        db.collection("records")
                                .add(pointData)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "Record added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding record", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting user", e);
                    }
                });
    }


    private void showAlertDialog() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.setOnSaveClickListener(new AlertDialogFragment.OnSaveClickListener() {
            @Override
            public void onSaveClick() {
                // Perform the save action here
                saveRecordList(mPointList);
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getChildFragmentManager(), "AlertDialogFragment");
    }
}






package com.example.sandunmadola.dialogpoolup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sandun_Madola on 10/13/2014.
 */
public class DriverMapActivity extends FragmentActivity{
    private static final LatLng SYDNEY = new LatLng(7,81);
    private static final LatLng MOUNTAIN_VIEW = new LatLng(6.9270786,  79.861243);

    private GoogleMap googleMap;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drivermap);
            setUpMapIfNeeded();
        }


    //@SuppressLint("NewApi")
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {


                setUpMap();
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }
    private void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMyLocationEnabled(true);

       // private GoogleMap map;
       // Obtain the map from a MapFragment or MapView.

// Move the camera instantly to Sydney with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));

// Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder



        /*double latitude =  location.getLatitude();
        double longitude = MyLocation().getLongitude();*/
/*
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria,true);
        Location location = locationManager.getLastKnownLocation(provider);

        double latitude =  location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude,longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Me"));
*/
    }




}

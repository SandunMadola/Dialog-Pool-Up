package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sandun_Madola on 9/23/2014.
 */
public class PassengerMapActivity extends FragmentActivity {
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengermap);
        setUpMapIfNeeded();

    }

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
        googleMap.setMyLocationEnabled(true);

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
    }
}

package com.example.sandunmadola.dialogpoolup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PassengerMapActivity extends FragmentActivity {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    public static final double SL_lat = 7.8775394; //sri laka
    public static final double SL_lng = 80.7003428; //sri lanka
    private static final float DEFAULTZOOM = 7;
    private static final float DEFAULTZOOM1 = 15;
    LinearLayout primary;
    LinearLayout secondary;
    LinearLayout ternary;
    Button cntinue;
    GoogleMap mMap;
    Marker marker;
    ArrayList<LatLng> markerPoints;
    ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOK()){


            setContentView(R.layout.activity_passengermap);
            primary = (LinearLayout) findViewById(R.id.primaryLayout);
            secondary = (LinearLayout) findViewById(R.id.secondaryLayout);
            ternary = (LinearLayout) findViewById(R.id.ternaryLayout);
            cntinue = (Button) findViewById(R.id.btnContinue);

            if(initMap()){
                Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
                gotoLocation(SL_lat,SL_lng,DEFAULTZOOM);
                mMap.setMyLocationEnabled(true);

                // Initializing
                markerPoints = new ArrayList<LatLng>();



                // Getting Map for the SupportMapFragment


                // Setting onclick event listener for the map
                if(mMap!=null) {
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


                        @Override
                        public void onMapLongClick(LatLng point) {

                            // Already two locations
                            if (markerPoints.size() > 1) {
                                markerPoints.clear();
                                mMap.clear();
                            }

                            // Adding new item to the ArrayList
                            markerPoints.add(point);

                            // Creating MarkerOptions
                            MarkerOptions options = new MarkerOptions();

                            // Setting the position of the marker
                            options.position(point);

                            /**
                             * For the start location, the color of marker is GREEN and
                             * for the end location, the color of marker is RED.
                             */
                            if (markerPoints.size() == 1) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            } else if (markerPoints.size() == 2) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            }


                            // Add new marker to the Google Map Android API V2
                            mMap.addMarker(options);



                            //markers.add(mkr);
                            if (markerPoints.size() == 2) {
                                cntinue.setEnabled(true);
                                secondary.setVisibility(View.INVISIBLE);
                                ternary.setVisibility(View.VISIBLE);

                            }

                            // Checks, whether start and end locations are captured


                        }
                    });

                   // Toast.makeText(this,marker.getId(), Toast.LENGTH_SHORT).show();
                }


            }
            else{
                Toast.makeText(this,"Map not available", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            setContentView(R.layout.activity_mode);
        }

        final Button btn = (Button)findViewById(R.id.toggleButton);
        btn.setText("Satellite View");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String txt= btn.getText().toString();

                if(txt.equals("ON")){

                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    Toast.makeText(PassengerMapActivity.this,"Changed to Sattelite view ",Toast.LENGTH_LONG).show();
                    btn.setText("Normal View");
                }
               else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Toast.makeText(PassengerMapActivity.this,"Changed to Normal view ",Toast.LENGTH_LONG).show();
                    btn.setText("Sattelite View");
                }

            }
        });

    }

    public boolean servicesOK(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,this,GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this,"Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap(){
        if (mMap == null){
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
            mMap = mapFrag.getMap();
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mMap.moveCamera(update);//animate camera
    }

    public void gotoGeoLocate(View v) throws IOException{
        hideSoftKeyboard(v);
        final LinearLayout parent = (LinearLayout) v.getParent();
        int txtID;
        String point;
        if(parent.equals(primary)){
            txtID=R.id.txtPlace1;
            point = "Start";
        }
        else{
            txtID=R.id.txtPlace2;
            point = "End";
        }

        EditText et = (EditText) findViewById(txtID);
        String location = et.getText().toString();
        List<Address> list;
        try {
            Geocoder gc = new Geocoder(this);
            list = gc.getFromLocationName(location, 1);
            Address add = list.get(0);
            String locality = add.getLocality();

            Toast.makeText(this,locality,Toast.LENGTH_LONG).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();

            gotoLocation(lat,lng,DEFAULTZOOM1);
          /*  if(marker != null){ //to make one marker
                marker.remove();
            }*/
           // MarkerOptions options = new MarkerOptions().title(locality+"/"+point).position(new LatLng(lat,lng));
           // marker = mMap.addMarker(options);

        }catch (IOException e){

        } finally{

            if(parent.equals(primary)) {
                parent.setVisibility(View.INVISIBLE);
                secondary.setVisibility(View.VISIBLE);
            }
            else{
                secondary.setVisibility(View.INVISIBLE);
                ternary.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void gotoDriverList(View view){
        Intent intent = new Intent(this, AvailableDriverListActivity.class);
        startActivity(intent);
    }
    public  void gotoReset(View view){
        ternary.setVisibility(View.INVISIBLE);
        primary.setVisibility(View.VISIBLE);
        mMap.clear();
        gotoLocation(SL_lat,SL_lng,DEFAULTZOOM);
        cntinue.setEnabled(false);
    }

}

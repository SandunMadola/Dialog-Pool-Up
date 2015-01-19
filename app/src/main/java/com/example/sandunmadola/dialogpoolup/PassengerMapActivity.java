package com.example.sandunmadola.dialogpoolup;

import android.app.Dialog;
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
    public static final double SL_lat = 7.8775394;
    public static final double SL_lng = 80.7003428;
    private static final float DEFAULTZOOM = 7;
    private static final float DEFAULTZOOM1 = 15;

    GoogleMap mMap;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOK()){

            setContentView(R.layout.activity_passengermap);

            if(initMap()){
                Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
                gotoLocation(SL_lat,SL_lng,DEFAULTZOOM);
                mMap.setMyLocationEnabled(true);
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

                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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

           /* if(mMap != null){//not working
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                        public void onMapLongClick(LatLng ll) {
                            Geocoder gc = new Geocoder(PassengerMapActivity.this);
                            List<Address> list;
                            try {
                                list = gc.getFromLocation(ll.latitude, ll.latitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                            Address add = list.get(0);
                            String locality = add.getLocality();
                            MarkerOptions options = new MarkerOptions()
                                    .title(locality).position(new LatLng(ll.latitude, ll.latitude))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            marker = mMap.addMarker(options);
                        }
                });//not working
            }*/
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mMap.moveCamera(update);//animate camera
    }

    //json
    public static LatLng getLocationFromString(String address)
            throws JSONException, UnsupportedEncodingException {

        HttpGet httpGet = new HttpGet(
                "http://maps.google.com/maps/api/geocode/json?address="
                        + URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject = new JSONObject(stringBuilder.toString());

        double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

        double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        return new LatLng(lat, lng);
    }

    public static List<Address> getStringFromLocation(double lat, double lng)
            throws ClientProtocolException, IOException, JSONException {

        String address = String
                .format(Locale.ENGLISH,                                 "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                        + Locale.getDefault().getCountry(), lat, lng);
        HttpGet httpGet = new HttpGet(address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        List<Address> retList = null;

        response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
        int b;
        while ((b = stream.read()) != -1) {
            stringBuilder.append((char) b);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject = new JSONObject(stringBuilder.toString());

        retList = new ArrayList<Address>();

        if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String indiStr = result.getString("formatted_address");
                Address addr = new Address(Locale.getDefault());
                addr.setAddressLine(0, indiStr);
                retList.add(addr);
            }
        }

        return retList;
    }
    //json

    public void gotoGeoLocate(View v) throws IOException{
        hideSoftKeyboard(v);
        EditText et = (EditText) findViewById(R.id.txtPlace1);
       // et.setVisibility(View.INVISIBLE);//btn hide
        Button bt = (Button) findViewById(R.id.btnGo);
       // bt.setVisibility(View.INVISIBLE);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location,1);

        //if(list.get(0)==null){gotoGeoLocate(v);}//add
        Address add = list.get(0);
        String locality = add.getLocality();

        Toast.makeText(this,locality,Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat,lng,DEFAULTZOOM1);
        if(marker != null){ //to make one marker
            marker.remove();
        }
        MarkerOptions options = new MarkerOptions().title(locality).position(new LatLng(lat,lng));
        marker = mMap.addMarker(options);

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
                Toast.makeText(this,"pop",Toast.LENGTH_LONG).show();
                break;
            case R.id.mapTypeSatellite:
                Toast.makeText(this,"pop",Toast.LENGTH_LONG).show();

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                Toast.makeText(this,"pop",Toast.LENGTH_LONG).show();

                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                Toast.makeText(this,"pop",Toast.LENGTH_LONG).show();

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapTypeNone:
                Toast.makeText(this,"pop",Toast.LENGTH_LONG).show();

                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

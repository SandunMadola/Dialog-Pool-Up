package com.example.sandunmadola.dialogpoolup;


/**
 * Created by Sandun_Madola on 10/13/2014.
 */

import android.support.v4.app.FragmentActivity;
import android.app.Dialog;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.net.*;
import java.util.*;


import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class DriverMapActivity extends FragmentActivity {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    public static final double SL_lat = 7.8775394;
    public static final double SL_lng = 80.7003428;
    private static final float DEFAULTZOOM = 7;
    private static final float DEFAULTZOOM1 = 15;
    LinearLayout primary;
    LinearLayout secondary;
    Button proceed;
    Button reset;

    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    GoogleMap mMap;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOK()){

            setContentView(R.layout.activity_drivermap);
            primary = (LinearLayout) findViewById(R.id.primaryLayout);
            secondary = (LinearLayout) findViewById(R.id.secondaryLayout);
            proceed = (Button) findViewById(R.id.btnProceed);
            reset = (Button) findViewById(R.id.btnReset);
            reset.setVisibility(View.GONE);
            markerPoints = new ArrayList<LatLng>();

            if(initMap()){//Loading the Google libries
                Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
                gotoLocation(SL_lat,SL_lng,DEFAULTZOOM);
                mMap.setMyLocationEnabled(true);
            }
            //Map draw
            // Initializing
            markerPoints = new ArrayList<LatLng>();

            // Getting reference to SupportMapFragment of the activity_main
            //SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1);

            // Getting Map for the SupportMapFragment
            //map = fm.getMap();

            if(map!=null){

                // Enable MyLocation Button in the Map
                //map.setMyLocationEnabled(true);

                // Setting onclick event listener for the map
                map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng point) {

                        // Already two locations
                        if (markerPoints.size() > 1) {
                            markerPoints.clear();
                            map.clear();
                        }

                        // Adding new item to the ArrayList
                        markerPoints.add(point);
                        if(markerPoints.size()>=2){

                        }

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
                        map.addMarker(options);

                        // Checks, whether start and end locations are captured
                        if (markerPoints.size() >= 2) {
                            LatLng origin = markerPoints.get(0);
                            LatLng dest = markerPoints.get(1);

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl(origin, dest);

                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);

                            proceed.setEnabled(true);
                            //reset.animate();
                            reset.setVisibility(View.VISIBLE);
                            primary.setVisibility(View.GONE);
                            secondary.setVisibility(View.GONE);


                        }

                    }
                });
            }
            //New code
            else{
                Toast.makeText(this,"Map not available", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            setContentView(R.layout.activity_mode);
        }
        final Button btn = (Button)findViewById(R.id.toggleButton2);
        btn.setText("Satellite View");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt= btn.getText().toString();

                if(txt.equals("ON")){

                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    Toast.makeText(DriverMapActivity.this,"Changed to Sattelite view ",Toast.LENGTH_LONG).show();
                    btn.setText("Normal View");
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Toast.makeText(DriverMapActivity.this,"Changed to Normal view ",Toast.LENGTH_LONG).show();
                    btn.setText("Sattelite View");
                }

            }
        });

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean
    servicesOK(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
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
            this.map=mMap;

            if(mMap != null){//not working
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng ll) {
                        Geocoder gc = new Geocoder(DriverMapActivity.this);
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
            }
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);//animate camera
    }



    public void gotoGeoLocate2(View v) throws IOException{
        hideSoftKeyboard(v);
        final LinearLayout parent = (LinearLayout) v.getParent();
        int txtID;
        String point;
        if(parent.equals(primary)){
            txtID=R.id.txtPlace3;
            point = "Start";
        }
        else{
            txtID=R.id.txtPlace4;
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
            //MarkerOptions options = new MarkerOptions().title(locality+"/"+point).position(new LatLng(lat,lng));
            //marker = mMap.addMarker(options);

        }catch (IOException e){

        } finally{

            if(parent.equals(primary)) {
                parent.setVisibility(View.INVISIBLE);
                secondary.setVisibility(View.VISIBLE);
            }
        }
    }
    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

public  void reset(View v){

    proceed.setEnabled(false);
    //reset.animate();
    reset.setVisibility(View.GONE);
    primary.setVisibility(View.VISIBLE);
    mMap.clear();
    gotoLocation(SL_lat,SL_lng,DEFAULTZOOM);
    //ssecondary.setVisibility(View.GONE);
}
}

package com.saubhagyam.quickfinderplaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.readystatesoftware.systembartint.SystemBarTintManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by yagnesh on 19/03/18.
 */

public class Category_Activity_TextSearch extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static String dbkey = "radious";
    public static String range = "range";
    GoogleMap mGoogleMap;
    Toolbar toolbar1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    View view;
    RecyclerView rv_data;
    SharedPreferences sharedPreferences;
    Double latitude;
    Double longitude;
    int PROXIMATRY_RADIUS;
    String key = "AIzaSyDQxEF-rUWRvcJsiM-gRLWJHnsQDt8o9Rk";
    Bundle bundle;
    String type;
    TextView txt_tool;
    String NearByPlace = "";

    Handler handler;
    Runnable finalizer;

    //String textsearch;
    View place_list_view;
    ArrayList<JSONPojo> arr_list;
    boolean isZoomSet = false;
    String Finalurl = "";
    LinearLayoutManager manager;
    PlaceListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity_textsearch);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

// set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#E57200"));


        bundle = getIntent().getExtras();

        //get selected Name from HomeFragment

        type = bundle.getString("q");

        System.out.println("search type---->" + type);

        arr_list = new ArrayList<>();
        place_list_view = findViewById(R.id.place_list_view);
        toolbar1 = findViewById(R.id.toolbar1);
        txt_tool = toolbar1.findViewById(R.id.toolbar_category_title);
        txt_tool.setText(type);

       // toolbar1.setTitle("Category");

        setSupportActionBar(toolbar1);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rv_data = findViewById(R.id.rv_data);

        sharedPreferences = getSharedPreferences(dbkey, Context.MODE_PRIVATE);

        PreferencesManager preferencesManager = new PreferencesManager(this);
        int distance = preferencesManager.getSeekbar();


       /* int distance = sharedPreferences.getInt(range, 0);
        if (distance == 0) {
            distance = 5;
        }
*/
        /*Interstatitial Ads starts*/

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                final Handler handler = new Handler();
                handler.postDelayed(finalizer = new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                }, 10000);


            }
        });
        /*Interstatitial Ads ends*/
        PROXIMATRY_RADIUS = distance*1000;
        System.out.println("=========>" + PROXIMATRY_RADIUS);


        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new PlaceListAdapter(this, arr_list);
        rv_data.setLayoutManager(manager);
        rv_data.setAdapter(adapter);



   /*    setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/


        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // place_list_view.setOnClickListener(this);

        // bundle = getIntent().getExtras();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.atm);
        mapFragment.getMapAsync(this);

        //Intent intent=getIntent();


    }



/*    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(finalizer);
    }*/

/*    @Override
    protected void onStop()
    {
        super.onStop();
        handler.removeCallbacks(finalizer);;
    }*/


    public void onPause() {
        super.onPause();
        handler.removeCallbacks(finalizer);

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(Category_Activity_TextSearch.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        //get selected Name from HomeFragment


        Finalurl = getUrl(latitude, longitude, type);


        System.out.println("Text search URL ======>" + Finalurl);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


        //move map camera
        if (isZoomSet == false) {
            getNearByPlace();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            isZoomSet = true;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Category_Activity_TextSearch.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
        // Add a marker in Sydney and move the camera

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(Category_Activity_TextSearch.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        /*mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){
            @Override
            public void onMapLoaded() {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
            }
        });*/
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Category_Activity_TextSearch.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Category_Activity_TextSearch.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(Category_Activity_TextSearch.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Category_Activity_TextSearch.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(Category_Activity_TextSearch.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(Category_Activity_TextSearch.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Category_Activity_TextSearch.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void getNearByPlace() {
        mGoogleMap.clear();
        //final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location= " + latitude + ", " + longitude + " &radius= " + PROXIMATRY_RADIUS + " &type= " + NearByPlace + "&key=" + key;
        final StringRequest request = new StringRequest(Request.Method.GET, Finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");

                    if (results.length() == 0) {
                        Toast.makeText(Category_Activity_TextSearch.this, "No Result Found", Toast.LENGTH_SHORT).show();
                    }


                    for (int i = 0; i < results.length(); i++) {
                        JSONPojo pojo = new JSONPojo();

                        JSONObject c = results.getJSONObject(i);
                        JSONObject jobjGeometry = c.getJSONObject("geometry");
                        JSONObject jobjLocation = jobjGeometry.getJSONObject("location");
                        String jobjicon = c.getString("icon");
                        String jobjadd = c.has("vicinity") ? c.getString("vicinity") : c.getString("formatted_address");
                        String reting = c.has("rating") ? c.getString("rating") : "0.0";
                        String id = c.getString("place_id");

                        String open_now = "false";

                        if (c.has("opening_hours")) {
                            JSONObject opening_hours = c.getJSONObject("opening_hours");
                            open_now = opening_hours.getString("open_now");
                        }

                        System.out.println("Open NOW ------------->" + open_now);
                        pojo.setLat(jobjLocation.getString("lat"));
                        pojo.setLng(jobjLocation.getString("lng"));

                        Location locationA = new Location("point A");

                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("point B");

                        locationB.setLatitude(Double.parseDouble(jobjLocation.getString("lat")));
                        locationB.setLongitude(Double.parseDouble(jobjLocation.getString("lng")));

                        float distance = locationA.distanceTo(locationB) / 1000;
                        String dis = String.format("%.01f", distance);
                        pojo.setDistance(dis);

                        pojo.setId(id);
                        pojo.setName(c.getString("name"));
                        pojo.setVicinity(jobjadd);
                        pojo.setImage(jobjicon);
                        pojo.setRating(reting);


                        pojo.setOpen_now(open_now.equals("true") ? "Open" : "Closed");


                        arr_list.add(pojo);
                    }
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < arr_list.size(); i++) {
                        double lat = Double.parseDouble(arr_list.get(i).getLat());
                        double lng = Double.parseDouble(arr_list.get(i).getLng());
                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(arr_list.get(i).getName()));
                        //    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
                        //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),13));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
                    }

                    Collections.sort(arr_list, new Comparator<JSONPojo>() {
                        @Override
                        public int compare(JSONPojo o1, JSONPojo o2) {
                            return o1.getDistance().compareTo(o2.getDistance());
                        }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();

            }

        };
        VolleySingleton.getInstance(Category_Activity_TextSearch.this).addToRequestQueue(request);

    }

    public String getUrl(double latitude, double longitude, String NearByPlace) {

        //String types = "name";


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + NearByPlace);
        sb.append("&location=" + latitude + "," + longitude);
        sb.append("&radius=" + PROXIMATRY_RADIUS);
        sb.append("&key=" + key);
        Log.d("Map", "search place URL: " + sb.toString());
        return sb.toString();


    }


/*    public class CustomComparator implements Comparator<JSONPojo> {
        @Override
        public int compare(JSONPojo o1, JSONPojo o2) {
            return o1.getDistance().compareTo(o2.getDistance());
        }
    }*/

/*    @Override
    public void onClick(View view) {
        Toast.makeText(Category_Activity.this, "Detals Of Near BY", Toast.LENGTH_SHORT).show();
    }*/


}

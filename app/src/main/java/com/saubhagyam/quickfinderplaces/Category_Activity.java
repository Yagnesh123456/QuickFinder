package com.saubhagyam.quickfinderplaces;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

public class Category_Activity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,ConnectivityReceiver.ConnectivityReceiverListener {

    private AdView adView;
    private AdRequest adRequest;
    InterstitialAd interstitialAd;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "Category_Activity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static String dbkey = "radious";
    public static String range = "range";
    GoogleMap mGoogleMap;
    Toolbar toolbar;
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
    View place_list_view;
    String priority_high = "HighToLow";
    String getPriority_low = "LowToHigh";
    ArrayList<JSONPojo> arr_list;
    boolean isZoomSet = false;
    String Finalurl = "";
    LinearLayoutManager manager;
    PlaceListAdapter adapter;
    TextView txt_tool;
    String type;
    String t_title;
    String jobjicon, name, dis, open_now, id, jobjadd, reting;

    private AlertDialog progressDialog;

    Handler handler;
    Runnable finalizer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_activity);

        // Manually checking internet connection
        checkConnection();

        handler=new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
   /*     // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        progressDialog = new SpotsDialog(Category_Activity.this, R.style.Custom);
// set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#E57200"));
*/

        bundle = getIntent().getExtras();

        //get selected Name from HomeFragment

        type = bundle.getString("category");

        t_title = bundle.getString("toolbartitle");


        arr_list = new ArrayList<>();
        place_list_view = findViewById(R.id.place_list_view);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_tool = toolbar.findViewById(R.id.toolbar_title);
        txt_tool.setText(t_title);

        Typeface khandBold = Typeface.createFromAsset(getApplication().getAssets(), "fonts/SegoeUI-Light.ttf");

        txt_tool.setTypeface(khandBold);


        rv_data = findViewById(R.id.rv_data);

        adView=findViewById(R.id.adView);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-7796828333997958/4622009204");
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new PlaceListAdapter(this, arr_list);
        rv_data.setLayoutManager(manager);
        rv_data.setAdapter(adapter);


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
               handler = new Handler();
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


       /* setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category");*/

        sharedPreferences = getSharedPreferences(dbkey, Context.MODE_PRIVATE);

        //int distance=sharedPreferences.getInt("range",5000);

        PreferencesManager preferencesManager = new PreferencesManager(this);
        int distance = preferencesManager.getSeekbar();

        PROXIMATRY_RADIUS = distance * 1000;
        System.out.println("=========>" + PROXIMATRY_RADIUS);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // place_list_view.setOnClickListener(this);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.atm);
        mapFragment.getMapAsync(this);

        displayLocationSettingsRequest(this);
        //CheckPermission();

        //Intent intent=getIntent();


    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab2), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


   /* @Override
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


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result;
        result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        getNearByPlace();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Category_Activity.this, MY_PERMISSIONS_REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            //  Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


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
        if (ContextCompat.checkSelfPermission(Category_Activity.this,
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


        System.out.println("You select This type --->" + type);
        Finalurl = getUrl(latitude, longitude, type);



        System.out.println("Category Activity URL"+Finalurl);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


        //move map camera
        if (isZoomSet == false) {

            // show dialog
            getNearByPlace();
            // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
            isZoomSet = true;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Category_Activity.this,
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
        mGoogleApiClient = new GoogleApiClient.Builder(Category_Activity.this)
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
        if (ContextCompat.checkSelfPermission(Category_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Category_Activity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
               /* new AlertDialog.Builder(Category_Activity.this)
                        .setTitle(Html.fromHtml("\"<b>\"+<font color='#000000'>Location Permission Needed</font>" + "</b>"))
                        .setMessage("      This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton(Html.fromHtml("Ok"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Category_Activity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();*/

                createDialog(Category_Activity.this);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(Category_Activity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void createDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Html.fromHtml("\"<b>\"+<font color='#000000'>Location Permission Needed</font>" + "</b>"));
        builder.setMessage("This app needs the Location permission, please accept to use location functionality");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(Category_Activity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(Color.MAGENTA);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.BLACK);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(Category_Activity.this,
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
                    Toast.makeText(Category_Activity.this, "permission denied", Toast.LENGTH_LONG).show();
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void getNearByPlace() {

        progressDialog.show();
        mGoogleMap.clear();
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
*/



        //final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location= " + latitude + ", " + longitude + " &radius= " + PROXIMATRY_RADIUS + " &type= " + NearByPlace + "&key=" + key;
        final StringRequest request = new StringRequest(Request.Method.GET, Finalurl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");


                    if (results.length() == 0) {
                        Toast.makeText(Category_Activity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < results.length(); i++) {

                        JSONPojo pojo = new JSONPojo();
                        JSONObject c = results.getJSONObject(i);
                        if (c.getString("name").equals("Photomangraphy")||c.getString("name").contains("Photography")||c.getString("name").contains("Studio")||c.getString("name").contains("Dhruvesh")) {
                            Log.e(TAG, "onResponse: " + c.getString("name"));
                        } else {
                            JSONObject jobjGeometry = c.getJSONObject("geometry");
                            JSONObject jobjLocation = jobjGeometry.getJSONObject("location");
                            jobjicon = c.getString("icon");
                            jobjadd = c.has("vicinity") ? c.getString("vicinity") : c.getString("formatted_address");
                            reting = c.has("rating") ? c.getString("rating") : "0.0";
                            id = c.getString("place_id");
                            open_now = "false";

                            if (c.has("opening_hours")) {
                                JSONObject opening_hours = c.getJSONObject("opening_hours");
                                open_now = opening_hours.getString("open_now");
                            }

                            //  System.out.println("Open NOW ------------->" + open_now);
                            pojo.setLat(jobjLocation.getString("lat"));
                            pojo.setLng(jobjLocation.getString("lng"));

                            Location locationA = new Location("point A");

                            locationA.setLatitude(latitude);
                            locationA.setLongitude(longitude);

                            Location locationB = new Location("point B");

                            locationB.setLatitude(Double.parseDouble(jobjLocation.getString("lat")));
                            locationB.setLongitude(Double.parseDouble(jobjLocation.getString("lng")));

                            float distance = locationA.distanceTo(locationB) / 1000;
                            dis = String.format("%.01f", distance);
                            name = c.getString("name");
                            Log.e(TAG, "getAllName " + c.getString("name"));

                            pojo.setDistance(dis);
                            pojo.setId(id);
                            pojo.setName(name);
                            pojo.setVicinity(jobjadd);
                            pojo.setImage(jobjicon);
                            pojo.setRating(reting);
                            pojo.setOpen_now(open_now.equals("true") ? "Open" : "Closed");

                            //pojo.setPlace_id();


                            arr_list.add(pojo);


                        }




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

                    //set data high ti low ratting priority

 /*                   String lowpriority = sharedPreferences.getString("LowToHigh", null);

                    Collections.sort(arr_list, new CustomComparator());
                    Collections.reverse(arr_list);
                    if (lowpriority.length() != 0) {
                        Collections.sort(arr_list, new CustomComparator());
                    }*/


                    //sort data by distance

                    Collections.sort(arr_list, new CustomComparator());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog dismiss
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();

            }

        };
        VolleySingleton.getInstance(Category_Activity.this).addToRequestQueue(request);


    }


    public String getUrl(double latitude, double longitude, String NearByPlace) {


        String types = "";
        if (getIntent().getExtras().getString("isCat").equals("1")) {
            types = "type";
        } else {
            types = "name";
        }


        if (getIntent().getExtras().getString("isCat").equals("1")) {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location=" + latitude + "," + longitude);
            sb.append("&radius=" + PROXIMATRY_RADIUS);
            sb.append("&" + types + "=" + NearByPlace);
            sb.append("&sensor=true");
            sb.append("&key=" + key);
            Log.d("Map", " search place URL: " + sb.toString());
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
            sb.append("query=" + NearByPlace);
            sb.append("&location=" + latitude + "," + longitude);
            sb.append("&radius=" + PROXIMATRY_RADIUS);
            sb.append("&key=" + key);
            Log.d("Map", "search place URL: " + sb.toString());
            return sb.toString();
        }


    }


//method of sorting ratting

/*    public class CustomComparator implements Comparator<JSONPojo> {
        @Override
        public int compare(JSONPojo o1, JSONPojo o2) {
            return o1.getRating().compareTo(o2.getRating());
        }
    }*/

    public class CustomComparator implements Comparator<JSONPojo> {
        @Override
        public int compare(JSONPojo o1, JSONPojo o2) {
            return o1.getDistance().compareTo(o2.getDistance());
        }
    }


}

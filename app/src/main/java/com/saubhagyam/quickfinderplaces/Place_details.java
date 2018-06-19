package com.saubhagyam.quickfinderplaces;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by yagnesh on 24/03/18.
 */

public class Place_details extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    boolean showingFirst = true;
    String place_id;
    String key = "AIzaSyDQxEF-rUWRvcJsiM-gRLWJHnsQDt8o9Rk";
    String Finalurl = "";
    String website;
    String formatted_phone_number;
    Toolbar toolbar;
    TextView txt_tool;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    DatabasePojo databasePojo;
    Double latitude;
    Double longitude;
    LatLng latLng;
    double getlet;
    double getlong;
    String dis;
    String name;
    String formatted_address;
    String getImage;
    String getRating;
    String status;
    LinearLayoutManager manager;
    ArrayList<JSONPojo> arr_list;
    Review_Adapter review_adapter;
    RecyclerView recyclerView;
    boolean isZoomSet = false;
    ImageView location_phone_icon, location_website_icon, location_favourite_icon,small_location_icon,small_phone_icon,small_website_icon,small_location_status_icon,small_location_distance_icon;
    TextView location_address_text_view, location_phone_number_text_view, location_website_text_view, location_status_text_view, location_distance_text_view;
    private AlertDialog progressDialog;

    DatabaseHandler db;
    Handler handler;
    Runnable finalizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail_layout);

        handler = new Handler();

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        // set a custom tint color for all system bars

        tintManager.setTintColor(Color.parseColor("#E57200"));
        recyclerView = findViewById(R.id.rv_reviewdata);

        progressDialog = new SpotsDialog(Place_details.this, R.style.Custom);

        toolbar = findViewById(R.id.toolbar_placedetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_tool = toolbar.findViewById(R.id.toolbar_title_placedetails);


        db=new DatabaseHandler(this);

     /*   adView=findViewById(R.id.adView);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-7796828333997958/4622009204");
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
*/

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

        arr_list = new ArrayList<>();
//        review_adapter = new Review_Adapter(Place_details.this, arr_list);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        location_phone_icon = findViewById(R.id.location_phone_icon);
        location_website_icon = findViewById(R.id.location_website_icon);
        location_favourite_icon = findViewById(R.id.location_favourite_icon);


        location_address_text_view = findViewById(R.id.location_address_text_view);
        location_phone_number_text_view = findViewById(R.id.location_phone_number_text_view);
        location_website_text_view = findViewById(R.id.location_website_text_view);
        location_status_text_view = findViewById(R.id.location_status_text_view);
        location_distance_text_view = findViewById(R.id.location_distance_text_view);


        small_location_icon = findViewById(R.id.small_location_icon);
        small_phone_icon = findViewById(R.id.small_phone_icon);
        small_website_icon = findViewById(R.id.small_website_icon);
        small_location_status_icon = findViewById(R.id.small_location_status_icon);
        small_location_distance_icon = findViewById(R.id.small_location_distance_icon);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.details_map);
        mapFragment.getMapAsync(this);

        try {
            place_id = getIntent().getStringExtra("placeID");
            getImage = getIntent().getStringExtra("imagesend");
            getRating = getIntent().getStringExtra("ratingsend");

        } catch (Exception e) {

        }


        Finalurl = getUrl();

        System.out.println("Review Data URL" + Finalurl);

        location_phone_icon.setOnClickListener(this);
        location_website_icon.setOnClickListener(this);
        location_favourite_icon.setOnClickListener(this);


        for (int i=0;i<db.getdata().size();i++)
        {
            System.out.println("Place Id Print " +db.getdata().get(i).getUplaceid());

            if (db.getdata().get(i).getUplaceid().equals(place_id))
            {
                location_favourite_icon.setImageResource(R.drawable.ic_fav_imageasset_black);
            }
        }




    }

    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(finalizer);
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

    public String getUrl() {

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("placeid=" + place_id);
        sb.append("&key=" + key);
        Log.d("Map", "url of place ID: " + sb.toString());
        return sb.toString();


    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new SpotsDialog(Place_details.this, R.style.Custom);
        Log.e("TAG", "onStart: " );
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = new SpotsDialog(Place_details.this, R.style.Custom);
        Log.e("TAG", "onResume: " );
    }

    public void getDetailOfPlace() {

/*        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        if(!(Place_details.this).isFinishing())
        {
            progressDialog.show();
        }

        //final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location= " + latitude + ", " + longitude + " &radius= " + PROXIMATRY_RADIUS + " &type= " + NearByPlace + "&key=" + key;
        final StringRequest request = new StringRequest(Request.Method.GET, Finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                //JSONObject jsonObject = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    formatted_address = result.getString("formatted_address");

                    JSONObject selectedplace = result.getJSONObject("geometry");
                    JSONObject selectedlocation = selectedplace.getJSONObject("location");
                    String selected_lat = selectedlocation.getString("lat");
                    String selected_lng = selectedlocation.getString("lng");


                    try {
                        JSONArray review_arry = result.getJSONArray("reviews");
                        review_adapter = new Review_Adapter(Place_details.this, review_arry);

                        review_adapter.notifyDataSetChanged();

                        recyclerView.setAdapter(review_adapter);

                        Log.e("SIZE", arr_list.size() + "");
                        // review = review_arry.getString();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    name = result.getString("name");

                    txt_tool.setText(name);

                    System.out.println("Selected Place Name" + name);


                    getlet = Double.parseDouble(selected_lat);
                    getlong = Double.parseDouble(selected_lng);

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_imageasset_orange);
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(getlet, getlong)).icon(icon));


                    //    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getlet, getlong), 13));


                    website = result.has("website") ? result.getString("website") : " ";
                    formatted_phone_number = result.has("formatted_phone_number") ? result.getString("formatted_phone_number") : "";
                    String open_now = "false";
                    if (result.has("opening_hours")) {
                        JSONObject opening_hours = result.getJSONObject("opening_hours");
                        open_now = opening_hours.getString("open_now");
                    }


                    status = open_now.equals("true") ? "Open" : "Closed";

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(new LatLng(latitude, longitude))
                            .color(Color.BLUE)
                            .add(new LatLng(getlet, getlong));

                    Polyline polyline = mGoogleMap.addPolyline(polylineOptions);
                    polyline.isVisible();

                    Location locationA = new Location("point A");

                    locationA.setLatitude(latitude);
                    locationA.setLongitude(longitude);

                    Location locationB = new Location("point B");

                    locationB.setLatitude(getlet);
                    locationB.setLongitude(getlong);

                    float distance = locationA.distanceTo(locationB) / 1000;
                    dis = String.format("%.01f", distance);




                    location_address_text_view.setText(formatted_address);
                    if (location_address_text_view.getText().toString()==null || location_address_text_view.getText().toString().isEmpty())
                    {
                        small_location_icon.setVisibility(View.GONE);
                        location_address_text_view.setVisibility(View.GONE);
                    }

                    location_phone_number_text_view.setText(formatted_phone_number);

                    if (location_phone_number_text_view.getText().toString()==null || location_phone_number_text_view.getText().toString().isEmpty())
                    {
                        small_phone_icon.setVisibility(View.GONE);
                        location_phone_number_text_view.setVisibility(View.GONE);
                    }
                    location_website_text_view.setText(website);
                    String mwebSite=location_website_text_view.getText().toString().trim();
                    Log.e("TAG", "WebSite"+mwebSite );
                    if (mwebSite.equals("")||mwebSite.equals(" "))
                    {
                        Log.e("Tag", "done");
                        small_website_icon.setVisibility(View.GONE);
                        location_website_text_view.setVisibility(View.GONE);
                    }

                    location_status_text_view.setText(status);

                    if (location_status_text_view.getText().toString()==null || location_status_text_view.getText().toString().isEmpty())

                    {
                        small_location_status_icon.setVisibility(View.GONE);
                        location_status_text_view.setVisibility(View.GONE);
                    }

                    location_distance_text_view.setText(dis + " Km");

                    if (location_distance_text_view.getText().toString()==null || location_distance_text_view.getText().toString().isEmpty())
                    {
                        small_location_distance_icon.setVisibility(View.GONE);
                        location_distance_text_view.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();

            }

        };
        VolleySingleton.getInstance(Place_details.this).addToRequestQueue(request);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.location_website_icon: {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(website));
                startActivity(viewIntent);
                break;
            }

            case R.id.location_phone_icon: {
                //  Toast.makeText(getApplicationContext(), "Phone" + formatted_phone_number, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + formatted_phone_number));
                startActivity(intent);
                break;
            }
            case R.id.location_favourite_icon: {
                databasePojo = new DatabasePojo();

                if (showingFirst == true) {
                    location_favourite_icon.setImageResource(R.drawable.ic_fav_imageasset_black);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                           db.insertData(new Contact_Databse_pojo(name,place_id,formatted_address,status,getRating,dis));
                           /* // databasePojo = new DatabasePojo();
                            databasePojo.setPlacename(name);
                            databasePojo.setAddress(formatted_address);
                            databasePojo.setPlaceicon(getImage);
                            databasePojo.setRating(getRating);
                            databasePojo.setKm(dis);
                            databasePojo.setPlace_id(place_id);

                            databasePojo.setStatus(status);
                            DatabaseCreate.AppDatabase.getAppDatabase(getApplicationContext()).userDao().insertdata(databasePojo);
*/

                        }
                    }).start();
                    showingFirst = false;
                } else {
                    location_favourite_icon.setImageResource(R.drawable.favourite_details_white);
/*                    int possition = Integer.parseInt(arr_list.get(1).getId());

                    db.deleteData(arr_list.get(possition).getId());*/

                        db.deleteData(place_id);


                   // System.out.println("Place ID-->" + arr_list.get(1).getId());


                   // DatabaseCreate.AppDatabase.getAppDatabase(this).userDao().delete(databasePojo);
                    showingFirst = true;
                }
              /*  location_favourite_icon.setImageResource(R.drawable.favourite_detailsdark);

                location_favourite_icon.setEnabled(false);*/
                // Toast.makeText(Place_details.this, "Data added to Favourate", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
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
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        getDetailOfPlace();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_imageasset_black);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(icon);
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        if (isZoomSet == false) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 3000, null);
            isZoomSet = true;
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                Toast.makeText(this, "Please give location permition", Toast.LENGTH_SHORT).show();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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


}

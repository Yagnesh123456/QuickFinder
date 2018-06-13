package com.saubhagyam.quickfinderplaces;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity
        implements View.OnClickListener {

    Fragment fragment = null;
    TextView imghome, imgvoicesearch, imgfavorite, imgsetting;
    SearchView searchView;
    Toolbar toolbar;

    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemLocation;
    private ResideMenuItem itemFavorites;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemAbout;

    private ResideMenuItem itemShare;
    private ResideMenuItem itemRateUs;
    private ResideMenuItem itemExit;
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
           // Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
           // Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);


// set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#BB77AA"));

     //   set a custom navigation bar resource
     //   tintManager.setNavigationBarTintResource(R.drawable.gradient);

// set a custom status bar drawable
       // tintManager.setStatusBarTintDrawable(android.R.color.background_dark);


        toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
                handler.postDelayed(new Runnable() {
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

        //hide system functinality

       hideSystemUI();

        //Code for Status bar

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE );


        mContext = this;
        setUpMenu();

        imghome = findViewById(R.id.img_home);
        imgvoicesearch = findViewById(R.id.voice_search);
        imgfavorite = findViewById(R.id.img_fav);
        imgsetting = findViewById(R.id.img_setting);

        imghome.setOnClickListener(this);
        imgvoicesearch.setOnClickListener(this);
        imgfavorite.setOnClickListener(this);
        imgsetting.setOnClickListener(this);


        fragment = new Home_Fragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.framlayout, fragment);
        transaction.commit();


    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.gradient);

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.ic_home_white, "Home");
        itemLocation = new ResideMenuItem(this, R.drawable.ic_mylocation_white, "My Location");
        itemFavorites = new ResideMenuItem(this, R.drawable.ic_favorites_white, "Favorites");
        itemSettings = new ResideMenuItem(this, R.drawable.ic_setting_white, "Settings");
        itemShare = new ResideMenuItem(this, R.drawable.ic_share_white, "Share");
        itemRateUs = new ResideMenuItem(this, R.drawable.ic_rate_white, "Rate Us");
        itemExit = new ResideMenuItem(this, R.drawable.ic_exit_white, "Exit");


        itemHome.setOnClickListener(this);
        itemLocation.setOnClickListener(this);
        itemFavorites.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemShare.setOnClickListener(this);
        itemRateUs.setOnClickListener(this);
        itemExit.setOnClickListener(this);


        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLocation, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFavorites, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRateUs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExit, ResideMenu.DIRECTION_LEFT);

        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.collapseActionView();

        // menu.getItem(0).setIcon(R.drawable.ic_favorites_white);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Near By Place"); // set the hint text to display in the query text field
        searchView.setPadding(20, 30, 20, 10);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    Intent i = new Intent(MenuActivity.this, Category_Activity_TextSearch.class);
                    i.putExtra("q", query);
                    startActivity(i);
                } else {
                    Toast.makeText(MenuActivity.this, "Please Search Near Place", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    //adapter.filter("");
                    //listView.clearTextFilter();
                } else {
                    //adapter.filter(newText);
                }

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

           /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                                                  @Override
                                                  public boolean onQueryTextSubmit(String s) {
               // [You actions here]
                                                      return false;
                                                  }
                                              }*/
           // Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void Exitonback() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("\"<b>\"+<font color='#000000'>Confirm?</font>" + "</b>"));
        builder.setMessage("      Do You Really Want To Exit?")
                .setPositiveButton(Html.fromHtml("Yes"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(Html.fromHtml("No"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_primary_text));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.color_primary_text));
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            fragment = new Home_Fragment();


            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_purpul), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == itemLocation) {

            fragment = new Location_Fragment();

            //Toast.makeText(this, "My Location", Toast.LENGTH_SHORT).show();
            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == itemFavorites) {
            fragment = new Favourite_fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();

            //Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_purpul), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == itemSettings) {

            //Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            fragment = new Setting_Fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_purpul), null, null);


        } /*else if (view == itemAbout) {

          *//*  fragment = new About_Fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();*//*

            //Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        }*/ else if (view == itemShare) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLooking for nearest place like Cafe,Theatre,ATM,Bank,Bus,Bakery?-Checkout following amazing app which provides nearest places quickly on your mobile.\n\n Quick Finder ";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.saubhagyam.quickfinderplaces\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == itemRateUs) {

            Uri uri = Uri.parse("market://details?id=" + MenuActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.saubhagyam.quickfinderplaces" + MenuActivity.this.getPackageName())));
            }
            //Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show();
            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == itemExit) {
            Exitonback();

        } else if (view == imghome) {

            fragment = new Home_Fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_purpul), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == imgvoicesearch) {
            //Toast.makeText(this, "Voice", Toast.LENGTH_SHORT).show();
            final int RECOGNIZER_REQ_CODE = 1234;

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            startActivityForResult(intent, RECOGNIZER_REQ_CODE);


            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_purpul), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);
        } else if (view == imgfavorite) {

            fragment = new Favourite_fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();

           // Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_purpul), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_grey), null, null);


        } else if (view == imgsetting) {

            fragment = new Setting_Fragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();
           // Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();

            imghome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_home_grey), null, null);
            imgvoicesearch.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_voice_search_grey1), null, null);
            imgfavorite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey), null, null);
            imgsetting.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_purpul), null, null);
        }


        if (fragment != null) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.framlayout, fragment);
            transaction.commit();
        }


        resideMenu.closeMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            ArrayList<String> textMatchList = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (textMatchList.size() > 0) {

                String listString = "";

                for (String s : textMatchList) {
                    listString += s + "";
                }

                Intent i = new Intent(MenuActivity.this, Category_Activity.class);
                i.putExtra("category", listString);
                i.putExtra("isCat", "0");
                startActivity(i);
            }
        }
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }


}

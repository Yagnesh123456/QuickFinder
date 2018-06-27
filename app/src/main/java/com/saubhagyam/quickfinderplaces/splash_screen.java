package com.saubhagyam.quickfinderplaces;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import gr.net.maroulis.library.EasySplashScreen;


public class splash_screen extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

// Manually checking internet connection
        checkConnection();


        View easySplashScreenView = new EasySplashScreen(splash_screen.this)
                    .withFullScreen()
                    .withTargetActivity(MenuActivity.class)
                    .withSplashTimeOut(2000)
                    .withBackgroundResource(R.color.colorWhite)
                    .withHeaderText("")
                    .withFooterText("")
                    .withBeforeLogoText("")
                    .withLogo(R.drawable.app_logo_saubhagyam)
                    .withAfterLogoText("")
                    .create();

            setContentView(easySplashScreenView);





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
        //android hive demo for Android Detect Internet Connection Status
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

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

}

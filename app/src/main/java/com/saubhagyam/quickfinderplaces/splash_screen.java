package com.saubhagyam.quickfinderplaces;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


import gr.net.maroulis.library.EasySplashScreen;


public class splash_screen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


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

}

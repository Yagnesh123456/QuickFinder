package com.saubhagyam.quickfinderplaces;

import android.app.Application;

public class CustomFontApp extends Application {   //This Activity code used in My Application

    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/SegoeUI-Light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/SegoeUI-Light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/SegoeUI-Light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/SegoeUI-Light.ttf");

    }
}

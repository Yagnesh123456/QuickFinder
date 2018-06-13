package com.saubhagyam.quickfinderplaces;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yagnesh on 08/04/18.
 */

public class PreferencesManager {

    public static final String PREF_NAME = "NEAR_ME";
    public static final String SEEKBAR_PREF = "seekbar";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    public PreferencesManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public int getSeekbar() {
        return pref.getInt(SEEKBAR_PREF, 5);
    }

    public void setSeekbar(int progress) {
        editor.putInt(SEEKBAR_PREF, progress);
        editor.commit();
    }
}

package com.saubhagyam.quickfinderplaces;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.special.ResideMenu.ResideMenu;
import com.warkiz.widget.IndicatorSeekBar;

/**
 * Created by yagnesh on 30/03/18.
 */

public class Setting_Fragment extends Fragment implements View.OnClickListener {


    private AdView adView;
    private AdRequest adRequest;
    InterstitialAd interstitialAd;

    public static String dbkey = "radious";
    public static String range = "range";
    public static String last_range = "last_range";
    View view;
    IndicatorSeekBar seekbar_setting;
    TextView txtdistance;
    String HightToLow = "HighToLow";
    String LowToHigh = "LowToHigh";
    String priority_high, priority_low;
    Button btnpriority;
    int count = 0;
    String popupname;
    SharedPreferences sharedPreferences;
    PreferencesManager preferencesManager;
    private ResideMenu resideMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container, false);
        resideMenu = new ResideMenu(getActivity());
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        preferencesManager = new PreferencesManager(getActivity());
        seekbar_setting = view.findViewById(R.id.seekbar_setting);

        txtdistance = view.findViewById(R.id.txtdistance);
        btnpriority = view.findViewById(R.id.btnpriority);
        btnpriority.setOnClickListener(this);

        adView=view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity(),"ca-app-pub-7796828333997958/4622009204");
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        // resideMenu.addIgnoredView(seekbar_setting);  //on change seekbar value then side menu cant open

        seekbar_setting.setProgress(preferencesManager.getSeekbar());
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);// disable left swipe
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);// disable right swipe

        if (last_range.length() == 0) {
            seekbar_setting.setProgress(5);
        } else {
            seekbar_setting.setProgress(preferencesManager.getSeekbar());
        }


        seekbar_setting.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

                count = progress;
                //txtdistance.setText("Distance :" + count + " km");
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                //count= count*1000;
                //progress = count;
                edit.putInt(range, count);
                edit.putInt(last_range, count);
                edit.apply();
                edit.commit();
                preferencesManager.setSeekbar(progress);

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                //count=count/1000;
                //Toast.makeText(getActivity(), "Your Selected Range is"+" "+count+" "+"km", Toast.LENGTH_SHORT).show();
                seekbar_setting.setProgress(count);
            }
        });


        sharedPreferences = getActivity().getSharedPreferences(dbkey, Context.MODE_PRIVATE);

/*
        if (last_range.length()!=0)
        {
            seekbar_setting.setProgress(sharedPreferences.getInt(last_range,5));
        }
*/


        return view;
    }


    @Override
    public void onClick(View v) {

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        PopupMenu popupMenu = new PopupMenu(getActivity(), btnpriority);

        popupMenu.getMenuInflater().inflate(R.menu.priority, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.hightolow:
                        editor.clear();
                        priority_high = item.getTitle().toString();
                        //System.out.println("====================>"+priority);
                        editor.putString("HighToLow", priority_high);
                        editor.commit();
                        //Toast.makeText(getActivity(), "You Clicked High To Low", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.lowtohigh:
                        editor.clear();
                        priority_low = item.getTitle().toString();
                        editor.putString("LowToHigh", priority_low);
                        editor.commit();
                        break;
                }
                return false;
            }
        });


    }

}

package com.saubhagyam.quickfinderplaces;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


/**
 * Created by yagnesh on 15/03/18.
 */

public class Home_Fragment extends Fragment {

    public static RecyclerView recyclerView;
    public static ArrayList<RecyclerPojo> data;
    static View.OnClickListener myOnClickListener;
    private static RecyclerView.Adapter adapter;
    View view;
    private RecyclerView.LayoutManager layoutManager;

    private AdView adView;
    private AdRequest adRequest;
    InterstitialAd interstitialAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

      /*  RelativeLayout rootView=view.findViewById(R.id.rootView);
        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.scale_down);
        rootView.setLayoutAnimation(anim);*/  //Animation at home fragment

        adView=view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity(),"ca-app-pub-7796828333997958/4622009204");
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<RecyclerPojo>();
        for (int i = 0; i < MyData.nameArray.length; i++) {


            data.add(new RecyclerPojo(
                    MyData.nameArray[i],
                    MyData.drawableArray[i]
            ));
        }


        adapter = new Recycler_Adapter(data);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Home Screen");
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;
        Fragment fragment = null;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            showItem(v);

            //Toast.makeText(context, "ATM", Toast.LENGTH_SHORT).show();
        }

        private void showItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();


            FragmentTransaction transection = getFragmentManager().beginTransaction();
            Category_Activity mfragment = new Category_Activity();

            //selected category name send to Category_Activity

            Intent i = new Intent(getActivity(), Category_Activity.class);
            i.putExtra("category", MyData.googleArray[selectedItemPosition]);
            i.putExtra("toolbartitle", MyData.nameArray[selectedItemPosition]);
            //  System.out.println("Category value++++++++++" +MyData.googleArray[selectedItemPosition]);
            i.putExtra("isCat", "1");
            i.putExtra("type", MyData.googleArray[selectedItemPosition]);
            startActivity(i);


        }


    }
}


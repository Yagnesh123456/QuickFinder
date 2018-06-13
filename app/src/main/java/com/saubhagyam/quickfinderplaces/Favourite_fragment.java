package com.saubhagyam.quickfinderplaces;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yagnesh on 30/03/18.
 */

public class Favourite_fragment extends Fragment {

    View view;
    ListView listView;
    List<DatabasePojo> list;
    CustomRoomAdapter customRoomAdapter;
    private AdView adView;
    private AdRequest adRequest;
    InterstitialAd interstitialAd;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favourite_fragment, container, false);


        listView = view.findViewById(R.id.listview);

        list = new ArrayList<>();

        list = DatabaseCreate.AppDatabase.getAppDatabase(getActivity()).userDao().getAll();


        customRoomAdapter = new CustomRoomAdapter(getActivity(), list);
        listView.setAdapter(customRoomAdapter);

        adView=view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity(),"ca-app-pub-7796828333997958/4622009204");
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        if (list.size() == 0) {
            Toast.makeText(getActivity(), "Nothing  in Favourite", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public class CustomRoomAdapter extends ArrayAdapter<DatabasePojo> {

        List<DatabasePojo> list = new ArrayList<>();
        Context context;

        public CustomRoomAdapter(@NonNull Context context, List<DatabasePojo> list) {
            super(context, R.layout.custom_room_adapter, list);
            this.context = context;
            this.list = list;

        }


        @Override
        public int getCount() {
            return list.size();
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_room_adapter, null);

            TextView txtplace = convertView.findViewById(R.id.txt_place_name1);
            TextView txtplaceadd = convertView.findViewById(R.id.txt_place_address1);
          //  ImageView detailsicon = convertView.findViewById(R.id.img_location_icon1);
            TextView detailskm = convertView.findViewById(R.id.txt_place_distance_text_view1);
            TextView detailsopen = convertView.findViewById(R.id.txt_place_open_status1);
            RatingBar detailsrating = convertView.findViewById(R.id.place_rating1);


            //change ratingbar color
          /*  LayerDrawable stars = (LayerDrawable) detailsrating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


          // Button imgdelete = convertView.findViewById(R.id.imgdelete);



            String detailurl = list.get(position).getPlaceicon();
            String rate = list.get(position).getRating();

            float detailrate = Float.parseFloat(rate);


            detailsrating.setRating(detailrate);

            txtplace.setText(list.get(position).getPlacename());
            txtplaceadd.setText(list.get(position).getAddress());
            detailskm.setText(list.get(position).getKm() + " km");
            detailsopen.setText(list.get(position).getStatus());


            //Deleted Data on swipe in Favourite Fragment
            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(
                            listView,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {

                                    DatabaseCreate.AppDatabase.getAppDatabase(context).userDao().delete(list.get(position));
                                    return true;
                                }

                                @Override
                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {

                                        list.remove(position);
                                        customRoomAdapter.notifyDataSetChanged();

                                    }


                                }
                            });
            listView.setOnTouchListener(touchListener);


         /*   Glide.with(context).load(detailurl)
                    .thumbnail(0.5f)
                    .into(detailsicon);*/

          /*  imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseCreate.AppDatabase.getAppDatabase(context).userDao().delete(list.get(position));
                    Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(Favourite_fragment.this).attach(Favourite_fragment.this).commit();
                }
            });*/

            return convertView;
        }
    }

}

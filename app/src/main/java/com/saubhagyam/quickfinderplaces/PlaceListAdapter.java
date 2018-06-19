package com.saubhagyam.quickfinderplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;


/**
 * Created by yagnesh on 22/03/18.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.MyViewHolder> {

    public static String dbkey = "radious";
    public ArrayList<JSONPojo> dataSet;
    Context context;
    String placeid;
    //public static String range="range";
    SharedPreferences sharedPreferences;
   /* String hightolow, lowtohigh;*/
    String data;

    public PlaceListAdapter(Context context, ArrayList<JSONPojo> data) {

        this.context = context;
        this.dataSet = data;
    }

    @Override
    public PlaceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item_layout, parent, false);

        //view.setOnClickListener(Home_Fragment.myOnClickListener);

        PlaceListAdapter.MyViewHolder myViewHolder = new PlaceListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        /*Animation animation = AnimationUtils.loadAnimation(context, R.anim.slidein);
        animation.setDuration(500);
        holder.rootView.startAnimation(animation);*/

        TextView textViewDistance = holder.txt_place_distance_text_view;
        TextView textViewPlaceName = holder.txt_place_name;
        TextView textViewPlaceAddress = holder.txt_place_address;
        TextView textViewOpenStatus = holder.txt_place_open_status;
        RatingBar Rat_bar = holder.place_rating;

      /*  LayerDrawable stars = (LayerDrawable) Rat_bar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);  //Change color off RattingBar*/



        String var = dataSet.get(listPosition).getRating();
        Float var1 = Float.parseFloat(var);
        Float minrates = Float.MIN_VALUE;

        //System.out.println("==========>Max Rate"+maxrates);
        System.out.println("==========>Min Rate" + minrates);

        placeid = dataSet.get(listPosition).getId();

        ImageView imageView = holder.img_location_icon;
        String imgurl = dataSet.get(listPosition).getImage().toString();
        textViewDistance.setText(dataSet.get(listPosition).getDistance() + " km");

        textViewPlaceName.setText(dataSet.get(listPosition).getName());
        textViewPlaceAddress.setText(dataSet.get(listPosition).getVicinity());
        textViewOpenStatus.setText(dataSet.get(listPosition).getOpen_now());

        Rat_bar.setRating(Float.parseFloat(dataSet.get(listPosition).getRating()));




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_place_distance_text_view, txt_place_name, txt_place_address, txt_place_open_status;
        ImageView img_location_icon;
        RatingBar place_rating;
        RelativeLayout rootView;


        public MyViewHolder(final View itemView) {
            super(itemView);
            txt_place_distance_text_view = itemView.findViewById(R.id.txt_place_distance_text_view);
            img_location_icon = itemView.findViewById(R.id.img_location_icon);
            txt_place_name = itemView.findViewById(R.id.txt_place_name);
            txt_place_address = itemView.findViewById(R.id.txt_place_address);
            txt_place_open_status = itemView.findViewById(R.id.txt_place_open_status);
            place_rating = itemView.findViewById(R.id.place_rating);
            rootView = itemView.findViewById(R.id.plit_rootView);

/*            sharedPreferences = context.getSharedPreferences(dbkey, Context.MODE_PRIVATE);

            hightolow = sharedPreferences.getString("HighToLow", null);
            lowtohigh = sharedPreferences.getString("LowToHigh", null);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Place_details.class);
                    //  i.putExtra("keydata",dataSet.get(getAdapterPosition()).getId());  //place id from JSON
                    i.putExtra("placeID", dataSet.get(getAdapterPosition()).getId());
                    i.putExtra("ratingsend", dataSet.get(getAdapterPosition()).getRating());
                    i.putExtra("imagesend", dataSet.get(getAdapterPosition()).getImage());
                    context.startActivity(i);


                    // Toast.makeText(context, "HELLO", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
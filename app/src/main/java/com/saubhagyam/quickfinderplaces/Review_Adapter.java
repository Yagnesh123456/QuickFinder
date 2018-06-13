package com.saubhagyam.quickfinderplaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yagnesh on 06/04/18.
 */

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder> {

    public static String dbkey = "radious";
//    public ArrayList<JSONPojo> dataSet;
    Context context;
    String placeid;
    //public static String range="range";


  /*  public Review_Adapter(Context context, ArrayList<JSONPojo> data) {

        this.context = context;
        this.dataSet = data;
        Log.e("SIZE", dataSet.size() + "");

    }*/


  JSONArray array;

    public Review_Adapter(Context context, JSONArray array) {

        this.context = context;
        this.array = array;

    }

    @NonNull
    @Override
    public Review_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_data, parent, false);

        //view.setOnClickListener(Home_Fragment.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Review_Adapter.MyViewHolder holder, final int listPosition) {

        try {
            JSONObject c = array.getJSONObject(listPosition);


            ImageView img_review = holder.img_review;
//            String imgurl = dataSet.get(listPosition).getReview_image().toString();
//            String imgurl = opbj.getString("icon");

//            System.out.println("================>>>>>>>" + imgurl);


//            placeid = dataSet.get(listPosition).getReview();

            holder.txt_review.setText(c.getString("text"));
         //   placeid= c.getString("id");
            holder.txt_reviewname.setText(  c.getString("author_name"));
            String imgurl = c.getString("profile_photo_url");

//            txt_review.setText(dataSet.get(listPosition).getReview());
//            txt_review_name.setText(dataSet.get(listPosition).getReview_name());

            Glide.with(context)
                    .load(imgurl)
                    .into(img_review);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_review, txt_reviewname;
        ImageView img_review;


        public MyViewHolder(final View itemView) {
            super(itemView);
            txt_review = itemView.findViewById(R.id.txtreview);
            txt_reviewname = itemView.findViewById(R.id.txtreviewname);
            img_review = itemView.findViewById(R.id.img_review);


        }
    }
}
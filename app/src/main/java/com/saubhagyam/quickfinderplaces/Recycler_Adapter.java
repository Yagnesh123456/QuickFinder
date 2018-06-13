package com.saubhagyam.quickfinderplaces;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by yagnesh on 15/03/18.
 */

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.MyViewHolder> {

    private ArrayList<RecyclerPojo> dataSet;

    public Recycler_Adapter(ArrayList<RecyclerPojo> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        view.setOnClickListener(Home_Fragment.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        ImageView imageView = holder.imageViewIcon;
        try {
            textViewName.setText(dataSet.get(listPosition).getName());
            imageView.setImageResource(dataSet.get(listPosition).getImage());
        } catch (Exception e) {
            Log.i("data", e.toString());
        }
       /* if() {  //Change bagraound programatically
        ((CardView)holder.post_card_view).setCardBackgroundColor(Color.GRAY);
    }
else
    {
        ((CardView)holder.post_card_view).setCardBackgroundColor(Color.RED);
    }
*/

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageViewIcon;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
        }
    }
}
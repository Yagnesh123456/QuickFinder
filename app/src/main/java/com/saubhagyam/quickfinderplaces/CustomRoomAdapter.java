package com.saubhagyam.quickfinderplaces;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class CustomRoomAdapter extends RecyclerView.Adapter<CustomRoomAdapter.MyViewHolder>  {

    private Context context;
    List<Contact_Databse_pojo> list;
    String place_id;
    RecyclerView recyclerView;
    DatabaseHandler db;

    public CustomRoomAdapter(Context context, List<Contact_Databse_pojo> list) {
        this.context = context;
        this.list = list;
        db = new DatabaseHandler(context);
    }

    @Override
    public CustomRoomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_room_adapter, parent, false);

        view.setOnClickListener(Home_Fragment.myOnClickListener);

        CustomRoomAdapter.MyViewHolder myViewHolder = new CustomRoomAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomRoomAdapter.MyViewHolder holder, final int position) {

        String rate = list.get(position).getUrate();
        float detailrate = Float.parseFloat(rate);


        holder.txtplace.setText(list.get(position).getUname());
        holder.txtplaceid.setText(list.get(position).getUplaceid());
        holder.txtplaceadd.setText(list.get(position).getUadd());
        holder.detailsopen.setText(list.get(position).getUstatus());
        holder.detailskm.setText(list.get(position).getUkm());
        holder.ratingBar.setRating(detailrate);


        //put her recycler swipe delete

                


     /*   //Deleted Data on swipe in Favourite Fragment
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        recyclerView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {

                                db.deleteData(p);

                               // DatabaseCreate.AppDatabase.getAppDatabase(context).userDao().delete(list.get(position));
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    list.remove(position);

                            }



                            }
                        });
        recyclerView.setOnTouchListener(touchListener);*/



        Log.e("Tag", "onBindViewHolder: "+list.get(position).getUadd());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_id = list.get(position).getUplaceid();

                System.out.println("Place Id in Favourite" + place_id);
                Intent i = new Intent(context, Place_details.class);


                //  i.putExtra("keydata",dataSet.get(getAdapterPosition()).getId());  //place id from JSON
                i.putExtra("placeID", place_id);
                // i.putExtra("ratingsend", dataSet.get(getAdapterPosition()).getRating());
                // i.putExtra("imagesend", dataSet.get(getAdapterPosition()).getImage());
                context.startActivity(i);
            }
        });



    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtplace, txtplaceadd, detailskm, detailsopen,txtplaceid;
        RelativeLayout relativeLayout;
        RatingBar ratingBar;



        public MyViewHolder(final View itemView) {
            super(itemView);

            relativeLayout =(RelativeLayout) itemView.findViewById(R.id.onclickmethod);

            txtplace = itemView.findViewById(R.id.txt_place_name1);
            txtplaceid = itemView.findViewById(R.id.placeid);
            txtplaceadd = itemView.findViewById(R.id.txt_place_address1);
            detailsopen = itemView.findViewById(R.id.txt_place_open_status1);
            detailskm = itemView.findViewById(R.id.txt_place_distance_text_view1);
            ratingBar = itemView.findViewById(R.id.place_rating1);









        }


    }

   /* public void remove(int position) {
        if (position < 0 || position >= list.size() ) {
            return;
        }
        list.remove(position);
        notifyItemRemoved(position);
    }*/
}

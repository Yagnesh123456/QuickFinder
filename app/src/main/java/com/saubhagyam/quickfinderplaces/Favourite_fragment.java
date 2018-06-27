package com.saubhagyam.quickfinderplaces;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    RecyclerView recyclerView;
    List<Contact_Databse_pojo> list;
    CustomRoomAdapter customRoomAdapter;
    LinearLayoutManager manager;
    InterstitialAd interstitialAd;
    TextView txtmsg;
    String place_id;
    DatabaseHandler db;
    private AdView adView;
    private AdRequest adRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favourite_fragment, container, false);


        recyclerView = view.findViewById(R.id.rvfavourite);

        txtmsg = view.findViewById(R.id.txtmsg);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        db = new DatabaseHandler(getActivity());

        list = db.getdata();

        //  list = DatabaseCreate.AppDatabase.getAppDatabase(getActivity()).userDao().getAll();
        customRoomAdapter = new CustomRoomAdapter(getActivity(), list);
        customRoomAdapter.notifyDataSetChanged();
        //customRoomAdapter = new CustomRoomAdapter(getActivity(), list);
        recyclerView.setAdapter(customRoomAdapter);

/*        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        recyclerView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {

                                //  DatabaseCreate.AppDatabase.getAppDatabase(context).userDao().delete(list.get(position));
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    list.remove(position);
                                    customRoomAdapter.notifyDataSetChanged();

                                }


                            }
                        });

        recyclerView.setOnTouchListener(touchListener);*/


       /* recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                place_id = list.get(position).getUplaceid();

                System.out.println("Place Id in Favourite" + place_id);
                Intent i = new Intent(getActivity(), Place_details.class);
                //  i.putExtra("keydata",dataSet.get(getAdapterPosition()).getId());  //place id from JSON
                i.putExtra("placeID", place_id);
                // i.putExtra("ratingsend", dataSet.get(getAdapterPosition()).getRating());
                // i.putExtra("imagesend", dataSet.get(getAdapterPosition()).getImage());
                getActivity().startActivity(i);
                // Toast.makeText(getActivity(), "Welcome On item click ", Toast.LENGTH_SHORT).show();
            }
        });*/

        adView = view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity(), "ca-app-pub-7796828333997958/4622009204");
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        if (list.size() == 0) {
            txtmsg.setVisibility(View.VISIBLE);
            // Toast.makeText(getActivity(), "Nothing  in Favourite", Toast.LENGTH_LONG).show();
        }
        setUpItemTouchHelper();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Favourite Screen");
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                CustomRoomAdapter adapter = (CustomRoomAdapter) recyclerView.getAdapter();
              //  adapter.remove(swipedPosition);
               // db.deleteData(swipedPosition)

                adapter.notifyItemRemoved(swipedPosition);

                db.deleteData(list.get(swipedPosition).getUplaceid());
                list.remove(swipedPosition);
                adapter.notifyDataSetChanged();

            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }



   /* public class CustomRoomAdapter extends ArrayAdapter<Contact_Databse_pojo> {

        List<Contact_Databse_pojo> list;
        Context context;

        public CustomRoomAdapter(@NonNull Context context, List<Contact_Databse_pojo> list) {
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

            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.onclickmethod);

            TextView txtplace = convertView.findViewById(R.id.txt_place_name1);
            TextView txtplaceadd = convertView.findViewById(R.id.txt_place_address1);
            //  ImageView detailsicon = convertView.findViewById(R.id.img_location_icon1);
            TextView detailskm = convertView.findViewById(R.id.txt_place_distance_text_view1);
            TextView detailsopen = convertView.findViewById(R.id.txt_place_open_status1);
            RatingBar detailsrating = convertView.findViewById(R.id.place_rating1);

            final TextView txtplaceid = convertView.findViewById(R.id.placeid);



            //change ratingbar color
          *//*  LayerDrawable stars = (LayerDrawable) detailsrating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*//*


            // Button imgdelete = convertView.findViewById(R.id.imgdelete);


            // String detailurl = list.get(position).getPlaceicon();
            String rate = list.get(position).getUrate();

//            float detailrate = Float.parseFloat(rate);


            //        detailsrating.setRating(detailrate);

            txtplace.setText(list.get(position).getUname());
            txtplaceadd.setText(list.get(position).getUadd());
            detailskm.setText(list.get(position).getUkm());
            detailsopen.setText(list.get(position).getUstatus());
            txtplaceid.setText(list.get(position).getUplaceid());
            Log.e("tag", "getView: "+txtplaceid.getText().toString() );
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "onClick: "+txtplaceid.getText().toString() );
                }
            });

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


         *//*   Glide.with(context).load(detailurl)
                    .thumbnail(0.5f)
                    .into(detailsicon);*//*

          *//*  imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseCreate.AppDatabase.getAppDatabase(context).userDao().delete(list.get(position));
                    Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(Favourite_fragment.this).attach(Favourite_fragment.this).commit();
                }
            });*//*

            return convertView;
        }
    }
*/
}

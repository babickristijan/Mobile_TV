
package com.everywhereTV.everywhereTV;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.everywhereTV.everywhereTV.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class AdapterChannel extends RecyclerView.Adapter<MainActivity.VideoViewHolder> implements Filterable{
    private ArrayList<Data> mArrayList;
    private ArrayList<Data> mFilteredList;

        private MainActivity.OnVideoClickListener listener;

        private Context context;
        private LayoutInflater inflater;
        //List<Data> data= Collections.emptyList();


        // create constructor to innitilize context and data sent from MainActivity
        public AdapterChannel(Context context, ArrayList<Data> data,@NonNull MainActivity.OnVideoClickListener listener) {
            this.listener = listener;
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.mArrayList=data;
            mFilteredList = data;
        }



        // Inflate the layout when viewholder created
        @NonNull
        @Override
        public MainActivity.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.container_channel, parent,false);
            AdapterChannel.MyHolder holder=new AdapterChannel.MyHolder(view);
            return new MainActivity.VideoViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.VideoViewHolder holder, int position) {
            // Get current position of item in recyclerview to bind data and assign values from list

            holder.bind(mFilteredList.get(position));

            MainActivity.VideoViewHolder myHolder= (MainActivity.VideoViewHolder) holder;

            Data current=mFilteredList.get(position);
            myHolder.title.setText(current.title);


            // load image into imageview using glide
            Glide.with(context).load("http://www.gtnet.hr/tv/assets/picon/" + current.logo)
                    .placeholder(R.drawable.error)
                    .error(R.drawable.error)
                    .into(myHolder.ivlogo);
        }


        // return total item from List
        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }

    @Override
    public Filter getFilter() {
        return new Filter() {


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Data> filteredList = new ArrayList<>();

                    for (Data data : mFilteredList) {

                        if (data.title.toLowerCase().contains(charString) || data.programUrl.toLowerCase().contains(charString) || data.logo.toLowerCase().contains(charString)) {

                            filteredList.add(data);
                        }
                    }

                    mFilteredList = filteredList;
                }



                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Data>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class MyHolder extends RecyclerView.ViewHolder{

            TextView title;
            ImageView ivLogo;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                title= (TextView) itemView.findViewById(R.id.Title);
                ivLogo= (ImageView) itemView.findViewById(R.id.ivLogo);

            }

        }

    }



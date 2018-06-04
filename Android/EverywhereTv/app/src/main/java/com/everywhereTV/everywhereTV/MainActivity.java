package com.everywhereTV.everywhereTV;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.everywhereTV.everywhereTV.R;
import com.appunite.appunitevideoplayer.PlayerActivity;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity  {

    List<Data> data=Collections.emptyList();


    interface OnVideoClickListener {
        void onVideoClick(@NonNull Data item);
    }


    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public AdapterChannel mAdapter;



   public static class VideoViewHolder extends RecyclerView.ViewHolder {


        @NonNull
        private final OnVideoClickListener listener;

        Data data;

         TextView title;
         ImageView ivlogo;



        public VideoViewHolder(@NonNull final View itemView, @NonNull final OnVideoClickListener listener) {
            super(itemView);
            this.listener = listener;

            title= (TextView) itemView.findViewById(R.id.Title);
            ivlogo= (ImageView) itemView.findViewById(R.id.ivLogo);

            ivlogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVideoClick(data);
                }
            });
        }



       public void bind(@NonNull Data item) {
            this.data = item;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Everywhere-TV");
        toolbar.setLogo(R.drawable.icon_low);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));




        new AsyncLogin().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }


    public class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                url = new URL("http://www.json-generator.com/api/json/get/bVxtdCfNiW?indent=2");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        public void onPostExecute(String result) {


            pdLoading.dismiss();
            final ArrayList<Data> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Data channelData = new Data();
                    channelData.logo = json_data.getString("logo");
                    channelData.title = json_data.getString("title");
                    channelData.programUrl = json_data.getString("url");

                    data.add(channelData);
                }
                // Setup and Handover data to recyclerview
                final RecyclerView mRVChannel = ButterKnife.findById(MainActivity.this, R.id.MainRecyclerView);
                ButterKnife.bind(MainActivity.this);
                final GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,
                        2);
                mRVChannel.setHasFixedSize(true);
               mRVChannel.setLayoutManager(layoutManager);


                mAdapter = new AdapterChannel(MainActivity.this,data ,new OnVideoClickListener(){

                    @Override
                    public void onVideoClick(@NonNull Data item) {
                        String htv_1="HTV 1";
                        String htv_2="HTV 2";
                        String htv_3="HTV 3";
                        String htv_4="HTV 4";
                        String rtl="RTL";
                        String nova_tv="NOVA TV";
                        String rtl_2="RTL 2";
                        String doma_tv="DOMA TV";

                        if(item.title.equals(htv_1)||item.title.equals(htv_2)||item.title.equals(htv_3)||item.title.equals(htv_4)||item.title.equals(rtl)
                               ||item.title.equals(nova_tv)||item.title.equals(rtl_2)||item.title.equals(doma_tv)){
                        startActivity(PlayerActivity.getVideoPlayerIntent(MainActivity.this, item.programUrl, item.title, 0));

                    }
                    else{ Toast.makeText(getBaseContext()," Please buy full version to watch all channels!",Toast.LENGTH_LONG).show();
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                   }
                    }
                });

                mRVChannel.setAdapter(mAdapter);


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}


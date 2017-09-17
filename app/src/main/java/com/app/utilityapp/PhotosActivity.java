package com.app.utilityapp;

import com.app.utilityapp.models.FlickrPhoto;
import com.app.utilityapp.models.FlickrPhoto_Table;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends Activity {

    com.codepath.apps.restclienttemplate.FlickrClient client;

    ArrayList<FlickrPhoto> photoItems;

    GridView gvPhotos;

    PhotoArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        client = FlickrClientApp.getRestClient();
        photoItems = new ArrayList<FlickrPhoto>();
        gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        adapter = new PhotoArrayAdapter(this, photoItems);
        gvPhotos.setAdapter(adapter);
        loadPhotos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    public void loadPhotos() {
        client.getInterestingnessList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                // Add new photos to SQLite
                String resp = isNetworkAvailable();
                if (resp.equalsIgnoreCase("true")) {
                    try {
                        JSONArray photos = json.getJSONObject("photos").getJSONArray("photo");
                        for (int x = 0; x < photos.length(); x++) {
                            String uid = photos.getJSONObject(x).getString("id");
                            FlickrPhoto p = FlickrPhoto.byPhotoUid(uid);
                            if (p == null) {
                                p = new FlickrPhoto(photos.getJSONObject(x));
                            }

                            p.save();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("debug", e.toString());
                    }

                    // Load into GridView from DB
                    for (FlickrPhoto p : FlickrPhoto.recentItems()) {
                        adapter.add(p);
                    }
                    Log.d("DEBUG", "Total: " + photoItems.size());
                } else {
                    warning();
                }
            }
        });
    }
    //Not internet warning
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void warning() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.warning);
        dialog.setCancelable(false);
        dialog.setTitle("WARNING!");
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text1);
        text.setTextColor(getResources().getColor(R.color.blue));
        text.setText("No Internet connectivity found!");
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setBackground(getResources().getDrawable(R.drawable.button_lay));
        dialogButton.setTextColor(getResources().getColor(R.color.white));
        dialogButton.setCursorVisible(true);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                    dialog.dismiss();
                } catch (Exception e) {
                    finish();
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }

    // INTERNET Connectivity check method
    public String isNetworkAvailable() {
        String avail;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            avail = "true";
        } else {
            avail = "false";
        }
        return avail;
    }

    @Override
    protected void onDestroy()
    {
        try {
            //stop service
            super.onDestroy(); //finally
        } catch (Exception e) {
            Log.w("Launcher->onDestroy", e.getMessage());
        }

    }
}

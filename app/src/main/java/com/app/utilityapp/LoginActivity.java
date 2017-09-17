package com.app.utilityapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActivity;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends OAuthLoginActivity<com.codepath.apps.restclienttemplate.FlickrClient> {

    String msg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.applogin);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        String resp = isNetworkAvailable();
        if (resp.equalsIgnoreCase("true")) {
            getMenuInflater().inflate(R.menu.login, menu);
            return true;
        } else {
            warning();
        }
        return false;
	}
	
    @Override
    public void onLoginSuccess() {
        String resp = isNetworkAvailable();
        if (resp.equalsIgnoreCase("true")) {
            Intent i = new Intent(this, PhotosActivity.class);
            startActivity(i);
        }else{
            warning();
        }
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    public void loginToRest(View view) {
        EditText loginid = (EditText) findViewById(R.id.LoginUserName);
        String name = loginid.getText().toString();

        if (name.isEmpty()){
            msg = "Your Flickr Email id";
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
        }else {
            msg = null;
        }

        if(!checkEmail(name)){
            msg = "Invalid Email Address";
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
        } else {
            msg = null;
        }

        if (msg == null) {
            String resp = isNetworkAvailable();
            if (resp.equalsIgnoreCase("true")) {
                getClient().connect();
            } else {
                warning();
            }
        }
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
						finish();
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

	public boolean checkEmail(String value1){
		String regex = "^(.+)@(.+)$";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(value1);

		return matcher.matches();
	}
}

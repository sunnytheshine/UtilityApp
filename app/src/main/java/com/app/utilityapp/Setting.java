package com.app.utilityapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This Activity class is to create list of install apps
 *
 * Created by sukhvindersingh on 2017-09-16.
 */

public class Setting extends Activity implements CompoundButton.OnCheckedChangeListener{
    ArrayList<String> selectedapp  = new ArrayList<String>();
    HashMap<String, String> hashdata = new HashMap<>();
    PackageManager packageManager;
    ListView apkList;
    Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        setContentView(R.layout.setting);

        btn = (Button) findViewById(R.id.findSelected);

        Toast.makeText(getApplicationContext(), "Select Any 6 Applications To Set On Circle.",Toast.LENGTH_LONG).show();
        packageManager = getPackageManager();
        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();

        packageList1.clear();
        selectedapp.clear();

        //To filter out System apps
        for (PackageInfo pi : packageList) {
            boolean b = isSystemPackage(pi);
            if (!b) {
                packageList1.add(pi);
            }
        }

        apkList = (ListView) findViewById(R.id.applist);
        apkList.setAdapter(new SeparatedListAdapter(this, packageList1, packageManager));

        btn = (Button) findViewById(R.id.findSelected);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(selectedapp.size() > 6) {
                    Toast.makeText(getApplicationContext(), "Kindly Select Only 6 Apps."+String.valueOf(selectedapp.size()),Toast.LENGTH_SHORT).show();
                    //selectedapp.clear();
                }else{
                    try {
                        DBModel controller = new DBModel(getApplicationContext());
                        for (int s=0; s < selectedapp.size(); s++) {
                            String app = "APP"+String.valueOf(s);
                            hashdata.put("name",String.valueOf(selectedapp.get(s)));
                            hashdata.put("type",app);
                            Log.d("DATA",String.valueOf(hashdata));
                            controller.update1(hashdata);
                        }
                    }catch (Exception e){
                        Log.d("SOME ERROR", String.valueOf(e));
                    }
                    Toast.makeText(getApplicationContext(), "Shortcut Created",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //int pos = apkList.getPositionForView(buttonView);
        String name = buttonView.getTag().toString();

        if(isChecked){
            //Log.d(buttonView.getTag().toString(),String.valueOf(pos));
            selectedapp.add(name);
            //Toast.makeText(getApplicationContext(), "Checked Changed", Toast.LENGTH_SHORT).show();
        }else{
            selectedapp.remove(name);
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(selectedapp);
        selectedapp.clear();
        selectedapp.addAll(hs);

    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
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
}

package com.app.utilityapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

public class Launcher extends Activity {

    public static int PERMISSION_REQUEST_CODE= 5469;
    public static int MY_REQUEST_CODE = 53232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.testPermission();

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
            }

        }catch (Exception e) {
            Log.d("Luncher->onCreate",e.getMessage());
        }

        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConfig.PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // You have permission
                Intent intent = new Intent(this, LauncherService.class);
                startService(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
               // Intent intent = new Intent(this, LauncherService.class);
                //startService(intent);
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
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

    /**
     * Method to check permission
     */
    public void testPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            Log.d("sunny","im asdadasdas");
        } else {
            Log.d("sunny","im here12dasda");
            Intent intent = new Intent(this, LauncherService.class);
            startService(intent);
        }
    }
}

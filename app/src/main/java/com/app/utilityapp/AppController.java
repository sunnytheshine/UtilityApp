package com.app.utilityapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  Main controller class of app
 *
 * Created by sukhvindersingh on 2017-09-16.
 */

public class AppController extends Application {
    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    ContentResolver cr = null;
    SharedPreferences.Editor editor;
    private Camera camera, mCamera;
    private Camera.Parameters parameters;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared Preferences reference
    SharedPreferences pref;

    // Sharedpref file name
    final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    final String KEY_RETAILERID = "name";
    final String KEY_LAST_LOGIN = "lastLogedIn";

    /**
     * Insert Method is used to insert log/data to file
     *
     * @param data      The data to insert
     * @param filename  The file name
     */
    public void insertdata(String data, String filename){
        Writer writer;
        File root = android.os.Environment.getExternalStorageDirectory();
        File outDir = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH);

        if (!outDir.isDirectory()) {
            outDir.mkdir();
        }

        try {

            if (!outDir.isDirectory()) {
                 throw new IOException(
                       "Unable to create directory " +AppConfig.GLOBAL_PATH+ ". Maybe the SD card is unmounted?");
            }
            File outputFile = new File(outDir, filename);
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(data);
            writer.close();

        } catch (Exception e) {
            Log.w("Controller->insertlog", e.getMessage(), e);
        }
    }

    /**
     * Method to read config file
     *
     * @param filename The file name
     *
     * @return data in string formate
     */
    public String readfile(String filename){
        String contents="";
        try {
            File root = Environment.getExternalStorageDirectory();
            File outDir = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH);
            File outputFile = new File(outDir, filename);
            if (outputFile.exists()) {
                int length = (int) outputFile.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(outputFile);
                try {
                    in.read(bytes);
                } finally {
                    in.close();
                }
                contents = new String(bytes);
            } else {
                Log.d("NO MODE", "FILE NOT FOUND");
            }
        }catch(Exception e){
            Log.d("NO MODE", String.valueOf(e));
        }
        return contents;
    }

    /**
     * create user login session
     *
     * @param name
     * @param lastLogedIn
     * @param context
     */
    public void createUserLoginSession(String name, String lastLogedIn, Context context){
        Log.d("name",name);
        Log.d("time",lastLogedIn);
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        try {
            editor.putBoolean(IS_USER_LOGIN, true); //LOGIN STATUS
            editor.putString(KEY_RETAILERID, name); //RETAILER ID
            editor.putString(KEY_LAST_LOGIN, lastLogedIn); // LAST LOGIN DATETIME
            // commit changes
            editor.commit();
        }
        catch(Exception e)
        {
            Log.e("CREATEUSERLOGINSESSION", e.getMessage());
        }
    }

    /**
     * Check User Login
     *
     * @param context context
     *
     * @return boolean if login or not
     */
    public boolean isUserLoggedIn(Context context){

        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    // Get Username
    public String getUserName(Context context){

        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        return pref.getString(KEY_RETAILERID, null);
    }

    /**
     * Open apps method
     *
     * @param appName App name to open
     * @param app Context of app
     */
    public void openAppManually(String appName, Context app){
        try {
            Intent LaunchIntent = app.getPackageManager().getLaunchIntentForPackage(appName);
            app.startActivity(LaunchIntent);
        }
        catch (Exception e) {
            Log.e("OPENAPPMANUALLY", e.toString());
        }
    }

    /**
     * Get icon position
     *
     * @param icon iconname
     * @param file filename
     *
     * @return return cordinates
     */
    public String[] iconposition(String icon,String file) {
        String contents = readfile(file);
        String [] resp = new String[4];
        try {
            JSONObject obj_data = new JSONObject(contents);
            JSONObject arry_data = obj_data.getJSONObject(icon);
            resp[0] = arry_data.getString("W");
            resp[1] = arry_data.getString("H");
            resp[2] = arry_data.getString("X");
            resp[3] = arry_data.getString("Y");

        } catch (Exception e) {
            Log.d("ICONPOSITION",String.valueOf(e));
        }
        return resp;
    }

    /**
     *  Clean
     *
     * @param context
     */
    public void clean(Context context){
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     *  Delete directory
     *
     * @param dir Directory name
     *
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Check if phone has flash / LED light
     *
     * @return return true if yes
     */
    public boolean hasFlash() {
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            if(parameters.getSupportedFlashModes() == null) {
                camera.release();
                camera = null;

                return false;
            }
            if (parameters.getFlashMode() == null){
                camera.release();
                camera = null;

                return false;
            }
        } catch (Exception e) {
            Log.e("ERROR", "CameraInstance", e);
            camera.release();
            camera = null;
        }
        camera.release();
        camera = null;
        return true;
    }

    /**
     *  check flashlight status
     *
     */
    public void flashlight(){

        File root = Environment.getExternalStorageDirectory();
        File outputFile = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
        if (outputFile.exists()) {
            File filedelete = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
            filedelete.delete();

            torch(0);
        }else {
            try {
                File file = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
                file.createNewFile();
            }catch(Exception e){
                Log.e("FLASHLIGHT",e.getMessage());
            }
            torch(1);
        }
    }

    /**
     * Open camera
     *
     * @param app
     */
    public void camera(Application app){
        Intent camera_action = new Intent("android.media.action.IMAGE_CAPTURE");
        camera_action.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        camera_action.putExtra("android.intent.extras.CAMERA_FACING", 1);
        camera_action.putExtra("android.intent.extras.FLASH_MODE_ON", 1);
        camera_action.putExtra("android.intent.extras.QUALITY_HIGH", 1);
        app.startActivity(camera_action);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void torch(int val){
        if(val == 1) {
            try {
                camera = Camera.open();
                camera.setPreviewTexture(new SurfaceTexture(0));
            } catch (Exception e) {
                //Log.e("ERROR", "CameraInstance", e);
                camera.stopPreview();
                camera.release();
                camera = null;

                File root = Environment.getExternalStorageDirectory();
                File filedelete = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
                filedelete.delete();
            }
        }
        if(val == 1) {
            if (camera != null) {
                try {
                    parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                }catch (Exception e){

                }
            }
        }else{
            //camera.stopPreview();
            try {
                camera.release();
                camera = null;
            }catch(Exception e){

                File root = Environment.getExternalStorageDirectory();
                File filedelete = new File(root.getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
                filedelete.delete();
            }
        }
    }

    /**
     * Open calculator
     *
     * @param app
     */
    public void calculator(Application app){
        ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();
        final PackageManager pm =  app.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if( pi.packageName.toString().toLowerCase().contains("calcul")){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
        }
        if(items.size()>=1){
            String packageName = (String) items.get(0).get("packageName");
            Log.d("PPP",packageName);
            Intent i = pm.getLaunchIntentForPackage(packageName);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (i != null)
                app.startActivity(i);
        }
        else{
            // Application not found
        }
    }

    /**
     * Open Maps application
     *
     * @param app
     */
    public void maps(Application app){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:21.0000,78.0000"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        app.startActivity(i);
    }
}

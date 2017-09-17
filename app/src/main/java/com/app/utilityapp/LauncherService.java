package com.app.utilityapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by sukhvindersingh on 2017-09-16.
 */

public class LauncherService extends Service {
    private WindowManager windowManager;

    HashMap<String, String> smalldata = new HashMap<>();

    private ImageView floatingFaceBubble;

    private boolean hud=true;

    AppController controller = new AppController();

    private WindowManager.LayoutParams Params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT);

    private WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);



    private View vew;
    public void onCreate() {
        super.onCreate();
        floatingFaceBubble = new ImageView(this);

        //create view for controller
        final CircleView control_cirlce = new CircleView(this);

        //small circle
        final SmallCircleView small_circle = new SmallCircleView(this);

        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.mipmap.floating_bubble);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //here is all the science of params
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 100;

        // add a floatingbubble icon in window
        windowManager.addView(control_cirlce, Params);
        try {
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            if(Math.abs(event.getRawX()-initialTouchX )<5){
                                //remove floating bubble
                                windowManager.removeView(floatingFaceBubble);

                                Params.gravity = Gravity.TOP | Gravity.CENTER;
                                //add controller on window
                                windowManager.addView(control_cirlce, Params);
                                return false;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX()-initialTouchX );
                            myParams.y = initialY + (int) (event.getRawY()-initialTouchY );
                            if(hud) {
                                windowManager.updateViewLayout(v, myParams);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                    return false;
                }
            });
            //onlong click floating bubble
            floatingFaceBubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View arg0) {
                    Log.e("event", "sunny");
                    hud = false;
                    hidebubble("1");
                    return true;
                }
            });

            //small circle
            small_circle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent eve) {
                    WindowManager.LayoutParams paramsT = Params;
                    final int action = eve.getAction();
                    int index = eve.getActionIndex();
                    int pointerId = eve.getPointerId(index);
                    DBModel cont = new DBModel(getApplicationContext());
                    switch(eve.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            Log.d("Controlls", "Coordinates "+ eve.getX() + " "+ eve.getY());
                            final int evtouchpossitiondownX = (int) eve.getX();
                            final int evtouchpossitiondownY = (int) eve.getY();
                            //check if Touched in circle
                            if(smallcirclegetcord(evtouchpossitiondownX, evtouchpossitiondownY, "OUTERCIRCLE")){
                                Log.d("OUT CIRCLE", "REMOVE CONTROL CIRCLE AND PUT FLOATING BUBBLE");
                                windowManager.removeView(small_circle);
                                windowManager.addView(floatingFaceBubble, myParams);
                            }else {
                                //check if Touched in small circle
                                if (smallcirclegetcord(evtouchpossitiondownX, evtouchpossitiondownY, "INNERCIRCLE")) {
                                    Log.d("IN SMALL CIRCLE", "REMOVE CONTROL CIRCLE AND PUT FLOATING BUBBLE AND OPEN SETTING");
                                    windowManager.removeView(small_circle);
                                    windowManager.addView(floatingFaceBubble, myParams);
                                    //setting
                                    Intent intn = new Intent(getApplication(), Setting.class);
                                    intn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intn);
                                }else {
                                    try {
                                        Log.d("", "OTHER PART OF SMALL CIRCLE");
                                        if (cont.select("APP0")) {
                                            //app0
                                            smalldata = cont.getdata("APP0");
                                            Log.d("", String.valueOf(smalldata));
                                            int W0 = Integer.valueOf(smalldata.get("w"));
                                            int H0 = Integer.valueOf(smalldata.get("h"));
                                            int X0 = Integer.valueOf(smalldata.get("x"));
                                            int Y0 = Integer.valueOf(smalldata.get("y"));
                                            String app0 = smalldata.get("name");
                                            //app1
                                            smalldata = cont.getdata("APP1");
                                            int W1 = Integer.valueOf(smalldata.get("w"));
                                            int H1 = Integer.valueOf(smalldata.get("h"));
                                            int X1 = Integer.valueOf(smalldata.get("x"));
                                            int Y1 = Integer.valueOf(smalldata.get("y"));
                                            String app1 = smalldata.get("name");
                                            //app2
                                            smalldata = cont.getdata("APP2");
                                            int W2 = Integer.valueOf(smalldata.get("w"));
                                            int H2 = Integer.valueOf(smalldata.get("h"));
                                            int X2 = Integer.valueOf(smalldata.get("x"));
                                            int Y2 = Integer.valueOf(smalldata.get("y"));
                                            String app2 = smalldata.get("name");

                                            //app3
                                            smalldata = cont.getdata("APP3");
                                            int W3 = Integer.valueOf(smalldata.get("w"));
                                            int H3 = Integer.valueOf(smalldata.get("h"));
                                            int X3 = Integer.valueOf(smalldata.get("x"));
                                            int Y3 = Integer.valueOf(smalldata.get("y"));
                                            String app3 = smalldata.get("name");

                                            //app4
                                            smalldata = cont.getdata("APP4");
                                            int W4 = Integer.valueOf(smalldata.get("w"));
                                            int H4 = Integer.valueOf(smalldata.get("h"));
                                            int X4 = Integer.valueOf(smalldata.get("x"));
                                            int Y4 = Integer.valueOf(smalldata.get("y"));
                                            String app4 = smalldata.get("name");

                                            //app5
                                            smalldata = cont.getdata("APP5");
                                            int W5 = Integer.valueOf(smalldata.get("w"));
                                            int H5 = Integer.valueOf(smalldata.get("h"));
                                            int X5 = Integer.valueOf(smalldata.get("x"));
                                            int Y5 = Integer.valueOf(smalldata.get("y"));
                                            String app5 = smalldata.get("name");


                                            if (evtouchpossitiondownX >= X0 && evtouchpossitiondownX < (X0 + W0)
                                                    && evtouchpossitiondownY >= Y0 && evtouchpossitiondownY < (Y0 + H0)) {
                                                //app0
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app0);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);

                                            } else if (evtouchpossitiondownX >= X1 && evtouchpossitiondownX < (X1 + W1)
                                                    && evtouchpossitiondownY >= Y1 && evtouchpossitiondownY < (Y1 + H1)) {
                                                //app1
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app1);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);
                                            } else if (evtouchpossitiondownX >= X2 && evtouchpossitiondownX < (X2 + W2)
                                                    && evtouchpossitiondownY >= Y2 && evtouchpossitiondownY < (Y2 + H2)) {
                                                //app2
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app2);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);

                                            } else if (evtouchpossitiondownX >= X3 && evtouchpossitiondownX < (X3 + W3)
                                                    && evtouchpossitiondownY >= Y3 && evtouchpossitiondownY < (Y3 + H3)) {
                                                //app3
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app3);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);
                                            } else if (evtouchpossitiondownX >= X4 && evtouchpossitiondownX < (X4 + W4)
                                                    && evtouchpossitiondownY >= Y4 && evtouchpossitiondownY < (Y4 + H4)) {
                                                //app4
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app4);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);
                                            } else if (evtouchpossitiondownX >= X5 && evtouchpossitiondownX < (X5 + W5)
                                                    && evtouchpossitiondownY >= Y5 && evtouchpossitiondownY < (Y5 + H5)) {
                                                //app5
                                                PackageManager pm = getPackageManager();
                                                Intent intent = pm.getLaunchIntentForPackage(app5);
                                                startActivity(intent);

                                                windowManager.removeView(small_circle);
                                                windowManager.addView(floatingFaceBubble, myParams);
                                            } else {
                                                Log.d("NOPE", "NOTHING TO DO");
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e( "APP-Launch",  e.getMessage());
                                    }
                                }
                            }
                            break;
                    }
                    return false;
                }
            });

            //on control_cirlce click
            control_cirlce.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    WindowManager.LayoutParams paramsV = Params;
                    final int action = ev.getAction();
                    int index = ev.getActionIndex();
                    int pointerId = ev.getPointerId(index);

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d("Controlls", "Coordinates "+ ev.getX() + " "+ ev.getY());
                            final int evtouchpossitiondownX = (int) ev.getX();
                            final int evtouchpossitiondownY = (int) ev.getY();
                            //check if Touched in circle
                            if(circlegetcord(evtouchpossitiondownX, evtouchpossitiondownY, AppConfig.CONFIG_FILE, "OUTERCIRCLE")){
                                Log.d("OUT CIRCLE", "REMOVE CONTROL CIRCLE AND PUT FLOATING BUBBLE");
                                windowManager.removeView(control_cirlce);
                                windowManager.addView(floatingFaceBubble, myParams);
                            }else{
                                //check if Touched in small circle
                                if(circlegetcord(evtouchpossitiondownX, evtouchpossitiondownY, AppConfig.CONFIG_FILE, "INNERCIRCLE")){
                                    Log.d("IN SMALL CIRCLE", "REMOVE CONTROL CIRCLE AND PUT FLOATING BUBBLE");
                                    windowManager.removeView(control_cirlce);
                                    windowManager.addView(floatingFaceBubble, myParams);
                                    //check login

                                    if(controller.isUserLoggedIn(getApplicationContext())){
                                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH, AppConfig.GLOBAL_FILE_PLAYSTORE).delete();
                                        Intent intn = new Intent(getApplication(), LoginActivity.class);
                                        intn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intn);
                                    }else {
                                        Intent intn = new Intent(getApplication(), LoginActivity.class);
                                        intn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intn);
                                    }
                                }else {
                                    Log.d("IN MAIN CIRCLE", "");
                                    //mirroricon
                                    String[] mresult =  controller.iconposition("mirroricon",AppConfig.CONFIG_FILE);
                                    int WM = Integer.valueOf(mresult[0]);
                                    int HM = Integer.valueOf(mresult[1]);
                                    int XM = Integer.valueOf(mresult[2]);
                                    int YM = Integer.valueOf(mresult[3]);
                                    //cleanericon
                                    String[] cresult =  controller.iconposition("cleanericon",AppConfig.CONFIG_FILE);
                                    int WC = Integer.valueOf(cresult[0]);
                                    int HC = Integer.valueOf(cresult[1]);
                                    int XC = Integer.valueOf(cresult[2]);
                                    int YC = Integer.valueOf(cresult[3]);
                                    //calculatoricon
                                    String[] lcresult =  controller.iconposition("calculatoricon",AppConfig.CONFIG_FILE);
                                    int WCL = Integer.valueOf(lcresult[0]);
                                    int HCL = Integer.valueOf(lcresult[1]);
                                    int XCL = Integer.valueOf(lcresult[2]);
                                    int YCL = Integer.valueOf(lcresult[3]);
                                    //moreicon
                                    String[] hresult =  controller.iconposition("moreicon",AppConfig.CONFIG_FILE);
                                    int WH = Integer.valueOf(hresult[0]);
                                    int HH = Integer.valueOf(hresult[1]);
                                    int XH = Integer.valueOf(hresult[2]);
                                    int YH = Integer.valueOf(hresult[3]);
                                    //torchicon
                                    String[] tresult =  controller.iconposition("torchicon",AppConfig.CONFIG_FILE);
                                    int WT = Integer.valueOf(tresult[0]);
                                    int HT = Integer.valueOf(tresult[1]);
                                    int XT = Integer.valueOf(tresult[2]);
                                    int YT = Integer.valueOf(tresult[3]);
                                    //mapicon
                                    String[] mapresult =  controller.iconposition("mapicon",AppConfig.CONFIG_FILE);
                                    int WMAP = Integer.valueOf(mapresult[0]);
                                    int HMAP = Integer.valueOf(mapresult[1]);
                                    int XMAP = Integer.valueOf(mapresult[2]);
                                    int YMAP = Integer.valueOf(mapresult[3]);
                                    //hommyicon
                                    String[] hommyresult =  controller.iconposition("hommyicon",AppConfig.CONFIG_FILE);
                                    int WHOM = Integer.valueOf(hommyresult[0]);
                                    int HHOM = Integer.valueOf(hommyresult[1]);
                                    int XHOM = Integer.valueOf(hommyresult[2]);
                                    int YHOM = Integer.valueOf(hommyresult[3]);
                                    //browsericon
                                    String[] browserresult =  controller.iconposition("browsericon",AppConfig.CONFIG_FILE);
                                    int WBRO = Integer.valueOf(browserresult[0]);
                                    int HBRO = Integer.valueOf(browserresult[1]);
                                    int XBRO = Integer.valueOf(browserresult[2]);
                                    int YBRO = Integer.valueOf(browserresult[3]);

                                    if (evtouchpossitiondownX >= XM && evtouchpossitiondownX < (XM + WM)
                                            && evtouchpossitiondownY >= YM && evtouchpossitiondownY < (YM + HM)) {
                                        //camera
                                        controller.camera(getApplication());
                                        windowManager.removeView(control_cirlce);
                                        hidebubble("0");
                                    }else if (evtouchpossitiondownX >= XC && evtouchpossitiondownX < (XC + WC)
                                            && evtouchpossitiondownY >= YC && evtouchpossitiondownY < (YC + HC)){
                                        //cleaner
                                        Random rand = new Random();
                                        int randomNum = rand.nextInt((90 - 10) + 1) + 10;
                                        String msg = String.valueOf(randomNum)+" MB Cleaned!";
                                        Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
                                        controller.clean(getApplicationContext());

                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);
                                    }else if (evtouchpossitiondownX >= XCL && evtouchpossitiondownX < (XCL + WCL)
                                            && evtouchpossitiondownY >= YCL && evtouchpossitiondownY < (YCL + HCL)) {
                                        //calculator
                                        controller.calculator(getApplication());

                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);
                                    }else if(evtouchpossitiondownX >= XH && evtouchpossitiondownX < (XH + WH)
                                            && evtouchpossitiondownY >= YH && evtouchpossitiondownY < (YH + HH)) {
                                        //more
                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(small_circle,Params);

                                    }else if(evtouchpossitiondownX >= XT && evtouchpossitiondownX < (XT + WT)
                                            && evtouchpossitiondownY >= YT && evtouchpossitiondownY < (YT + HT)){
                                        //Torch
                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);
                                        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                                            controller.flashlight();
                                        }else{
                                            File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AppConfig.GLOBAL_PATH + File.separator + AppConfig.TORCH_STATUS);
                                            if (outputFile.exists()) {
                                                controller.flashlight();
                                            }else if(controller.hasFlash()){
                                                controller.flashlight();
                                            }else {
                                                Intent intn = new Intent(getApplication(), Torch.class);
                                                intn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intn);
                                                Toast.makeText(getApplication(), "Flashlight not available", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }else if(evtouchpossitiondownX >= XMAP && evtouchpossitiondownX < (XMAP + WMAP)
                                            && evtouchpossitiondownY >= YMAP && evtouchpossitiondownY < (YMAP + HMAP)){
                                        //Maps
                                        controller.maps(getApplication());

                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);

                                    }else if(evtouchpossitiondownX >= XHOM && evtouchpossitiondownX < (XHOM + WHOM)
                                            && evtouchpossitiondownY >= YHOM && evtouchpossitiondownY < (YHOM + HHOM)){
                                        //Homme button
                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startMain);

                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);
                                    }else if(evtouchpossitiondownX >= XBRO && evtouchpossitiondownX < (XBRO + WBRO)
                                            && evtouchpossitiondownY >= YBRO && evtouchpossitiondownY < (YBRO + HBRO)){
                                        //BROWSER
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        windowManager.removeView(control_cirlce);
                                        windowManager.addView(floatingFaceBubble, myParams);
                                    }else{
                                        Log.d("NOPE", "");
                                    }
                                }
                            }
                            break;
                    }
                    return false;

                }
            });
        } catch (Exception e) {
            Log.e("ERROR",String.valueOf(e));
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    private boolean circlegetcord(float x, float y, String file,String type){
        boolean result = false;
        String contents = controller.readfile(file);
        try {
            JSONObject obj_data = new JSONObject(contents);
            JSONObject arry_data = obj_data.getJSONObject(type);
            String radius = arry_data.getString("RADIUS");
            String xx = arry_data.getString("CENTERX");
            String yy = arry_data.getString("CENTERY");
            int outradius = Integer.valueOf(radius);
            int centerx = Integer.valueOf(xx);
            int centery = Integer.valueOf(yy);
            double val= Math.sqrt((x - centerx)*(x - centerx) + (y - centery)*(y - centery));
            if(val >= outradius && type.equalsIgnoreCase("OUTERCIRCLE")){
                result=true;
            } else if(val <= outradius && type.equalsIgnoreCase("INNERCIRCLE")){
                result=true;
            }
        } catch (Exception e){
            Log.e("circlegetcord", e.getMessage());
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void hidebubble(String state){

        if(Integer.valueOf(android.os.Build.VERSION.SDK) < 16){
            if (state.equalsIgnoreCase("1")) {
                windowManager.removeView(floatingFaceBubble);
                Toast.makeText(getApplication(), "Bubble Hiding feature not available for this model", Toast.LENGTH_LONG).show();
            }
            stopSelf();
        }else {
            Intent intents = new Intent(getApplication(), Launcher.class);

            PendingIntent pIntent = PendingIntent.getActivity(getApplication(), 0, intents, 0);
            Notification noti = new Notification.Builder(getApplication())
                    .setContentTitle("Bubdub is here")
                    .setContentText("Tap here to wake up Bubdub.").setSmallIcon(R.mipmap.notify)
                    .setContentIntent(pIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, noti);

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(30);
            if (state.equalsIgnoreCase("1")) {
                windowManager.removeView(floatingFaceBubble);
            }
            stopSelf();
        }
    }
    private boolean smallcirclegetcord(float x, float y,String type){
        DBModel dbmodel = new DBModel(getApplicationContext());
        boolean result = false;
        HashMap<String, String> hashget;
        try{
            hashget=dbmodel.getdata(type);
            int outradius = Integer.valueOf(hashget.get("radius"));
            int centerx = Integer.valueOf(hashget.get("x"));
            int centery = Integer.valueOf(hashget.get("y"));
            double val= Math.sqrt((x - centerx)*(x - centerx) + (y - centery)*(y - centery));
            Log.d("Circle value ", String.valueOf(val));
            if(val >= outradius && type.equalsIgnoreCase("OUTERCIRCLE")){
                result=true;
            } else if(val <= outradius && type.equalsIgnoreCase("INNERCIRCLE")){
                result=true;
            }
        } catch (Exception e){
            Log.e("smallgetcord", e.getMessage() );
        }
        return result;
    }
}
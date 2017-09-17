package com.app.utilityapp;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * SmallCirleView class is to create small circle for custom app shortcuts
 *
 * Created by sukhvindersingh on 2017-09-16.
 */

public class SmallCircleView extends View {
    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = 60;
    HashMap<String, String> hashdata = new HashMap<>();
    PackageManager packageManager;
    Drawable appIcon0, appIcon1, appIcon2, appIcon3, appIcon4, appIcon5;
    private Paint mPaint;
    private RectF mOvalRect = null;
    private RectF minner = null;
    private int mItemCount = 6;
    private int mSweepAngle;
    private int mInnerRadius;
    private int mOuterRadius;
    private Bitmap app0,app1,app2,app3,app4,app5,setting;

    DBModel dbcontroller = new DBModel(getContext());
    Service host = (Service) getContext();

    public SmallCircleView(Context context) {
        this(context, null);
    }

    public SmallCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);

        mSweepAngle = TOTAL_DEGREE / mItemCount;

        int[] rado = circlesize(host);

        mInnerRadius = rado[1];
        mOuterRadius = rado[0];

        setting = BitmapFactory.decodeResource(getResources(), R.mipmap.setting);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        appIcon0=appimg(getContext(),"APP0");
        appIcon1=appimg(getContext(),"APP1");
        appIcon2=appimg(getContext(),"APP2");
        appIcon3=appimg(getContext(),"APP3");
        appIcon4=appimg(getContext(),"APP4");
        appIcon5=appimg(getContext(),"APP5");

        try {
            if(appIcon0 != null) {
                Bitmap appbit = ((BitmapDrawable) appIcon0).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app0=getResizedBitmap(newappbit,60,60);
            }
            if(appIcon1 != null) {
                Bitmap  appbit = ((BitmapDrawable) appIcon1).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app1=getResizedBitmap(newappbit,60,60);
            }
            if(appIcon2 != null) {
                Bitmap appbit = ((BitmapDrawable) appIcon2).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app2=getResizedBitmap(newappbit,60,60);
            }
            if(appIcon3 != null) {
                Bitmap appbit = ((BitmapDrawable) appIcon3).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app3=getResizedBitmap(newappbit,60,60);
            }
            if(appIcon4 != null) {
                Bitmap appbit = ((BitmapDrawable) appIcon4).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app4=getResizedBitmap(newappbit,60,60);
            }
            if(appIcon5 != null) {
                Bitmap appbit = ((BitmapDrawable) appIcon5).getBitmap();
                Bitmap newappbit = appbit.copy(Bitmap.Config.ARGB_8888, true);
                app5=getResizedBitmap(newappbit,60,60);
            }
        }catch (Exception e){
            Log.d("SOME ERROR IN IMG", String.valueOf(e));
        }

        int width = getWidth();
        int height = getHeight();

        if (mOvalRect == null) {
            mOvalRect = new RectF(width / 2 - mOuterRadius, height / 2 - mOuterRadius, width / 2 + mOuterRadius, height / 2 + mOuterRadius);
            minner = new RectF(width / 2 - mInnerRadius, height / 2 - mInnerRadius, width / 2 + mInnerRadius, height / 2 + mInnerRadius);

            int xCenterm = (int) minner.centerX();
            int yCenterm = (int) minner.centerY();

            int xCentero = (int) mOvalRect.centerX();
            int yCentero = (int) mOvalRect.centerY();
            try {
                if(!dbcontroller.select("OUTERCIRCLE")){
                    hashdata.put("type","OUTERCIRCLE");
                    hashdata.put("x",String.valueOf(xCentero));
                    hashdata.put("y",String.valueOf(yCentero));
                    hashdata.put("radius",String.valueOf(mOuterRadius));
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);
                    hashdata.clear();
                    hashdata.put("type","INNERCIRCLE");
                    hashdata.put("x",String.valueOf(xCenterm));
                    hashdata.put("y",String.valueOf(yCenterm));
                    hashdata.put("radius",String.valueOf(mInnerRadius));
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP0");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP1");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP2");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP3");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP4");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);

                    hashdata.clear();
                    hashdata.put("type","APP5");
                    hashdata.put("x","0");
                    hashdata.put("y","0");
                    hashdata.put("radius","0");
                    hashdata.put("w","0");
                    hashdata.put("h","0");
                    hashdata.put("name","0");
                    dbcontroller.insert(hashdata);
                }
            }catch (Exception e){
                Log.d("SOME ERROR", String.valueOf(e));
            }

        }
        for (int i = 0; i < mItemCount; i++) {
            int startAngle = START_DEGREE + i * mSweepAngle;
            mPaint.setColor(Color.DKGRAY);
            mPaint.setAlpha(125);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(mOvalRect, startAngle, mSweepAngle, true, mPaint);

            mPaint.setColor(Color.DKGRAY);
            mPaint.setAlpha(150);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mOvalRect, startAngle, mSweepAngle, true, mPaint);

        }
        if(app0 != null) {
            int center0X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 0 * mSweepAngle + mSweepAngle / 2)));
            int center0Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 0 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app0, width / 2 + center0X - app0.getWidth() / 2, height / 2 + center0Y - app0.getHeight() / 2, null);

            if(dbcontroller.select("APP0")) {
                hashdata.clear();
                hashdata.put("type","APP0");
                hashdata.put("x",String.valueOf(width / 2 + center0X - app0.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center0Y - app0.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app0.getWidth()));
                hashdata.put("h",String.valueOf(app0.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type", "APP0");
                hashdata.put("x",String.valueOf(width / 2 + center0X - app0.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center0Y - app0.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app0.getWidth()));
                hashdata.put("h",String.valueOf(app0.getHeight()));
                dbcontroller.insert(hashdata);
            }
        }
        if(app1 != null) {
            int center1X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 1 * mSweepAngle + mSweepAngle / 2)));
            int center1Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 1 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app1, width / 2 + center1X - app1.getWidth() / 2, height / 2 + center1Y - app1.getHeight() / 2, null);

            if(dbcontroller.select("APP1")) {
                hashdata.clear();
                hashdata.put("type","APP1");
                hashdata.put("x",String.valueOf(width / 2 + center1X - app1.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center1Y - app1.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app1.getWidth()));
                hashdata.put("h",String.valueOf(app1.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type","APP1");
                hashdata.put("x",String.valueOf(width / 2 + center1X - app1.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center1Y - app1.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app1.getWidth()));
                hashdata.put("h",String.valueOf(app1.getHeight()));
                dbcontroller.insert(hashdata);
            }

        }
        if(app2 != null) {
            int center2X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 2 * mSweepAngle + mSweepAngle / 2)));
            int center2Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 2 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app2, width / 2 + center2X - app2.getWidth() / 2, height / 2 + center2Y - app2.getHeight() / 2, null);

            if(dbcontroller.select("APP2")) {
                hashdata.clear();
                hashdata.put("type","APP2");
                hashdata.put("x",String.valueOf(width / 2 + center2X - app2.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center2Y - app2.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app2.getWidth()));
                hashdata.put("h",String.valueOf(app2.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type","APP1");
                hashdata.put("x",String.valueOf(width / 2 + center2X - app2.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center2Y - app2.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app2.getWidth()));
                hashdata.put("h",String.valueOf(app2.getHeight()));
                dbcontroller.insert(hashdata);
            }
        }
        if(app3 != null) {
            int center3X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 3 * mSweepAngle + mSweepAngle / 2)));
            int center3Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 3 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app3, width / 2 + center3X - app3.getWidth() / 2, height / 2 + center3Y - app3.getHeight() / 2, null);

            if(dbcontroller.select("APP3")) {
                hashdata.clear();
                hashdata.put("type","APP3");
                hashdata.put("x",String.valueOf(width / 2 + center3X - app3.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center3Y - app3.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app3.getWidth()));
                hashdata.put("h",String.valueOf(app3.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type","APP3");
                hashdata.put("x",String.valueOf(width / 2 + center3X - app3.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center3Y - app3.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app3.getWidth()));
                hashdata.put("h",String.valueOf(app3.getHeight()));
                dbcontroller.insert(hashdata);
            }
        }
        if(app4 != null) {
            int center4X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 4 * mSweepAngle + mSweepAngle / 2)));
            int center4Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 4 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app4, width / 2 + center4X - app4.getWidth() / 2, height / 2 + center4Y - app4.getHeight() / 2, null);

            if(dbcontroller.select("APP4")) {
                hashdata.clear();
                hashdata.put("type","APP4");
                hashdata.put("x",String.valueOf(width / 2 + center4X - app4.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center4Y - app4.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app4.getWidth()));
                hashdata.put("h",String.valueOf(app4.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type","APP4");
                hashdata.put("x",String.valueOf(width / 2 + center4X - app4.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center4Y - app4.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app4.getWidth()));
                hashdata.put("h",String.valueOf(app4.getHeight()));
                dbcontroller.insert(hashdata);
            }
        }
        if(app5 != null) {
            int center5X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 5 * mSweepAngle + mSweepAngle / 2)));
            int center5Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 5 * mSweepAngle + mSweepAngle / 2)));
            canvas.drawBitmap(app5, width / 2 + center5X - app5.getWidth() / 2, height / 2 + center5Y - app5.getHeight() / 2, null);

            if(dbcontroller.select("APP5")) {
                hashdata.clear();
                hashdata.put("type","APP5");
                hashdata.put("x",String.valueOf(width / 2 + center5X - app5.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center5Y - app5.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app5.getWidth()));
                hashdata.put("h",String.valueOf(app5.getHeight()));
                dbcontroller.update(hashdata);
            }else{
                hashdata.clear();
                hashdata.put("type","APP5");
                hashdata.put("x",String.valueOf(width / 2 + center5X - app5.getWidth() / 2));
                hashdata.put("y",String.valueOf(height / 2 + center5Y - app5.getHeight() / 2));
                hashdata.put("radius","0");
                hashdata.put("w",String.valueOf(app5.getWidth()));
                hashdata.put("h",String.valueOf(app5.getHeight()));
                dbcontroller.insert(hashdata);
            }
        }
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius + 3, mPaint);
        super.onDraw(canvas);

        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);
        canvas.drawBitmap(setting, width / 2 - setting.getWidth() / 2, height / 2 - setting.getHeight() / 2, null);
        super.onDraw(canvas);
    }
    private int[] circlesize(Service act){
        int[] result = new int[2];
        WindowManager wm = (WindowManager) act.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        try {
            display.getSize(size);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        int width = size.x;
        double oradius = 0.30*width;
        double inradius = 0.11*width;
        result[0] = (int) oradius;
        result[1] = (int) inradius;
        return result;
    }
    private Drawable appimg(Context context, String app){
        HashMap<String , String> hashget;
        Drawable appIcon=null;
        if(dbcontroller.select(app)) {
            try {
                hashget = dbcontroller.getdata(app);
                packageManager = context.getPackageManager();
                appIcon = packageManager.getApplicationIcon(hashget.get("name"));
                return appIcon;
            }catch (Exception e){
                Log.d("sukhvinder@amlpl.com","catch");
                hashdata.clear();
                hashdata.put("type",app);
                hashdata.put("x","0");
                hashdata.put("y","0");
                hashdata.put("radius","0");
                hashdata.put("w","0");
                hashdata.put("h","0");
                hashdata.put("name","0");
                dbcontroller.insert(hashdata);
                dbcontroller.update1(hashdata);
            }
        }
        return appIcon;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
}

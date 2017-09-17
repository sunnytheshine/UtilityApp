package com.app.utilityapp;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONObject;

import java.io.File;

/**
 * This class is to create the main circle view for shortcuts
 *
 * Created by sukhvindersingh on 2017-09-16.
 */

public class CircleView extends View {
    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = 90;
    private final int mItemCount = 8;
    private Paint mPaint;
    private RectF mOvalRect = null;
    private RectF minner = null;
    private int mSweepAngle;
    private int mInnerRadius;
    private int mOuterRadius;
    private int height;
    private Bitmap mapicon, cleanericon, torchicon, prizeicon, mirroricon, moreicon, calculatoricon, hommyicon, browsericon;
    AppController controller = new AppController();
    Service host = (Service) getContext();
    File file;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mSweepAngle = TOTAL_DEGREE / mItemCount;

        int[] rado = circlesize(host);

        mInnerRadius = rado[1];
        mOuterRadius = rado[0];
        height = rado[2];

        mapicon = BitmapFactory.decodeResource(getResources(), R.mipmap.map);
        cleanericon = BitmapFactory.decodeResource(getResources(), R.mipmap.cleaner);
        moreicon = BitmapFactory.decodeResource(getResources(), R.mipmap.more);
        hommyicon = BitmapFactory.decodeResource(getResources(), R.mipmap.home);
        mirroricon = BitmapFactory.decodeResource(getResources(), R.mipmap.mirror);
        calculatoricon = BitmapFactory.decodeResource(getResources(), R.mipmap.calculator);
        torchicon = BitmapFactory.decodeResource(getResources(), R.mipmap.torch);
        prizeicon = BitmapFactory.decodeResource(getResources(), R.mipmap.prize);
        browsericon = BitmapFactory.decodeResource(getResources(), R.mipmap.browser);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        JSONObject main = new JSONObject();

        int width = getWidth();
        //int height = getHeight();

        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + AppConfig.GLOBAL_PATH;
            file = new File(path + AppConfig.CONFIG_FILE);
        } catch (Exception e){
            Log.e("FILE ERROR", e.getMessage());
        }

        if (mOvalRect == null || !file.exists()) {
            mOvalRect = new RectF(width / 2 - mOuterRadius, height / 2 - mOuterRadius, width / 2 + mOuterRadius, height / 2 + mOuterRadius);
            minner = new RectF(width / 2 - mInnerRadius, height / 2 - mInnerRadius, width / 2 + mInnerRadius, height / 2 + mInnerRadius);

            int xCenterm = (int) minner.centerX();
            int yCenterm = (int) minner.centerY();

            int xCentero = (int) mOvalRect.centerX();
            int yCentero = (int) mOvalRect.centerY();
            try {
                if(!file.exists()) {
                    JSONObject sub1 = new JSONObject();
                    JSONObject sub2 = new JSONObject();

                    main.put("OUTERCIRCLE", sub1);
                    sub1.put("CENTERX", String.valueOf(xCentero));
                    sub1.put("CENTERY", String.valueOf(yCentero));
                    sub1.put("RADIUS", mOuterRadius);

                    main.put("INNERCIRCLE", sub2);
                    sub2.put("CENTERX", String.valueOf(xCenterm));
                    sub2.put("CENTERY", String.valueOf(yCenterm));
                    sub2.put("RADIUS", mInnerRadius);

                }
            }catch (Exception e){
                Log.e("OUT/IN CIRCLE", e.getMessage());
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
        int center0X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 0 * mSweepAngle + mSweepAngle / 2)));
        int center0Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 0 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(torchicon, width / 2 + center0X - torchicon.getWidth() / 2, height / 2 + center0Y - torchicon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub3 = new JSONObject();
                main.put("torchicon", sub3);
                sub3.put("X", String.valueOf(width / 2 + center0X - torchicon.getWidth() / 2));
                sub3.put("Y", String.valueOf(height / 2 + center0Y - torchicon.getHeight() / 2));
                sub3.put("W", String.valueOf(torchicon.getWidth()));
                sub3.put("H", String.valueOf(torchicon.getHeight()));
            }
        }catch (Exception e){
            Log.e("TORCH", e.getMessage());
        }

        int center1X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 1 * mSweepAngle + mSweepAngle / 2)));
        int center1Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 1 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(mirroricon, width / 2 + center1X - mirroricon.getWidth() / 2, height / 2 + center1Y - mirroricon.getHeight() / 2, null);

        try {
            if(!file.exists()) {
                JSONObject sub4 = new JSONObject();
                main.put("mirroricon", sub4);
                sub4.put("X", String.valueOf(width / 2 + center1X - mirroricon.getWidth() / 2));
                sub4.put("Y", String.valueOf(height / 2 + center1Y - mirroricon.getHeight() / 2));
                sub4.put("W", String.valueOf(mirroricon.getWidth()));
                sub4.put("H", String.valueOf(mirroricon.getHeight()));
            }
        }catch (Exception e){
            Log.e("MIRROR", e.getMessage());
        }

        int center2X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 2 * mSweepAngle + mSweepAngle / 2)));
        int center2Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 2 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(calculatoricon, width / 2 + center2X - calculatoricon.getWidth() / 2, height / 2 + center2Y - calculatoricon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub5 = new JSONObject();
                main.put("calculatoricon", sub5);
                sub5.put("X", String.valueOf(width / 2 + center2X - calculatoricon.getWidth() / 2));
                sub5.put("Y", String.valueOf(height / 2 + center2Y - calculatoricon.getHeight() / 2));
                sub5.put("W", String.valueOf(calculatoricon.getWidth()));
                sub5.put("H", String.valueOf(calculatoricon.getHeight()));
            }
        }catch (Exception e){
            Log.e("CALCULATOR", e.getMessage());
        }

        int center3X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 3 * mSweepAngle + mSweepAngle / 2)));
        int center3Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 3 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(mapicon, (width / 2 + center3X - (mapicon.getWidth()) / 2)+5, (height / 2 + center3Y - (mapicon.getHeight()) / 2)-5, null);

        Log.d("MAP ICON VALUES", "Screen Width = " + String.valueOf(width)
                + "Screen Height = " + String.valueOf(height)
                + "Center 3x = " + String.valueOf(center3X)
                + "Center 3y = " + String.valueOf(center3Y)
                + "MAP ICON Width = " + String.valueOf(mapicon.getWidth())
                + "MAP ICON Height = " + String.valueOf(mapicon.getHeight())
                + "Value 1 = " + String.valueOf(width / 2 + center3X - mapicon.getWidth() / 2)
                + "Value 2 = " + String.valueOf(height / 2 + center3Y - mapicon.getHeight() / 2));
        try {
            if(!file.exists()) {
                JSONObject sub6 = new JSONObject();
                main.put("mapicon", sub6);
                sub6.put("X", String.valueOf(width / 2 + center3X - mapicon.getWidth() / 2));
                sub6.put("Y", String.valueOf(height / 2 + center3Y - mapicon.getHeight() / 2));
                sub6.put("W", String.valueOf(mapicon.getWidth()));
                sub6.put("H", String.valueOf(mapicon.getHeight()));
            }
        }catch (Exception e){
            Log.e("MAP", e.getMessage());
        }

        int center4X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 4 * mSweepAngle + mSweepAngle / 2)));
        int center4Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 4 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(moreicon, width / 2 + center4X - moreicon.getWidth() / 2, height / 2 + center4Y - moreicon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub7 = new JSONObject();
                main.put("moreicon", sub7);
                sub7.put("X", String.valueOf(width / 2 + center4X - moreicon.getWidth() / 2));
                sub7.put("Y", String.valueOf(height / 2 + center4Y - moreicon.getHeight() / 2));
                sub7.put("W", String.valueOf(moreicon.getWidth()));
                sub7.put("H", String.valueOf(moreicon.getHeight()));
            }
        }catch (Exception e){
            Log.e("MORE", e.getMessage());
        }

        int center5X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 5 * mSweepAngle + mSweepAngle / 2)));
        int center5Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 5 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(cleanericon, width / 2 + center5X - cleanericon.getWidth() / 2, height / 2 + center5Y - cleanericon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub8 = new JSONObject();
                main.put("cleanericon", sub8);
                sub8.put("X", String.valueOf(width / 2 + center5X - cleanericon.getWidth() / 2));
                sub8.put("Y", String.valueOf(height / 2 + center5Y - cleanericon.getHeight() / 2));
                sub8.put("W", String.valueOf(cleanericon.getWidth()));
                sub8.put("H", String.valueOf(cleanericon.getHeight()));
            }
        }catch (Exception e){
            Log.e("CLEAR", e.getMessage());
        }

        int center6X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 6 * mSweepAngle + mSweepAngle / 2)));
        int center6Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 6 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(hommyicon, width / 2 + center6X - hommyicon.getWidth() / 2, height / 2 + center6Y - hommyicon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub9 = new JSONObject();
                main.put("hommyicon", sub9);
                sub9.put("X", String.valueOf(width / 2 + center6X - hommyicon.getWidth() / 2));
                sub9.put("Y", String.valueOf(height / 2 + center6Y - hommyicon.getHeight() / 2));
                sub9.put("W", String.valueOf(hommyicon.getWidth()));
                sub9.put("H", String.valueOf(hommyicon.getHeight()));
            }
        }catch (Exception e){
            Log.e("HOME", e.getMessage());
        }

        int center7X = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(START_DEGREE + 7 * mSweepAngle + mSweepAngle / 2)));
        int center7Y = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(START_DEGREE + 7 * mSweepAngle + mSweepAngle / 2)));
        canvas.drawBitmap(browsericon, width / 2 + center7X - browsericon.getWidth() / 2, height / 2 + center7Y - browsericon.getHeight() / 2, null);
        try {
            if(!file.exists()) {
                JSONObject sub10 = new JSONObject();
                main.put("browsericon", sub10);
                sub10.put("X", String.valueOf(width / 2 + center7X - browsericon.getWidth() / 2));
                sub10.put("Y", String.valueOf(height / 2 + center7Y - browsericon.getHeight() / 2));
                sub10.put("W", String.valueOf(browsericon.getWidth()));
                sub10.put("H", String.valueOf(browsericon.getHeight()));
                controller.insertdata(main.toString(), AppConfig.CONFIG_FILE);
            }
        }catch (Exception e){
            Log.e("BROWSER", e.getMessage());
        }

        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius + 3, mPaint);
        super.onDraw(canvas);

        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);
        canvas.drawBitmap(prizeicon, width / 2 - prizeicon.getWidth() / 2, height / 2 - prizeicon.getHeight() / 2, null);
        super.onDraw(canvas);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private int[] circlesize(Service act){
        int[] result = new int[3];
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
        int height = size.y;
        double oradius = 0.37*width;
        double inradius = 0.13*width;
        result[0] = (int) oradius;
        result[1] = (int) inradius;
        result[2] = height;
        return result;
    }
}

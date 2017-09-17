package com.app.utilityapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by sukhvindersingh on 2017-09-16.
 */

public class Torch extends Activity {
    private Button off;
    RelativeLayout.LayoutParams param;
    private Button bt;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.torch);
        lin = (LinearLayout) findViewById(R.id.powerlay);
        off = (Button) findViewById(R.id.power);
        bt = new Button(getApplicationContext());
        param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        bt.setWidth(55);
        bt.setHeight(55);
        bt.setBackgroundDrawable(getResources().getDrawable(R.mipmap.power_off));
        bt.setGravity(0);
        // bt.setMinHeight(125);
        //bt.setMinWidth(125);
        setOnClickOpenApp(bt);
        bt.setClickable(true);
        bt.setEnabled(true);
        try {
            off.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    lin.removeView(off);
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            lin.addView(bt,param);
                            finish();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_CANCEL:

                            break;
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }
    }
    private void setOnClickOpenApp(final Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Exception ex) {
                }
            }
        });
    }
    public void onClickM(View v)
    {
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
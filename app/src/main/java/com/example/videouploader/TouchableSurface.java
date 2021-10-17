package com.example.videouploader;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

public class TouchableSurface extends SurfaceView {
    public boolean istouch = false;
    public int start_x, start_y, end_x, end_y;
    public RectDrawCallBack callBack;
    public TouchableSurface(Context context) {
        super(context);
    }

    public TouchableSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchableSurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        //获取屏幕分辨率
        DisplayMetrics metric=new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(metric);

        int x = (int)event.getX();
        int y = (int)event.getY();

        int width=metric.widthPixels;  // 宽度（PX）
        int height=metric.heightPixels;  // 高度（PX）

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                istouch=false;
                start_x = x;
                start_y = y;
                Log.e("surfaceviewtouch", "onTouch: down");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("surfaceviewtouch", "onTouch: up");
                istouch=true;
                end_x = x;
                end_y = y;
                callBack.setActions(istouch, start_x, start_y, end_x, end_y);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("surfaceviewtouch", "onTouch: move");
                istouch=true;
                end_x = x;
                end_y = y;
                callBack.setActions(istouch, start_x, start_y, end_x, end_y);
                break;
        }
        return true;

    }
}

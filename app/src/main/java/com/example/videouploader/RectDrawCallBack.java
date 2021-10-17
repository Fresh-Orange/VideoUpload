package com.example.videouploader;

import android.app.Notification;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RectDrawCallBack implements SurfaceHolder.Callback {
    Thread drawThread = null;
    public boolean istouch = false;
    public int start_x, start_y, end_x, end_y;
    boolean exit = false;
    int height;
    int width;
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //在surface的大小发生改变时激发
        System.out.println("surfaceChanged");
        this.height = height;
        this.width = width;
    }

    public void setActions(boolean istouch, int start_x, int start_y, int end_x, int end_y)
    {
        this.istouch = istouch;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        drawThread = new Thread(){
            public void run() {
                while(!exit){
                    //获取得到画布
                    Canvas c = holder.lockCanvas(new Rect(0, 0, width, height));
                    // 清空画布
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    // 画矩形
                    Paint p =new Paint();
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(5);
                    p.setColor(Color.rgb(255,255,255));
                    Rect aa  =  new Rect(start_x, start_y, end_x, end_y);
                    c.drawRect(aa, p);

                    //更新提交屏幕显示内容
                    holder.unlockCanvasAndPost(c);
                    try {
                        Thread.sleep(100);

                    } catch (Exception e) {
                    }
                }
            };
        };
        drawThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁时激发，一般在这里将画图的线程停止、释放。
        System.out.println("surfaceDestroyed==");
        drawThread.interrupt();
        exit =true;
    }
}

package com.example.videouploader;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawHandler implements Runnable{
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsRunning;

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while (mIsRunning) {
            draw();
        }
    }

    private void draw() {
        mCanvas = mHolder.lockCanvas();
        if (mCanvas != null) {
            try {
                //使用获得的Canvas做具体的绘制
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}

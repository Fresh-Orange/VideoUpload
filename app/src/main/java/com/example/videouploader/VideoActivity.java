package com.example.videouploader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;


public class VideoActivity extends AppCompatActivity implements OnCompletionListener,
        OnPreparedListener, SurfaceHolder.Callback {
    int VIDEO_CAPTURE = 1;
    private SurfaceView video_surfaceView;
    private TouchableSurface edit_surfaceView;
    private SurfaceHolder video_holder;
    private SurfaceHolder edit_holder;
    private MediaPlayer player;
    private int vWidth,vHeight;

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsRunning;
    public static final String INTENT_KEY = "VideoActivity.Intent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.video_activity);

        Uri infoString = getIntent().getParcelableExtra(INTENT_KEY);

        //下面开始实例化MediaPlayer对象
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        //然后指定需要播放文件的路径，初始化MediaPlayer
        Uri dataPath = infoString;
        try {
            player.setDataSource(this, dataPath);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Next:::", "IOException " + e.getMessage());
            e.printStackTrace();
        }

        video_surfaceView = (SurfaceView)this.findViewById(R.id.surface_view_player);
        //给SurfaceView添加CallBack监听
        video_holder = video_surfaceView.getHolder();
        video_holder.addCallback(this);
        //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
        video_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        edit_surfaceView = (TouchableSurface)this.findViewById(R.id.surface_view_draw);

        edit_holder = edit_surfaceView.getHolder();
        edit_surfaceView.callBack = new RectDrawCallBack();
        edit_holder.addCallback(edit_surfaceView.callBack);
        edit_surfaceView.setZOrderOnTop(true);
        edit_surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // 当Surface尺寸等参数改变时触发
        Log.v("Surface Change:::", "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当SurfaceView中的Surface被创建的时候被调用
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player.setDisplay(holder);
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        player.prepareAsync();

    }

    @Override
    public void onPrepared(MediaPlayer player) {
        // 当prepare完成后，该方法触发，在这里我们播放视频

        //首先取得video的宽和高
        vWidth = player.getVideoWidth();
        vHeight = player.getVideoHeight();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        Log.d("onPrepared", String.format("vWidth=%d, vHeight=%d, screenWidth=%d, screenHeight=%d", vWidth, vHeight, screenWidth,screenHeight));

        float Ratio = (float)vHeight/(float)vWidth;

        int newHeight = (int)Math.ceil((float)screenWidth*Ratio);

        video_surfaceView.setLayoutParams(new ConstraintLayout.LayoutParams(screenWidth, newHeight));

//        if(vWidth > screenWidth || vHeight > screenHeight){
//            Log.d("onPrepared", "ReSized");
//            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
//            float wRatio = (float)vWidth/(float)screenWidth;
//            float hRatio = (float)vHeight/(float)screenHeight;
//
//            //选择大的一个进行缩放
//            float ratio = Math.max(wRatio, hRatio);
//
//            vWidth = (int)Math.ceil((float)vWidth/ratio);
//            vHeight = (int)Math.ceil((float)vHeight/ratio);
//
//            //设置surfaceView的布局参数
////            video_surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
//        }
        //然后开始播放视频

        Log.d("onPrepared", "onPrepared: ");

        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        // 当MediaPlayer播放完成后触发
        Log.v("Play Over:::", "onComletion called");
        //数据使用Intent返回
        Intent intent = new Intent();
        intent.putExtra("position_x", Math.min(edit_surfaceView.start_x, edit_surfaceView.end_x));
        intent.putExtra("position_y", Math.min(edit_surfaceView.start_y, edit_surfaceView.end_y));
        intent.putExtra("height", Math.abs(edit_surfaceView.end_y - edit_surfaceView.start_y));
        intent.putExtra("width", Math.abs(edit_surfaceView.end_x - edit_surfaceView.start_x));
        this.setResult(RESULT_OK, intent);
        //关闭Activity
        this.finish();
    }

}
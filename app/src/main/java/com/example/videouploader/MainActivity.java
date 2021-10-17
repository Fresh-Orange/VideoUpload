package com.example.videouploader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {
    int VIDEO_CAPTURE = 1;
    int DRAW_RECT = 2;
    Uri videoUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextInputEditText versionInputText = findViewById(R.id.VersionEditText);
        Button recordButton = findViewById(R.id.record_button);

        Log.d("MainActivity:::", "MainActivity called");

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 用这个Intent去跨应用调用系统相机
                Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
//                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,10485760L);
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);

                startActivityForResult(intent,VIDEO_CAPTURE);
            }
        });

        Button postButton = findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String version = versionInputText.getText().toString(); // 用户输入的版本号
                String output = "version:" + version;
                Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();

            }
        });

        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoUri == null){
                    Toast.makeText(MainActivity.this, "先拍摄视频", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent startIntent = new Intent(getApplicationContext(),
                        VideoActivity.class);
                startIntent.putExtra(VideoActivity.INTENT_KEY, videoUri);

                startActivityForResult(startIntent, DRAW_RECT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==VIDEO_CAPTURE){
            videoUri=data.getData();
            TextView filePathText = findViewById(R.id.file_path_text);
            filePathText.setText(videoUri.toString());
            Toast.makeText(this, videoUri.toString(), Toast.LENGTH_SHORT).show();
        }
        else if (resultCode==RESULT_OK && requestCode==DRAW_RECT) {
            int position_x = data.getExtras().getInt("position_x");
            int position_y = data.getExtras().getInt("position_y");
            int height = data.getExtras().getInt("height");
            int width = data.getExtras().getInt("width");
            TextView positionText = findViewById(R.id.positionEditText);
            String positionInfo = String.format("x:%d, y:%d, h:%d, w:%d", position_x,position_y,height,width);
            positionText.setText(positionInfo);
            Toast.makeText(this, positionInfo, Toast.LENGTH_SHORT).show();
        }

        Log.d("MainActivity:::", "MainActivity called");
    }
}
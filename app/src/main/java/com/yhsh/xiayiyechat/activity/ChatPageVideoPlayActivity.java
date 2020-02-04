package com.yhsh.xiayiyechat.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yhsh.xiayiyechat.util.AndroidUtils;
import com.yhsh.xiayiyechat.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author xiayiye
 */
public class ChatPageVideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_page_video_play);
        AndroidUtils.getInstance().setTittleTransparent(this);
        AndroidUtils.getInstance().setFullScreen(this);
        String videoPath = getIntent().getStringExtra("video_path");
        String imgPath = getIntent().getStringExtra("img_path");
        VideoView videoFullscreen = findViewById(R.id.video_fullscreen);
        ImageView ivFullscreen = findViewById(R.id.iv_fullscreen);
        if (!TextUtils.isEmpty(videoPath)) {
            videoFullscreen.setVisibility(View.VISIBLE);
            videoFullscreen.start();
            MediaController mediaController = new MediaController(this);
            videoFullscreen.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoFullscreen);
            videoFullscreen.setVideoPath(videoPath);
        } else if (!TextUtils.isEmpty(imgPath)) {
            //显示图片
            ivFullscreen.setVisibility(View.VISIBLE);
            ivFullscreen.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        }
    }
}

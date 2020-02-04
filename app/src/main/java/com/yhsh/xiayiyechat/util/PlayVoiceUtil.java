package com.yhsh.xiayiyechat.util;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageView;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 下一页5（轻飞扬）
 */
public class PlayVoiceUtil {
    public static void startPlay(MediaPlayer mp, ImageView ivMessageVoice, String playUrl) {
        FileInputStream mFIS = null;
        FileDescriptor mFD;
        final AnimationDrawable mVoiceAnimation = (AnimationDrawable) ivMessageVoice.getDrawable();
        if (mp.isPlaying()) {
            mp.stop();
            //结束语音动画
            mVoiceAnimation.stop();
            return;
        }
        try {
            mp.reset();
            mFIS = new FileInputStream(playUrl);
            mFD = mFIS.getFD();
            mp.setDataSource(mFD);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    //播放语音的动画
                    mVoiceAnimation.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    //播放结束,结束动画
                    mVoiceAnimation.stop();
                }
            });

        } catch (Exception e) {
            ToastUtil.show("文件丢失, 尝试重新获取");
        } finally {
            try {
                if (mFIS != null) {
                    mFIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

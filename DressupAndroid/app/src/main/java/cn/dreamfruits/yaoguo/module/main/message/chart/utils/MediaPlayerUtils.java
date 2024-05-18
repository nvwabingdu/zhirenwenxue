package cn.dreamfruits.yaoguo.module.main.message.chart.utils;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;

/**
 * Function:
 * Desc:
 */
public class MediaPlayerUtils {
    private static volatile MediaPlayerUtils instance;

    public static synchronized MediaPlayerUtils getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerUtils.class) {
                if (instance == null)
                    instance = new MediaPlayerUtils();
            }
        }
        return instance;
    }

    protected MediaPlayer mMediaPlayer;

    public synchronized void playMedia(Context context, String voicePath,
                                       MediaPlayer.OnCompletionListener onCompletionListener,
                                       MediaPlayer.OnPreparedListener onPreparedListener,
                                       MediaPlayer.OnErrorListener onErrorListener) {

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }


//        mMediaPlayer = MediaPlayer.create(context, Uri.parse(voicePath));
        mMediaPlayer = new MediaPlayer();
//        voicePath = "https://www.365english.com/audio/right_answer.mp3";
        try {
            mMediaPlayer.setDataSource(voicePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mMediaPlayer == null) {
            Looper.prepare();
            ToastUtils.showShort("播放失败,请稍后重试");
            Looper.loop();
            return;
        }
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        //需使用异步缓冲
        mMediaPlayer.prepareAsync();

    }


    public synchronized void playMedia(Context context, int voicePath,
                                       MediaPlayer.OnCompletionListener onCompletionListener,
                                       MediaPlayer.OnPreparedListener onPreparedListener,
                                       MediaPlayer.OnErrorListener onErrorListener) {

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }


//        mMediaPlayer = MediaPlayer.create(context, Uri.parse(voicePath));
        mMediaPlayer = MediaPlayer.create(context, voicePath);
        mMediaPlayer.start();
//        voicePath = "https://www.365english.com/audio/right_answer.mp3";
//        try {
//            mMediaPlayer.setDataSource(voicePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
//        需使用异步缓冲
//        mMediaPlayer.prepareAsync();

    }


    public void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                LogUtils.e(">>>> 暂停");
                mMediaPlayer.pause();
            }
        }
    }

    public void restart() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
            }
        }
    }

    public Boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
//            LogUtils.e(">>>> 开始");
            mMediaPlayer.start();
        }
    }

    public static int gettime(String string) {   //使用此方法可以直接在后台获取音频文件的播放时间，而不会真的播放音频
        MediaPlayer player = new MediaPlayer();  //首先你先定义一个mediaplayer
        try {
            player.setDataSource(string);  //String是指音频文件的路径
            player.prepare();        //这个是mediaplayer的播放准备 缓冲

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听准备

            @Override
            public void onPrepared(MediaPlayer player) {
                int size = player.getDuration();
                String timelong = size / 1000 + "''";

            }
        });
        int size = player.getDuration();//得到音频的时间
//        int timelong1 = (int) Math.ceil((size / 1000)) ;//转换为秒 单位为''
        player.stop();//暂停播放
        player.release();//释放资源
        return size;  //返回音频时间

    }


}

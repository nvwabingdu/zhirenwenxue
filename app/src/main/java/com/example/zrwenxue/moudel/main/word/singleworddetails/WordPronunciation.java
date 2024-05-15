package com.example.zrwenxue.moudel.main.word.singleworddetails;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-15
 * Time: 20:24
 */
public class WordPronunciation {
    public MediaPlayer player; // 定义多媒体对象
    public String mp3Url;

    public WordPronunciation(MediaPlayer player, String mp3Url) {
        this.player = player;
        this.mp3Url = mp3Url;
    }


    public void play() {
        try {
            player.reset(); //重置多媒体
            // 指定参数为音频文件
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(mp3Url);//为多媒体对象设置播放路径
            player.prepare();//准备播放
            player.start();//开始播放
            //setOnCompletionListener 当当前多媒体对象播放完成时发生的事件
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
//                    next();//如果当前歌曲播放完毕,自动播放下一首.
                }
            });

        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    //继续播放
    public  void goPlay(){
        int position = getCurrentProgress();
        player.seekTo(position);//设置当前MediaPlayer的播放位置，单位是毫秒。
        try {
            player.prepare();//  同步的方式装载流媒体文件。
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }
    // 获取当前进度
    public int getCurrentProgress() {
        if (player != null & player.isPlaying()) {
            return player.getCurrentPosition();
        } else if (player != null & (!player.isPlaying())) {
            return player.getCurrentPosition();
        }
        return 0;
    }

//    public void next() {
//        songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
//        play();
//    }

//    public void last() {
//        songNum = songNum == 0 ? musicList.size() - 1 : songNum - 1;
//        play();
//    }
    // 暂停播放
    public void pause() {
        if (player != null && player.isPlaying()){
            player.pause();
        }
    }
    //停止播放
    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
    }
}

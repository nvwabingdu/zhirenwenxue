package com.zr.kernel.impl.media;

import android.content.Context;

import com.zr.kernel.factory.PlayerFactory;


/**
 * 不推荐，系统的MediaPlayer兼容性较差，建议使用IjkPlayer或者ExoPlayer  抽象工厂具体实现类
 */
public class MediaPlayerFactory extends PlayerFactory<AndroidMediaPlayer> {

    public static MediaPlayerFactory create() {
        return new MediaPlayerFactory();
    }

    @Override
    public AndroidMediaPlayer createPlayer(Context context) {
        return new AndroidMediaPlayer(context);
    }
}

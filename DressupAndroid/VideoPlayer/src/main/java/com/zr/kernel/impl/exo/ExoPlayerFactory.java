package com.zr.kernel.impl.exo;

import android.content.Context;

import com.zr.kernel.factory.PlayerFactory;

/**
 * exo视频播放器Factory 抽象工厂具体实现类
 */
public class ExoPlayerFactory extends PlayerFactory<ExoMediaPlayer> {

    public static ExoPlayerFactory create() {
        return new ExoPlayerFactory();
    }

    @Override
    public ExoMediaPlayer createPlayer(Context context) {
        return new ExoMediaPlayer(context);
    }
}

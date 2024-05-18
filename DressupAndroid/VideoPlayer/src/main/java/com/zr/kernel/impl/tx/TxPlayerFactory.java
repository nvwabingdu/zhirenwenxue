package com.zr.kernel.impl.tx;

import android.content.Context;

import com.zr.kernel.factory.PlayerFactory;


/**
 * tx视频播放器Factory
 */
public class TxPlayerFactory extends PlayerFactory<TxMediaPlayer> {

    public static TxPlayerFactory create() {
        return new TxPlayerFactory();
    }

    @Override
    public TxMediaPlayer createPlayer(Context context) {
        return new TxMediaPlayer(context);
    }
}

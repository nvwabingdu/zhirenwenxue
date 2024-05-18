package com.zr.kernel.impl.ijk;

import android.content.Context;

import com.zr.kernel.factory.PlayerFactory;


/**
 * ijk视频播放器Factory  抽象工厂具体实现类
 */
public class IjkPlayerFactory extends PlayerFactory<IjkVideoPlayer> {

    public static IjkPlayerFactory create() {
        return new IjkPlayerFactory();
    }

    @Override
    public IjkVideoPlayer createPlayer(Context context) {
        return new IjkVideoPlayer(context);
    }
}

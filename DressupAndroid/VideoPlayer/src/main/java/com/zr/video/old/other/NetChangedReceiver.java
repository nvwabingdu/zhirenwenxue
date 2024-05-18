package com.zr.video.old.other;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zr.kernel.utils.VideoLogUtils;
import com.zr.video.config.ConstantKeys;
import com.zr.video.old.controller.AbsVideoPlayerController;
import com.zr.video.old.player.OldVideoPlayer;
import com.zr.video.tool.NetworkUtils;

/**
 * 网络状态监听广播
 */
@Deprecated
public class NetChangedReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        VideoLogUtils.i("网络状态监听广播接收到数据了");
        OldVideoPlayer mVideoPlayer = VideoPlayerManager.instance().getCurrentVideoPlayer();
        if (mVideoPlayer!=null){
            AbsVideoPlayerController controller = mVideoPlayer.getController();
            switch (NetworkUtils.getConnectState(context)) {
                case MOBILE:
                    VideoLogUtils.i("当网络状态监听前连接了移动数据");
                    break;
                case WIFI:
                    VideoLogUtils.i("网络状态监听当前连接了Wifi");
                    break;
                case UN_CONNECTED:
                    VideoLogUtils.i("网络状态监听当前没有网络连接");
                    if (mVideoPlayer.isPlaying() || mVideoPlayer.isBufferingPlaying()) {
                        VideoLogUtils.i("网络状态监听当前没有网络连接---设置暂停播放");
                        mVideoPlayer.pause();
                    }
                    if (controller!=null){
                        controller.onPlayStateChanged(ConstantKeys.CurrentState.STATE_ERROR);
                    }
                    break;
                default:
                    VideoLogUtils.i("网络状态监听其他情况");
                    break;
            }
        }
    }
}

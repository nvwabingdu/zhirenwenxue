package com.zr.video.old.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.zr.kernel.utils.VideoLogUtils;
import com.zr.video.config.ConstantKeys;
import com.zr.video.old.controller.AbsVideoPlayerController;
import com.zr.video.old.player.OldVideoPlayer;

/**
 * 电量状态监听广播
 */
@Deprecated
public class BatterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        VideoLogUtils.i("电量状态监听广播接收到数据了");
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);
        OldVideoPlayer mVideoPlayer = VideoPlayerManager.instance().getCurrentVideoPlayer();
        if (mVideoPlayer!=null){
            AbsVideoPlayerController controller = mVideoPlayer.getController();
            if (controller!=null){
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    // 充电中
                    controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_CHARGING);
                } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                    // 充电完成
                    controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_FULL);
                } else {
                    // 当前电量
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    // 总电量
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                    int percentage = (int) (((float) level / scale) * 100);
                    VideoLogUtils.i("广播NetworkReceiver------当前电量"+level);
                    VideoLogUtils.i("广播NetworkReceiver------总电量"+scale);
                    VideoLogUtils.i("广播NetworkReceiver------百分比"+percentage);
                    if (percentage <= 10) {
                        controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_10);
                    } else if (percentage <= 20) {
                        controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_20);
                    } else if (percentage <= 50) {
                        controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_50);
                    } else if (percentage <= 80) {
                        controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_80);
                    } else if (percentage <= 100) {
                        controller.onBatterStateChanged(ConstantKeys.BatterMode.BATTERY_100);
                    }
                }
            }
        }
    }

}

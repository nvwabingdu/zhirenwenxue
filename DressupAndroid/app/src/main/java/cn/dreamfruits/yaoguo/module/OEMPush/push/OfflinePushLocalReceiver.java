package cn.dreamfruits.yaoguo.module.OEMPush.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;

import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIUtils;
import cn.dreamfruits.yaoguo.module.main.message.chart.TUIConstants;

public class OfflinePushLocalReceiver extends BroadcastReceiver {
    public static final String TAG = ">>>> "+OfflinePushLocalReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "BROADCAST_PUSH_RECEIVER intent = " + intent);
        if (intent != null) {
            String ext = intent.getStringExtra(TUIConstants.TUIOfflinePush.NOTIFICATION_EXT_KEY);
            TUIUtils.handleOfflinePush(ext, null);
        } else {
            LogUtils.e(TAG, "onReceive ext is null");
        }
    }
}

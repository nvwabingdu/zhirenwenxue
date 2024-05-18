package cn.dreamfruits.yaoguo.module.OEMPush;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.hihonor.push.sdk.HonorMessageService;
import com.hihonor.push.sdk.bean.DataMessage;

public class MyHonorMessageService extends HonorMessageService {
    private static final String TAG =">>>> push "+ MyHonorMessageService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        LogUtils.i(TAG, "onNewToken token=" + token);

        if (TextUtils.isEmpty(token)) {
            return;
        }

        if (OEMPushSetting.mPushCallback != null) {
            OEMPushSetting.mPushCallback.onTokenCallback(token);
        }
    }

    @Override
    public void onMessageReceived(DataMessage dataMessage) {
        LogUtils.i(TAG, "onMessageReceived message=" + dataMessage);
    }
}

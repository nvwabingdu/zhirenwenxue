package cn.dreamfruits.yaoguo.module.OEMPush;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.messaging.FirebaseMessagingService;

public class GoogleFCMMsgService extends FirebaseMessagingService {
    private static final String TAG =">>>> push "+ GoogleFCMMsgService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        LogUtils.i(TAG, "onNewToken google fcm onNewToken : " + token);

        if (OEMPushSetting.mPushCallback != null) {
            OEMPushSetting.mPushCallback.onTokenCallback(token);
        }
    }
}

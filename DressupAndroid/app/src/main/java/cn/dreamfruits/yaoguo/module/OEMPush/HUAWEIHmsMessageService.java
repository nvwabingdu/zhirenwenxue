package cn.dreamfruits.yaoguo.module.OEMPush;

import com.blankj.utilcode.util.LogUtils;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIOfflinePushErrorBean;

public class HUAWEIHmsMessageService extends HmsMessageService {
    private static final String TAG =">>>> push "+ HUAWEIHmsMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        LogUtils.i(TAG, "onMessageReceived message=" + message);
    }

    @Override
    public void onMessageSent(String msgId) {
        LogUtils.i(TAG, "onMessageSent msgId=" + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        LogUtils.i(TAG, "onSendError msgId=" + msgId);
    }

    @Override
    public void onNewToken(String token) {
        LogUtils.i(TAG, "onNewToken token=" + token);

        if (OEMPushSetting.mPushCallback != null) {
            OEMPushSetting.mPushCallback.onTokenCallback(token);
        }
    }

    @Override
    public void onTokenError(Exception exception) {
        LogUtils.i(TAG, "onTokenError exception=" + exception);
        if (OEMPushSetting.mPushCallback != null) {
            TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
            errorBean.setErrorCode(TUIOfflinePushConfig.REGISTER_TOKEN_ERROR_CODE);
            errorBean.setErrorDescription("huawei onTokenError exception = " + exception);
            OEMPushSetting.mPushCallback.onTokenErrorCallBack(errorBean);
        }
    }

    @Override
    public void onMessageDelivered(String msgId, Exception exception) {
        LogUtils.i(TAG, "onMessageDelivered msgId=" + msgId);
    }
}

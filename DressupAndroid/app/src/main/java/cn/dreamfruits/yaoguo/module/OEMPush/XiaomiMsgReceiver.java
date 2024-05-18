package cn.dreamfruits.yaoguo.module.OEMPush;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIOfflinePushErrorBean;

public class XiaomiMsgReceiver extends PushMessageReceiver {
    private static final String TAG = ">>>> push " + XiaomiMsgReceiver.class.getSimpleName();
    private String mRegId;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        LogUtils.d(TAG, "onReceivePassThroughMessage is called. ");
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        LogUtils.d(TAG, "onNotificationMessageClicked miPushMessage " + miPushMessage.toString());

        if (OEMPushSetting.mPushCallback == null) {
            return;
        }

        Map<String, String> extra = miPushMessage.getExtra();
        String ext = extra.get("ext");
        if (TextUtils.isEmpty(ext)) {
            LogUtils.w(TAG, "onNotificationMessageClicked: no extra data found");
            return;
        }
        /*Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
        intent.putExtra("ext", ext);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);*/
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        LogUtils.d(TAG, "onNotificationMessageArrived is called. ");
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        LogUtils.d(TAG, "onReceiveRegisterResult is called. " + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);

        LogUtils.d(TAG,
                "cmd: " + command + " | arg: " + cmdArg1 + " | result: " + miPushCommandMessage.getResultCode() + " | reason: " + miPushCommandMessage.getReason());

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }

        LogUtils.d(TAG, "regId: " + mRegId);
        if (OEMPushSetting.mPushCallback != null) {
            if (miPushCommandMessage.getResultCode() != ErrorCode.SUCCESS) {
                TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
                errorBean.setErrorCode(miPushCommandMessage.getResultCode());
                errorBean.setErrorDescription("xiaomi error code: " + String.valueOf(miPushCommandMessage.getResultCode()));
                OEMPushSetting.mPushCallback.onTokenErrorCallBack(errorBean);
            } else {
                OEMPushSetting.mPushCallback.onTokenCallback(mRegId);
            }
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }
}

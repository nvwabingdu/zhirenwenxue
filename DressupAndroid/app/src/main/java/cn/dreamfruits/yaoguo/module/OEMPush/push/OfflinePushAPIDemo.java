package cn.dreamfruits.yaoguo.module.OEMPush.push;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Map;

import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIUtils;
import cn.dreamfruits.yaoguo.module.main.message.chart.TUIConstants;
import cn.dreamfruits.yaoguo.module.main.message.chart.TUICore;
import cn.dreamfruits.yaoguo.module.main.message.chart.interfaces.ITUINotification;

public class OfflinePushAPIDemo {
    public static final String TAG =">>>> push";
    /**
     *  手动注册离线推送服务, IM 账号登录成功时调用,调用该接口不需要再填充参数到组件的 PrivateConstants 里。
     *
     *  Register offline push service, called when IM account login is successful.Calling this interface does not require filling parameters into the
     * component's PrivateConstants.
     *
     *  @note  json格式如下：
     * {
     *    // huawei
     *   "huaweiPushBussinessId": "30206",    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
     *   "huaweiBadgeClassName": "", // 角标参数，默认为应用的 launcher 界面的类名
     * launcher interface
     *
     *   // xiaomi
     *   "xiaomiPushBussinessId": "",// 在腾讯云控制台上传第三方推送证书后分配的证书ID
     *   "xiaomiPushAppId": "",// 小米开放平台分配的应用APPID
     *   "xiaomiPushAppKey": "",// 小米开放平台分配的应用APPKEY
     *
     *   // meizu
     *   "meizuPushBussinessId": "", // 在腾讯云控制台上传第三方推送证书后分配的证书ID
     *   "meizuPushAppId": "",// 魅族开放平台分配的应用APPID
     *   "meizuPushAppKey": "",// 魅族开放平台分配的应用APPKEY
     *
     *   // vivo
     *   "vivoPushBussinessId": "30222", // 在腾讯云控制台上传第三方推送证书后分配的证书ID
     *
     *   // google
     *   "fcmPushBussinessId": "", // 在腾讯云控制台上传第三方推送证书后分配的证书ID
     *
     *   // oppo
     *   "oppoPushBussinessId": "30198", // 在腾讯云控制台上传第三方推送证书后分配的证书ID // The certificate ID assigned in the Tencent Cloud console
     *   "oppoPushAppKey": "b3efaa6c131545fb9b7ab45a500a1ac2",// oppo开放平台分配的应用 AppKey // AppKey, Allocated by OPPO Open Platform
     *   "oppoPushAppSecret": "e0cd89294a0c46bf949e22c5526bc1e1",// oppo开放平台分配的应用 AppSecret // AppSecret, Allocated by OPPO Open Platform
     *
     *   // honor
     *   "honorPushBussinessId": "30208",    // 在腾讯云控制台上传第三方推送证书后分配的证书ID  // The certificate ID assigned in the Tencent Cloud console
     *  }
     *
     */
    public void registerPush(Context context) {
        OfflinePushParamBean offlinePushParamBean = new OfflinePushParamBean();
        offlinePushParamBean.setHuaweiPushBussinessId("30206");
        offlinePushParamBean.setHuaweiBadgeClassName("cn.dreamfruits.yaoguo.module.splash.SplashActivity");

        offlinePushParamBean.setXiaomiPushBussinessId("");
        offlinePushParamBean.setXiaomiPushAppId("");
        offlinePushParamBean.setXiaomiPushAppKey("");

        offlinePushParamBean.setMeizuPushBussinessId("");
        offlinePushParamBean.setMeizuPushAppId("");
        offlinePushParamBean.setMeizuPushAppKey("");

        offlinePushParamBean.setVivoPushBussinessId("30222");

        offlinePushParamBean.setFcmPushBussinessId("");
        offlinePushParamBean.setFcmPushChannelId("");
        offlinePushParamBean.setFcmPushChannelSoundName("");

        offlinePushParamBean.setOppoPushBussinessId("30198");
        offlinePushParamBean.setOppoPushAppKey("b3efaa6c131545fb9b7ab45a500a1ac2");
        offlinePushParamBean.setOppoPushAppSecret("e0cd89294a0c46bf949e22c5526bc1e1");

        offlinePushParamBean.setHonorPushBussinessId("30208");

        String jsonStr = new Gson().toJson(offlinePushParamBean);
        if (TextUtils.isEmpty(jsonStr)) {
            LogUtils.e(TAG, "registerPush json is null");
            return;
        }

        LogUtils.d(TAG, "registerPush json = " + jsonStr);
        try {
            Class clz = Class.forName("cn.dreamfruits.yaoguo.module.OEMPush.pushnative.TUIOfflinePushManager");
            Method methodIntance = clz.getDeclaredMethod("getInstance", new Class[0]);
            Object intance = methodIntance.invoke(null, new Object[] {});
            Method methodRegister = clz.getDeclaredMethod("registerPush", String.class, Context.class);
            methodRegister.setAccessible(true);
            methodRegister.invoke(intance, jsonStr, context);
        } catch (Exception e) {
            LogUtils.e(TAG, "registerPush exception = " + e);
        }
    }

    // 直接调用组件类设置，影响解耦性
    // Call the component class settings directly，affect decoupling
    public void registerPush2(Context context) {
        /*PrivateConstants.huaweiPushBussinessId = 0;
        PrivateConstants.huaweiBadgeClassName = "";

        PrivateConstants.xiaomiPushBussinessId = 0;
        PrivateConstants.xiaomiPushAppId = "";
        PrivateConstants.xiaomiPushAppKey = "";

        PrivateConstants.meizuPushBussinessId = 0;
        PrivateConstants.meizuPushAppId = "";
        PrivateConstants.meizuPushAppKey = "";

        PrivateConstants.vivoPushBussinessId = 0;

        PrivateConstants.fcmPushBussinessId = 0;

        PrivateConstants.oppoPushBussinessId = 0;
        PrivateConstants.oppoPushAppKey = "";
        PrivateConstants.oppoPushAppSecret = "";

        DemoLog.d(TAG, "registerPush2");
        try {
            Class clz = Class.forName("com.tencent.qcloud.tim.tuiofflinepush.TUIOfflinePushManager");
            Method methodIntance = clz.getDeclaredMethod("getInstance", null);
            Object intance = methodIntance.invoke(null, null);
            Method methodRegister = clz.getDeclaredMethod("registerPush", Context.class);
            methodRegister.setAccessible(true);
            methodRegister.invoke(intance, context);
        } catch (Exception e) {
            DemoLog.e(TAG, "registerPush exception = " + e);
        }*/
    }

    /*
     * 当 IM 控制台设置点击后续动作为 "使用推送组件回调跳转"，推送成功后
     * ，通过 TUICore 注册 NotifyEvent，点击通知栏事件会通过 TUICore.onNotifyEvent 回调返回。
     *
     * When the IM console sets the click follow-up action to "use push component callback to jump"，After the push is successful, register NotifyEvent through
     * TUICore, and click the notification bar event will be returned through the TUICore.onNotifyEvent callback.
     */
    public void registerNotifyEvent() {
        TUICore.registerEvent(TUIConstants.TUIOfflinePush.EVENT_NOTIFY,
                TUIConstants.TUIOfflinePush.EVENT_NOTIFY_NOTIFICATION, new ITUINotification() {
            @Override
            public void onNotifyEvent(String key, String subKey, Map<String, Object> param) {
                Log.d(TAG, "onNotifyEvent key = " + key + "subKey = " + subKey);
                if (TUIConstants.TUIOfflinePush.EVENT_NOTIFY.equals(key)) {
                    if (TUIConstants.TUIOfflinePush.EVENT_NOTIFY_NOTIFICATION.equals(subKey)) {
                        if (param != null) {
                            String extString = (String) param.get(TUIConstants.TUIOfflinePush.NOTIFICATION_EXT_KEY);
                            TUIUtils.handleOfflinePush(extString, null);
                        }
                    }
                }
            }
        });
    }

    /*
     * 当 IM 控制台设置点击后续动作为 "使用推送组件回调跳转"，推送成功后，注册广播接收者，点击通知栏事件通过发送广播回调返回，IntentFilter 为
     * TUIConstants.TUIOfflinePush.NOTIFICATION_BROADCAST_ACTION。
     *
     * When the IM console sets the click follow-up action to "use push component callback to jump"，After the push is successful, register the broadcast
     * receiver, click the notification bar event to return by sending the broadcast callback. IntentFilter 为
     * TUIConstants.TUIOfflinePush.NOTIFICATION_BROADCAST_ACTION.
     */
    public void registerNotificationReceiver(Context context, OfflinePushLocalReceiver localReceiver) {
        LogUtils.d(TAG, "registerNotificationReceiver ");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TUIConstants.TUIOfflinePush.NOTIFICATION_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(localReceiver, intentFilter);
    }
}

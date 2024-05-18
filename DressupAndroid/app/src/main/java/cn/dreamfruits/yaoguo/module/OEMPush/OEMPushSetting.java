package cn.dreamfruits.yaoguo.module.OEMPush;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.messaging.FirebaseMessaging;
import com.heytap.msp.push.HeytapPushManager;
import com.hihonor.push.sdk.HonorInstanceId;
import com.hihonor.push.sdk.HonorMessaging;
import com.hihonor.push.sdk.tasks.OnCompleteListener;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.meizu.cloud.pushsdk.PushManager;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import cn.dreamfruits.yaoguo.module.OEMPush.interfaces.PushCallback;
import cn.dreamfruits.yaoguo.module.OEMPush.interfaces.PushSettingInterface;
import cn.dreamfruits.yaoguo.module.OEMPush.pushnative.PrivateConstants;
import cn.dreamfruits.yaoguo.module.OEMPush.pushnative.utils.BrandUtil;
import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIOfflinePushErrorBean;

public class OEMPushSetting implements PushSettingInterface {
    private static final String TAG = ">>>> push " + OEMPushSetting.class.getSimpleName();
    protected static PushCallback mPushCallback = null;

    public void initPush(Context context) {
        int brand = BrandUtil.getInstanceType();

        LogUtils.e(TAG, "brand = " + brand);
        switch (brand) {
            case TUIOfflinePushConfig.BRAND_XIAOMI:
                // 小米离线推送
                LogUtils.e(TAG, "注册小米离线推送");
                MiPushClient.registerPush(context, PrivateConstants.xiaomiPushAppId, PrivateConstants.xiaomiPushAppKey);
                break;
            case TUIOfflinePushConfig.BRAND_HONOR:
                LogUtils.e(TAG, "注册荣耀离线推送");
                HonorMessaging.getInstance(context).turnOnPush().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(com.hihonor.push.sdk.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            LogUtils.i(TAG, "Honor turnOn push successfully.");
                        } else {
                            LogUtils.i(TAG, "Honor turnOn push failed." + task.getException());
                        }
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String pushToken = HonorInstanceId.getInstance(context).getPushToken();
                            LogUtils.i(TAG, "Honor get pushToken " + pushToken);

                            // 判断pushToken是否为空
                            if (!TextUtils.isEmpty(pushToken)) {
                                // PushToken保存到您的服务器上
                                if (mPushCallback != null) {
                                    mPushCallback.onTokenCallback(pushToken);
                                } else {
                                    LogUtils.e(TAG, "mPushCallback is null");
                                }
                            }
                        } catch (com.hihonor.push.sdk.common.data.ApiException e) {
                            LogUtils.e(TAG, "Honor get pushToken failed, " + e);
                            if (mPushCallback != null) {
                                TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
                                errorBean.setErrorCode(TUIOfflinePushConfig.REGISTER_TOKEN_ERROR_CODE);
                                errorBean.setErrorDescription("honor ApiException: " + e);
                                mPushCallback.onTokenErrorCallBack(errorBean);
                            } else {
                                LogUtils.e(TAG, "mPushCallback is null");
                            }
                        }
                    }
                }).start();
                break;
            case TUIOfflinePushConfig.BRAND_HUAWEI:
                // 华为离线推送，设置是否接收Push通知栏消息调用示例
                LogUtils.e(TAG, "注册华为离线推送");
                HmsMessaging.getInstance(context).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            LogUtils.i(TAG, "huawei turnOnPush Complete");
                        } else {
                            LogUtils.e(TAG, "huawei turnOnPush failed: ret=" + task.getException().getMessage());
                        }
                    }
                });

                // 华为离线推送
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // read from agconnect-services.json
                            String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
                            String token = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
                            LogUtils.i(TAG, "huawei get token:" + token);
                            if (!TextUtils.isEmpty(token)) {
                                if (mPushCallback != null) {
                                    mPushCallback.onTokenCallback(token);
                                } else {
                                    LogUtils.e(TAG, "mPushCallback is null");
                                }
                            }
                        } catch (ApiException e) {
                            LogUtils.e(TAG, "huawei get token failed, " + e);
                            if (mPushCallback != null) {
                                TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
                                errorBean.setErrorCode(TUIOfflinePushConfig.REGISTER_TOKEN_ERROR_CODE);
                                errorBean.setErrorDescription("huawei ApiException: " + e);
                                mPushCallback.onTokenErrorCallBack(errorBean);
                            } else {
                                LogUtils.e(TAG, "mPushCallback is null");
                            }
                        }
                    }
                }.start();
                break;
            case TUIOfflinePushConfig.BRAND_MEIZU:
                // 魅族离线推送
                LogUtils.e(TAG, "注册魅族离线推送");
                PushManager.register(context, PrivateConstants.meizuPushAppId, PrivateConstants.meizuPushAppKey);
                break;
            case TUIOfflinePushConfig.BRAND_OPPO:
                LogUtils.e(TAG, "注册OPPO离线推送");
                HeytapPushManager.init(context, false);

                LogUtils.e(">>>  isSupportPush = " + HeytapPushManager.isSupportPush(context));

                if (HeytapPushManager.isSupportPush(context)) {
                    // oppo离线推送
                    OPPOPushImpl oppo = new OPPOPushImpl();
                    oppo.createNotificationChannel(context);
                    HeytapPushManager.register(context, PrivateConstants.oppoPushAppKey, PrivateConstants.oppoPushAppSecret, oppo);

                    // OPPO 手机默认关闭通知，需要申请
                    HeytapPushManager.requestNotificationPermission();
                }
                break;
            case TUIOfflinePushConfig.BRAND_VIVO:
                LogUtils.e(TAG, "注册VIVO离线推送");
                // vivo离线推送
                PushClient.getInstance(context).initialize();

                LogUtils.i(TAG, "vivo support push: " + PushClient.getInstance(context).isSupport());
                PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
                    @Override
                    public void onStateChanged(int state) {
                        if (state == 0) {
                            String regId = PushClient.getInstance(context).getRegId();
                            LogUtils.i(TAG, "vivopush open vivo push success regId = " + regId);
                            if (mPushCallback != null) {
                                mPushCallback.onTokenCallback(regId);
                            } else {
                                LogUtils.e(TAG, "mPushCallback is null");
                            }
                        } else {
                            // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                            LogUtils.e(TAG, "vivopush open vivo push fail state = " + state);
                            if (mPushCallback != null) {
                                TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
                                errorBean.setErrorCode(state);
                                errorBean.setErrorDescription("vivo error code: " + String.valueOf(state));
                                mPushCallback.onTokenErrorCallBack(errorBean);
                            } else {
                                LogUtils.e(TAG, "mPushCallback is null");
                            }
                        }
                    }
                });
                break;
            default:
                if (isGoogleServiceSupport()) {
//                    FirebaseMessaging.getInstance().getToken()
//                            .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<String>() {
//                        @Override
//                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
//                            if (!task.isSuccessful()) {
//                                LogUtils.e(TAG, "getInstanceId failed exception = " + task.getException());
//                                if (mPushCallback != null) {
//                                    TUIOfflinePushErrorBean errorBean = new TUIOfflinePushErrorBean();
//                                    errorBean.setErrorCode(TUIOfflinePushConfig.REGISTER_TOKEN_ERROR_CODE);
//                                    errorBean.setErrorDescription("fcm exception: " + task.getException());
//                                    mPushCallback.onTokenErrorCallBack(errorBean);
//                                } else {
//                                    LogUtils.e(TAG, "mPushCallback is null");
//                                }
//                                return;
//                            }
//
//                            // Get new Instance ID token
//                            String token = task.getResult();
//                            LogUtils.i(TAG, "google fcm getToken = " + token);
//                            createPrivateNotification(context);
//                            if (mPushCallback != null) {
//                                mPushCallback.onTokenCallback(token);
//                            } else {
//                                LogUtils.e(TAG, "mPushCallback is null");
//                            }
//                        }
//                    });
                }
                break;
        }
    }

    public void setPushCallback(PushCallback callback) {
        mPushCallback = callback;
    }

    public boolean isGoogleServiceSupport() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(TUIOfflinePushConfig.getInstance().getContext());
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void createPrivateNotification(Context context) {
        if (context == null) {
            LogUtils.e(TAG, "createPrivateNotification context is null");
            return;
        }
        if (TUIOfflinePushConfig.getInstance().isAndroidPrivateRing() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            if (nm != null) {
                NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup("MyGroupId", "CustomGroup");
                nm.createNotificationChannelGroup(notificationChannelGroup);

                NotificationChannel notificationChannel =
                        new NotificationChannel(PrivateConstants.fcmPushChannelId, "CustomGroup", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setGroup("MyGroupId");
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                // "android.resource://com.tencent.qcloud.tim.tuikit/raw/private_ring"
                String soundUri = "android.resource://" + context.getPackageName() + "/raw/" + PrivateConstants.fcmPushChannelSoundName;
                notificationChannel.setSound(Uri.parse(soundUri), null);

                nm.createNotificationChannel(notificationChannel);
            }
        }
    }
}

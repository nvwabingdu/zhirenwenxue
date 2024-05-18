package cn.dreamfruits.yaoguo.module.OEMPush.utils;

import android.text.TextUtils;


import com.blankj.utilcode.util.LogUtils;

import cn.dreamfruits.yaoguo.module.OEMPush.TUIOfflinePushConfig;
import cn.dreamfruits.yaoguo.module.OEMPush.pushnative.PrivateConstants;
import cn.dreamfruits.yaoguo.module.OEMPush.pushnative.utils.BrandUtil;

public class TUIOfflinePushUtils {
    public static final String TAG = TUIOfflinePushUtils.class.getSimpleName();

    public static long json2TUIOfflinePushParamBean(TUIOfflinePushParamBean params) {
        long bussinessId;
        int deviceBrand = BrandUtil.getInstanceType();
        switch (deviceBrand) {
            case TUIOfflinePushConfig.BRAND_XIAOMI: // xiaomi
                String xiaomiPushBussinessId = params.getXiaomiPushBussinessId();
                if (TextUtils.isEmpty(xiaomiPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- xiaomiPushBussinessId is null");
                } else {
                    PrivateConstants.xiaomiPushBussinessId = Long.parseLong(xiaomiPushBussinessId);
                }
                String xiaomiPushAppId = params.getXiaomiPushAppId();
                if (TextUtils.isEmpty(xiaomiPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- xiaomiPushAppId is null");
                } else {
                    PrivateConstants.xiaomiPushAppId = xiaomiPushAppId;
                }
                String xiaomiPushAppKey = params.getXiaomiPushAppKey();
                if (TextUtils.isEmpty(xiaomiPushAppKey)) {
                    LogUtils.e(TAG, "registerPush-- xiaomiPushAppKey is null");
                } else {
                    PrivateConstants.xiaomiPushAppKey = xiaomiPushAppKey;
                }

                bussinessId = PrivateConstants.xiaomiPushBussinessId;
                break;
            case TUIOfflinePushConfig.BRAND_HONOR: // honor
                String honorPushBussinessId = params.getHonorPushBussinessId();
                if (TextUtils.isEmpty(honorPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- honorPushBussinessId is null");
                } else {
                    PrivateConstants.honorPushBussinessId = Long.parseLong(honorPushBussinessId);
                }

                bussinessId = PrivateConstants.honorPushBussinessId;
                break;
            case TUIOfflinePushConfig.BRAND_HUAWEI: // huawei
                String huaweiPushBussinessId = params.getHuaweiPushBussinessId();
                if (TextUtils.isEmpty(huaweiPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- huaweiPushBussinessId is null");
                } else {
                    PrivateConstants.huaweiPushBussinessId = Long.parseLong(huaweiPushBussinessId);
                }

                String huaweiBadgeClassName = params.getHuaweiBadgeClassName();
                if (TextUtils.isEmpty(huaweiBadgeClassName)) {
                    LogUtils.e(TAG, "registerPush-- huaweiBadgeClassName is null");
                } else {
                    PrivateConstants.huaweiBadgeClassName = huaweiBadgeClassName;
                }

                bussinessId = PrivateConstants.huaweiPushBussinessId;
                break;
            case TUIOfflinePushConfig.BRAND_MEIZU: // meizu
                String meizuPushBussinessId = params.getMeizuPushBussinessId();
                if (TextUtils.isEmpty(meizuPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- meizuPushBussinessId is null");
                } else {
                    PrivateConstants.meizuPushBussinessId = Long.parseLong(meizuPushBussinessId);
                }
                String meizuPushAppId = params.getMeizuPushAppId();
                if (TextUtils.isEmpty(meizuPushAppId)) {
                    LogUtils.e(TAG, "registerPush-- meizuPushAppId is null");
                } else {
                    PrivateConstants.meizuPushAppId = meizuPushAppId;
                }
                String meizuPushAppKey = params.getMeizuPushAppKey();
                if (TextUtils.isEmpty(meizuPushAppKey)) {
                    LogUtils.e(TAG, "registerPush-- meizuPushAppKey is null");
                } else {
                    PrivateConstants.meizuPushAppKey = meizuPushAppKey;
                }

                bussinessId = PrivateConstants.meizuPushBussinessId;
                break;
            case TUIOfflinePushConfig.BRAND_OPPO: // oppo
                String oppoPushBussinessId = params.getOppoPushBussinessId();
                if (TextUtils.isEmpty(oppoPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- oppoPushBussinessId is null");
                } else {
                    PrivateConstants.oppoPushBussinessId = Long.parseLong(oppoPushBussinessId);
                }
                String oppoPushAppKey = params.getOppoPushAppKey();
                if (TextUtils.isEmpty(oppoPushAppKey)) {
                    LogUtils.e(TAG, "registerPush-- oppoPushAppKey is null");
                } else {
                    PrivateConstants.oppoPushAppKey = oppoPushAppKey;
                }
                String oppoPushAppSecret = params.getOppoPushAppSecret();
                if (TextUtils.isEmpty(oppoPushAppSecret)) {
                    LogUtils.e(TAG, "registerPush-- oppoPushAppSecret is null");
                } else {
                    PrivateConstants.oppoPushAppSecret = oppoPushAppSecret;
                }

                bussinessId = PrivateConstants.oppoPushBussinessId;
                break;
            case TUIOfflinePushConfig.BRAND_VIVO: // vivo
                String vivoPushBussinessId = params.getVivoPushBussinessId();
                if (TextUtils.isEmpty(vivoPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- vivoPushBussinessId is null");
                } else {
                    PrivateConstants.vivoPushBussinessId = Long.parseLong(vivoPushBussinessId);
                }

                bussinessId = PrivateConstants.vivoPushBussinessId;
                break;
            default: // fcm
                String fcmPushBussinessId = params.getFcmPushBussinessId();
                if (TextUtils.isEmpty(fcmPushBussinessId)) {
                    LogUtils.e(TAG, "registerPush-- fcmPushBussinessId is null");
                } else {
                    PrivateConstants.fcmPushBussinessId = Long.parseLong(fcmPushBussinessId);
                }

                String fcmPushChannelId = params.getFcmPushChannelId();
                if (TextUtils.isEmpty(fcmPushChannelId)) {
                    LogUtils.e(TAG, "registerPush-- fcmPushChannelId is null");
                } else {
                    PrivateConstants.fcmPushChannelId = fcmPushChannelId;
                }

                String fcmPushChannelSoundName = params.getFcmPushChannelSoundName();
                if (TextUtils.isEmpty(fcmPushChannelSoundName)) {
                    LogUtils.e(TAG, "registerPush-- fcmPushChannelSoundName is null");
                } else {
                    PrivateConstants.fcmPushChannelSoundName = fcmPushChannelSoundName;
                }

                bussinessId = PrivateConstants.fcmPushBussinessId;
                break;
        }

        return bussinessId;
    }
}

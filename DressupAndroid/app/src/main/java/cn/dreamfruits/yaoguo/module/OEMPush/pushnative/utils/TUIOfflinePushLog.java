package cn.dreamfruits.yaoguo.module.OEMPush.pushnative.utils;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.imsdk.common.IMLog;

public class TUIOfflinePushLog extends IMLog {
    private static final String PRE = "TUIOfflinePush-";

    private static String mixTag(String tag) {
        return PRE + tag;
    }

    public static void v(String strTag, String strInfo) {
//        IMLog.v(mixTag(strTag), strInfo);

        LogUtils.v(strTag, strInfo);
    }

    public static void d(String strTag, String strInfo) {
//        IMLog.d(mixTag(strTag), strInfo);
        LogUtils.d(strTag, strInfo);
    }

    public static void i(String strTag, String strInfo) {
        LogUtils.i(strTag, strInfo);
    }

    public static void w(String strTag, String strInfo) {
        LogUtils.w(strTag, strInfo);
    }

    public static void e(String strTag, String strInfo) {
        LogUtils.e(strTag, strInfo);
    }
}

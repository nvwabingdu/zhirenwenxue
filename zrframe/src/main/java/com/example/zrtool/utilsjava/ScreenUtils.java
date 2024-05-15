package com.example.zrtool.utilsjava;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Method;

/**
 * @Author qiwangi
 * @Date 2023/8/17
 * @TIME 08:29
 */
public class ScreenUtils {
    /**
     * 获取顶部statusBar高度
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("tag", "Status height:" + height);
        return height;
    }


    /**
     * 获取底部navigationBar高度
     * @return
     */
    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("tag", "Navi height:" + height);
        return height;
    }


    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;

    }


    /**
     * 在页面未加载出来前，获取控件的宽高
     */
    public static int[] getViewWidthAndHeight(View view) {
        int[] viewW_H = new int[2];
        view.post(new Runnable() {
            @Override
            public void run() {
                viewW_H[0] = view.getWidth();//view宽度
                viewW_H[1] = view.getHeight();//view高度
            }
        });
        if (viewW_H[0] != 0) {
            return viewW_H;
        }
        return null;
    }


    /**
     * 获取屏幕宽度
     * 在页面未加载出来前，直接通过getWidth()获取的宽度为0，即使你在onResume()方法中执行这个方法也是一样。
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 设置屏幕常亮。
     * 需要 申请屏幕 WAKE_LOCK 唤醒锁 权限
     * <uses-permission android:name="android.permission.WAKE_LOCK" />
     */
    public static void setScreenBright(Activity mActivity) {
        mActivity.getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}

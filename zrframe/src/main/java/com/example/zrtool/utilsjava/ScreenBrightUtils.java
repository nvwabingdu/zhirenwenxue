package com.example.zrtool.utilsjava;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-20
 * Time: 18:40
 */
public class ScreenBrightUtils {

    public static void onCreateSet(Bundle savedInstanceState, Activity activity) {
        /*
         * 屏幕常亮需要 申请屏幕 WAKE_LOCK 唤醒锁 权限
         *  <uses-permission android:name="android.permission.WAKE_LOCK" />
         */
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//禁用触摸
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);//禁用Home等键，但会与其发生冲突，会导致程序无响应，待解决
//        activity.getWindow().setColorMode(ActivityInfo.COLOR_MODE_DEFAULT);//设置颜色模式，测试失败
        //申请权限
//        if (!Settings.System.canWrite(getApplicationContext())) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, 2);
//        }

        //亮度设置成手动，且设置较低亮度值5；当停止/取消后亮度设置成自动
//        customScreenBrightness();
    }


    /**
     *
     * @param activity
     */
    public static void onStopSet(Activity activity) {
        try{
            int screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC,activity);
            }
        }catch (Exception localException){
            localException.printStackTrace();
        }

    }

    /**
     * 用于销毁后恢复亮度
     * @param activity
     */
    public static void onDestroySet(Activity activity) {
        try{
            int screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC,activity);
            }
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }


    /**
     * * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     *
     */
    public static void customScreenBrightness(Activity activity) {
        try {
            int screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            // 获得当前屏幕亮度值 0--255
            int screenBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,activity);
            }
            setScreenBrightness(5,activity);//手动设置的亮度值
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置屏幕亮度模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     *
     * @param value
     */
    public static void setScreenMode(int value,Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    /**
     * 设置屏幕亮度
     *
     * @param value
     */
    public static void setScreenBrightness(float value,Activity activity) {
        Window mWindow = activity.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        float f = value / 255.0f;
        params.screenBrightness = f;
        mWindow.setAttributes(params);
        // 保存设置的屏幕亮度值
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }

}

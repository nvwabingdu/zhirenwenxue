package cn.dreamfruits.yaoguo.module.main.home.tool;

import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * @Author qiwangi
 * @Date 2023/7/17
 * @TIME 16:15
 */
public class StatuBar {
    /** 使用原生系统 API 来设置状态栏文字颜色 */
    public static void setStatusBarLightModeOrigin(Window window, boolean lightMode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        View decorView = window.getDecorView();
        int originVisibility = decorView.getSystemUiVisibility();
        // 亮色模式，使用黑色文字
        if (lightMode && (originVisibility & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == 0) {
            originVisibility = originVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        // 暗色模式，使用白色文字
        if (!lightMode && (originVisibility & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0) {
            originVisibility = originVisibility ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        decorView.setSystemUiVisibility(originVisibility);
    }
}

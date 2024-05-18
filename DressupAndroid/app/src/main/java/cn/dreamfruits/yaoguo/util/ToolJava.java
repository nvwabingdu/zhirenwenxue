package cn.dreamfruits.yaoguo.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsActivity;

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 19:53
 */
public class ToolJava {

    /**
     * 获取随机字符串
     * @param j
     * @return
     */
    public static String getRandomStr(int j) {
        char[] mapTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9'};
        // 取随机产生的随机码
        String strEnsure = "";
        // j代表j位随机码
        for (int i = 0; i < j; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }



        return strEnsure;
    }

    /**
     * 获取随机数字
     *
     * @param j
     * @return
     */
    public static String getRandomNuber(int j) {
        char[] mapTable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        // 取随机产生的随机码
        String strEnsure = "";
        // j代表j位随机码
        for (int i = 0; i < j; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }
        return strEnsure;
    }


    public static void main(String[] args) {
        System.out.println("生成的随机六位数验证码："+ToolJava.getRandomNuber(6));
        //System.out.println(RandomUtil.getRandomNuber(128));
        System.out.println("生成的随机六位字符串验证码："+ToolJava.getRandomStr(6));
    }


    /**
     * 使用方法：
     * // 将时间戳转换为"yyyy-MM-dd"格式的年月日字符串
     * String dateStr = ToolJava.timeStamp2Date(timeStamp, "yyyy-MM-dd");
     * @param timeStamp
     * @param format
     * @return
     */
    public static String timeStamp2Date(long timeStamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        List<String> list = new ArrayList<>();

        return sdf.format(new Date(timeStamp));
    }


    /**
     * 保留一位小数  用于数字显示规则 大于9999的 保留一位小数
     * @return
     */
    public static String getDecimalFormatOne(int tempNum) {
        try {
            DecimalFormat df = new DecimalFormat("#.0");
            String result = df.format(tempNum/10000d);
            return result;
        }catch (Exception e){
            return tempNum+"";
        }
    }
}


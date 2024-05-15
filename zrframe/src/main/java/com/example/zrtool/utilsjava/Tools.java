package com.example.zrtool.utilsjava;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;


public class Tools {
    /**
     * 赋值文本到剪贴板
     * @param context
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
        Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义有时间的吐司
     * @param activity
     * @param word
     * @param time
     */
    public static void showToast(final Activity activity, final String word, final long time) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }

    /**
     * 显示Log 文本
     * @param content
     */
    public static void showLog(String content) {
        /*1、Log.v 黑色，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v("","")；
        2、Log.d 蓝色，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择；
        3、Log.i 绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息；
        4、Log.w 橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息；
        5、Log.e 红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了；*/
        Log.e("tag", content);
    }

    /**
     * 截取字符串
     * @param text   文本
     * @param length 长度大于多少的
     * @param size   截取多少长度
     * @return
     */
    public static String subTextSize(String text, int length, int size) {
        if (text.length() > length) {
            return text.substring(0, size);
        }
        return text;
    }

    /**
     * 保留指定位数的小数
     * @param number 传入的数
     * @param size   保留的位数
     * @return
     */
    public static String keepNumDecimals(double number, int size) {
        StringBuffer strTemp = new StringBuffer("######0.0");
        for (int i = 1; i < size; i++) {
            strTemp.append("0");
        }
        DecimalFormat df = new DecimalFormat(strTemp.toString());
        return df.format(number);
    }

    /**
     * 取消文本中的flag   比如"-"
     * @param str
     * @param FLAG
     * @return
     */
    public static String cancelSomeFlag(String str, String FLAG) {
        String[] tList = str.split(FLAG);
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < tList.length; i++) {
            strbuf.append(tList[i]);
        }
        return strbuf.toString().trim();
    }

    /**
     * 四舍六入五成双
     * @param Number
     * @return
     */
    public static Double getFourFiveSix(Double Number) {
        try {
            DecimalFormat df = new DecimalFormat("#.0000");
            //保留4位小数332.3453
            String str = df.format(Number);
            //舍去最后一位 332.345
            String newStr = str.substring(0, str.length() - 1);
            //截取需要的
            String needStr = str.substring(0, str.length() - 2);
            //截取出最后一位数字  5
            int temp = Integer.parseInt(newStr.substring(newStr.length() - 1, newStr.length()));
            int temp2 = Integer.parseInt(newStr.substring(newStr.length() - 2, newStr.length() - 1));
//            Log.e("4dadasad", "temp=====>" + temp);
//            Log.e("5dadasad", "temp2=====>" + temp2);
            //转换数字
            double dNum = Double.parseDouble(needStr);
            //判断
            if (temp >= 6) {
                dNum = dNum + 0.01;

            } else if (temp <= 4) {
                //舍去
            } else if (temp == 5) {
                if (temp2 % 2 == 1) {//5前为奇 进1
                    dNum = dNum + 0.01;
                } else {//5前为偶 舍去
                    //舍去
                }
            }
//            Log.e("adsda", str);
            return dNum;
        } catch (Exception e) {
            return Number;
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context
     * @param dpValue
     * @return
     */
    public static int getdpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int getpxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int winwidth = dm.widthPixels;
        return winwidth;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int winheight = dm.heightPixels;
        return winheight;
    }

    /**
     * layout布局距离屏幕上下左右的距离
     * @param context     上下文
     * @param dpValue     dp值
     * @param mLayout     需要改变的layout
     * @param orientation 上下左右哪个方向
     */
    private void setViewMargin(Context context, float dpValue, View mLayout, String orientation) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int params = (int) (dpValue * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (orientation) {
            case "top":
                layoutParams1.setMargins(0, params, 0, 0);//4个参数按顺序分别是左上右下
                break;
            case "left":
                layoutParams1.setMargins(params, 0, 0, 0);//4个参数按顺序分别是左上右下
                break;
            case "right":
                layoutParams1.setMargins(0, 0, params, 0);//4个参数按顺序分别是左上右下
                break;
            case "down":
                layoutParams1.setMargins(0, 0, 0, params);//4个参数按顺序分别是左上右下
                break;
        }
        mLayout.setLayoutParams(layoutParams1);
    }

    /**
     * 方法不全，需要重写 以后考虑特殊封装
     * @param list
     * @return
     */
    public static ArrayList<Object> getOrderbyList(ArrayList<Object> list) {
        //排序
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
//                    if (o1.getNum() > o2.getNum()) {
//                        return -1;
//                    }
//                    if (o1.getNum() == o2.getNum()) {
//                        return 0;
//                    }
                    return 1;
                } catch (Exception e) {
                    return 0;
                }
            }
        });
        return list;
    }

    /**
     * 提取map集合的key的值 并转换成String
     * @param TempMap
     * @return
     */
    public static ArrayList<String> getKetSetList(Map TempMap) {
        Set<String> keyText = TempMap.keySet();
        ArrayList<String> keyList = new ArrayList<>();
        for (String s : keyText) {
            keyList.add(s);
        }
        return keyList;
    }

    /**
     * 获取MD5
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        //16位加密，从第9位到25位
        return md5StrBuff.toString().toUpperCase();
    }

    /**
     * 设置控件的背景drawable
     * @param context
     * @param mView
     * @param resources
     * @return
     */
    public boolean setDrawable(Context context, View mView, int resources) {
        Drawable drawable = context.getResources().getDrawable(resources);
        mView.setBackground(drawable);
        return true;
    }

    /**
     * @param Activity showSnackbar  还需要封装
     */
    private void showSnackbar(Activity Activity,View idView) {
        if(Activity!=null){
            Snackbar.make(idView, "标题", Snackbar.LENGTH_LONG)
                    .setAction("点击事件", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(
                                    Activity,
                                    "Toast",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).setDuration(1000).show();
        }
    }



}

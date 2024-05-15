package com.example.zrwenxue.moudel.main.word;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.example.zrtool.utilsjava.Tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-11-27
 * Time: 14:23
 */
public class WordNode {
    private static Handler handler;

    /**
     * @param context 上下文
     * @param webView webview
     * @param rawID   词典地址  R.raw.jmyhcd
     */
    public static void startThread(Context context, WebView webView, int rawID, String ZM, int num, int flag) {
        //设置一个Handler用于更新查找后的数据  并更新webview
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // 加载并显示HTML代码
                if (msg.what == 77) {
                    if (msg.obj.toString().equals("")) {
                        Tools.showToast((Activity) context, "暂无收录……", 2000);
                    } else {
                        webView.loadDataWithBaseURL(null, msg.obj.toString(), "text/html", "utf-8", null);
                    }
                    Singleton.getInstance().wordFlag = true;
                    return;
                }
            }
        };

        //webview线程需要在activity中运行，因此需要下面方式写
        webView.post(new Runnable() {
            @Override
            public void run() {
                getHtmlDate(context, rawID, ZM, num, flag);//开始查找字符串
            }
        });


    }


    /**
     * @param context 上下文
     * @param rawID   词典地址  R.raw.jmyhcd
     */
    private static void getHtmlDate(Context context, int rawID, String ZM, int num, int flag) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            arrayList = getOneLineTxt(context, context, rawID, ZM, num, flag);//此行用于返回查找findW返回的数据
        } catch (Exception e) {
            Tools.showToast((Activity) context, e.toString(), 2000);
        }
        String temp = "";
        for (String str : arrayList) {
            temp = temp + str + "<br>";//加入某词典
        }

        //handler发送字符串
        Message tempMsg = handler.obtainMessage();
        tempMsg.what = 77;
        tempMsg.obj = temp;
        handler.handleMessage(tempMsg);
    }


    /**
     * @param mcontext 上下文
     * @param rawID    词典地址  例如：R.raw.jmyhcd
     * @return 获取一行的数据
     * @throws Exception
     */
    public static ArrayList<String> getOneLineTxt(Context context, Context mcontext, int rawID, String ZM, int num, int flag) throws Exception {
        ArrayList<String> arrayList = new ArrayList<>();

        // 创建流
//        BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(new FileInputStream(""), "UTF-8"));

        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(mcontext.getResources().openRawResource(rawID), "UTF-8"));


        if (flag < 6) {//说明是查询纯单词

            if (!ZM.equals("")) {//输入了字母
                //读取流
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.substring(0, 1).equals(ZM)) {//首字母是输入字母
                        arrayList.add(line); //就添加此行
                    }
                }
            } else {
                Tools.showToast((Activity) context, "请输入一个字母", 2000);
            }


        } else {//单词篇

            if (!ZM.equals("")) {//输入了字母
                //读取流
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.substring(0, 1).equals(ZM)) {//首字母是输入字母
                        arrayList.add(line.substring(line.indexOf("<"), line.lastIndexOf(">") + 1));
                    }
                }
            } else {
                Tools.showToast((Activity) context, "请输入一个字母", 2000);
            }

        }


        bufferedReader.close();//关闭流
        return arrayList;
    }


    //单词，还是有解释的
    private static String AorB(String line) {
        if (line.equals("<")) {
            line = line.substring(line.indexOf("<"), line.lastIndexOf(">") + 1);
        } else {

        }
        return line;
    }


}

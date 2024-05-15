package com.example.zrwenxue.moudel.main.home.eng;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.WebView;

import androidx.annotation.NonNull;


import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-11-27
 * Time: 14:23
 */
public class EngNode {
    private static Handler handler;

    /**
     * @param context 上下文
     * @param webView webview
     * @param rawID   词典地址  R.raw.jmyhcd
     * @param name    词典名称   cgccd
     * @param findW   love
     */
    public static void startThread(Context context, WebView webView, int rawID, String name, String findW, int num) {
        //接受handler修改UI
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 77) {
                    if (msg.obj.toString().equals("")) {
//                        Tools.showToast(context.this, "暂无收录……", 2000);
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
                getHtmlDate(context, name, findW, rawID, num);//开始查找字符串
            }
        });
    }

    /**
     * mine_用于复写
     * @param context
     * @param webView
     * @param rawID
     * @param name
     * @param findW
     * @param num
     * @param flag
     * @return
     */
    public static void startThread(Context context, WebView webView, int rawID, String name, String findW, int num,boolean flag) {
        //接受handler修改UI
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 77) {
                    if (msg.obj.toString().equals("")) {
                        MyStatic.showDialog(context, "非常抱歉，你输入的单词在本词典暂无收录……");
                    } else {
                        String s = msg.obj.toString();
                        webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);

                        //装入共享参数
                        if(flag&&!s.equals("")){
                            String ss=Singleton.getInstance().getSharedPreferences(context,"mine_word_old",true);
                            MyStatic.showLog("装入共享参数--==="+ss);

                            Singleton.getInstance().setSharedPreferences(context,"mine_word_old",ss+s);
                        }
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
                getHtmlDate(context, name, findW, rawID, num);//开始查找字符串
            }
        });
    }

    /**
     * @param context 上下文
     * @param name    所查找的词典
     * @param findW   love
     * @param rawID   词典地址  R.raw.jmyhcd
     */
    private static void getHtmlDate(Context context, String name, String findW, int rawID, int num) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            arrayList = getOneLineTxt(findW, context, rawID, num);//此行用于返回查找findW返回的数据
        } catch (Exception e) {
//            Tools.showToast((Activity) context, e.toString(), 2000);
        }
        String temp = "";
        for (String str : arrayList) {
            temp = temp + str + "<br>" + "<p align=\"right\">" + MyStatic.choice(name) + "</p>";//加入某词典
            temp=temp.replace("\\n","");//去掉多于的\n
            temp=temp.replace("<img src=\"file://tag.gif\">","");//去掉多于的图片链接
            temp=temp.replace("<hr>","");//去掉多于的水平线
        }

        //handler发送字符串
        Message tempMsg = handler.obtainMessage();
        tempMsg.what = 77;
        tempMsg.obj = temp;
        handler.handleMessage(tempMsg);
    }


    /**
     * @param findW    love
     * @param mcontext 上下文
     * @param rawID    词典地址  例如：R.raw.jmyhcd
     * @return 获取一行的数据
     * @throws Exception
     */
    public static ArrayList<String> getOneLineTxt(String findW, Context mcontext, int rawID, int num) throws Exception {
        ArrayList<String> arrayList = new ArrayList<>();

        // 创建流
//        BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(new FileInputStream(""), "UTF-8"));

        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(mcontext.getResources().openRawResource(rawID), "UTF-8"));
        //读取流
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (arrayList.size() < num) {//只查找5条数据
                if (rawID == R.raw.cgccd) {//特殊处理  love <br>-------/n   这样的形式
                    if (regex(line, findW)) {
                        arrayList.add(line);
                    }
                } else {
                    if (regex(line, findW)) {//正常处理
                        arrayList.add(line.substring(line.indexOf("<"), line.lastIndexOf(">") + 1));
                    }
                }

            } else {
                break;
            }
        }
        bufferedReader.close();//关闭流
        return arrayList;
    }


    /**
     * @param line  读取的某行
     * @param findW 需要查找的某个单词 love
     * @return
     */
    public static boolean regex(String line, String findW) {
        if (line.contains("<")) {//如果包含这个标记，说明是一个单词的一行开始
            if (line.startsWith(findW)) {//是从这个单词开始的
                return true;
            }
        }
        return false;
    }

}

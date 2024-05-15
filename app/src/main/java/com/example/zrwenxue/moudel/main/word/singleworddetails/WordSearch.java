package com.example.zrwenxue.moudel.main.word.singleworddetails;

import android.app.Activity;
import android.content.Context;

import com.example.newzr.R;
import com.example.zrtool.utilsjava.Tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-28
 * Time: 13:02
 */
public class WordSearch {
    /**
     * @param context 上下文
     * @param name    所查找的词典
     * @param findW   love
     * @param rawID   词典地址  R.raw.jmyhcd
     */
    public static String getHtmlDate(Context context, String name, String findW, int rawID, int num) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            arrayList = getOneLineTxt(findW, context, rawID, num);//此行用于返回查找findW返回的数据
        } catch (Exception e) {
            Tools.showToast((Activity) context, e.toString(), 2000);
        }
        String temp = "";
        for (String str : arrayList) {
            temp = temp + str;
            temp = temp.replace("\\n", "");//去掉多于的\n
            temp = temp.replace("<img src=\"file://tag.gif\">", "");//去掉多于的图片链接
            temp = temp.replace("<hr>", "");//去掉多于的水平线
        }


        return temp;
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

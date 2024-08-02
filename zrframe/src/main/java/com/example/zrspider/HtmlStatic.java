package com.example.zrspider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-11
 * Time: 18:08
 */
public class HtmlStatic {
    /**
     * 通过网站域名URL获取该网站的源码
     *
     * @param url
     * @return String
     * @throws Exception
     */
    public static String getURLSource(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();  //通过输入流获取html二进制数据
        byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
        String htmlSource = new String(data);
        return htmlSource;
    }

    /** */
    /**
     * 把二进制流转化为byte字节数组
     *
     * @param instream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        instream.close();
        return outStream.toByteArray();
    }

    /**
     * @param str 取消文本html的标记对比如<p></p>  <font></font>
     * @return
     */
    public static String delHtmlTag(String str) {
        String tempStr = str;
        int number = 0;
        try {
            while (true) {
                number++;
                if (number > str.length()) {//循环次数超过文本长度，必然出现死循环了，这里需要跳出
                    break;
                }
                if (str.contains("<") && str.contains(">")) {
                    tempStr = str.replace(str.substring(str.indexOf("<"), str.indexOf(">") + 1), "");
                } else {
                    break;
                }
                str = tempStr;
            }
            return tempStr;
        } catch (Exception e) {
            return tempStr + "(此段或有错误)";
        }
    }


    /**
     * 取消固定的标记对  保留中间内容  只取消<div> 或 <script/>
     *
     * @param str
     * @param arags meta div script 删除类似的标记<mete></div><script>
     * @return
     */
    public static String delHtmlTag(String str, String... arags) {
        try {
            //是否包含如 div mete script  因为要删除这些
            for (int i = 0; i < arags.length; i++) {
                if (str.contains(arags[i])) {
                    //第一步替换所有结束标记   如：</div>
                    str = str.replace("</" + arags[i] + ">", "");
                    //第二步，替换所有开始标记  如：<div adadadada>
                    while (true) {
                        if (str.contains("<" + arags[i])) {
                            str = subStr(str,"<" + arags[i], ">");
                        } else {
                            break;
                        }
                    }
                }
            }
            return setH2(delSomeTag(setHttps(str)));
        } catch (Exception e) {
            return str + "(此段或有错误)";
        }
    }


    /**
     * 示例：   <in><div 986829033112121></div>
     * 保证截取的字符串是第一个先  第二个后   避免角标越界
     *
     * @param T1 <div
     * @param T2 >
     * @return <in></div>
     */
    public static String subStr(String content, String T1, String T2) {
        String first;
        //第一步先把之前的截取了  这里就是<in>
        first = content.substring(0, content.indexOf(T1));
        //第二步开始截取
        content=content.replace(first, "");//置空first
        content = first + content.replace(content.substring(content.indexOf(T1), content.indexOf(T2)+1), "");
        return content;
    }

    /**
     *
     * @param content      1111<in><div 986829033112121></div>2222
     * @param T1   <in>
     * @param T2   </div>
     * @param tag   1    返回  <div 986829033112121>
     * @param tag   2    返回  <in><div 986829033112121></div>
     * @param tag   3    返回  11112222
     * @param tag   4    返回
     * @return
     */
    public static String subStr(String content, String T1, String T2,int tag) {
        if(!content.contains(T1)||!content.contains(T2)) {//不包含T1或者T2 就直接退出。
            return content;
        }

        /**
         * 这里表示把传进来的文本切分为两段 1111<in><div 986829033112121></div>2222     因为有可能包含多个t1或者t2的情况，这里只针对第一个t1和紧接着的第二个T2做处理
         * content=1111<in><div 986829033112121></div>
         * endStr=2222
         */
        String tempStr="";
        String endStr=content.substring(content.indexOf(T2)+T2.length(),content.length());
        content=content.substring(0,content.indexOf(T2)+T2.length());

        switch (tag) {
            case 1:
                tempStr=content.substring(content.indexOf(T1)+T1.length(),content.indexOf(T2));
                return tempStr;
            case 2:
                tempStr=content.substring(content.indexOf(T1)+T1.length(),content.indexOf(T2));
                return T1+tempStr+T2;
            case 3:
                tempStr=content.substring(content.indexOf(T1)+T1.length(),content.indexOf(T2));
                tempStr=content.replace(T1+tempStr+T2, "");
                return tempStr+endStr;
            case 4: return "";
            default:return content+"(此段或有错误)";
        }
    }

    /**
     * 用于图片设置https:
     * 插入https:    用于有些图片无法显示的问题
     * @return
     */
    public static String setHttps(String content) {
        String[] tempArray = content.split("src='");
        String tempStr = "";
        if (tempArray.length <= 0) {
            return content;
        }

        for (int i = 0; i < tempArray.length; i++) {
            tempStr = tempStr + tempArray[i] + "src='https:";
        }

        return tempStr.substring(0,tempStr.length()-"src='https:".length());
    }


    /**
     * 删除var isCrawler = 0;  类似这种
     * @return
     */
    public static String delSomeTag(String content){
        if(content.contains("var isCrawler = 0;")){
            content=content.replace("var isCrawler = 0;","");
            return content;
        }if(content.contains("var isCrawler = 1;")){
            content=content.replace("var isCrawler = 1;","");
            return content;
        }if(content.contains("var isCrawler = 2;")){
            content=content.replace("var isCrawler = 2;","");
            return content;
        }
        if(content.contains("var isCrawler = 3;")){
            content=content.replace("var isCrawler = 3;","");
            return content;
        }
            return content;
    }

    /**
     * 把H1 改为 H3  感觉太大了
     * @return
     */
    public static String setH2(String content){
        return content.replace("h1","h2");
    }

    /**
     * 通过数字返回星级的文本
     * @param num
     * @return
     */
    public static String getStarTEXT(int num){
        switch (num){
            case 0:
                return "";
            case 1:
                return "★☆☆☆☆";
            case 2:
                return "★★☆☆☆";
            case 3:
                return "★★★☆☆";
            case 4:
                return "★★★★☆";
            case 5:
                return "★★★★★";
        }
        return "";
    }


    /**
     * 删除某些特定标签及其内部文本 保留剩余文本
     * @param content  "1111111<!-- 222222222222222 -->3333333<!-- 444444444444 -->55555555"
     * @param t1    <!--
     * @param t2    -->
     */
    public  String delTag(String content,String t1,String t2){
        String tempstr="";
        String[] strs=content.split(t2);
        for(int i=0;i<strs.length;i++){
            if(strs[i].contains(t1)){

                tempstr=tempstr+strs[i].substring(0,strs[i].indexOf(t1));
            }else {
                tempstr=tempstr+strs[i];
            }


        }

        return tempstr;

    }

}

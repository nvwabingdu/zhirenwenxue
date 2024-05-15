package com.example.zrprint;

public class JavaStateMethod {
    /**
     * 截取两端文字中间的
     *
     * @param str       <h3>我是标题</h3>
     * @param startTEXT <h3>
     * @param endTEXT   </h3>
     * @return 我是标题
     */
    public static String getSubStr(String str, String startTEXT, String endTEXT) {
        try {
            return str.substring(str.indexOf(startTEXT) + startTEXT.length(), str.indexOf(endTEXT));
        } catch (Exception e) {
            return "";
        }

    }




}
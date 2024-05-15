package com.example.zrwenxue.moudel.main.word;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.newzr.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-10-13
 * Time: 11:25
 */
public class MyStatic {
    /**
     * @param a 三元运算符比较大小
     * @param b
     * @param c
     * @return
     */
    public static int three_math(int a, int b, int c) {
        a = a > b ? a : b;
        a = a > c ? a : c;
        return a;
    }

    /**
     * @param arr 冒泡排序
     * @return
     */
    public static String maoPao(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    //方法一：通用
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    //方法二：换成二进制然后在进行（整型）
//                  arr[j]  = arr[j] ^ arr[j+1];
//                  arr[j+1]= arr[j] ^ arr[j+1];
//                  arr[j]  = arr[j] ^ arr[j+1];
                    //方法三：  数字型
//                  arr[j] = arr[j] + arr[j+1];
//                  arr[j+1] = arr[j] -arr[j+1];
//                  arr[j] = arr[j] -arr[j+1];
                }
            }
        }
        return Arrays.toString(arr);
    }

    /**
     * @param arr 二位数组遍历
     */
    public static void twoforeach(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                Log.d("arr.length", arr.length + "");
                Log.d("arr[].length", arr[i].length + "");
            }
        }
    }

    /**
     * @return 二分查找方法
     * 方法一：直接调用BinarySearch()二分查找方法
     */

    public static int halfSearch() {
        int[] arr = new int[]{45, 78, 79, 102, 123, 145, 165, 178, 198, 255, 320};
        Log.e("binarySearch", Arrays.binarySearch(arr, 78) + "");
        Log.e("binarySearch", arr[Arrays.binarySearch(arr, 78)] + "");
        return 0;
    }

    ;

    /**
     * @param arr 方法二：自己手写
     * @param key
     * @return
     */
    public static int halfSearsh(int[] arr, int key) {
        int min, max, mid;
        min = 0;
        max = arr.length - 1;
        mid = (min + max) / 2;
        while (arr[mid] != key) {
            if (key > arr[mid]) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
            mid = (min + max) / 2;
            if (min > max) {
                return -1;
            }
        }
        return mid;
    }


    /**
     * 返回包含b字符串的个数
     *
     * @param a qqq$33333$333$444
     * @param b $
     * @return 3个
     */
    public static int getAContainsBSum(String a, String b) {
        return (a.length() - a.replace(b, "").length()) / b.length();
    }


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


    /**
     * 设置替换比如
     *  
     * @param line    1321《2》45232《3》
     * @param a       《2》
     * @param content 新的数据
     * @param b       《3》
     * @return 《2》新的数据《3》
     */
    public String setLine(String line, String a, String content, String b) {

        // TODO: 2022-05-01 有问题 
        return line.replace(a + getSubStr(line, a, b) + b, a + content + b);
    }

    /**
     * 返回处理后截取的文字段  《词根词缀词典》  用于后面部分的操作
     *
     * @param cgccd_Line accelerate    <br>陪伴(ac+company伙伴→陪伴)[ab-,ac-,ad-,af-,ag-,an-,ap-,ar-,as-,at-等加在同辅音字母的词根前,表示"一再"等加强意]\n<br>加速器[celer=quick,speed,表示"快,速"]\n<br>v.使加速,使增速,促进vi.加快,增加He decided to accelerate his output.他决定增加产量.\n
     * @return [单词 ,  第一段 ,  第二段……]的数组  很方便操作了
     */
    public static String[] fix_cgccd(String cgccd_Line) {
        try {
            String[] splitArray = new String[0];
            if (cgccd_Line.contains("<br>")) {//是否包含这个标记
                splitArray = cgccd_Line.split("<br>");//通过这个标记拆分字符串
            }

            if (splitArray.length != 0) {//长度不为0的话 开始处理
                for (int i = 0; i < splitArray.length; i++) {

                    splitArray[i] = splitArray[i].trim();//去掉空格

                    if (splitArray[i].contains("<br>")) {
                        splitArray[i] = splitArray[i].replace("<br>", "");//去掉<br>
                    } else if (splitArray[i].contains("\\n")) {
                        splitArray[i] = splitArray[i].replace("\\n", "");//去掉 \n
                    }

                }
            /*System.out.print(Arrays.toString(splitArray));//打印看看
            return Arrays.toString(splitArray);//返回数组字符串*/
                return splitArray;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 主要截取音标   《柯林斯高阶双解词典》
     *
     * @param klsgjsjcdLine 柯林斯高阶双解词典的一行
     * @return [vertical   [vɜ:rtikə'l] ]  结果
     */
    public static String[] fix_klsgjsjcd_one(String klsgjsjcdLine) {
        String[] tempArray = new String[2];
        try {
            if (klsgjsjcdLine.contains("[")) {  //是否包含[    包含说明有音标
                if (klsgjsjcdLine.contains("<br>")) {//是否包含<br> 包含说明是单词
                    tempArray[0] = klsgjsjcdLine.substring(0, klsgjsjcdLine.indexOf("<br>")).trim();// 截取单词并存入数组第一个角标
                    tempArray[1] = "[" + getSubStr(klsgjsjcdLine, "[", "]") + "]";// 截取音标
                    //开始音标处理
                    if (!tempArray[1].contains(" ")) {//处理1：判断单词去掉前后空格是否还有空格  有空格说明在中间 说明是短语
                        if (tempArray[1].contains("<u>")) {//处理2：包含<u> </u>  这个是长音？
                            //todo 可以做处理
                            //方式1
                            tempArray[1] = tempArray[1].replace("<u>", "");
                            tempArray[1] = tempArray[1].replace("</u>", ":");
                            //方式2
                        }
                        if (tempArray[1].contains("<sup>")) {//处理3：包含<sup>  上标？
                            //todo 可以做处理
                            //方式1
                            tempArray[1] = tempArray[1].replace("<sup>", "");
                            tempArray[1] = tempArray[1].replace("</sup>", "'");
                        }
                        //处理3：去掉HTML标记
                        tempArray[1] = HtmlStatic.delHtmlTag(tempArray[1]);
                    }

                    return tempArray;
                }
            }
            //其他情况返回空
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 《柯林斯高阶双解词典》 截取不规则词性部分
     *
     * @param klsgjsjcdLine 一行数据
     * @return apologize   [apologizes, apologizing, apologized]
     */
    public static String fix_klsgjsjcd_two(String klsgjsjcdLine) {
        String[] tempArray = new String[2];
        if (klsgjsjcdLine.contains("<br>")) {//包含<br> 包含说明是单词
            tempArray[0] = klsgjsjcdLine.substring(0, klsgjsjcdLine.indexOf("<br>")).trim();// 截取单词并存入数组第一个角标
            if (klsgjsjcdLine.contains("#FFCC00")) {
                ArrayList<String> tempList = new ArrayList<>();
                int times = getAContainsBSum(klsgjsjcdLine, "FFCC00");
                for (int i = 0; i < times; i++) {
                    klsgjsjcdLine = klsgjsjcdLine.substring(klsgjsjcdLine.indexOf("#FFCC00"), klsgjsjcdLine.length());//截取后面一截
                    String temp = klsgjsjcdLine.substring(klsgjsjcdLine.indexOf("FFCC00") + 8, klsgjsjcdLine.indexOf("</font>"));//截取中间需要的
                    tempList.add(HtmlStatic.delHtmlTag(temp));
                    //笨方法
                    klsgjsjcdLine = klsgjsjcdLine.replace("FFCC00\">" + temp + "</font>", "");
                }
                //todo
                if (tempList.size() != 0) {
                    tempArray[1] = tempList.toString();
                    return tempArray[0] + "   " + tempArray[1];
                }
            }
        }
        return "";
    }

    /**
     * 《柯林斯高阶双解词典》 提取类型标签
     *
     * @param klsgjsjcdLine
     * @return abandon   CET4
     */
    public static String fix_klsgjsjcd_three(String klsgjsjcdLine) {
        String[] tempArray = new String[2];
        if (klsgjsjcdLine.contains("<br>")) {//包含<br> 包含说明是单词
            tempArray[0] = klsgjsjcdLine.substring(0, klsgjsjcdLine.indexOf("<br>")).trim();// 截取单词并存入数组第一个角标
            if (klsgjsjcdLine.contains("<font color=grape>")) {
                klsgjsjcdLine = klsgjsjcdLine.substring(klsgjsjcdLine.indexOf("font color=grape>"), klsgjsjcdLine.length());//截取后面一截
                tempArray[1] = getSubStr(klsgjsjcdLine, "font color=grape>", "<");//截取中间需要的
            }
            return tempArray[0] + "   " + tempArray[1];
        }
        return "";
    }

    /**
     * 《简明英汉词典》
     *
     * @param str 词典的一行 比较多就不复制了
     */
    public static String[] fix_jmyhcd(String str) {
        if (!getSubStr(str, "<h3>", "</h3>").trim().equals(" ")) {
            str = delTheTagContent(str, "<font color=\"Navy\">", "</font>");//此方法有介绍
            String[] tempArray;
            if (str.contains("<font color=\"DarkMagenta\">")) {
                tempArray = str.split("<font color=\"DarkMagenta\">");//按此字符串拆分，方便操作
                tempArray[0] = getSubStr(str, "<h3>", "</h3>");//第一个数组位置装单词
                for (int i = 1; i < tempArray.length; i++) {
                    tempArray[i] = HtmlStatic.delHtmlTag(tempArray[i]);// 从1开始
                    if (tempArray[i].contains("&nbsp")) {
                        tempArray[i] = tempArray[i].replace("&nbsp", "");
                    }
                    if (tempArray[i].contains("\\n")) {
                        tempArray[i] = tempArray[i].replace("\\n", "");
                    }
                }
            } else {
                tempArray = new String[2];
                tempArray[0] = getSubStr(str, "<h3>", "</h3>");//第一个数组位置装单词
                tempArray[1] = getSubStr(str, "</h3>", "<br>");
            }

            return tempArray;
            /*System.out.println(Arrays.toString(tempArray));// 试着打印出来*/
        }
        return null;
    }

    /**
     * 循环删掉某一种标记和其内容
     *
     * @param tempStr 123<font color="Navy">&nbsp&nbsp&nbsp The girl has a good look.</font>你好  包含这种标记对的
     * @param tag1    <font color="Navy">
     * @param tag2    </font>
     * @return 123你好
     */
    public static String delTheTagContent(String tempStr, String tag1, String tag2) {
        try {
            if (tempStr.contains(tag1)) {
                String temp = "";
                String temp2 = "";
                while (true) {
                    if (tempStr.contains(tag1)) {
                        temp = tempStr.substring(0, tempStr.indexOf(tag1));
                        temp2 = tempStr.substring(tempStr.indexOf(tag1), tempStr.length());
                        //getsubstr意为提取中间变化的数据，tag1+变化数据+tag2就是需要去除的某种数据格式
                        tempStr = temp + temp2.replace(tag1 + getSubStr(temp2, tag1, tag2) + tag2, "");
                    } else {
                        break;
                    }
                }
                return tempStr;
            }
            return tempStr;
        } catch (Exception e) {
            return tempStr;
        }
    }

    /**
     * 设置RecyclerView 类似于 ListView
     *
     * @param obj          RecyclerView.Adapter  适配器的实例
     * @param recyclerView recyclerView控件
     * @param context      上下文
     * @param flag         true 纵向列表   false横向列表
     */
    public static void setRecyclerView(Object obj, RecyclerView recyclerView, Context context, boolean flag) {
//        (RecyclerView.Adapter) obj adapter = new (RecyclerView.Adapter)obj(context, arrayList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        if (flag) {
            manager.setOrientation(LinearLayoutManager.VERTICAL);//纵向
        } else {
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);//横向
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter((RecyclerView.Adapter) obj);

//        kotlin版本：
//        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        adapter = RvAdapter()
//        recyclerView.adapter = adapter
//        adapter.setData(data)
    }

    /**
     * 设置RecyclerView 类似于 Gridview
     *
     * @param obj          RecyclerView.Adapter  适配器的实例
     * @param recyclerView recyclerView控件
     * @param context      上下文
     * @param itemNumber   一行多少个
     * @param flag         true纵向列表(一般用这个)  false横向列表
     */
    public static void setRecyclerViewGridView(Object obj, RecyclerView recyclerView, Context context, int itemNumber, boolean flag) {
        //构造函数第二个参数为一行有多少个item  item宽度要设置为match
        GridLayoutManager manager = new GridLayoutManager(context, itemNumber);
        if (flag) {
            manager.setOrientation(GridLayoutManager.VERTICAL);//纵向
        } else {
            manager.setOrientation(GridLayoutManager.HORIZONTAL);//横向
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter((RecyclerView.Adapter) obj);
    }


    /**
     * 设置RecyclerView 类似于 瀑布流
     *
     * @param obj          适配器实例
     * @param recyclerView recyclerView控件
     * @param context      上下文
     * @param itemNumber   和GridView一样的参数  表示行数 或者 列数
     * @param flag         true纵向列表  false横向列表
     */
    public static void setRecyclerViewStaggeredGridLayoutManager(Object obj, RecyclerView recyclerView, Context context, int itemNumber, boolean flag) {
        StaggeredGridLayoutManager manager;
        if (flag) {
            manager = new StaggeredGridLayoutManager(itemNumber, StaggeredGridLayoutManager.VERTICAL);//纵向
        } else {
            manager = new StaggeredGridLayoutManager(itemNumber, StaggeredGridLayoutManager.HORIZONTAL);//横向
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter((RecyclerView.Adapter) obj);
    }

    /**
     * 设置字体颜色
     *
     * @param times
     * @return
     */
    public static int setTextColor(int times) {
        switch (times) {
            case 0:
                return R.color.word_text_background_0;
            case 1:
                return R.color.word_text_background_1;
            case 2:
                return R.color.word_text_background_2;
            case 3:
                return R.color.word_text_background_3;
            case 4:
                return R.color.word_text_background_4;
            case 5:
                return R.color.word_text_background_5;
            case 6:
                return R.color.word_text_background_6;
            case 7:
                return R.color.word_text_background_7;
            case 8:
                return R.color.word_text_background_8;
            case 9:
                return R.color.word_text_background_9;
            case 10:
                return R.color.word_text_background_10;
            case 11:
                return R.color.word_text_background_11;
            case 12:
                return R.color.word_text_background_12;
        }
        return R.color.word_text_background_12;
    }

    /**
     * @param TAG 转化为中文名称
     * @return
     */
    public static String choice(String TAG) {
        switch (TAG) {
            case "cgccd":
                return "《词根词缀词典》";
            case "jmyhcd":
                return "《简明英汉词典》";
            case "klsgjsjcd":
                return "《柯林斯高阶双解词典》";
            case "sklxjcd":
                return "《柯林斯星级词典》";
            case "njdydccd":
                return "《牛津短语动词词典》";
            case "njtyccd":
                return "《牛津同义词词典》";
            case "njyydpcd":
                return "《牛津英语搭配词典》";
            case "xdyhzhcd":
                return "《现代英汉综合词典》";
            case "yccydycd":
                return "《英语常用短语词典》";
        }
        return "";
    }

    /**
     * 带一个String的跳转页面
     *
     * @param activityA
     * @param activityB
     * @param tag       标记
     * @param content   传递的内容
     */
    public static void setActivityString(Activity activityA, Class<?> activityB, String tag, String content) {
        Intent intent = new Intent(activityA.getBaseContext(), activityB);
        intent.putExtra(tag, content);
        activityA.startActivity(intent);
    }

    /**
     * 获取activity跳转过来的一个参数值
     *
     * @param activity
     * @param tag
     * @return
     */
    public static String getActivityString(Activity activity, String tag) {
        return activity.getIntent().getStringExtra(tag);
    }

//
//    /**
//     * 使用自定义dialog，仅有确认和取消
//     *
//     * @param context               上下文
//     * @param content               内容文本
//     * @param commonDialogInterface 实现的接口
//     */
//    public static void setDialog(Context context, String content, CommonDialogInterface commonDialogInterface) {
//        final CommonDialog dialog = new CommonDialog(context);
//        dialog.setMessage(content)
////                .setImageResId(R.mipmap.ic_launcher)
//                .setTitle(context.getResources().getString(R.string.app_name))
//                .setSingle(false).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
//            @Override
//            public void onPositiveClick() {
//                dialog.dismiss();
//                commonDialogInterface.PositiveClick();
//            }
//
//            @Override
//            public void onNegtiveClick() {
//                dialog.dismiss();
//                commonDialogInterface.NegtiveClick();
//            }
//        }).show();
//    }

    /**
     * 此接口用于实现自定义dialog点击确认和取消的两个方法
     */
    public interface CommonDialogInterface {
        void PositiveClick();

        void NegtiveClick();
    }


    /**
     * 设置popwindow
     *
     * @param activity
     * @param popUpWindowLayout
     * @param referenceView     Activity结束时 dismiss掉PopWindow 避免内存泄漏
     */
    public static void showPopupWindow(Activity activity, int popUpWindowLayout, View referenceView) {
        /**
         * 加载poplayout布局
         */
        View contentLayout = LayoutInflater.from(activity).inflate(popUpWindowLayout, null);
        PopupWindow mPopWindow = new PopupWindow(//构造
                contentLayout,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        /**
         *1.设置PopupWindow的背景
         * mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//可设置为透明
         */
        mPopWindow.setBackgroundDrawable(null);
        mPopWindow.setOutsideTouchable(true);//设置PopupWindow是否能响应外部点击事件

        /**
         *2.设置外部背景变暗
         */
        Window mWindow = activity.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = 0.3f;
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setAttributes(params);

        /**
         *3.设置PopupWindow是否能响应点击事件
         */
        mPopWindow.setTouchable(true);

        /**
         *4.下面两行防止和软键盘冲突
         */
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        /**
         * 5.参照view
         *  1.直接显示在参照View 的左下方
         *  mPopWindow.showAsDropDown(View anchor);
         *
         *  2.xoff yoff相对于锚点的左下角,坐标系是android传统坐标系（即左上角）
         *  mPopWindow.showAsDropDown(View anchor, int xoff, int off)
         *
         *  3.对于整个屏幕的window而言，通过gravity调整显示在左、上、右、下、中. x,y调整两个方向的偏移
         *  mPopWindow.showAsDropDown(View parent, int gravity, int x, int y)
         */
        mPopWindow.showAtLocation(referenceView, Gravity.CENTER, 0, 0);

        /**
         * 7.设置动画
         */
//        mPopWindow.setAnimationStyle(R.style.animTranslate);

        /**
         *8.消失监听
         */
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mWindow != null) {
                    WindowManager.LayoutParams params = mWindow.getAttributes();
                    params.alpha = 1.0f;
                    mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    mWindow.setAttributes(params);
                }
            }
        });
    }


    /**
     * 设置窗体背景    暂无用
     *
     * @param activity
     * @param flag
     */
    public static void setWindow(Activity activity, boolean flag) {
        if (flag) {
            //设置窗体始终点亮
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            //设置窗体背景模糊
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        }
    }


    /**
     * 返回值就是状态栏的高度,得到的值单位px
     *
     * @param activity
     * @return
     */
    public float getStatusBarHeight(Activity activity) {
        float result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimension(resourceId);
        }
        return result;
    }


    /**
     * 返回值就是导航栏的高度,得到的值单位px
     *
     * @param activity
     * @return
     */
    public float getNavigationBarHeight(Activity activity) {
        float result = 0;
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimension(resourceId);
        }
        return result;
    }


    /**
     * 打印tag
     *
     * @param content
     */
    public static void showLog(String content) {
        Log.e("tag", content);
    }

    /**
     * 简单弹窗提醒
     *
     * @param context
     * @param text
     */
    public static void showDialog(Context context, String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("智人")
                .setMessage(text)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alertDialog.show();
    }

    /**
     * 弹吐司Toast
     *
     * @param context
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }


    /**
     * 把本地的int类型的图片转换成drawable
     *
     * @param context
     * @param drawableInt
     * @return
     */
    public static Drawable IntToDrawable(Context context, int drawableInt) {
        return context.getResources().getDrawable(drawableInt);
    }


    /**
     * 把本地的int类型的图片转换成Bitmap
     *
     * @param context
     * @param drawableInt
     * @return
     */
    public static Bitmap IntToBitmap(Context context, int drawableInt) {
        Resources r = context.getResources();
        InputStream is = r.openRawResource(drawableInt);
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        Bitmap bmp = bmpDraw.getBitmap();
        return bmp;
    }


    /**
     * Bitmap转Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable BitmapToDrawable(Bitmap bitmap) {
        Drawable bd = new BitmapDrawable(bitmap);
        return bd;
    }

    /**
     * 通过BitmapShader实现圆形边框
     *
     * @param bitmap
     * @param outWidth  输出的图片宽度
     * @param outHeight 输出的图片高度
     * @param radius    圆角大小
     * @param boarder   边框宽度
     */
    public static Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius, int boarder) {
        if (bitmap == null) {
            return null;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //给着色器配置matrix
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        //创建矩形区域并且预留出border
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);
        //把传入的bitmap绘制到圆角矩形区域内
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }


    /**
     * 通过BitmapShader实现圆形边框
     *
     * @param bitmap
     * @param outWidth  输出的图片宽度
     * @param outHeight 输出的图片高度
     * @param boarder   边框大小
     */
    public static Bitmap getCircleBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int boarder) {
        int radius;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (outHeight > outWidth) {
            radius = outWidth / 2;
        } else {
            radius = outHeight / 2;
        }
//创建canvas
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, paint);
        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, boarderPaint);
        }
        return desBitmap;
    }


    public static void listenKeyboardVisible(View view) {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB) {
                    MyStatic.showLog("软键盘弹出");
                    return true;
                }
                MyStatic.showLog("软键盘mei弹出");
                return false;
            }
        });
    }

//    /**
//     * 动态设置padding状态栏的高度
//     * @param view
//     */
//    public static void setStatusBarPaddingHeight(Activity activity, View view) {
//        view.setPadding(0, GetXXHeight.getStatusBarHeight(activity), 0, 0);
//    }


    /**
     * 生成 [m,n] 的数字
     * @param n
     * @param m
     * @return
     */
    public static int getAtoZ(int n, int m) {
        return new Random().nextInt(m - n) + n;
    }


    /**
     * 返回随机颜色值
     *
     * @return
     */
    public static int getRandomColor() {
        return new Random().nextInt(0xffffff) + 0xff000000;
    }


    /**
     * 延时操作方法------------------temp
     */
    public static void postDelayed_() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: 2022-04-03  
            }
        }, 10);
    }

    /**
     * char 转为ascii
     *
     * @param str
     * @return
     */
    public static int getChartoASCII(String str) {
        String tempStr=str.substring(0,1);
        char chr = tempStr.charAt(0);
        return (int) chr;
    }





}
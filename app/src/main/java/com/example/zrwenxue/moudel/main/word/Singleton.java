package com.example.zrwenxue.moudel.main.word;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.memory.SoundMarkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-11-27
 * Time: 12:21
 */
public class Singleton {
    //简单单例
    private static Singleton single = null;
    private Singleton() {}

    public static Singleton getInstance() {
        if (single == null) {
            synchronized (Singleton.class) {
                if (single == null) {
                    single = new Singleton();
                }
            }
        }
        return single;
    }
    //常用变量
    public  String wq="wq";
    public  String TAG="智人：";

    public  Boolean showSoundMark=false;
    public  String BaseUrl="http://v.juhe.cn/toutiao/";
    public  String NewsKey="2131f22f6c1e2cec174d8c7b803770c5";
    public  Boolean wordFlag=true;

    public ArrayList<String> fileLines=new ArrayList();//英语单词记忆100法



    public String[] nameArry=new String[]{
            "jmyhcd",
            "klsgjsjcd",
            "sklxjcd",
            "njdydccd",
            "njtyccd",
            "njyydpcd",
            "xdyhzhcd",
            "yccydycd",
            "cgccd"};
    public String[] nameArryTXT=new String[]{"" +
            "《简明英汉词典》",
            "《柯林斯高阶双解词典》",
            "《柯林斯星级词典》",
            "《牛津短语动词词典》",
            "《牛津同义词词典》",
            "《牛津英语搭配词典》"
            ,"《现代英汉综合词典》"
            ,"《英语常用短语词典》",
            "《词根词缀词典》"};

    public int[] rawID=new int[]{
            R.raw.jmyhcd,
            R.raw.klsgjsjcd,
            R.raw.sklxjcd,
            R.raw.njdydccd,
            R.raw.njtyccd,
            R.raw.njdydccd,
            R.raw.xdyhzhcd,
            R.raw.yccydycd,
            R.raw.cgccd};


    /**
     * 设置共享参数
     * @param context  上下文
     * @param key 名称
     * @param flag 布尔值
     */
    public void setSharedPreferences(Context context, String key, Boolean flag){
        /*从share.xml中获取共享参数对象
        getSharedPreferences方法的第一个参数是文件名，上面的 share 表示当前使用的共享参数文件名是share.xml;
        第二个参数是操作模式，一般都填MODE_PRIVATE,表示私有模式。共享参数存储数据要借助于Editor类，示例代码如下:
        从share.xml中获取共享参数对象*/

        SharedPreferences shared = context.getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor= shared.edit(); //获得编辑器的对象
        editor.putBoolean(key, flag); //添加-个布尔型参数

        /*editor.putString("name", "MrLee"); //添加一个名叫name的字符串参数
        editor.putInt("age", 30); //添加一个名叫age的整型参数
        editor.putFloat("weight", 100f); //添加一个名叫weight的浮点数参数*/

        // 提交编辑器中的修改
        editor.commit();
    }


    /**
     * 通过key获取共享参数
     * @param context 上下文
     * @param key 任意名
     * @return 存储的布尔值
     */
    public boolean getSharedPreferences(Context context,String key){
        //共享参数读取数据相对简单，直接使用对象即可完成数据读取方法的调用，
        // 注意get方法的第二个参数表示默认值，示例代码如下:

        SharedPreferences shared = context.getSharedPreferences("share", MODE_PRIVATE);
        return shared.getBoolean(key, false); //从共享参数中获得名叫maried的布尔数

        /*String name = shared.getString("name",""); //从共享参数中获得名叫name的字符串
        int age = shared.getInt("age",0); //从共享参数中获得名叫age的整型数
        float weight= shared.getFloat("weight",0); //从共享参数中获得名叫weight的浮点数*/
    }


    /**
     * 设置共享参数
     * @param context  上下文
     * @param key 名称
     */
    public void setSharedPreferences(Context context, String key, String content){
        /*从share.xml中获取共享参数对象
        getSharedPreferences方法的第一个参数是文件名，上面的 share 表示当前使用的共享参数文件名是share.xml;
        第二个参数是操作模式，一般都填MODE_PRIVATE,表示私有模式。共享参数存储数据要借助于Editor类，示例代码如下:
        从share.xml中获取共享参数对象*/

        SharedPreferences shared = context.getSharedPreferences("mine_word", MODE_PRIVATE);
        SharedPreferences.Editor editor= shared.edit(); //获得编辑器的对象
        editor.putString(key,content);

        /*editor.putString("name", "MrLee"); //添加一个名叫name的字符串参数
        editor.putInt("age", 30); //添加一个名叫age的整型参数
        editor.putFloat("weight", 100f); //添加一个名叫weight的浮点数参数*/

        // 提交编辑器中的修改
        editor.commit();
    }


    /**
     * 通过key获取共享参数
     * @param context 上下文
     * @param key 任意名
     * @return 存储的布尔值
     */
    public String getSharedPreferences(Context context,String key,boolean flag){
        //共享参数读取数据相对简单，直接使用对象即可完成数据读取方法的调用，
        // 注意get方法的第二个参数表示默认值，示例代码如下:

        SharedPreferences shared = context.getSharedPreferences("mine_word", MODE_PRIVATE);
        return shared.getString(key, ""); //从共享参数中获得名叫maried的布尔数

        /*String name = shared.getString("name",""); //从共享参数中获得名叫name的字符串
        int age = shared.getInt("age",0); //从共享参数中获得名叫age的整型数
        float weight= shared.getFloat("weight",0); //从共享参数中获得名叫weight的浮点数*/
    }

    public List<Wordbean> wordbeanArrayList=new ArrayList<>();
    public ArrayList<SoundMarkBean> SoundMarkList=new ArrayList<>();

//    ArrayList<Wordbean> wordbeanArrayList = new ArrayList<>();

    public Wordbean mWordCurrentWordBean=null;

    public int gameWordBeanListTAG=-1;//用于存储game的对象的角标
    public int gameWordBean_time=0;//用于存储game的对象点击的次数






}

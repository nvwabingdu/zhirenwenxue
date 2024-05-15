package com.example.zrwenxue.moudel.main.word;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.newzr.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-05-02
 * Time: 9:30
 */
public class WordFromShardToSingleton {


    /**
     * 取出共享参数
     * @return  good-1 ok-3 open-0
     */
    public static String getShardWordTimes(Context context){
            SharedPreferences shared = context.getSharedPreferences("soundmark", MODE_PRIVATE);
            return shared.getString("word_times", "");
    }

    /**
     * 设置word共享参数数据
     * @param context
     * @param content  good-1 ok-3 open-0
     */
    public static void setShardWordTimes(Context context,String content){
            SharedPreferences shared = context.getSharedPreferences("soundmark", MODE_PRIVATE);
            SharedPreferences.Editor editor= shared.edit(); //获得编辑器的对象
            editor.putString("word_times",content);
            editor.commit();
    }


    /**
     *修改单例内的数据
     * @param shardContent  shard的内容  goog-1 ok-7
     * @param currentWord  正在点击的单词
     */
    public static void  fixSingletonList(Context context,String shardContent,String currentWord){
        ArrayList<String> shardList;
        if(!shardContent.equals("")){//如果共享参数存在，说明有记忆的单词
            String[] arrayTimes=shardContent.split("#");//通过#拆分成数组 good-1#ok-7 这种数组
            List<String> list=Arrays.asList(arrayTimes);//把数组转为集合 此集合没有add方法？发生问题的原因如下：
//            调用Arrays.asList()生产的List的add、remove方法时报异常，这是由Arrays.asList() 返回的市Arrays的内部类ArrayList， 而不是java.util.ArrayList。Arrays的内部类ArrayList和java.util.ArrayList都是继承AbstractList，remove、add等方法AbstractList中是默认throw UnsupportedOperationException而且不作任何操作。java.util.ArrayList重新了这些方法而Arrays的内部类ArrayList没有重新，所以会抛出异常
            shardList=new ArrayList<>(list);//解决方法

            boolean flag=true;//如果没有找到数据  说明是新记忆的单词
            for(int i=0;i<shardList.size();i++){
                if(shardList.get(i).split("-")[0].equals(currentWord)){//good = good
                    int times=Integer.parseInt(shardList.get(i).split("-")[1])+1;
                    shardList.set(i,currentWord+"-"+times+"#");//设置点击单词记忆次数+1
                    flag=false;
                }
            }
            if(flag){
                shardList.add(currentWord+"-"+1+"#");
            }
        }else {
            shardList=new ArrayList<>();
            shardList.add(currentWord+"-"+1+"#");
        }

        //装入共享参数
        StringBuffer stringBuffer=new StringBuffer();
        for (String s:shardList) {
//            MyStatic.showLog("0000000000---s-----"+s);
            if(!s.contains("#")){
                s=s+"#";
            }
            stringBuffer.append(s);
        }
        setShardWordTimes(context,"");
        setShardWordTimes(context,stringBuffer.toString());
    }


    /**
     * 返回共享参数的生成的集合
     * @param context
     * @return
     */
    public static ArrayList<String>  getShardList(Context context) {
        ArrayList<String> shardList = null;
        String shardContent = getShardWordTimes(context);
        if (!shardContent.equals("")) {//如果共享参数存在，说明有记忆的单词
            String[] arrayTimes = shardContent.split("#");//通过#拆分成数组 good-1#ok-7 这种数组
            List<String> list = Arrays.asList(arrayTimes);//把数组转为集合 此集合没有add方法？发生问题的原因如下：
            shardList = new ArrayList<>(list);//解决方法
        }
        return shardList;
    }

    /**
     * 返回和共享参数结合的集合
     * @param context
     * @param wordbeanArrayList
     * @return
     */
    public static ArrayList<Wordbean> getReplaceTimesList(Context context,ArrayList<Wordbean> wordbeanArrayList){
        if(getShardWordTimes(context).equals("")){//如果为空直接返回原始集合
            return wordbeanArrayList;
        }

        String[] arrayTimes=getShardWordTimes(context).split("#");//通过#拆分成数组 good-1#ok-7 这种数组
        List<String> list=Arrays.asList(arrayTimes);
        ArrayList<String> shardList=new ArrayList<>(list);

        for(int i=0;i<shardList.size();i++){
            for(int j=0;j<wordbeanArrayList.size();j++){
                if(shardList.get(i).split("-")[0].equals(wordbeanArrayList.get(j).word)){//good  ==  good
                    wordbeanArrayList.get(j).setWord_times(Integer.parseInt(getDleTag(shardList.get(i).split("-")[1])));
                }
            }
        }
        return wordbeanArrayList;
    }

    /**
     * 除掉#
     * @param s
     * @return
     */
   public static String getDleTag(String s){
        return s.replace("#","");
    }




    /**
     * 把raw中的数据装入单例集合
     * @param context
     * @return
     * @throws Exception
     */
    public static ArrayList<Wordbean> getWordBeanList(Context context) throws Exception {
        ArrayList<Wordbean> arrayList = new ArrayList<>();
        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.goodnow), "UTF-8"));
        //读取流
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if(MyStatic.getSubStr(line,"<4>", "<5>").equals("CET4")) {
                arrayList.add(new Wordbean(
                        MyStatic.getSubStr(line, "<1>", "<2>") + "",//单词
                        MyStatic.getSubStr(line, "<2>", "<3>") + "",//音标
                        MyStatic.getSubStr(line, "<3>", "<4>") + "",//不规则形式
                        MyStatic.getSubStr(line, "<5>", "<6>") + "",//解释
                        MyStatic.getSubStr(line, "<1>", "<2>").length() + "",//长度
                        MyStatic.getSubStr(line, "<6>", "<7>") + "",//星级
                        0,//次数
                        MyStatic.getSubStr(line, "<4>", "<5>") + "",//自定义分类
                        "",//助记
                        "",//同义词
                        "",//短语
                        "",//常用搭配
                        ""//收藏
                ));
            }
        }
        bufferedReader.close();//关闭流
        Singleton.getInstance().wordbeanArrayList.clear();
        Singleton.getInstance().wordbeanArrayList=arrayList;

        return arrayList;
    }



}

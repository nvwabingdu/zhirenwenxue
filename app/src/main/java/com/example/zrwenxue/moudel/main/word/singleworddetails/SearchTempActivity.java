package com.example.zrwenxue.moudel.main.word.singleworddetails;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.newzr.R;
import com.example.zrtool.utilsjava.Tools;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;
import com.example.zrwenxue.moudel.main.word.Wordbean;
import com.example.zrwenxue.moudel.main.word.roomdata.MyDatabase;
import com.example.zrwenxue.moudel.main.word.roomdata.WordDao;
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.ViewPagerFragmentAdapter;
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.Web2Fragment;
import com.example.zrwenxue.moudel.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-03-25
 * Time: 21:56
 */
public class SearchTempActivity extends BaseActivity {
    String tempWord;
    TextView word, times, sound_mark, buguize, star;

    ImageView voice;

    RecyclerView recyclerView, recyclerView2;

    private static Handler handler;
    int doubleNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加跳转过来传递的单词
        tempWord = MyStatic.getActivityString(this, "soundmark");
        //添加控件
        initView();
    }



    /**
     * 寻找控件
     */
    public void initView() {
        word = findViewById(R.id.activity_word_search_word);
        times = findViewById(R.id.activity_word_search_times);
        sound_mark = findViewById(R.id.activity_word_search_sound_mark);
        buguize = findViewById(R.id.activity_word_search_buguize);
        voice = findViewById(R.id.activity_word_search_speech);
        //设置声音
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(NetworkUtilsJava.isNetworkConnected(SearchTempActivity.this)){
                    String mp3url = "http://ssl.gstatic.com/dictionary/static/sounds/oxford/" + tempWord.toLowerCase() + "--_us_1.mp3";
                    WordPronunciation wordPronunciation = new WordPronunciation(new MediaPlayer(), mp3url);
                    wordPronunciation.play();
                    }else {
                        Tools.showToast(SearchTempActivity.this, "请检查网络设置", 2000);
                    }
                } catch (Exception e) {
                    // TODO: 2022-01-05
                    Tools.showToast(SearchTempActivity.this, "未知错误", 2000);
                }
            }
        });

        star = findViewById(R.id.activity_word_search_star);

        recyclerView = findViewById(R.id.activity_word_search_RecyclerView);

        recyclerView2 = findViewById(R.id.activity_word_search_RecyclerView2);
    }

    /**
     * 获取焦点时操作
     */
    @Override
    public void onResume() {
        super.onResume();
        //设置状态栏的背景颜色
//        StatusBarUtil.setStatusBar(this,0);
        showWaiting();
        //设置布局
        setView_1(tempWord);

        //设置数据库
        setInitData();

        //接受handler修改UI
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 10) {
                    setView_1(tempWord);//数据库设置完毕然后开始设置view
                }
                if (msg.what == 11) {
                    Log.e("TAG", "-----------设置view了-----------");
                    setView_2((Wordbean) msg.obj);//获取Handler传过来的，查询到的数据库中的一行数据，然后设置view
                }
                if (msg.what == 12) {
                    Log.e("TAG", "-----------设置tablayout_viewpager-----------");
                    //设置联动
                    setTabLayout_viewpager_fragment(msg.getData().getStringArrayList("dataList"),
                            msg.getData().getStringArrayList("mTitleList"));//传递词典查出来的数据
                }
            }
        };

        //线程获取数据
        getHtmlData();

    }


    /**
     * 设置数据库数据
     */
    private void setInitData() {
        //如果数据已设置就直接退出，用共享参数设置
        if (Singleton.getInstance().getSharedPreferences(this, "DatabaseFlag")) {
            return;
        }
        //开启线程获取
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.获取数据
                    getWordBeanArrayList();
                    //2.设置数据库
                    UseWordDatabase();
                    //3.设置状态
                    Singleton.getInstance().setSharedPreferences(SearchTempActivity.this, "DatabaseFlag", true);

                    //handler发送字符串 用于通知数据库已经设置，可以更新UI
                    Message tempMsg = handler.obtainMessage();
                    tempMsg.what = 10;
                    handler.handleMessage(tempMsg);
                } catch (Exception e) {
                    // TODO: 2022-01-04  是否需要错误处理？？
                }
            }
        }).start();
    }

    /**
     * 设置控件
     */
    public void setView_1(String str) {
        if (!Singleton.getInstance().getSharedPreferences(SearchTempActivity.this, "DatabaseFlag")) {
            return;
        }//说明数据库已经设置，只能设置数据库之后设置view

        //开启线程查询
        new Thread(new Runnable() {
            @Override
            public void run() {
                WordDao dao = MyDatabase.getDatabaseInstance(SearchTempActivity.this).getWordDao();
                Wordbean wordbean = dao.queryWord(str);
                Log.e("TAG", "-----------发送查询数据库中语句-----------");
                //handler发送字符串
                Message tempMsg = handler.obtainMessage();
                tempMsg.what = 11;
                tempMsg.obj = wordbean;//把查询到的对象传过去
                handler.handleMessage(tempMsg);
            }
        }).start();
    }

    //设置上半截控件
    void setView_2(Wordbean wordbean) {

        //确保在UI线程？？？？？
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", wordbean.toString());
                word.setText(wordbean.getWord() + "");
                times.setText(wordbean.getWord_times() + "");
                times.setTextColor(MyStatic.setTextColor((int) wordbean.getWord_times()));
                sound_mark.setText(wordbean.getWord_sound_mark() + "");
                buguize.setText("不规则形式：" + wordbean.getWord_irregular() + "");
                star.setText(wordbean.getWord_star() + "");

                //解释
                ArrayList<ExplainBean> beanArrayList = new ArrayList<>();
                String[] arrayStr = wordbean.getWord_explain_list().split("-----");

                for (String ss : arrayStr) {
                    if (ss.equals("vt. & vi.")) {
                        ss = ss.replace("vt. & vi.", "v.");
                    }
                    if (!ss.equals("")) {
                        beanArrayList.add(new ExplainBean(ss.substring(0, ss.indexOf(".") + 1), ss.substring(ss.indexOf(".") + 1)));
                    }
                }

                Log.e("TAG", "-----------------" + beanArrayList);
                MyStatic.setRecyclerView(new SearchAdpter(SearchTempActivity.this, beanArrayList), recyclerView, SearchTempActivity.this, true);


                // TODO: 2022-01-04 标签 还需要改下
                ArrayList<String> strlist = new ArrayList<>();
                if (!wordbean.getWord_diy_type().equals("") && !wordbean.getWord_diy_type().equals("null")) {
                    strlist.add(wordbean.getWord_diy_type());
                    //
                    MyStatic.setRecyclerViewGridView(new SearchAdapter2(SearchTempActivity.this, strlist), recyclerView2, SearchTempActivity.this,5, true);
                }
                if(doubleNum==1){
                    //设置dialog消失
                    progressDialog.dismiss();
                    doubleNum=0;
                }else {
                    doubleNum=doubleNum+1;
                }
            }
        });

    }

    /**
     * 数据库操作
     */

    private void UseWordDatabase() {
        Log.e("TAG", "----------获取数据访问对象----------");
        //1.获取数据访问对象。（需要在工作线程中使用，否则耗时操作将会导致Application崩溃）
        WordDao dao = MyDatabase.getDatabaseInstance(SearchTempActivity.this).getWordDao();

        Log.e("TAG", "----------循环插入数据库----------");
        //2.循环插入数据库
        for (Wordbean wordbean : wordbeanArrayList) {
            dao.insertData(wordbean);
        }
        Log.e("TAG", "----------循环插入结束----------");


        /*这个数据必须是查询出来的，否则即便你再构造一个一模一样的数据也删不掉*/
//        dao.deleteData(dao.queryWord("good"));
//        这个数据必须是查询出来的，否则即便你再构造一个一模一样的数据也修改不成功
/*        Emperor liuBei = dao.queryEmperorById(2);
        liuBei.age = "54";
        dao.updateEmperor(liuBei);
        mperor sunQuan = dao.queryEmperorById(1);
        List<Emperor> list = dao.queryEmperorsByAge("54");
        List<Emperor> emperors = dao.queryEmperors();*/
    }


    /**
     * 把本地文件整理好的数据读取出来装入集合
     *
     * @throws Exception
     */
    ArrayList<Wordbean> wordbeanArrayList = new ArrayList<>();

    public void getWordBeanArrayList() throws Exception {
        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(this.getResources().openRawResource(R.raw.good), "UTF-8"));
        //读取流
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            wordbeanArrayList.add(new Wordbean(
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
        bufferedReader.close();//关闭流
    }


    //tab+viewpager
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    /**
     * 设置setTabLayout_viewpager_fragment
     */
    public void setTabLayout_viewpager_fragment(ArrayList<String> dataList,ArrayList<String> mTitleList) {
        mViewPager = findViewById(R.id.activity_word_search_viewpager);
        mTabLayout = findViewById(R.id.activity_word_search_tabs);

        //在UI线程中操作
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPagerFragmentAdapter webAdapter =
                        new ViewPagerFragmentAdapter(
                                getSupportFragmentManager(),
                                getFragmentList(dataList),
                                mTitleList);//数组转化为集合
                //给ViewPager设置适配器
                mViewPager.setAdapter(webAdapter);
                //将TabLayout和ViewPager关联起来。
                mTabLayout.setupWithViewPager(mViewPager);
                //给TabLayout设置适配器
                mTabLayout.setTabsFromPagerAdapter(webAdapter);

                //设置dialog消失
                if(doubleNum==1){
                    //设置dialog消失
                    progressDialog.dismiss();
                    doubleNum=0;
                }else {
                    doubleNum=doubleNum+1;
                }
            }
        });

    }

    /**
     * @return 获取带参数的fragment集合 用于装载在viewpager里面
     */
    public ArrayList<Fragment> getFragmentList(ArrayList<String> dataList) {
        if (dataList.size() != 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Web2Fragment web2Fragment = new Web2Fragment();
                Bundle bundle = new Bundle();

                bundle.putString("data", dataList.get(i));//返回查询到的一行 对应每个词典

                web2Fragment.setArguments(bundle);
                fragmentArrayList.add(web2Fragment);
            }
        }
        return fragmentArrayList;
    }


    /**
     * 开启线程获取每个词典的一行
     */
    private void getHtmlData() {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Arrays.asList(Singleton.getInstance().nameArryTXT).size(); i++) {
                    String temp=WordSearch.getHtmlDate(SearchTempActivity.this,
                            Singleton.getInstance().nameArry[i]
                            , tempWord
                            , Singleton.getInstance().rawID[i]
                            , 1);
                    if(!temp.equals("")){
                        dataList.add(temp);
                        mTitleList.add(Singleton.getInstance().nameArryTXT[i]);
                    }
                }

                //handler发送字符串
                Message tempMsg = handler.obtainMessage();
                tempMsg.what = 12;
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("dataList",dataList);
                bundle.putStringArrayList("mTitleList",mTitleList);
                tempMsg.setData(bundle);
                handler.handleMessage(tempMsg);
            }
        }).start();
    }

    //初始化数据库，耗时  从good2里面获取并建立数据库

    //查找数据库数据，耗时。 从数据库中查找。
    // 设置上半截view

    //查找下半截数据，耗时。 从各个词典获取数据
    //设置下半截view
    /**
     * 圆圈加载进度的 dialog
     */
    ProgressDialog progressDialog;
    private void showWaiting() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("智人");
        progressDialog.setMessage("数据加载中...");
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_word_search;
    }

    @Override
    protected void init() {

    }
}

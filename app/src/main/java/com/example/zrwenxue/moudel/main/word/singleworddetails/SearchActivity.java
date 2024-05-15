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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.newzr.R;
import com.example.zrtool.ui.custom.TitleBarView;
import com.example.zrtool.utilsjava.Tools;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;
import com.example.zrwenxue.moudel.main.word.Wordbean;
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.ViewPagerFragmentAdapter;
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.Web2Fragment;
import com.example.zrwenxue.moudel.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-23
 * Time: 16:46
 */
public class SearchActivity extends BaseActivity {
    String tempWord;
    TextView word, times, sound_mark, buguize, star;
    ImageView voice;
    RecyclerView recyclerView, recyclerView2;
    private static Handler handler;
    int doubleNum;
    int timeNum = 0;

    @Override
    protected int layoutResId() {
        return R.layout.activity_word_search;
    }

    @Override
    protected void init() {
        showWaiting();
        //设置标题
        setTopView("单词详情");
        //接收传递过来的数据
        initIntentData();
        //添加控件
        initView();
        //设置数据
        setData();
    }

    private TitleBarView topView;
    private void setTopView(String title) {
        topView = findViewById(com.example.zrdrawingboard.R.id.title_view);
        topView.setTitle(title);
        topView.setOnclickLeft(View.VISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setOnclickRight(View.INVISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     * 接收传递过来的数据
     */
    private void initIntentData() {
        try {
            //添加跳转过来传递的单词
            tempWord = MyStatic.getActivityString(this, "soundmark");
            timeNum = Integer.parseInt(MyStatic.getActivityString(this, "times"));
        } catch (Exception e) {
        }
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
                    if (NetworkUtilsJava.isNetworkConnected(SearchActivity.this)) {
                        String mp3url = "http://ssl.gstatic.com/dictionary/static/sounds/oxford/" + tempWord.toLowerCase() + "--_us_1.mp3";
                        WordPronunciation wordPronunciation = new WordPronunciation(new MediaPlayer(), mp3url);
                        wordPronunciation.play();
                    } else {
                        Tools.showToast(SearchActivity.this, "请检查网络设置", 2000);
                    }
                } catch (Exception e) {
                    Tools.showToast(SearchActivity.this, "未知错误", 2000);
                }
            }
        });

        star = findViewById(R.id.activity_word_search_star);
        recyclerView = findViewById(R.id.activity_word_search_RecyclerView);
        recyclerView2 = findViewById(R.id.activity_word_search_RecyclerView2);
    }
    /**
     * 设置数据
     */
    private void setData(){

        //2.设置上部分
        setView_2(Singleton.getInstance().mWordCurrentWordBean);//共享参数中取


        //接受handler修改UI
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 12) {
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
     * 设置上半截控件
     * @param wordbean
     */
    void setView_2(Wordbean wordbean) {
        Log.e("TAG", wordbean.toString());
        if (wordbean == null) {
            return;
        }

        Log.e("TAG", wordbean.toString());
        word.setText(wordbean.getWord() + "");
        times.setText(timeNum + "");
        times.setTextColor(MyStatic.setTextColor(timeNum));
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
        MyStatic.setRecyclerView(new SearchAdpter(SearchActivity.this, beanArrayList), recyclerView, SearchActivity.this, true);


        // TODO: 2022-01-04 标签 还需要改下
        ArrayList<String> strlist = new ArrayList<>();
        if (!wordbean.getWord_diy_type().equals("") && !wordbean.getWord_diy_type().equals("null")) {
            strlist.add(wordbean.getWord_diy_type());
            //
            MyStatic.setRecyclerViewGridView(new SearchAdapter2(SearchActivity.this, strlist), recyclerView2, SearchActivity.this, 5, true);
        }
    }

    //tab+viewpager
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    /**
     * 设置setTabLayout_viewpager_fragment
     */
    public void setTabLayout_viewpager_fragment(ArrayList<String> dataList, ArrayList<String> mTitleList) {
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
                progressDialog.dismiss();
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
                    String temp = WordSearch.getHtmlDate(SearchActivity.this,
                            Singleton.getInstance().nameArry[i]
                            , tempWord
                            , Singleton.getInstance().rawID[i]
                            , 1);
                    if (!temp.equals("")) {
                        dataList.add(temp);
                        mTitleList.add(Singleton.getInstance().nameArryTXT[i]);
                    }
                }

                //handler发送字符串
                Message tempMsg = handler.obtainMessage();
                tempMsg.what = 12;
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", dataList);
                bundle.putStringArrayList("mTitleList", mTitleList);
                tempMsg.setData(bundle);
                handler.handleMessage(tempMsg);
            }
        }).start();
    }


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
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * 本页面不需要左滑返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }


}

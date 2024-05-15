package com.example.zrwenxue.moudel.main.home.phrase;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.example.newzr.R;
import com.example.zrtool.ui.custom.TitleBarView;
import com.example.zrwenxue.moudel.main.home.eng.EngNode;
import com.example.zrwenxue.moudel.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-26
 * Time: 8:55
 */
public class PhraseActivity extends BaseActivity {
    Random random = new Random();
    View view;
    WebView webView;
    FloatingActionButton floatingActionButton;

    @Override
    protected int layoutResId() {
        return R.layout.fragment_phrase;
    }

    @Override
    protected void init() {
        setTopView("短语");
        initView();
        //查找
        search();
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

    private void search() {
        ArrayList<String> arrayList = new ArrayList();
        try {
            arrayList = getOneLineTxt();

            int index = random.nextInt(arrayList.size());

            String wordOfLine = arrayList.get(index);//获得一行

            Log.e("TAG", wordOfLine);

            EngNode.startThread(this, webView, R.raw.yccydycd, "yccydycd", wordOfLine.substring(0, wordOfLine.indexOf(" ")).trim(), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //初始化控件
    public void initView() {
        webView = findViewById(R.id.dj_webview);
        //返回键

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查找
                search();
            }
        });
    }


    public ArrayList<String> getOneLineTxt() throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();

        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(this.getResources().openRawResource(R.raw.yccydycd), "UTF-8"));
        //读取流
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            arrayList.add(line.substring(0, line.indexOf("<")));
        }
        bufferedReader.close();//关闭流
        Log.e("TAG", arrayList.size() + "");
        return arrayList;
    }


}

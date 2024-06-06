package com.example.zrwenxue.moudel.main.home.eng;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newzr.R;
import com.example.zrtool.ui.custom.MyDrawerLayout;
import com.example.zrwenxue.app.TitleBarView;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;
import com.example.zrwenxue.moudel.BaseActivity;



/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-11-27
 * Time: 13:00
 */
public class SearchWordActivity extends BaseActivity {

    /**
     * 设置顶部
     */
    TitleBarView topView;
    private void setTopView(){
        topView = findViewById(R.id.title_bar);
        topView.setTitle("搜索单词");
        //左边返回
        topView.setOnclickLeft(View.VISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //右边弹出pop
        topView.setOnclickRight(View.INVISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected int layoutResId() {
        return R.layout.activity_eng_main;
    }

    @Override
    protected void init() {
        setTopView();
    }

    RecyclerView recyclerView;
    EditText editText;
    ImageView img,img2;
    private MyDrawerLayout mDrawerLayout;
    private ListView listView;
    private int DictTAG=0;


    WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            initView();//初始化数据
            webView = findViewById(R.id.eng_webview);
            //查找数据
            EngNode.startThread(this,webView, Singleton.getInstance().rawID[DictTAG],Singleton.getInstance().nameArry[DictTAG],"good",2);
    }

    /**
     * 初始化控件等数据
     */
    public void initView() {

        editText=findViewById(R.id.eng_edit);


        MyStatic.listenKeyboardVisible(editText);//监听软键盘是否摊开

        img=findViewById(R.id.eng_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!editText.getText().toString().equals("")){

                        if(!Validation.isEnglish(editText.getText().toString())){
                            MyStatic.showToast(getBaseContext(),"你的输入不正确，请输入英文");
                            editText.setText("");
                        }else {

                            if(Singleton.getInstance().wordFlag){
                                Singleton.getInstance().wordFlag=false;
                                try {
                                    closeInputMethod();
                                    //查找数据
                                    EngNode.startThread(getBaseContext(),webView,Singleton.getInstance().rawID[DictTAG],Singleton.getInstance().nameArry[DictTAG],editText.getText().toString().toLowerCase(),5);
                                }catch (Exception e){
                                    MyStatic.showToast(getBaseContext(),"发生了一个未知错误");
                                }
                            }else {
                                MyStatic.showToast(getBaseContext(),"正在搜索中");
                            }
                        }

                    }else {
                        MyStatic.showToast(getBaseContext(),"请输入数据");
                    }



            }
        });
        img2=findViewById(R.id.eng_img2);
        img2.setOnClickListener(new View.OnClickListener() {//设置Drawerlayout的开关
            @Override
            public void onClick(View view) {
                if(mDrawerLayout.isOpen()){
                    mDrawerLayout.close();
                }else {
                mDrawerLayout.open();
                }
//                finish();
            }
        });

        //设置侧滑布局
        mDrawerLayout= findViewById(R.id.drawer);
        mDrawerLayout.setScrimColor(Color.WHITE);//默认灰色  设置为其他颜色就好
        listView=findViewById(R.id.list_view);
        //设置适配器
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Singleton.getInstance().nameArryTXT));
        //设置监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击后隐藏侧边布局
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //查找数据  延时200毫秒等 drawerlayout 关闭
                        EngNode.startThread(getBaseContext(),webView,Singleton.getInstance().rawID[i],Singleton.getInstance().nameArry[i],editText.getText().toString(),3);
                    }
                }, 300);
                //设置词典
                DictTAG=i;
            }
        });
        }



    /**
     * 收起键盘
     */
    public void closeInputMethod() {
        /*用于判断虚拟软键盘是否是显示的*/
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

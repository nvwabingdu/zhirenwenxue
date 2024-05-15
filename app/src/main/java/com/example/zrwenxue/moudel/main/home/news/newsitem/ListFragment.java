package com.example.zrwenxue.moudel.main.home.news.newsitem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.newzr.R;
import com.example.zrtool.oldnet.beans.MainNewsBean;
import com.example.zrtool.oldnet.zqrretrofit.MyRetrofit;
import com.example.zrtool.utilsjava.Tools;
import com.example.zrwenxue.moudel.main.home.news.WebViewActivity;
import com.example.zrwenxue.moudel.main.word.HtmlStatic;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;


import java.net.URL;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-06
 * Time: 0:05
 */
public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyClerViewAdapter recyClerViewAdapter;
    private String mTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.list_fragment, container, false);
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置adapter
        recyClerViewAdapter = new RecyClerViewAdapter(getContext());
        mRecyclerView.setAdapter(recyClerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        //获取传递过来的title
        mTitle = getArguments().getString("mTitle");
        getIpInformationForPath(mTitle, Singleton.getInstance().NewsKey);
    }


    /**
     * @param type retrofit网络请求
     * @param key
     */
    public void getIpInformationForPath(String type, String key) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Singleton.getInstance().BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyRetrofit.RetrofitGETInterface retrofitGETInterface = retrofit.create(MyRetrofit.RetrofitGETInterface.class);
        Call<MainNewsBean> call = retrofitGETInterface.getRequestBody(type, key);
        call.enqueue(new Callback<MainNewsBean>() {
            @Override
            public void onResponse(Call<MainNewsBean> call, Response<MainNewsBean> response) {
                //把请求数据加入适配器
                try {
                    recyClerViewAdapter.addList(response.body().getResult().getData());

//                    MyStatic.showLog(response.body().getResult().getData().toString());
                } catch (Exception e) {
//                    Tools.showToast(getActivity(), Singleton.getInstance().TAG + e.toString(), 2000);
                }
                //item点击事件 跳转到webView
                recyClerViewAdapter.getCallback(new RecyClerViewAdapter.Callback() {
                    @Override
                    public void onItemClick(View view, int position) {

                        MyStatic.showLog("点击了");

                        handler= new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what==100){
                                    MyStatic.showLog("收到100");
                                    NewsActivity newsActivity = (NewsActivity) getActivity();
                                    Intent intent = new Intent(newsActivity, WebViewActivity.class);
                                    intent.putExtra("url", msg.obj.toString());
                                    startActivity(intent);
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MyStatic.showLog("网址："+response.body().getResult().getData().get(position).getUrl());
                                    //用于处理这个URL之后的HTML静态文本
                                    String content =
                                            HtmlStatic.delHtmlTag(
                                                    HtmlStatic.getURLSource(
                                                            new URL(response.body().getResult().getData().get(position).getUrl())),
                                                    "div", "meta", "script", "input", "link", "article");
                                    MyStatic.showLog("开始100");
                                    Message message=new Message();
                                    message.what=100;
                                    message.obj=content;
                                    handler.handleMessage(message);
                                }catch (Exception e){
                                    MyStatic.showLog("错误"+e.toString());
                                }




                            }
                        }).start();






                    }
                });
            }

            @Override
            public void onFailure(Call<MainNewsBean> call, Throwable t) {
                Tools.showToast(getActivity(), Singleton.getInstance().TAG + t.toString(), 2000);
            }
        });
    }

    Handler handler;
}

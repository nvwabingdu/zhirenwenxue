package com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.example.newzr.R;


/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-01
 * Time: 11:33
 */
public class Web2Fragment extends Fragment {
    private View mView;
    private String data;
    private WebView webView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.search_webview_item, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webView=mView.findViewById(R.id.search_webView_items_webview);
        //获取传递过来的title
        data=getArguments().getString("data");


        //查找单词  设置webview
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

}

package com.example.zrwenxue.moudel.main.word.tab;

import static android.content.Context.MODE_PRIVATE;

import android.webkit.WebView;


import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.word.MessageEvent;
import com.example.zrwenxue.moudel.main.word.WordNode;
import com.example.zrwenxue.moudel.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-03-24
 * Time: 20:54
 */
public class TwoFragment extends BaseFragment {

    @Override
    protected int setLayout() {
        return R.layout.fragment_word_two_fragment;
    }

    WebView webView;
    @Override
    protected void initView() {
         webView=fvbi(R.id.fragment_word_two_fragment_webview);
    }



    /**
     * =======================================================Eventbus
     * eventbus的绑定
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] tempTitle=getActivity().getSharedPreferences("titles", MODE_PRIVATE).getString("title","柯林斯五星级单词:a").split(":");

        WordNode.startThread(
                getActivity(),
                webView,
                getRAW2(tempTitle[0]),
                tempTitle[1],
                0,
                7);
    }

    /**
     * eventbus的解绑定
     */
    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Eventbus处理事件
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {

        String[] tempTitle=getActivity().getSharedPreferences("titles", MODE_PRIVATE).getString("title","柯林斯五星级单词:a").split(":");

            WordNode.startThread(
                    getActivity(),
                    webView,
                    getRAW2(tempTitle[0]),
                    tempTitle[1],
                    0,
                    7);
        }

    /**
     * 通过星级获取相应的本地文件
     * @param starText
     * @return
     */
    public int getRAW2(String starText){
        switch (starText){
            case"柯林斯五星级单词":
                return R.raw.s5;
            case"柯林斯四星级单词":
                return R.raw.s4;
            case"柯林斯三星级单词":
                return R.raw.s3;
            case"柯林斯二星级单词":
                return R.raw.s2;
            case"柯林斯一星级单词":
                return R.raw.s1;
        }
        return R.raw.s5;
    }

    //    /**
//     * 通过星级获取相应的本地文件
//     * @param starText
//     * @return
//     */
//   public int getRAW(String starText){
//        switch (starText){
//            case"★★★★★":
//                return R.raw.s5;
//            case"★★★★☆":
//                return R.raw.s4;
//            case"★★★☆☆":
//                return R.raw.s3;
//            case"★★☆☆☆":
//                return R.raw.s2;
//            case"★☆☆☆☆":
//                return R.raw.s1;
//        }
//        return R.raw.s5;
//    }


}

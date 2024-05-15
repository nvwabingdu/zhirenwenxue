package com.example.zrwenxue.moudel.main.word.tab;

import static android.content.Context.MODE_PRIVATE;

import androidx.recyclerview.widget.RecyclerView;


import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.word.MessageEvent;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;
import com.example.zrwenxue.moudel.main.word.WordFromShardToSingleton;
import com.example.zrwenxue.moudel.main.word.Wordbean;
import com.example.zrwenxue.moudel.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-03-24
 * Time: 20:53
 */
public class OneFragment extends BaseFragment {


    @Override
    protected int setLayout() {
        return R.layout.fragment_word_one_fragment;
    }

    RecyclerView recyclerView;

    @Override
    protected void initView() {
        recyclerView = fvbi(R.id.fragment_word_one_fragment_recyclerview);
    }



    WordAdapter wordAdapter;

    /**
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

        String[] tempTitle = getActivity().getSharedPreferences("titles", MODE_PRIVATE).getString("title", "柯林斯五星级单词:a").split(":");

        ArrayList<Wordbean> tempList = null;
        try {
            tempList = getWordBeanArrayList(
                    toStar(tempTitle[0]),
                    tempTitle[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wordAdapter = new WordAdapter(getActivity(), tempList);
        MyStatic.setRecyclerViewGridView(wordAdapter, recyclerView, getActivity(), 3, true);
    }

    /**
     * evenbus的解绑定
     */
    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * eventbus----处理事件
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {

        String[] tempTitle = getActivity().getSharedPreferences("titles", MODE_PRIVATE).getString("title", "柯林斯五星级单词:a").split(":");

        ArrayList<Wordbean> tempList = null;
        try {
            tempList = getWordBeanArrayList(
                    toStar(tempTitle[0]),
                    tempTitle[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wordAdapter = new WordAdapter(getActivity(), tempList);
        MyStatic.setRecyclerViewGridView(wordAdapter, recyclerView, getActivity(), 3, true);
    }

    /**
     * 通过星级获取相应的本地文件
     *
     * @param starText
     * @return
     */
    public String toStar(String starText) {
        switch (starText) {
            case "柯林斯五星级单词":
                return "★★★★★";
            case "柯林斯四星级单词":
                return "★★★★☆";
            case "柯林斯三星级单词":
                return "★★★☆☆";
            case "柯林斯二星级单词":
                return "★★☆☆☆";
            case "柯林斯一星级单词":
                return "★☆☆☆☆";
        }
        return "★★★★★";
    }


    /**
     * 通过本地文件查找单词 后期需要优化(比如查找a后面就不用查找完了)
     *
     * @param starNum     柯林斯星级
     * @param firstLetter 根据首字母查询
     * @throws Exception
     */
    public ArrayList<Wordbean> getWordBeanArrayList(String starNum, String firstLetter) throws Exception {
        ArrayList<Wordbean> arrayList = new ArrayList<>();
        int num = Singleton.getInstance().wordbeanArrayList.size();
        if (num == 0) {
            WordFromShardToSingleton.getWordBeanList(getContext());//把raw中的数据装入单例集合
        }
        for (int i = 0; i < Singleton.getInstance().wordbeanArrayList.size(); i++) {
            boolean conditionTwo = Singleton.getInstance().wordbeanArrayList.get(i).getWord_star().equals(starNum);

            boolean conditionOne = Singleton.getInstance().wordbeanArrayList.get(i).getWord().startsWith(firstLetter);
            //优化，如果ASCII码大于查找首字母 就退出
            if (MyStatic.getChartoASCII(firstLetter) < MyStatic.getChartoASCII(Singleton.getInstance().wordbeanArrayList.get(i).getWord())) {
                break;
            }

            if (conditionOne && conditionTwo) {//添加判断星级和字母的条件
                arrayList.add(Singleton.getInstance().wordbeanArrayList.get(i));
            }
        }
        return WordFromShardToSingleton.getReplaceTimesList(getContext(), arrayList);
    }

}

package com.example.zrwenxue.moudel.main.word;


import static android.content.Context.MODE_PRIVATE;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.newzr.R;
import com.example.zrtool.utils.ToastUtils;
import com.example.zrwenxue.app.TitleBarView;
import com.example.zrwenxue.moudel.main.word.pickerandpop.CommonPopWindow;
import com.example.zrwenxue.moudel.main.word.pickerandpop.PickerScrollView;
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.ViewPagerFragmentAdapter;
import com.example.zrwenxue.moudel.main.word.tab.OneFragment;
import com.example.zrwenxue.moudel.main.word.tab.TwoFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-08
 * Time: 9:26
 */
public class WordFragment extends Fragment {
    View view;
    ImageView imgview, up, down, cleanAll,show_yb;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word, null);
        setTopView();
        return view;
    }

    /**
     * 设置顶部
     */
    TitleBarView topView;
    private void setTopView() {
        topView = view.findViewById(R.id.title_view);
        topView.setTitle("手写单词");
        //左边返回
        topView.setOnclickLeft(View.INVISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //右边弹出pop
        topView.setOnclickRight(View.VISIBLE, getResources().getDrawable(R.drawable.hp_icon_search), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(requireActivity(), "点击搜索", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();//初始化数据
        initCurrentWordTaglist();//装载集合
        setTabLayout_viewpager_fragment();//tab+fragment设置
    }

    @Override
    public void onResume() {
        super.onResume();

        String tempTitle = getActivity().getSharedPreferences("titles", MODE_PRIVATE).getString("title", "柯林斯五星级单词:a");
        topView.setTitle(tempTitle);
    }

    /**
     * 初始化控件等数据
     */
    public void initView() {
        imgview = view.findViewById(R.id.img_word_select);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //滚轮
                setAddressSelectorPopup(v);
            }
        });


        //显示音标
        show_yb=view.findViewById(R.id.img_word_show_yb);

        show_yb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startRotation(show_yb, 1, 500);// 旋转3次，时长为1秒
                 //显示音标和不显示音标
                 if (Singleton.getInstance().showSoundMark) {
                     Singleton.getInstance().showSoundMark = false;
                 } else {
                     Singleton.getInstance().showSoundMark = true;
                 }
                 turnON();
             }
         });


        //向下翻页
        up = view.findViewById(R.id.img_word_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnUP();
            }
        });

        //向上翻页
        down = view.findViewById(R.id.img_word_down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnDOWN();
            }
        });

        //清除所有记录
        cleanAll = view.findViewById(R.id.img_word_clean_all);
        cleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanAllRemenber();
            }
        });

    }

    /**
     * 清空所有记录
     */
    private void cleanAllRemenber() {
        AlertDialog alertDialog2 = new AlertDialog.Builder(getContext())
                .setTitle("智人")
                .setMessage("您确定清空所有记录吗？清空之后不可逆。")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WordFromShardToSingleton.setShardWordTimes(getActivity(), "");
                        
                        ToastUtils.INSTANCE.showCenterToast(getActivity(),"您已清空操作记录，重启APP之后生效");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alertDialog2.show();
    }

    ArrayList<String> currentWordTaglist = new ArrayList<>();

    void initCurrentWordTaglist() {
        //装入集合 便于上下选择
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 26; j++) {
                if (i == 0) {
                    currentWordTaglist.add("柯林斯五星级单词" + ":" + (char) (97 + j));
                }
                if (i == 1) {
                    currentWordTaglist.add("柯林斯四星级单词" + ":" + (char) (97 + j));
                }
                if (i == 2) {
                    currentWordTaglist.add("柯林斯三星级单词" + ":" + (char) (97 + j));
                }
                if (i == 3) {
                    currentWordTaglist.add("柯林斯二星级单词" + ":" + (char) (97 + j));
                }
                if (i == 4) {
                    currentWordTaglist.add("柯林斯一星级单词" + ":" + (char) (97 + j));
                }
            }
        }


        /**
         *滚动文字数据设置
         */
        for (int i = 1; i <= 26; i++) {//滚动选择字母数据
            String word = (char) (96 + i) + "";
            letterList.add(word);
        }
        letterList.add("z");

        starList.add("柯林斯五星级单词");//滚动选择词典数据
        starList.add("柯林斯四星级单词");
        starList.add("柯林斯三星级单词");
        starList.add("柯林斯二星级单词");
        starList.add("柯林斯一星级单词");
        starList.add("柯林斯一星级单词");
    }

    /**
     * 开启音标还是关闭
     */
    private void turnON() {
        //发送eventbus普通事件
        onSaveShared();
        EventBus.getDefault().post(new MessageEvent("ok"));
    }

    /**
     * 向下按的时候
     */
    private void turnDOWN() {
        String text = topView.getTitle();
        int currentIndex = currentWordTaglist.indexOf(text);
        if (currentIndex == currentWordTaglist.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex = currentIndex + 1;
        }
        topView.setTitle(currentWordTaglist.get(currentIndex));

        MyStatic.showLog(currentIndex + "");

        //发送eventbus普通事件
        onSaveShared();
        EventBus.getDefault().post(new MessageEvent("ok"));
    }

    /**
     * 向上按的时候
     */
    private void turnUP() {
        String text = topView.getTitle();
        int currentIndex = currentWordTaglist.indexOf(text);
        if (currentIndex == 0) {
            currentIndex = currentWordTaglist.size() - 1;
        } else {
            currentIndex = currentIndex - 1;
        }
        topView.setTitle(currentWordTaglist.get(currentIndex));

        MyStatic.showLog(currentIndex + "");

        //发送eventbus普通事件
        onSaveShared();
        EventBus.getDefault().post(new MessageEvent("ok"));
    }

    /**
     * ====================================================设置setTabLayout  + fragment
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public void setTabLayout_viewpager_fragment() {
        mViewPager = view.findViewById(R.id.fragment_word_viewpager);
        mTabLayout = view.findViewById(R.id.fragment_word_tabs);
        //tab文字的集合
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("单词");
        mTitleList.add("词意");

        ViewPagerFragmentAdapter webAdapter =
                new ViewPagerFragmentAdapter(getActivity().getSupportFragmentManager(),
                        getFragmentList(),
                        mTitleList);//数组转化为集合
        //给ViewPager设置适配器
        mViewPager.setAdapter(webAdapter);

        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(webAdapter);
    }

    /**
     * @return 获取带参数的fragment集合 用于装载在viewpager里面
     */
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public ArrayList<Fragment> getFragmentList() {
        OneFragment oneFragment = new OneFragment();
        fragmentArrayList.add(oneFragment);

        TwoFragment twoFragment = new TwoFragment();
        fragmentArrayList.add(twoFragment);

        return fragmentArrayList;
    }

    /**
     * ================================================================底部滚动弹出窗口
     */
    public ArrayList<String> starList = new ArrayList<>();
    public ArrayList<String> letterList = new ArrayList<>();
    public String mLetter = "a", mStar = "柯林斯五星级单词";//默认值

    public void setAddressSelectorPopup(View v) {
        /**
         * 弹出窗设置
         */
        CommonPopWindow.newBuilder()
                .setView(R.layout.word_pop)
                .setBackgroundDrawable(new BitmapDrawable())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(getResources().getDisplayMetrics().heightPixels * 0.3f))
                .setViewOnClickListener(new CommonPopWindow.ViewClickListener() {
                    @Override
                    public void getChildView(PopupWindow mPopupWindow, View view, int mLayoutResId) {
                        getChildView_(mPopupWindow, view, mLayoutResId);
                    }
                })
                .setBackgroundDarkEnable(true)
                .setBackgroundAlpha(0.7f)
                .setBackgroundDrawable(new ColorDrawable(999999))
                .build(getActivity())
                .showAsBottom(v);
    }

    /**
     * 承接上面的子控件点击监听
     *
     * @param mPopupWindow
     * @param view
     * @param mLayoutResId
     */
    public void getChildView_(final PopupWindow mPopupWindow, View view, int mLayoutResId) {

        if (mLayoutResId == R.layout.word_pop) {


            PickerScrollView starSelector = view.findViewById(R.id.star_select);
            starSelector.setData(starList);
            starSelector.setSelected(0);// 设置数据，默认选择第一条
            /**
             * 滚动监听事件
             */
            starSelector.setOnSelectListener(new PickerScrollView.onSelectListener() {
                @Override
                public void onSelect(String pickers) {
                    mStar = pickers;
                }
            });

            PickerScrollView letterSelector = view.findViewById(R.id.letter_select);
            letterSelector.setData(letterList);
            letterSelector.setSelected(0);// 设置数据，默认选择第一条
            /**
             * 滚动监听事件
             */
            letterSelector.setOnSelectListener(new PickerScrollView.onSelectListener() {
                @Override
                public void onSelect(String pickers) {
                    mLetter = pickers;
                }
            });

            /**
             * 完成按钮点击事件
             */
            TextView imageBtn = view.findViewById(R.id.img_ok);
            imageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //弹窗消失
                    mPopupWindow.dismiss();

                    Log.e("tag", "" + mStar + "========" + mLetter);

                    topView.setTitle(mStar + ":" + mLetter);//设置标题

                    //发送eventbus普通事件
                    onSaveShared();
                    EventBus.getDefault().post(new MessageEvent("ok"));
                }
            });
        }
    }


    /**
     * 保存共享参数
     */
    private void onSaveShared() {
        SharedPreferences shared = getActivity().getSharedPreferences("titles", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit(); //获得编辑器的对象
        editor.putString("title", topView.getTitle());
        editor.commit();
    }


    /**
     * ====================================================变换数据
     *
     * @param text
     * @return
     */
    public String getmStar(String text) {
        switch (text) {
            case "柯林斯一星级单词":
                return "★☆☆☆☆";
            case "柯林斯二星级单词":
                return "★★☆☆☆";
            case "柯林斯三星级单词":
                return "★★★☆☆";
            case "柯林斯四星级单词":
                return "★★★★☆";
            case "柯林斯五星级单词":
                return "★★★★★";
        }
        return "";
    }

    private void startRotation(ImageView imageView, int rotationCount, int duration) {
        // 计算旋转角度
        float fromDegree = 0.0f;
        float toDegree = 360.0f * rotationCount;

        // 创建旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(duration); // 设置旋转时长
        rotateAnimation.setFillAfter(true); // 让ImageView保持在最后一帧旋转的状态

        // 启动旋转动画
        imageView.startAnimation(rotateAnimation);
    }

}

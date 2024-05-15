package com.example.zrwenxue.moudel.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.example.newzr.R;
import com.example.zrdrawingboard.DoodleViewActivity;

import com.example.zrwenxue.moudel.main.home.anims.MyAnimActivity;
import com.example.zrwenxue.moudel.main.home.eng.SearchWordActivity;
import com.example.zrwenxue.moudel.main.home.led.LEDActivity;
import com.example.zrwenxue.moudel.main.home.lottery.LotteryActivity;
import com.example.zrwenxue.moudel.main.home.news.newsitem.NewsActivity;
import com.example.zrwenxue.moudel.main.home.phrase.PhraseActivity;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.WordFromShardToSingleton;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-13
 * Time: 13:50
 */
public class HomeFragment extends Fragment {
    View view;
    Banner banner;
    RecyclerView recyclerView1, recyclerView2;
    EditText home_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置banner
//        setBanner();
        //初始化适配器需要的数据
        initDate();
        //设置两个适配器
        setRecyclerView();


        //初始化单例集合
        try {
            WordFromShardToSingleton.getWordBeanList(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setBanner();
    }

    /**
     * ----------------------------------设置两个适配器的数据
     */
    ArrayList<HomeBean> list1 = new ArrayList<>();
    ArrayList<HomeBean> list2 = new ArrayList<>();

    private void initDate() {
        home_edit = view.findViewById(R.id.home_fragment_edit);
        home_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转查找单词页面
                MyStatic.setActivityString(getActivity(), SearchWordActivity.class, "", "");

            }
        });


        //第一个适配器需要的数据
        list1.add(new HomeBean("幸运彩票", MyStatic.IntToDrawable(getContext(), R.drawable.home_luck)));
        list1.add(new HomeBean("短语学习", MyStatic.IntToDrawable(getContext(), R.drawable.home_phrase)));
        list1.add(new HomeBean("涂鸦画板", MyStatic.IntToDrawable(getContext(), R.drawable.home_handwritten)));
        list1.add(new HomeBean("LED", MyStatic.IntToDrawable(getContext(), R.drawable.home_ied)));
//        list1.add(new HomeBean("四级六级", MyStatic.IntToDrawable(getContext(), R.drawable.home_four)));
//        list1.add(new HomeBean("出国留学", MyStatic.IntToDrawable(getContext(), R.drawable.home_abroad)));
//        list1.add(new HomeBean("阅读电影", MyStatic.IntToDrawable(getContext(), R.drawable.home_film)));
//        list1.add(new HomeBean("全球热点", MyStatic.IntToDrawable(getContext(), R.drawable.home_world)));

        //第二个适配器需要的数据
        list2.add(new HomeBean("if i had a single flower for every time i think about you, i could walk forever in my garden.", MyStatic.IntToDrawable(getContext(), R.drawable.home_1)));
        list2.add(new HomeBean("I am a slow walker,but I never walk backwards.", MyStatic.IntToDrawable(getContext(), R.drawable.home_2)));
        list2.add(new HomeBean("Do one thing at a time, and do well.", MyStatic.IntToDrawable(getContext(), R.drawable.home_3)));
        list2.add(new HomeBean("I will return, find you ,love you ,marry you and live without shame.", MyStatic.IntToDrawable(getContext(), R.drawable.home_4)));
    }

    /**
     * 设置两个适配器
     */
    public void setRecyclerView() {
        //横向grid
        recyclerView1 = view.findViewById(R.id.fragment_home_RecyclerView1);
        HomeAdapter homeAdapter = new HomeAdapter(getContext(), list1);
        //通过回调拿到position
        homeAdapter.homeAdapterCallback(new HomeAdapter.HomeAdapterInterface() {
            @Override
            public void onclick(int position) {
                upRecyclerViewOnclick(position);
            }
        });
        MyStatic.setRecyclerViewGridView(homeAdapter, recyclerView1, getContext(), 4, true);


        //纵向
        recyclerView2 = view.findViewById(R.id.fragment_home_RecyclerView2);
        HomeAdapter2 homeAdapter2 = new HomeAdapter2(getContext(), list2);
        homeAdapter2.HomeAdapter2CallBack(new HomeAdapter2.HomeAdapterInterfaceTWO() {
            @Override
            public void onclick(int position) {
                downRecyclerViewOnclick(position);
            }
        });
        MyStatic.setRecyclerView(homeAdapter2, recyclerView2, getContext(), true);



        //插入
        view.findViewById(R.id.rl_stroll_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStatic.setActivityString(getActivity(), DoodleViewActivity.class,"","");
            }
        });

        view.findViewById(R.id.rl_stroll_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStatic.setActivityString(getActivity(), LEDActivity.class,"","");
            }
        });

        view.findViewById(R.id.rl_stroll_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStatic.setActivityString(getActivity(), LotteryActivity.class, "", "");
            }
        });
    }


    /**
     * 上半部分recycleview点击事件
     *
     * @param position
     */
    void upRecyclerViewOnclick(int position) {
        switch (position) {
            case 0:
                MyStatic.setActivityString(getActivity(), LotteryActivity.class, "", "");
                break;
            case 1:
                MyStatic.setActivityString(getActivity(), PhraseActivity.class, "", "");
                break;
            case 2:
                MyStatic.setActivityString(getActivity(), DoodleViewActivity.class,"","");
                break;
            case 3:
//                MyStatic.showToast(getContext(), "开发中，敬请期待……");
                MyStatic.setActivityString(getActivity(), LEDActivity.class,"","");
                break;
            case 4:
                MyStatic.showToast(getContext(), "开发中，敬请期待……");
                MyStatic.setActivityString(getActivity(), MyAnimActivity.class, "", "");
                break;
            case 5:
                MyStatic.showToast(getContext(), "开发中，敬请期待……");
//                MyStatic.setActivityString(getActivity(), MyServiceActivity.class,"","");
                break;
            case 6:
                MyStatic.showToast(getContext(), "开发中，敬请期待……");
//                MyStatic.setActivityString(getActivity(), KlsWordActivity.class,"","");
                break;
            case 7:
                MyStatic.setActivityString(getActivity(), NewsActivity.class, "", "");
                break;
        }
    }

    /**
     * 下半部分recycleview点击事件
     *
     * @param position
     */
    void downRecyclerViewOnclick(int position) {
        MyStatic.showToast(getContext(), "点击了下半部分的--" + position);
    }


    /**
     * -----------------------------------------banner使用
     */
    List<DataBean> images = new ArrayList<>();
    List titles = new ArrayList<>();

    class DataBean {
        public DataBean(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        String imageUrl = "";

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    private void setBanner() {
        banner = view.findViewById(R.id.banner);

        //添加图片资源
        images.add(new DataBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2Fb9977de1ac58097989ddac2dbdf8cd8388020910.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654137685&t=402566c0a4a385032f4665e5d236ecdb"));
        images.add(new DataBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp3.itc.cn%2Fimages01%2F20210425%2F4d3f299bd88845a6934a267a30e8ad0a.png&refer=http%3A%2F%2Fp3.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654137685&t=ad5fbfe6ea04e9f2a075b68480e634a0"));
        images.add(new DataBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F9c66eab1e2a7ddbec554d281603e77d449f1588b.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654137685&t=f0dd08482a45b435a1d9aa94a7c4cecc"));
        images.add(new DataBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2Fa7e0094b246bfa0e74f773c5c5ab9c5a3f48b4f2.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654137685&t=3b97ba3a9c60295f47061b0927dc5a3f"));
        images.add(new DataBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F017dd56063d57d11013e87f4a2ee3a.jpg%401280w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654137685&t=77fc0282633e21223a1573171ca41432"));
        //添加标题
        titles.add("图片1");
        titles.add("图片2");
        titles.add("图片3");
        titles.add("图片4");
        titles.add("图片5");



        banner.setAdapter(new BannerImageAdapter<DataBean>(images) {
                    @Override
                    public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                        //图片加载自己实现
                        Glide.with(holder.itemView)
                                .load(data.imageUrl)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);
                    }
                })
                .addBannerLifecycleObserver(getActivity())//添加生命周期观察者
                .setIndicator(new CircleIndicator(getActivity()));

        banner.isAutoLoop(true);


        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();
            }
        });


    }

}

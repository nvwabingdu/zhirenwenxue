//package cn.dreamfruits.yaoguo.module.main.diy.pagebanner;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.youth.banner.Banner;
//import com.youth.banner.listener.OnBannerListener;
//import com.youth.banner.listener.OnPageChangeListener;
//import com.youth.banner.transformer.AlphaPageTransformer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.dreamfruits.yaoguo.R;
//import cn.dreamfruits.yaoguo.module.main.diy.DiyViewModel;
//import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionListByTypeState;
//import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity;
//import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean;
//import cn.dreamfruits.yaoguo.util.Singleton;
//
///**
// * @Author qiwangi
// * @Date 2023/6/19
// * @TIME 16:03
// */
//public class DiyPageBannerFragment extends Fragment {
//    private DiyViewModel diyViewModel;
//
//    Long diyBannerId = 0L;//diybanner页面的id
//    List<GetStyleVersionListByTypeBean.Item>  diyBannerList= new ArrayList();//diybanner页面的数据
//    View mRootView;
//    ZrBannerAdapter zrBannerAdapter;
//    private int requestState=0;//0 刷新  1 加载  2预加载
//    Banner mBanner1;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.diy_banner_page, container, false);
//        diyViewModel = new ViewModelProvider(this).get(DiyViewModel.class);
//        return mRootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //使用反射 修改banner的不可无限轮播
//         init();
//    }
//
//    //设置轮播
//    private void init() {
//        /**
//         * 设置banner的请求回调
//         */
//        diyViewModel.getGetStyleVersionListByTypeState().observe(this, new Observer<GetStyleVersionListByTypeState>() {
//            @Override
//            public void onChanged(GetStyleVersionListByTypeState it) {
//                    if (it instanceof GetStyleVersionListByTypeState.Success) {
//                        switch (requestState){
//                            case 0:
//                                Log.e("zqr1111   0  ",diyViewModel.getMGetStyleVersionListByTypeBean().toString());
//                                diyBannerList.clear();//清空集合
//                                diyBannerList = diyViewModel.getMGetStyleVersionListByTypeBean().getList();//加载请求后的数据
//                                zrBannerAdapter.setBannerData(diyBannerList, true);//设置数据 更新适配器
//                                if (diyBannerList.size() > 0) {
//                                    diyBannerId = diyBannerList.get(0).getId();//给ID赋值
//                                }
//                                break;
//                            case 1:
//                                Log.e("zqr1111   1  ",diyViewModel.getMGetStyleVersionListByTypeBean().toString());
//                                zrBannerAdapter.setBannerData(diyViewModel.getMGetStyleVersionListByTypeBean().getList(), false);//设置数据 更新适配器
//                                break;
//                        }
//                    } else if (it instanceof GetStyleVersionListByTypeState.Fail) {
//                        // Handle fail state
//                        Log.e("zqr1111   ",it.toString());
//                        Singleton.INSTANCE.isNetConnectedToast(requireActivity());
//                    }
//                }
//        });
//
//
//        mBanner1 = mRootView.findViewById(R.id.diy_banner_page);
//        zrBannerAdapter=new ZrBannerAdapter(diyBannerList);
//        mBanner1.setAdapter(zrBannerAdapter);
//        mBanner1.setBannerGalleryEffect(25, 12);
//        mBanner1.addPageTransformer(new AlphaPageTransformer());//透明
//        mBanner1.isAutoLoop(false);
//
//        // TODO: 2023/6/28  会导致banner加载头尾数据 角标越界
////        try {
//////            Class<?> clazz = mBanner1.getClass();
////            Field field = mBanner1.getClass().getDeclaredField("mIsInfiniteLoop");
////            field.setAccessible(true);
////            field.setBoolean(mBanner1, false);
////        }catch (Exception e){
////            Log.e("zqr  反射Exception",e.toString());
////        }
////
////        Log.e("zqr  反射之后的无限轮播：",mBanner1.isInfiniteLoop()+"");
//
//
//        /**
//         * 点击banner图片
//         */
//        mBanner1.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(Object data, int position) {
//                starUnity();
//            }
//        });
//
//        mBanner1.addOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                Log.e("zqr  onPageScrolled",   "diyBannerId："+diyBannerId+ "  position："+position);
//            }
//            @Override
//            public void onPageSelected(int position) {
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//        //点击退出
//        mRootView.findViewById(R.id.diy_page_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mInterface.onBack();
//            }
//        });
//        /**
//         * 点击确认按钮
//         */
//        mRootView.findViewById(R.id.diy_page_confirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                starUnity();
//            }
//        });
//    }
//
//    /**
//     * 跳转到unity
//     */
//    private void  starUnity(){
//        try {
//            if (mBanner1!=null&&diyBannerList.size()>0) {
//                Log.e("zqr  starUnity",   "diyBannerList.size"+diyBannerList.size()+ "  mBanner1.getCurrentItem()："+mBanner1.getCurrentItem());
//                diyBannerId=diyBannerList.get(mBanner1.getCurrentItem()).getId();
//                Singleton.INSTANCE.setNewScene(true);
//                Intent intent = new Intent(requireActivity(), AndroidBridgeActivity.class);
//                intent.putExtra("entryType", "EnterDIY");
//                intent.putExtra("data", diyBannerId + "");
//                startActivity(intent);
//            }else {
//                Toast.makeText(requireActivity(),"数据有误，请关闭此页面重试……",Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            // TODO: 2023/6/28 临时处理   出错 默认用第一条
//            diyBannerId=diyBannerList.get(0).getId();
//
//            Singleton.INSTANCE.setNewScene(true);
//            Intent intent = new Intent(requireActivity(), AndroidBridgeActivity.class);
//            intent.putExtra("entryType", "EnterDIY");
//            intent.putExtra("data", diyBannerId + "");
//            startActivity(intent);
//        }
//
//    }
//
//
//    //activity通知更新适配器
//
//    /**
//     * activity的通知 更新banner适配器 相当于请求
//     * @param id
//     */
//    public void upDateAdapter(Long id,int type){
//        Log.e("zqr  upDateAdapter",   "id："+id);
//        requestState=0;//设置为加载
//        diyViewModel.getStyleVersionListByType(
//                id,
//                10,
//                null
//        );
//    }
//
//    //回调
//    public interface InnerInterface {
//        void onConfirm();
//
//        void onBack();
//
//        void upDate();
//    }
//
//    InnerInterface mInterface = null;
//
//    public void setDiyPageBannerFragmentCallBack(InnerInterface mInterface) {
//        this.mInterface = mInterface;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mInterface = null;
//        mBanner1=null;
//    }
//}

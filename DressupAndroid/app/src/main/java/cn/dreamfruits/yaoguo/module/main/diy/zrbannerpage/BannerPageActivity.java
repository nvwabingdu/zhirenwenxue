//package cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.dreamfruits.yaoguo.R;
//import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.library.CardScaleHelper;
//
//public class BannerPageActivity extends Activity {
//    private RecyclerView mRecyclerView;
//
//
//    private List<Integer> mList = new ArrayList<>();
//    private CardScaleHelper mCardScaleHelper = null;
//    private Runnable mBlurRunnable;
//    private int mLastPos = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//
//        setContentView(R.layout.diy_banner_page);
//        init();
//    }
//
//    private void init() {
//        for (int i = 0; i < 10; i++) {
//            mList.add(R.drawable.pic4);
//            mList.add(R.drawable.pic5);
//            mList.add(R.drawable.pic6);
//        }
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setAdapter(new CardAdapter(mList));
//        // mRecyclerView绑定scale效果
//        mCardScaleHelper = new CardScaleHelper();
//        mCardScaleHelper.setCurrentItemPos(2);
//        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
//
//        initBlurBackground();
//    }
//
//    private void initBlurBackground() {
////        mBlurView = (ImageView) findViewById(R.id.blurView);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    notifyBackgroundChange();
//                }
//            }
//        });
//
//        notifyBackgroundChange();
//    }
//
//    private void notifyBackgroundChange() {
//        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
//        mLastPos = mCardScaleHelper.getCurrentItemPos();
//        final int resId = mList.get(mCardScaleHelper.getCurrentItemPos());
////        mBlurView.removeCallbacks(mBlurRunnable);
//        mBlurRunnable = new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
////                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
//            }
//        };
////        mBlurView.postDelayed(mBlurRunnable, 500);
//    }
//
//}

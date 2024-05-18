package cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.onlyimg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.base.ImageLoader;
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsActivity;
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean;
import cn.dreamfruits.yaoguo.util.Singleton;

/**
 * @Author qiwangi
 * @Date 2023/4/10
 * @TIME 12:10
 */
public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private Long feedId;
    private int coverHeight;
    private  int coverWidth;

    private List<WaterfallFeedBean.Item.Info.PicUrl> imagesUrl=new ArrayList<>();


    public MyPagerAdapter(Context context, Long feedId, int coverHeight, int coverWidth) {
        this.context = context;
        this.feedId = feedId;
        this.coverHeight = coverHeight;
        this.coverWidth = coverWidth;
    }

    public MyPagerAdapter(Context context) {
        this.context = context;
    }

    public void setImages(List<WaterfallFeedBean.Item.Info.PicUrl> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        ImageLoader.INSTANCE.loadImage2(
                imagesUrl.get(position).getUrl(),
                imageView,
                0,
                0
        );

        //长按保存图片
      imageView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View view) {
              Singleton.INSTANCE.showSavePhotoAndVideo(imagesUrl.get(position).getUrl(), (Activity) context,true,null);
              return false;
          }
      });

      //点击跳转到图片详情页
      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, PostDetailsActivity.class);
               intent.putExtra("feedId", feedId);
               intent.putExtra("coverHeight", coverHeight);
               intent.putExtra("coverWidth", coverWidth);
              context.startActivity(intent);
          }
      });


//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                /*.transform(GlideRoundTransform(5))*/
                .fitCenter()//缩放居中
                .skipMemoryCache(false)
                .load(imagesUrl.get(position).getUrl())
                .into(imageView);

        /*imageView.setImageResource(imagesUrl[position]);*/
        Log.e("132435453432","图片地址："+imagesUrl.get(position).getUrl());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 回调
     */
    interface MyPagerAdapterInterface {
        void onClick(int position);
    }
    private  MyPagerAdapterInterface  mMyPagerAdapterInterface;

    public void setMyPagerAdapterListener(MyPagerAdapterInterface mMyPagerAdapterInterface) {
        this.mMyPagerAdapterInterface = mMyPagerAdapterInterface;
    }

}

package cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.scaleimg

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.SaveImageOrVideo
import cn.dreamfruits.yaoguo.util.Singleton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class BannerImgFragment : Fragment() {
    private var view: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.home_fragment_banner_img, container, false)
        var img: ImageView = view!!.findViewById(R.id.child_img)//图片
//        var loveView: ImageView = view!!.findViewById(R.id.love_view)//图片
//        loveView.visibility=View.VISIBLE


        val url:String?=arguments!!.getString("url")

        //长按保存图片逻辑
        img.setOnLongClickListener {
            Singleton.showSavePhotoAndVideo(url!!,requireActivity(),true)
            true
        }
//        img.scaleType = ImageView.ScaleType.CENTER_CROP//裁切铺满

        //图片缓存？
        arguments!!.getString("url")?.let {
            ImageLoader.loadImage2(
                it,
                img,
                0,
                0
            )
        }

        Glide.with(context!!)
            .asBitmap()
            .dontAnimate()
            .fitCenter()//缩放居中
//            .centerCrop()//铺满
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            /*.transform(GlideRoundTransform(5))*/
            .skipMemoryCache(false)
            .load(arguments!!.getString("url"))
            .into(img)

        return view
    }

}
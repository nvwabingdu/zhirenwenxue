package cn.dreamfruits.yaoguo.util

import android.content.Context
import android.widget.ImageView
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.utils.ActivityCompatHelper

class GlideEngine : ImageEngine{

    companion object{
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideEngine()
        }
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context)
            .load(url)
            .into(imageView)
    }

    override fun loadImage(
        context: Context,
        imageView: ImageView,
        url: String,
        maxWidth: Int,
        maxHeight: Int
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        GlideApp.with(context)
            .load(url)
            .override(maxWidth,maxHeight)
            .into(imageView)

    }

    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context)
            .load(url)
            .override(180,180)
            .into(imageView)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        GlideApp.with(context)
            .load(url)
            .override(200,200)
            .centerCrop()
            .placeholder(com.luck.picture.lib.R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {
        GlideApp.with(context)
            .pauseAllRequests()
    }

    override fun resumeRequests(context: Context) {
        GlideApp.with(context)
            .resumeRequests()
    }
}
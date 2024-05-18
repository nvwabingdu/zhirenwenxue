package cn.dreamfruits.baselib.network.imageloader.glide

import android.content.Context
import cn.dreamfruits.baselib.network.imageloader.BaseImageLoaderStrategy
import cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl
import cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType
import cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType
import cn.dreamfruits.baselib.network.imageloader.glide.transformations.BlurTransformation
import cn.dreamfruits.baselib.network.imageloader.glide.transformations.RoundedCornersTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions


/**
 *description: Glide实现的图片加载.
 *@date 2018/10/11 19:02.
 *@author: YangYang.
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImageConfigImpl> {


    override fun loadImage(ctx: Context, configImpl: ImageConfigImpl) {

        val glideRequest = GlideApp.with(ctx)
            .asBitmap()
            .apply {

                if (!configImpl.url.isNullOrBlank()) {
                    load(CdnGlideUrl(configImpl.url))
                }

                configImpl.uri?.let {
                    load(it)
                }
                //设置缓存策略
                when (configImpl.cacheStrategy) {
                    DiskCacheStrategyType.ALL -> diskCacheStrategy(DiskCacheStrategy.ALL)

                    DiskCacheStrategyType.NONE -> diskCacheStrategy(DiskCacheStrategy.NONE)

                    DiskCacheStrategyType.RESOURCE -> diskCacheStrategy(DiskCacheStrategy.RESOURCE)

                    DiskCacheStrategyType.DATA -> diskCacheStrategy(DiskCacheStrategy.DATA)

                    DiskCacheStrategyType.AUTOMATIC -> diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                }

                //设置裁剪类型
                when (configImpl.imageLoadScaleType) {
                    ImageLoadScaleType.FitCenter -> fitCenter()

                    ImageLoadScaleType.CenterInside -> centerInside()

                    ImageLoadScaleType.CenterCrop -> centerCrop()

                    ImageLoadScaleType.CircleCrop -> circleCrop()

                    ImageLoadScaleType.NoScaleType -> {}
                }
                //设置占位符
                if (configImpl.placeholder != 0){
                    placeholder(configImpl.placeholder)
                }

                //设置错误的占位图片
                if (configImpl.errorPic != 0){
                    error(configImpl.errorPic)
                }

                //高斯模糊
                if (configImpl.blur){
                    transform(BlurTransformation(configImpl.blurRadius,configImpl.sampling))
                }

                //圆角
                if (configImpl.corner){
                   transform(RoundedCornersTransformation(configImpl.cornerRadius,configImpl.margin,configImpl.cornerType))
                }

                //设置宽高
                configImpl.size?.let {
                    if (it.size > 1 && it[0] > 0 && it[1] > 0){
                        override(it[0],it[1])
                    }
                }

                //渐入效果
                if (configImpl.useCrossFade){
                   transition(BitmapTransitionOptions.withCrossFade())
                }

                //缩略图
                thumbnail(configImpl.thumbnail)
            }

        configImpl.imageView?.let {
            glideRequest.into(it)
        }

    }

    override fun clear(ctx: Context, configImpl: ImageConfigImpl) {


    }

}
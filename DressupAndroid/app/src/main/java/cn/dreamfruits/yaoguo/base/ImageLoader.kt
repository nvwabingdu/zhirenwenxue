package cn.dreamfruits.yaoguo.base

import android.widget.ImageView
import cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType
import cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl
import cn.dreamfruits.baselib.network.imageloader.ImageLoader
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * 图片加载
 */
object ImageLoader : KoinComponent {


    fun loadImage(configImpl: ImageConfigImpl) {
        val imageLoader = get<ImageLoader>()

        imageLoader.loadImage(get(), configImpl)
    }


    fun loadImage(
        path: String,
        imageView: ImageView,
        resWidth: Int = 0,
        resHeight: Int = 0
    ) {
        val imageLoader = get<ImageLoader>()

        val imageUrl = Tool().decodePicUrls(path,"0",true)

        val config = ImageConfigImpl.Builder()
            .url(imageUrl)
            .imageView(imageView)
            .cacheStrategy(DiskCacheStrategyType.RESOURCE)
            .override(resWidth, resHeight)
            .build()

        imageLoader.loadImage(get(), config)
    }

    fun loadImage2(
        path: String,
        imageView: ImageView,
        resWidth: Int = 0,
        resHeight: Int = 0
    ) {
        val imageLoader = get<ImageLoader>()
        val config = ImageConfigImpl.Builder()
            .url(path)
            .imageView(imageView)
            .cacheStrategy(DiskCacheStrategyType.RESOURCE)
            .override(resWidth, resHeight)
            .build()

        imageLoader.loadImage(get(), config)
    }

}
package cn.dreamfruits.baselib.network.imageloader

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.engine.GlideException


open class ImageConfigImpl constructor(builder: Builder) : ImageConfig() {

    var uri = builder.uri
    var resourceId = builder.resourceId
    var cacheStrategy = builder.cacheStrategy
    var size = builder.size
    var useCrossFade = builder.useCrossFade
    var blur = builder.blur//是否使用高斯模糊
    var blurRadius = builder.blurRadius
    var sampling = builder.sampling
    var corner = builder.corner//是否使用圆角
    var cornerRadius = builder.cornerRadius
    var cornerType = builder.cornerType
    var margin = builder.margin
    var imageLoadScaleType = builder.imageLoadScaleType//ScaleType
    var success = builder.success
    var failed = builder.failed
    var progress = builder.progress
    var thumbnail = builder.thumbnail
    var failedCallBack = builder.failedCallBack
    var requestSuccessCallBack = builder.requestSuccessCallBack

    init {
        this.url = builder.url
        this.imageView = builder.imageView
        this.placeholder = builder.placeholder
        this.errorPic = builder.errorPic
    }


    class Builder {
        var url: String? = null //图片的url
        var uri: Uri? = null //图片的uri
        var resourceId: Int? = null //图片的资源ID
        var imageView: ImageView? = null //ImageView
        var placeholder: Int = 0  //占位图
        var errorPic: Int = 0 //错误图
        var cacheStrategy: DiskCacheStrategyType = DiskCacheStrategyType.ALL //磁盘缓存方式默认为全部缓存
        var size: IntArray? = null //自定义大小
        var useCrossFade: Boolean = true//是否使用渐入效果
        var blur: Boolean = false//是否使用高斯模糊
        var blurRadius: Int = 0 //使用高斯模糊的模糊度 0-25
        var sampling: Int = 0 //使用高斯模糊的采样，即缩放
        var corner: Boolean = false//是否使用圆角
        var cornerRadius: Int = 0 //圆角的角度px
        var cornerType: CornerType = CornerType.ALL //圆角类型，默认是四个角都有
        var margin: Int = 0 // 圆角加载的边距
        var imageLoadScaleType: ImageLoadScaleType = ImageLoadScaleType.CenterCrop //图片显示的类型，默认为CenterCrop
        var thumbnail: Float = 1.0F // 缩略图大小
        var success: ((bitmap: Bitmap?) -> Unit)? = null //图片加载成功的回调
        var failed: (() -> Unit)? = null //图片加载失败的回调
        var progress: ((url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)? =
            null //图片加载进度的回调
        var failedCallBack: ((GlideException) -> Unit)? = null

        var requestSuccessCallBack: ((Bitmap?) -> Unit)? = null

        /**
         * 图片地址
         */
        fun url(url: String) = apply { this.url = url }

        /**
         * 图片地址
         */
        fun uri(uri: Uri) = apply { this.uri = uri }

        /**
         * 图片资源ID
         */
        fun resourceId(@DrawableRes resourceId: Int) = apply {
            this.resourceId = resourceId
        }

        /**
         * 缓存策略:
         * DiskCacheStrategyType.ALL
         * DiskCacheStrategyType.NONE
         * DiskCacheStrategyType.RESOURCE
         * DiskCacheStrategyType.DATA
         * DiskCacheStrategyType.AUTOMATIC
         */
        fun cacheStrategy(cacheStrategy: DiskCacheStrategyType) = apply {
            this.cacheStrategy = cacheStrategy
        }

        /**
         * 占位图
         */
        fun placeholder(@DrawableRes placeholder: Int) = apply {
            this.placeholder = placeholder
        }

        /**
         * 错误占位图
         */
        fun errorPic(@DrawableRes errorPic: Int) = apply {
            this.errorPic = errorPic
        }

        fun imageView(imageView: ImageView) = apply {
            this.imageView = imageView
        }

        /**
         * 是否使用渐入效果
         */
        fun useCrossFade(useCrossFade: Boolean) = apply {
            this.useCrossFade = useCrossFade
        }

        /**
         * 指定大小
         */
        fun override(width: Int, height: Int) = apply {
            this.size = intArrayOf(width, height)
        }

        /**
         * 高斯模糊
         * @param radius 模糊度1-25
         * @param sampling 缩放
         */
        fun blur(radius: Int, sampling: Int) = apply {
            if (radius != 0)
                this.blur = true
            this.blurRadius = radius
            this.sampling = sampling
        }

        /**
         * 圆角
         * @param radius 圆角px
         * @param cornerType 圆角类型默认四个角都有
         * @param margin
         */
        fun corner(radius: Int, cornerType: CornerType = CornerType.ALL, margin: Int = 0) = apply {
            if (radius != 0)
                corner = true
            this.cornerRadius = radius
            this.cornerType = cornerType
            this.margin = margin
        }

        //默认
        fun centerCrop() = apply {
            this.imageLoadScaleType = ImageLoadScaleType.CenterCrop
        }

        fun centerInside() = apply {
            this.imageLoadScaleType = ImageLoadScaleType.CenterInside

        }

        fun fitCenter() = apply {
            this.imageLoadScaleType = ImageLoadScaleType.FitCenter
        }

        fun circleCrop() = apply {
            this.imageLoadScaleType = ImageLoadScaleType.CircleCrop
        }

        /**
         * 不需要ScaleType
         */
        fun noScaleType() = apply {
            this.imageLoadScaleType = ImageLoadScaleType.NoScaleType
        }

        /**
         * 加载成功的回调
         */
        fun onSuccess(success: (bitmap: Bitmap?) -> Unit) = apply {
            this.success = success
        }

        /**
         * 加载失败的回调
         */
        fun onFailed(failed: () -> Unit) = apply {
            this.failed = failed
        }

        /**
         * 加载进度的回调
         */
        fun onProgress(progress: (url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit) =
            apply {
                this.progress = progress
            }

        /**
         * 原始图像百分比的缩略图
         */
        fun thumbnail(thumbnail: Float) = apply {
            this.thumbnail = thumbnail
        }

        fun onFailedCallBack(callBack: ((GlideException) -> Unit)) = apply {
            failedCallBack = callBack
        }

        fun onRequestSuccessCallBack(callBack: (Bitmap?) -> Unit) = apply {
            requestSuccessCallBack = callBack
        }

        fun build(): ImageConfigImpl = ImageConfigImpl(this)
    }
}
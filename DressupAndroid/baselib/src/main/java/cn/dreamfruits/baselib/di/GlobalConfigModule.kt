package cn.dreamfruits.baselib.di

import cn.dreamfruits.baselib.network.GlobalHttpHandler
import cn.dreamfruits.baselib.network.imageloader.BaseImageLoaderStrategy
import cn.dreamfruits.baselib.network.imageloader.ImageLoader
import cn.dreamfruits.baselib.network.imageloader.glide.GlideImageLoaderStrategy
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

const val KODEIN_MODULE_GLOBALCONFIG_TAG = "koin_module_globecinfig_tag"

class GlobalConfigModule private constructor(builder: Builder) {
    private var mApiUrl: HttpUrl? = null
    private var mHandler: GlobalHttpHandler? = null
    private var mInterceptors: MutableList<Interceptor>? = null
    private var mCacheFile: File? = null

    val globalConfigModule = module {
        named(KODEIN_MODULE_GLOBALCONFIG_TAG)

        single { mApiUrl }

        single { mHandler }

        mInterceptors?.let {
            single { mInterceptors }
        }

        single { mCacheFile }

        single { GlideImageLoaderStrategy() } bind BaseImageLoaderStrategy::class

        single { ImageLoader(get()) }
    }

    init {
        mApiUrl = builder.apiUrl ?: "https://api.github.com/".toHttpUrl()
        mHandler = builder.handler ?: GlobalHttpHandler.EMPTY
        mInterceptors = builder.interceptors
        mCacheFile = builder.cacheFile
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var apiUrl: HttpUrl? = null
        var handler: GlobalHttpHandler? = null
        var interceptors: MutableList<Interceptor>? = null
        var cacheFile: File? = null

        fun baseurl(baseUrl: String): Builder {
            if (baseUrl.isBlank()) {
                throw IllegalArgumentException("baseUrl can not be empty")
            }
            this.apiUrl = baseUrl.toHttpUrl()
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {
            this.handler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (interceptors == null) {
                interceptors = ArrayList()
            }
            this.interceptors?.add(interceptor)
            return this
        }

        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }
}
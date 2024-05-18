package cn.dreamfruits.baselib.network.imageloader.glide

import com.bumptech.glide.load.model.GlideUrl

/**
 * 解决cdn 拼接url导致重复缓存
 */
class CdnGlideUrl(var url: String) : GlideUrl(url) {


    override fun getCacheKey(): String {
        if (url.contains("?sign=")){
            return url.substring(0,url.lastIndexOf("?sign="))
        }
        return url
    }

}
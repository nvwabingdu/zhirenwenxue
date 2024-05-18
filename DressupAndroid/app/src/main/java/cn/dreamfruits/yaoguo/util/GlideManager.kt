package cn.dreamfruits.yaoguo.util

import android.content.Context
import android.widget.ImageView
import cn.dreamfruits.yaoguo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions

/**
 * @author Lee
 * @createTime 2023-06-30 15 GMT+8
 * @desc :
 */
private var sCommonPlaceholder: Int = R.mipmap.cicrle_default
private var sRectPlaceholder: Int = R.mipmap.normal_error
fun ImageView.loadRoundImg(activity: Context?, obj: Any?) {
    try {
        val context = this.context
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(sCommonPlaceholder)
            .error(sCommonPlaceholder)
            .priority(Priority.NORMAL)
            .transform(GlideRoundImage())
        Glide.with(context)
            .load(obj)
            .apply(options)
            .into(this)
    } catch (e: Exception) {
    }
}

fun ImageView.loadRectImg(activity: Context?, obj: Any?) {
    try {
        val context = this.context
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(sRectPlaceholder)
            .error(sRectPlaceholder)
            .priority(Priority.NORMAL)
        Glide.with(context)
            .load(obj)
            .apply(options)
            .into(this)
    } catch (e: Exception) {
    }
}
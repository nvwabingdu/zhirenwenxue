package cn.dreamfruits.yaoguo.util

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.text.Editable
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Checkable
import android.widget.EditText
import android.widget.TextView
import cn.dreamfruits.yaoguo.R
import com.blankj.utilcode.util.GsonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *  author : alpha
 *  time   : 2020/01/07
 *  desc   : 描述信息
 */

/**
 * 将subscribeOn，observeOn 合并为dispatchDefault
 */
fun <T> Observable<T>.dispatchDefault(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun EditText.textStr(str: String) {
    this.text = Editable.Factory.getInstance().newEditable(str)
}

fun <T : View> T.shake(mContext: Context) {
    val animation =
        AnimationUtils.loadAnimation(mContext, R.anim.translate_checkbox_shake)
    this.startAnimation(animation)
}

fun String.phoneCall(mContext: Context) {
    mContext.startActivity(Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:${this}")
    ))
}

fun Map<String, Any>.mapRequestBody(): RequestBody = GsonUtils.getGson().toJson(this)
    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

fun TextView.addDeleteLine() {
    this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}


/**
 * 防止重复点击事件
 */
inline fun <T : View> T.singleClick(time: Long = 1000, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

//兼容点击事件设置为this的情况
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 800) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0



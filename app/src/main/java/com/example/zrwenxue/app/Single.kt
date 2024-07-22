package com.example.zrwenxue.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.center.crypt.dapter.WaterfallBean
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.Instant
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.time.Duration


/**
 * @Author qiwangi
 * @Date 2023/8/19
 * @TIME 09:51
 */
object Single {


//
//    // 数据库表名
//    const val TABLE_NAME = "image_info"
//
//    // 列名
//    const val COLUMN_ID = "id"
//    const val COLUMN_IMAGE_NAME = "image_name"
//    const val COLUMN_IMAGE_DESCRIPTION = "image_description"
//    const val COLUMN_AUTHOR = "author"
//    const val COLUMN_TEXT = "text"
//    const val COLUMN_IMAGE_DATA = "image_data"
//
//    const val COLUMN_NAME = "name_data"
//    const val COLUMN_DESCRIPTION = "description_data"




    fun generateRandomColors(): Triple<String, String, String> {
        val colorSet = HashSet<Int>()
        val colors = arrayListOf<String>()

        fun getRandomColor(): String {
            val red = Random.nextInt(0, 256)
            val green = Random.nextInt(0, 256)
            val blue = Random.nextInt(0, 256)
            return "#%02X%02X%02X".format(red, green, blue)
        }

        fun getColorDist(color: String, existingColors: List<String>): Int {
            val colorInt = color.substring(1).toInt(16)
            var minDist = Int.MAX_VALUE
            for (existingColor in existingColors) {
                val existingColorInt = existingColor.substring(1).toInt(16)
                val dist = Math.abs(colorInt - existingColorInt)
                if (dist < minDist) {
                    minDist = dist
                }
            }
            return minDist
        }

        while (colors.size < 3) {
            val color = getRandomColor()
            val minDist = getColorDist(color, colors)
            if (!colorSet.contains(minDist) && minDist > 100000) {
                colors.add(color)
                colorSet.add(minDist)
            }
        }

        return Triple(colors[0], colors[1], colors[2])
    }



    @SuppressLint("MissingInflatedId")
    fun showHtml(mActivity: Activity, textContent:String){
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_html, null)
        val webView = inflate.findViewById<WebView>(R.id.dialog_web_view)
        val v1 = inflate.findViewById<View>(R.id.v1)
        val v2 = inflate.findViewById<View>(R.id.v2)
        val l1 = inflate.findViewById<LinearLayout>(R.id.l1)

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        v1.setOnClickListener { mPopupWindow.dismiss() }
        v2.setOnClickListener { mPopupWindow.dismiss() }
        l1.setOnClickListener { mPopupWindow.dismiss() }

        webView.loadDataWithBaseURL(null, textContent, "text/html", "UTF-8", null)
        // 设置 WebView 可滚动
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false



        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
    }

    /**
     * 透明度
     */
    fun bgAlpha(context: Activity, f: Float) { //透明函数
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = f
        context.window.attributes = lp
    }

    /**
     * 获取app的生命周期，包括所有activity  找到现在使用的 并进行 防止内存泄露的操作  一般用于关闭pop
     */
    fun lifeCycleSet(mActivity: Activity, tempPop: PopupWindow) {
        (mActivity.application).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {
                    if (mActivity != null && mActivity === activity) {//判断是否是依附的pop
                        if (tempPop != null && tempPop.isShowing) {//如果正在显示，关闭弹窗。
                            tempPop.dismiss()
                        }
                    }
                }

                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
    }

    /**
     * 将汉字转为拼音
     */
    fun String.toPinyin(): String {
        val converter = PinyinHelper.toHanyuPinyinStringArray(this.first())[0]
        return converter.replaceFirstChar { it.uppercase() }
    }

    fun String.toPinyin2(): String {
        val outputFormat = HanyuPinyinOutputFormat()
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE)
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE)

        if (this.isNotEmpty()) {
            val c = this[0]
            if (Character.toString(c).matches(Regex("[\u4e00-\u9fa5]"))) {
                try {
                    val pinyins = PinyinHelper.toHanyuPinyinStringArray(c, outputFormat)
                    if (pinyins != null && pinyins.isNotEmpty()) {
                        return pinyins[0]
                    }
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    e.printStackTrace()
                }
            } else {
                return this.substring(0, 1)
            }
        }
        return ""
    }


    fun extractTextBetweenTags(input: String,leftStr:String,rightStr:String): String {
        val startIndex = input.indexOf(leftStr)
        val endIndex = input.indexOf(rightStr)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + leftStr.length, endIndex)
        }else{
            ""
        }
    }




    /**
     * 加密解密
     */
    fun encryptAES(plaintext: String, key: String): String {


        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))


        return encryptedBytes.toHexString()
    }

    fun decryptAES(ciphertext: String, key: String): String {


        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(ciphertext.hexStringToByteArray())


        return decryptedBytes.toString(Charsets.UTF_8)
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
    private fun String.hexStringToByteArray() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()


    /**
     * 中央吐司
     */
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    fun centerToast(context: Context, text: String) {
        val toast = Toast(context)
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.home_dialog_net_error, null)
        val tv: TextView = view.findViewById(R.id.center_toast_text)
        tv.text = text
        toast.setView(view)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    //获取MD5  小写
    fun getMD5Hash(text: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(text.toByteArray())
            val digest = md.digest()
            val builder = StringBuilder()
            for (b in digest) {
                builder.append(String.format("%02x", b.toInt() and 0xff))
            }
            return builder.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 截取字符串从第二个开始到最后
     */
    fun extractSubstring(text: String): String {
        // 检查字符串长度是否大于等于 3
        if (text.length >= 3) {
            // 从第三个字符开始截取到最后
            return text.substring(2)
        } else {
            // 如果字符串长度小于 3,则返回原字符串
            return text
        }
    }


    /**
     * 图片比例类型
     */
    fun getImageType(width: Int, height: Int): Int {
        // 计算宽高比
        val aspectRatio = width.toDouble() / height.toDouble()
        Log.e("daddada2121", "图片宽高比" + aspectRatio)
        try {
            // 判断哪种类型
            return when {
                aspectRatio <= (3.0 / 4.0) -> 1//比例小于3：4   就为3：4
                aspectRatio > (3.0 / 4.0) && aspectRatio < (4.0 / 3.0) -> 2//比例大于3：4  小于4：3  就为1：1
                aspectRatio >= (4.0 / 3.0) -> 3//比例大于4：3  就为4：3
                else -> 2
            }
        } catch (e: Exception) {
            Log.e("zqr", "图片宽高比异常" + e.message)
            return 4
        }
    }

    fun showConfirmationDialog(context: Context, title: String, message: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("确定") { _, _ ->
            onConfirm()
        }
        builder.setNegativeButton("取消") { _, _ ->
            onCancel()
        }
        val dialog = builder.create()
        dialog.show()
    }


    /**
     * 提取一段字符串 "<"前面的字符
     *
     */
    fun extractTextBeforeDelimiter(line: String, delimiter: String): String {
        val index = line.indexOf(delimiter)
        return if (index != -1) {
            line.substring(0, index)
        } else {
            line
        }
    }

    /**
     * 将一个字符从某个位置分割开来
     */
    fun splitStringAtFirstTag(str: String,tag:String): Array<String> {
        val firstSIndex = str.indexOf(tag)

        return if (firstSIndex != -1) {
            arrayOf(
                str.substring(0, firstSIndex).trim(),
                str.substring(firstSIndex).trim()
            )
        } else {
            arrayOf(str, "")
        }
    }



}
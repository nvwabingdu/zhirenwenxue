package cn.dreamfruits.yaoguo.util

import android.text.InputFilter
import android.text.Spanned
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

/**
 * @author Lee
 * @createTime 2023-06-26 10 GMT+8
 * @desc :
 */
class MaxLengthFilter : InputFilter {
    var nMax = 0
    var keep = 0
    var mToastMsg = ""


    constructor(nMax: Int, mToastMsg: String) {
        this.nMax = nMax
        this.mToastMsg = mToastMsg
    }


    /**
     * @return null:不修改
     */
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        return try {

            LogUtils.e(
                ">>>" +
                        "source = " + source.toString(),
                "dest = " + dest.toString(),
            )

            LogUtils.e(
                ">>>" +
                        "dstart = " + dstart,
                "dend = " + dend,
            )
            var destAdjust = dest.toString()
            var filterString = ""
            if (dstart != dend) {
                val destAdjust1 = dest.toString().substring(0, dstart)
                val destAdjust2 = dest.toString().substring(dend)
                destAdjust = destAdjust1 + destAdjust2
                filterString = dest.toString().substring(dstart, dend)
            }

            LogUtils.e(
                ">>>" +
                        "destAdjust = " + destAdjust,
                "filterString = " + filterString,
            )

            keep = nMax - destAdjust.length - source.toString().length
            if (keep < 0) {
                //此已输入的字符大于限定长度nMax
                ToastUtils.showShort(mToastMsg)
                if (nMax - destAdjust.length <= 0) {
                    filterString
                } else
                    source.substring(0, nMax - destAdjust.length)
            } else {
                //此已输入的字符小于限定长度nMax
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

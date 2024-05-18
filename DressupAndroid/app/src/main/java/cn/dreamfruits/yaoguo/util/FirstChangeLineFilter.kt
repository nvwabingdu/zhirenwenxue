package cn.dreamfruits.yaoguo.util

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils

/**
 * @author Lee
 * @createTime 2023-06-26 10 GMT+8
 * @desc :
 */
class FirstChangeLineFilter : InputFilter {

    lateinit var et: EditText

    constructor(et: EditText) {
        this.et = et

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

        if (TextUtils.isEmpty(et.text.toString())) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            }
        }
        return null
    }
}

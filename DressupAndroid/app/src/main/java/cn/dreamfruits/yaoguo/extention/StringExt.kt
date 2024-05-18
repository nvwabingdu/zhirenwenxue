package cn.dreamfruits.yaoguo.extention

import java.util.regex.Pattern


/**
 * 检验是否手机号码
 */
fun String.isPhone(): Boolean {
    val regExp =
        "^((13[0-9])|(14(0|[5-7]|9))|(15([0-3]|[5-9]))|(16(2|[5-7]))|(17[0-8])|(18[0-9])|(19([0-3]|[5-9])))\\d{8}\$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 检验是否全是数字
 */
fun String.isNumber(): Boolean {
    val regExp = "^[0-9]*\$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(this)
    return m.matches()
}
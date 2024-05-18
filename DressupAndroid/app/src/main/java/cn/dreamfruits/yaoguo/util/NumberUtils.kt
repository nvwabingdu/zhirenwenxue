package cn.dreamfruits.yaoguo.util


import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max


/**
 * 数字util
 */
object NumberUtils {


    /**
     * 转换数字格式 为 1k 1w 1亿
     */
    fun formatToStr(num: Long): String {
        val number = max(0, num)

        if (number < 999) {
            return number.toString()
        }

        val (format, unit) = when (number) {

            in 1000..9999 -> Pair("#.#k", number.toDouble() / 1000)

            in 10000..99999999 -> Pair("#.#w", number.toDouble() / 10000)

            else -> Pair("#.#亿", number.toDouble() / 100000000)
        }

        val df = DecimalFormat(format)
        df.roundingMode = RoundingMode.DOWN

        return df.format(unit)
    }

}
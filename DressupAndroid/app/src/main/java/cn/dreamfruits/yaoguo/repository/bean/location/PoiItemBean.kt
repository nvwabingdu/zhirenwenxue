package cn.dreamfruits.yaoguo.repository.bean.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * 周边位置
 */
@Parcelize
data class PoiItemBean(
    /**
     * 行政编码区码
     */
    val provinceCode:String,
    val provinceName:String,
    /**
     * 城市编码
     */
    val cityCode:String,
    val cityName:String,
    /**
     * 经度
     */
    val longitude:Double,
    /**
     * 维度
     */
    val latitude:Double,
    val title:String,
    val snippet:String,
    var isSelect:Boolean = false
): Parcelable
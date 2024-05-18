package cn.dreamfruits.yaoguo.module.main.message.chart.bean

import com.tencent.imsdk.v2.V2TIMImageElem.V2TIMImage

/**
 * @author Lee
 * @createTime 2023-07-05 11 GMT+8
 * @desc :
 */
class TIMImageElem {
    var path = ""
    var imageList: List<V2TIMImage> = arrayListOf()
}
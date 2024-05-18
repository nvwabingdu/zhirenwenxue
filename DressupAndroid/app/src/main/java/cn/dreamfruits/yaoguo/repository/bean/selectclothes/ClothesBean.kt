package cn.dreamfruits.yaoguo.repository.bean.selectclothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * 单品
 */

@Parcelize
data class ClothesBean(
    //val cLength: Any,

    /**
     * 封面图
     */
    var coverUrl: String,

    val createTime: Long,

    /**
     * 单品id
     */
    val id: Long,

    /**
     * 是否收藏 0-否 1-是
     */
    val isCollect: Int,

    //val labelsList: Any?,

    /**
     * 单品模型文件列表
     */
    val modelList: List<String>?,

    /**
     * 单品名称
     */
    val name: String,

    /**
     * 单品图片链接
     */
    val picUrls: List<String>?,

    /**
     * 部位 1-内搭，2-上衣，3-外套，4-袜子，5-鞋子，6-配饰
     */
    val position: Int,

    /**
     * 数据状态 1-正常 2-下架 3-不可用
     */
    val state: Long,
    /**
     * 类型 0-官方 1-个人
     */
    val type: Long,

    /**
     * 单品视频链接
     */
    val videoUrls: String?,
    var isSel: Boolean,
) : Parcelable, Serializable
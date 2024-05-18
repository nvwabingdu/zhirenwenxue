package cn.dreamfruits.yaoguo.repository.bean.selectclothes

/**
 * 单品二级分类
 */
data class ClothesCategoryBean(

    val coverUrl: String,

    val createTimestamp: Long,

    val cvId: String,

    val cvName: String,

    val gender: Int,

    val id: Long,

    val position: Long,

    val state: Int,

    val updateTimestamp: Long
)
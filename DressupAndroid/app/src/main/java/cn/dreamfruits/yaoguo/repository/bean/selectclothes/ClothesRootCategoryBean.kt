package cn.dreamfruits.yaoguo.repository.bean.selectclothes

/**
 * 单品的一级分类
 */
data class ClothesRootCategoryBean(
    val name: String,
    val position: Int,
    var enable: Boolean = true
)
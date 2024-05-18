package cn.dreamfruits.yaoguo.repository.bean.label

data class dada(
    val code: Int, // 200
    val `data`: Data,
    val msg: String // OK
) {
    data class Data(
        val id: Long, // 357149191448144
        val isCollect: Int, // 0
        val name: String, // 每日穿搭
        val outfitTryOnCount: Int, // 0
        val spTryOnCount: Int, // 0
        val state: Int, // 1
        val viewCount: Int // 788
    )
}
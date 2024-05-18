package cn.dreamfruits.yaoguo.repository.bean.search

data class SearchOutfitBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1663125527585
    val list: List<Item>,
    val page: Int, // 2
    val size: Int // 10
) {
    data class Item(
        val coverUrl: String, // https://testfile.dreamfruits.cn/app/327649999012000/1ebc913aaa23cd97d1c78995aa70d187
        val id: Long // 328738501585088
    )
}
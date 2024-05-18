package cn.dreamfruits.yaoguo.module.main.home.searchhistory

/**
 * @Author qiwangi
 * @Date 2023/5/18
 * @TIME 11:39
 */
data class HistoryBean(
    var list: MutableList<Item>
) {
    data class Item(
        var mStr: String
    )
}

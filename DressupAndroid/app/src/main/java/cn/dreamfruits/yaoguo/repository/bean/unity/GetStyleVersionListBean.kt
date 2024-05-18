package cn.dreamfruits.yaoguo.repository.bean.unity

data class GetStyleVersionListBean(
        val hasNext: Int, // 0
        val lastTime: Long, // 1683884904704
        val list: List<Item>
    ) {
        data class Item(
            val coverUrl: String, // https://devfile.dreamfruits.cn/pic/jinshentxu001.png
            val createTimestamp: Long, // 1683875177021
            val floor: Int, // 1
            val gender: Int, // 0
            val id: Int, // 2
            val materialList: List<Material>,
            val name: String, // T恤
            val partsList: List<Parts>,
            val position: Int, // 1
            val stampList: List<Stamp>,
            val state: Int, // 1
            val svId: String, // txu
            val type: Int, // 1
            val updateTimestamp: Long // 1683875177021
        ) {
            data class Material(
                val coverUrl: String, // https://devfile.dreamfruits.cn/pic/chaizhi001.jpg
                val id: Int, // 1
                val isDefault: Int, // 0
                val materialId: String, // chaizhiid001
                val name: String, // 材质1
                val resourceUrl: String // https://devfile.dreamfruits.cn/test/txu/jinshu.ab
            )

            data class Parts(
                val componentList: List<Component>,
                val id: Int, // 3
                val name: String, // 款式
                val partsId: String, // kuanshi001
                val type: Int // 1
            ) {
                data class Component(
                    val coverUrl: String, // https://devfile.dreamfruits.cn/test/txu/changkuan001.png
                    val defaultMaterialId: Int, // 4
                    val id: Int, // 5
                    val isDefault: Int, // 1
                    val materialList: List<Material>,
                    val name: String, // 长款
                    val resourceUrl: String // https://devfile.dreamfruits.cn/test/txu/txu_changkuan.ab
                ) {
                    data class Material(
                        val coverUrl: String, // https://devfile.dreamfruits.cn/pic/chaizhi002.jpg
                        val id: Int, // 4
                        val isDefault: Int, // 1
                        val materialId: String, // chaizhiid002
                        val name: String, // 材质2
                        val resourceUrl: String // https://devfile.dreamfruits.cn/test/txu/m_txu_changkuan_n.ab
                    )
                }
            }

            data class Stamp(
                val coverUrl: String, // https://devfile.dreamfruits.cn/test/resource/yinghua2.jpeg
                val id: Int, // 5
                val isDefault: Int, // 0
                val materialId: String, // yinhuaid002
                val name: String, // 印花2
                val resourceUrl: String // https://devfile.dreamfruits.cn/test/resource/yinghua1.jpg
            )
        }
}
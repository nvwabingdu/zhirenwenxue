package cn.dreamfruits.yaoguo.repository.bean.diy

data class GetStyleVersionListByTypeBean(
        val hasNext: Int, // 0
        val lastTime: Long, // 1685172236176
        val list: MutableList<Item>
    ) {
        data class Item(
            val createTimestamp: Long, // 1683875177021
            val floor: Int, // 1
            val gender: Int, // 0
            val materialList: Any, // null
            val partsList: Any, // null
            val position: Int, // 1
            val stampList: Any, // null
            val state: Int, // 1
            val svId: String, // txu
            val type: Int, // 1
            val updateTimestamp: Long, // 1683875177021
            val isOver:Int,//是否还有下一级 0否 1是isOver

            var status: Int, // 1

            //获取版型分类列表APP（目前用于diy）共用
            val name: String, // T恤
            var coverUrl: String, // https://devfile.dreamfruits.cn/pic/jinshentxu001.png
            val id: Long, // 2
            //左侧一级列表添加
            var isSelect: Boolean = false,

            //衣装item额外的
            val isCollect: Int, // 0
            val isOpen: Int, // 0
            val labels: Any, // null
            val resourceList: Any, // null
            val svName: String,
            val userInfo: Any, // null


            //diy banner bean
            val configUrl: String, // https://devfile.dreamfruits.cn/clothesServer/1/c594f011a68869270382b2702b00b039.json
            val url: String, // https://devfile.dreamfruits.cn/clothesServer/1/9bc5d43aba9ffa22d862774943b4af08.ab
            val zipUrl: String // https://devfile.dreamfruits.cn/clothesServer/1/65aa42b347a7a0580ac069a7d769f688.zip

            )
    }

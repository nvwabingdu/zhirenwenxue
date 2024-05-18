package cn.dreamfruits.yaoguo.repository.bean.stroll

data class GetClothesItemInfoBean(
        val clothesList: MutableList<Clothes>,
        val coverUrls: MutableList<String>,
        val createTimestamp: Long, // 1686024883625
        val floor: Int, // 1
        val gender: Int, // 0
        val id: Long, // 520585901034752
        val isCollect: Int, // 0
        val isOpen: Int, // 1
        val labels: MutableList<String>,
        val name: String, // 我的作品
        val position: Int, // 1
        val resourceList: Any, // null
        val state: Int, // 1
        val svName: String, // T恤
        val type: Int, // 1
        val updateTimestamp: Long, // 1686024930922
        val userInfo: UserInfo?
    ) {
        data class Clothes(
            val id: Long, // 520585901034752
            var picUrl: String // https://devfile.dreamfruits.cn/app/491727083463920/030fb790c6e0732cd6cccd1dbf562b88.png
        )

        data class UserInfo(
            val avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/17c4e8e7a14e1368409c7cf8d97da40f.png
            val followerCount: Int, // 2
            val id: Long, // 448606354679856
            val laudCount: Int, // 0
            val nickName: String, // yg448606354679856
            var relation: Int, // 0
            val tryOnCount: Int, // 0
            val worksCount: Int // 1
        )
    }

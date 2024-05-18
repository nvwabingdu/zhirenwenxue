package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean

data class GetSimilarRecommendBean(
        val list: MutableList<Item>
    ) {
        data class Item(
            val id: Long, // 519886067338410
            var picUrl: String // https://devfile.dreamfruits.cn/test/singlepro/xiazhuang/cover2.png
        )
    }

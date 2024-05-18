package cn.dreamfruits.yaoguo.repository.bean.label

import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean

data class GetLabelListBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 0
    val list: MutableList<WaterfallFeedBean.Item.Info>
)
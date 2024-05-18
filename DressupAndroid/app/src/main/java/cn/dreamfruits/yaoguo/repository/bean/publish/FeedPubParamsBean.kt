package cn.dreamfruits.yaoguo.repository.bean.publish

import cn.dreamfruits.yaoguo.repository.bean.media.PicBean
import cn.dreamfruits.yaoguo.repository.bean.media.VideoBean

/**
 * 发布帖子bean
 */
class FeedPubParamsBean {

    //发布任务的id
    var taskId: Long? =null

    // 帖子标题
    var title: String? = null

    //帖子内容
    var content: String? = null

    //帖子类型 0-图文 1-视频
    var type: Int? = null

    //图片地址 [{"url":"http://example.com/123.png","width":200,height:200},]
    var picBean: MutableList<PicBean>? = null

    //可见性 0-公开，1-好友可见 2-私密
    var seeLimit: Int? = null

    //视频地址
    var videoBean: VideoBean? = null

    //话题 [{"index":0,"len":10,"name":"11111"},{"index":20,"len":5,"name":"我是一个话题"}]
    var topicIds: String? = null

    //@用户 [{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
    var atUser: String? = null

    // 省码
    var provinceAdCode: Long? = null

    //城市码
    var cityAdCode: Long? = null

    //经度
    var longitude: Double? = null

    //纬度
    var latitude: Double? = null

    //位置
    var address: String? = null

    //单品id 逗号分割
    var singleIds:String? =null

    //发布状态 1 = 发布中 2 = 错误 3 = 完成
    var state = 0
}
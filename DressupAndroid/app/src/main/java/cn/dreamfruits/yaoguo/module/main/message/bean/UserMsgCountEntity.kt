package cn.dreamfruits.yaoguo.module.main.message.bean

/**
 * @author Lee
 * @createTime 2023-07-04 14 GMT+8
 * @desc :
 */
data class UserMsgCountEntity(
//    1、首先看黑名单关系，
//    2、如果不在黑名单，再看可发消息数，-1为无限制，1-还能发送一条，0-不能发送，再去看关注关系
//    3、当可发消息数为0时，看关注关系，进行展示
    var count: Int = -1,//可发消息数，-1：无限制
    var relation: Int = -1,//关注关系；0-无，1-我关注了他，2-他关注了我，3-互相关注
    var blackRelation: Int = -1,//黑名单关系；0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
)
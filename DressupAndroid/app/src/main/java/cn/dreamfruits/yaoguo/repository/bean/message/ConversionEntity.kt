package cn.dreamfruits.yaoguo.repository.bean.message

import cn.dreamfruits.yaoguo.adapter.base.entity.MultiItemEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareSingleEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareUserEntity
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMMessage

class ConversionEntity(override var itemType: Int = -1) : MultiItemEntity
     {
    var type: Int = 0 // 0 单聊会话消息
    var conversation: V2TIMConversation? = null
    var message: V2TIMMessage? = null
    var headerImgUrl: String = ""

    var sharePostEntity: SharePostEntity? = null
    var shareUserEntity: ShareUserEntity? = null
    var shareSingleEntity: ShareSingleEntity? = null


//    override fun compareTo(other: V2TIMConversation): Int {
//        if (this.conversation == null) {
//            return 0
//        }
//        return if (this.conversation!!.isPinned && !other.isPinned) {
//            -1
//        } else if (!this.conversation!!.isPinned && other.isPinned) {
//            1
//        } else {
//            val thisOrderKey: Long = this.conversation!!.orderKey
//            val otherOrderKey: Long = other.orderKey
//            if (thisOrderKey > otherOrderKey) {
//                -1
//            } else if (thisOrderKey == otherOrderKey) {
//                0
//            } else {
//                1
//            }
//        }
//    }

    override fun toString(): String {
        return "ConversionEntity(itemType=$itemType, type=$type, conversation=$conversation, message=$message)"
    }

}



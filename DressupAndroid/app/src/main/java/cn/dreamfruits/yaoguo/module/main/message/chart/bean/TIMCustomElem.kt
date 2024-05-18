package cn.dreamfruits.yaoguo.module.main.message.chart.bean

/**
 * @author Lee
 * @createTime 2023-07-05 11 GMT+8
 * @desc :
 */
data class TIMCustomElem(
    val clientTime: Int,
    val cloudCustomBytes: List<Any>,
    val excludedFromContentModeration: Boolean,
    val excludedFromLastMessage: Boolean,
    val excludedFromUnreadCount: Boolean,
    val faceUrl: String,
    val friendRemark: String,
    val groupID: String,
    val hasSentReceipt: Boolean,
    val isBroadcastMessage: Boolean,
    val isForward: Boolean,
    val isMessageSender: Boolean,
    val isPeerRead: Boolean,
    val isSelfRead: Boolean,
    val lifeTime: Int,
    val localCustomNumber: Int,
    val localCustomString: String,
    val messageBaseElements: List<TimMessageBaseElement>,
    val messageGroupAtInfoList: List<Any>,
    val messageStatus: Int,
    val messageType: Int,
    val messageVersion: Int,
    val msgID: String,
    val nameCard: String,
    val needReadReceipt: Boolean,
    val nickName: String,
    val platform: Int,
    val priority: Int,
    val random: Int,
    val receiptPeerRead: Boolean,
    val receiptReadCount: Int,
    val receiptUnreadCount: Int,
    val receiverUserID: String,
    val revokerTinyID: Int,
    val revokerUserID: String,
    val senderUserID: String,
    val seq: Int,
    val serverTime: Int,
    val supportMessageExtension: Boolean
)


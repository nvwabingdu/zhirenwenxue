package cn.dreamfruits.yaoguo.module.main.message.chart.bean

import com.tencent.cos.xml.common.VersionInfo.platform
import com.tencent.imsdk.message.*
import com.tencent.imsdk.v2.*

/**
 * @author Lee
 * @createTime 2023-07-04 20 GMT+8
 * @desc :
 */

data class QuoteMessageBean(
    var message: QuoteBean?,
)

data class QuoteBean(
    var faceUrl: String,
    var friendRemark: String,
    var localCustomString: String,
    var messageBaseElements: List<TimMessageBaseElement>,
    var messageStatus: Int,
    var msgID: String,
    var nameCard: String,
    var nickName: String,
    var receiverUserID: String,
    var revokerUserID: String,
    var senderUserID: String,
) {

}

data class TimMessageBaseElement(
    var elementType: Int,
    var snapshotDownloadUrl: String,
    var snapshotFilePath: String,
    var snapshotFileSize: Int,
    var snapshotHeight: Int,
    var snapshotType: String,
    var snapshotUUID: String,
    var snapshotWidth: Int,
    var videoBusinessID: Int,
    var videoDownloadUrl: String,
    var videoDuration: Int,
    var videoFilePath: String,
    var videoFileSize: Int,
    var videoType: String,
    var videoUUID: String,
    var textContentBytes: ByteArray,

    ) {
    override fun toString(): String {
        return "TimMessageBaseElement(elementType=$elementType, snapshotDownloadUrl='$snapshotDownloadUrl', snapshotFilePath='$snapshotFilePath', snapshotFileSize=$snapshotFileSize, snapshotHeight=$snapshotHeight, snapshotType='$snapshotType', snapshotUUID='$snapshotUUID', snapshotWidth=$snapshotWidth, videoBusinessID=$videoBusinessID, videoDownloadUrl='$videoDownloadUrl', videoDuration=$videoDuration, videoFilePath='$videoFilePath', videoFileSize=$videoFileSize, videoType='$videoType', videoUUID='$videoUUID', textContentBytes=$textContentBytes)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimMessageBaseElement

        if (elementType != other.elementType) return false
        if (snapshotDownloadUrl != other.snapshotDownloadUrl) return false
        if (snapshotFilePath != other.snapshotFilePath) return false
        if (snapshotFileSize != other.snapshotFileSize) return false
        if (snapshotHeight != other.snapshotHeight) return false
        if (snapshotType != other.snapshotType) return false
        if (snapshotUUID != other.snapshotUUID) return false
        if (snapshotWidth != other.snapshotWidth) return false
        if (videoBusinessID != other.videoBusinessID) return false
        if (videoDownloadUrl != other.videoDownloadUrl) return false
        if (videoDuration != other.videoDuration) return false
        if (videoFilePath != other.videoFilePath) return false
        if (videoFileSize != other.videoFileSize) return false
        if (videoType != other.videoType) return false
        if (videoUUID != other.videoUUID) return false
        if (!textContentBytes.contentEquals(other.textContentBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = elementType
        result = 31 * result + snapshotDownloadUrl.hashCode()
        result = 31 * result + snapshotFilePath.hashCode()
        result = 31 * result + snapshotFileSize
        result = 31 * result + snapshotHeight
        result = 31 * result + snapshotType.hashCode()
        result = 31 * result + snapshotUUID.hashCode()
        result = 31 * result + snapshotWidth
        result = 31 * result + videoBusinessID
        result = 31 * result + videoDownloadUrl.hashCode()
        result = 31 * result + videoDuration
        result = 31 * result + videoFilePath.hashCode()
        result = 31 * result + videoFileSize
        result = 31 * result + videoType.hashCode()
        result = 31 * result + videoUUID.hashCode()
        result = 31 * result + textContentBytes.contentHashCode()
        return result
    }
}

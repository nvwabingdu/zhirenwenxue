package cn.dreamfruits.sociallib.entiey.content

import android.content.Intent

/**
 * description: activity 回调操作的实体
 */
data class ActivityResultContent(
    var request: Int,
    var result: Int,
    var data: Intent?
) : OperationContent
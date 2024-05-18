package cn.dreamfruits.baselib.network.imageloader.glide

/**
 * description: Glide加载图片的进度回调.
 *
 * @date 2019/5/25 11:47.
 * @author: YangYang.
 */
interface OnImageProgressListener {
    fun onProgress(url: String, isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long)
}
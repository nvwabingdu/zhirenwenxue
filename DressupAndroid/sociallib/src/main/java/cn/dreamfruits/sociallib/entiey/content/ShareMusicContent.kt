package cn.dreamfruits.sociallib.entiey.content

import android.graphics.Bitmap

/**
 * description: 音乐分享
 */
data class ShareMusicContent(
    override var img: Bitmap?, //缩略图
    override var url: String? = null,      //音乐url
    override var description: String? = null,  //描述
    var title: String? = null,      //标题
    var aacUrl: String? = null     //音频地址
) : ShareContent()
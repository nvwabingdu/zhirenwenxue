package cn.dreamfruits.sociallib.entiey.content

import android.graphics.Bitmap

/**
 * description: 分享的基类
 */
open class ShareContent : OperationContent {
  open var img:Bitmap? = null   // 图片或者缩略图
  open var url:String? = null   // 网页的地址
  open var description:String? = null  // 描述
}
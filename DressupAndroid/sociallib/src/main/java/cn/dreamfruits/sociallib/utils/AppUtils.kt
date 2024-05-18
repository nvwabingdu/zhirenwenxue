package cn.dreamfruits.sociallib.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * description:
 * @author: Griee
 * @date: 2019/3/28 14:04
 */
object AppUtils {

  /**
   * 判断应用是否安装
   */
  fun isAppInstalled(name: String, context: Context): Boolean {
    val manager = context.packageManager
    return try {
      manager.getApplicationInfo(name, 0) != null
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
      false
    }
  }
}
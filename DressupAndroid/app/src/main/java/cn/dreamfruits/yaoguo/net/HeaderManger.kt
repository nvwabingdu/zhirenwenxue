package cn.dreamfruits.yaoguo.net

import cn.dreamfruits.yaoguo.repository.OauthRepository
import com.blankj.utilcode.util.AppUtils
import java.util.HashMap

/**
 * 接口请求头的管理类.
 */
class HeaderManger private constructor(){

    companion object{
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
           HeaderManger()
        }
    }

    //获取固定header
    fun getStaticHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["osPlatform"] = "0"  //1：ios    0:Android
        headers["osVersion"] = android.os.Build.VERSION.SDK_INT.toString()
        headers["appVersion"] = AppUtils.getAppVersionName()
        headers["appChannel"] = "0"
        return headers
    }


    //获取动态header
    fun getDynamicHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = OauthRepository.getAuthorization()
        return headers
    }

}
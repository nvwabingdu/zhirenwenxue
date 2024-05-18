package cn.dreamfruits.yaoguo.net.handler

import android.text.TextUtils
import cn.dreamfruits.baselib.network.GlobalHttpHandler
import cn.dreamfruits.yaoguo.net.HeaderManger
import cn.dreamfruits.yaoguo.repository.OauthRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 处理请求头
 */
class HeaderHttpHandler : GlobalHttpHandler{

    //Basic token的接口
    private var basicTokenUrls = arrayListOf(
        "/usercenter/api/login",
        "/usercenter/api/getCaptcha",
        "/usercenter/api/bindPhone",
        "/thirdparty/api/getAliPhoneKey",
        "/usercenter/api/rapidLogin",
        "/usercenter/api/refreshToken"
    )

    override fun onHttpResultResponse(
        httpResult: String,
        chain: Interceptor.Chain,
        response: Response
    ): Response {
        return response
    }



    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        val builder = request.newBuilder()
        val staticHeaders = HeaderManger.instance.getStaticHeaders()
        val dynamicHeaders = HeaderManger.instance.getDynamicHeaders()

        for ((key,value ) in staticHeaders){
            builder.removeHeader(key)
            builder.addHeader(key,value)
        }
        val url = request.url.toString()
        var isBasicToken = false

        for (i in 0 until basicTokenUrls.size){
            if (url.contains(basicTokenUrls[i])){
                isBasicToken = true
                break
            }
        }

        for ((key,value ) in dynamicHeaders){
            if (isBasicToken && TextUtils.equals("Authorization",key)){
                builder.removeHeader(key)
                builder.addHeader(key,OauthRepository.getBasicAuthorization())
            }else{
                builder.removeHeader(key)
                builder.addHeader(key, value)
            }
        }

        return builder.build()
    }
}
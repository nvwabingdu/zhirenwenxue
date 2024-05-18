package cn.dreamfruits.baselib.network


import cn.dreamfruits.baselib.util.ZipHelper
import com.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

class RequestIntercept(private val mHandler: GlobalHttpHandler?) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestBuffer = Buffer()

        request.body?.writeTo(requestBuffer)

        val originalResponse = chain.proceed(request)

        //读取服务器返回的结果
        val responseBody = originalResponse.body
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer

        val encoding = originalResponse.headers["Content-Encoding"]

        val clone = buffer.clone()
        val bodyString: String

        var charset = Charset.forName("UTF-8")
        val contentType = responseBody.contentType()

        if (contentType != null) {
            charset = contentType.charset(charset)
        }

        if (encoding != null && encoding.equals("gzip", ignoreCase = true)) { //content 使用 gzip 压缩
            bodyString = ZipHelper.decompressForGzip(
                clone.readByteArray(),
                convertCharset(charset)
            ) //解压

        } else if (encoding != null && encoding.equals("zlib", ignoreCase = true)) { //content 使用 zlib 压缩

            bodyString = ZipHelper.decompressToStringForZlib(
                clone.readByteArray(),
                convertCharset(charset)
            ) //解压
        } else { //content 没有被压缩, 或者使用其他未知压缩方式
            bodyString = clone.readString(charset)
        }


        return mHandler?.onHttpResultResponse(bodyString,chain,originalResponse) ?: originalResponse
    }

    companion object {

        fun convertCharset(charset: Charset): String? {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) s else s.substring(i + 1, s.length - 1)
        }
    }
}
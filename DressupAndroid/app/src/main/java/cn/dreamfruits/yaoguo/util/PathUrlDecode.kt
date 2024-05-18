package cn.dreamfruits.yaoguo.util

import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean
import com.blankj.utilcode.util.GsonUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author Lee
 * @createTime 2023-06-20 16 GMT+8
 * @desc :
 */

/**
 * 图片鉴权
 * md5hash	通过 MD5 算法计算出的固定长度为32位的字符串。md5hash 具体的计算公式如下：
• md5hash = md5sum(uri-timestamp-rand-uid-pkey)
• uri 资源访问路径以正斜线（/）开头
• timestamp：取值为上述中的timestamp
• rand： 取值为上述的rand
• uid： 取值为上述的uid
• pkey：自定义密钥：由6 - 40位大小写字母、数字构成，密钥需要严格保密，仅客户端与服务端知晓。


http://DomainName/Filename?sign=timestamp-rand-uid-md5hash

https://devfile.dreamfruits.cn/app/367782168227296/14fd67f9146b11cd26ba392bdb1c84f7

{"secretKey":"CyrNUuz8J8HZXQ1q6874z6tTzsx2G2zN","secondSecretKey":"5v4n14xTOz73I5Us89K028m89MgFyh","randStr":"sign"}
 */
fun String.decodePicUrls(uid: String = "0"): String {
    if (this.isEmpty()) {
        return ""
    }

    var pkey = ""

    pkey = try {
        val json = MMKVRepository.getCommonMMKV()
            .decodeString(MMKVConstants.GET_TENCENT_CDN_SECRETKEY, "")
        val bean = GsonUtils.fromJson(json, TencentCDNSecretKeyBean::class.java)
        bean.secretKey
    } catch (e: Exception) {
//                "CyrNUuz8J8HZXQ1q6874z6tTzsx2G2zN"
        ""
    }

    val timestamp = (System.currentTimeMillis() / 1000).toString()
    val rand = ToolJava.getRandomStr(100)
    val url = this.replace("//", "")//先处理//的情况  不然会截取到之前的
    val md5hash = getMD5Hash(
        "${
            url.substring(
                url.indexOf("/"),
                url.length
            )
        }-${timestamp}-${rand}-${uid}-${pkey}"
    )
    return "${this}?sign=${timestamp}-${rand}-${uid}-${md5hash}"
}

fun String.decodeVideoUrls(uid: String = "0"): String {
    if (this.isEmpty()) {
        return ""
    }

    var pkey = ""

    pkey = try {
        val json = MMKVRepository.getCommonMMKV()
            .decodeString(MMKVConstants.GET_TENCENT_CDN_SECRETKEY, "")
        val bean = GsonUtils.fromJson(json, TencentCDNSecretKeyBean::class.java)
        bean.secretKey
    } catch (e: Exception) {
        ""
    }

    val timestamp = (System.currentTimeMillis() / 1000).toString()
    val rand = ToolJava.getRandomStr(100)
    val url = this.replace("//", "")//先处理//的情况  不然会截取到之前的
    val md5hash = getMD5Hash(
        "${
            url.substring(
                url.indexOf("/"),
                url.length
            )
        }-${timestamp}-${rand}-${"0"}-${pkey}"
    )
    return "${this}?sign=${timestamp}-${rand}-${uid}-${md5hash}"
}

/**
 * 获取MD5加密  小写
 */
private fun getMD5Hash(text: String): String {
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(text.toByteArray())
        val digest = md.digest()
        val builder = StringBuilder()
        for (b in digest) {
            builder.append(String.format("%02x", b.toInt() and 0xff))
        }
        return builder.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}
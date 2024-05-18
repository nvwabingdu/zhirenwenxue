package cn.dreamfruits.yaoguo.util

import android.util.Log
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.message.GetMessageInnerPageListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean
import cn.dreamfruits.yaoguo.util.Singleton.getUrlX
import com.blankj.utilcode.util.GsonUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 19:36
 */
class Tool {

    /**
     * 获取MD5加密  小写
     */
    private fun getMD5Hash(text: String): String? {
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
        return null
    }

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
    fun decodePicUrls(tempUrl:String, uid:String="0",isPicUrl:Boolean,isZipNum:Int=40):String{
        if (tempUrl == ""){
            return tempUrl
        }
        var pkey=""
        if (isPicUrl){
             pkey = try {
                val json = MMKVRepository.getCommonMMKV().decodeString(MMKVConstants.GET_TENCENT_CDN_SECRETKEY, "")
                val bean = GsonUtils.fromJson(json, TencentCDNSecretKeyBean::class.java)
                bean.secretKey
            }catch (e:Exception){
                "CyrNUuz8J8HZXQ1q6874z6tTzsx2G2zN"
            }
        }else{
             pkey = try {
                val json = MMKVRepository.getCommonMMKV().decodeString(MMKVConstants.GET_TENCENT_CDN_SECRETKEY, "")
                val bean = GsonUtils.fromJson(json, TencentCDNSecretKeyBean::class.java)
                bean.secretKey
            }catch (e:Exception){
                "5v4n14xTOz73I5Us89K028m89MgFyh"
            }
        }
        val timestamp=(System.currentTimeMillis()/1000).toString()
        val rand= ToolJava.getRandomStr(100)
        val url=tempUrl.replace("//","")//先处理//的情况  不然会截取到之前的
        val md5hash= getMD5Hash("${url.substring(url.indexOf("/"),url.length)}-${timestamp}-${rand}-${uid}-${pkey}")
        if (isPicUrl){
                return "${tempUrl}?sign=${timestamp}-${rand}-${uid}-${md5hash}"+"&imageMogr2/thumbnail/!${isZipNum}p"//图片鉴权
        }else{
            return "${tempUrl}?sign=${timestamp}-${rand}-${uid}-${md5hash}"
        }
    }


    /**
     * 集合中的图片地址转鉴权URL地址   并返回bean
     */
    fun toUrlWaterfallFeedBean(mWaterfallFeedBean: WaterfallFeedBean?, uid:String): WaterfallFeedBean {
        mWaterfallFeedBean!!.list.forEach {
            try {
                if (it.info.videoUrls != null) {//0-图文动态，1-视频动态，2-广告
                    it.info.videoUrls[0].urlX = it.info.videoUrls[0].url
                    it.info.videoUrls[0].url = decodePicUrls(it.info.videoUrls[0].url, uid, false)//视频鉴权
                }
            } catch (e: Exception) {
                Log.e("zqr", e.toString())
            }

            try {
                if (it.info.picUrls != null) {//0-图文动态，1-视频动态，2-广告
                    it.info.picUrls.forEach {
                        it.urlX =it.url
                        it.url = getUrlX(it.url,true)
                    }
                }
            } catch (e: Exception) {
                Log.e("zqr", e.toString())
            }

            try {
                if (it.info.singleList != null) {//0-图文动态，1-视频动态，2-广告
                    it.info.singleList.forEach {
                        it.coverUrl = decodePicUrls(it.coverUrl, uid, true,100)
                    }
                }
            } catch (e: Exception) {
                Log.e("zqr", e.toString())
            }

            try {
                it.info.userInfo.avatarUrl =
                    decodePicUrls(it.info.userInfo.avatarUrl, uid, true)
            } catch (e: Exception) {
                Log.e("zqr", e.toString())
            }
        }
        return mWaterfallFeedBean
    }



    /**
     * 集合中的图片地址转鉴权URL地址   并返回bean  HomeRecommendUserListBean 感兴趣的人
     */
    fun toUrlHomeRecommendUserListBean(mHomeRecommendUserListBean: HomeRecommendUserListBean?, uid:String): HomeRecommendUserListBean?{
        mHomeRecommendUserListBean!!.list.forEach {
            try {
                it.avatarUrl=decodePicUrls(it.avatarUrl,uid,true)
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }
        }
        return mHomeRecommendUserListBean
    }



    /**
     * 动态详情 图片视频鉴权
     */
    fun toUrlFeedDetailsBean(mFeedDetailsBean: WaterfallFeedBean.Item.Info?, uid:String): WaterfallFeedBean.Item.Info?{
        //头像
        mFeedDetailsBean!!.userInfo.avatarUrl= decodePicUrls(mFeedDetailsBean.userInfo.avatarUrl,uid,true)

        //图片
        if (mFeedDetailsBean.picUrls!=null){
            mFeedDetailsBean.picUrls.forEach {
                try {
                    it.urlX=it.url
                    it.url= decodePicUrls(it.url,uid,true,80)
                }catch (e:Exception){
                    Log.e("zqr", e.toString())
                }
            }
        }

        try {
            if (mFeedDetailsBean.singleList != null) {//0-图文动态，1-视频动态，2-广告
                mFeedDetailsBean.singleList.forEach {
                    it.coverUrl = decodePicUrls(it.coverUrl, uid, true,100)
                }
            }
        } catch (e: Exception) {
            Log.e("zqr", e.toString())
        }

        if (mFeedDetailsBean.videoUrls!=null){
            //视频
            try {
                mFeedDetailsBean.videoUrls[0].urlX=  mFeedDetailsBean.videoUrls[0].url
                mFeedDetailsBean.videoUrls[0].url= decodePicUrls(mFeedDetailsBean.videoUrls[0].url,uid,false)
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }
        }

        return mFeedDetailsBean
    }



    /**
     * 互动消息 图片视频鉴权
     */
    fun toUrlGetMessageInnerPageListBean(mGetMessageInnerPageListBean: GetMessageInnerPageListBean?):
            GetMessageInnerPageListBean? {
        mGetMessageInnerPageListBean!!.list.forEach {
            try {
                it.coverUrl = decodePicUrls(it.coverUrl, "0", true)
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

            try {
                if (it.userInfo!=null&&it.userInfo.avatarUrl!=""){
                    it.userInfo.avatarUrl = decodePicUrls(it.userInfo.avatarUrl, "0", true)//图片鉴权
                }
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

            try {
                if (it.replyUserInfo!=null&&it.replyUserInfo.avatarUrl!=""  ){

                    it.replyUserInfo.avatarUrl = decodePicUrls(it.replyUserInfo.avatarUrl, "0", true)//图片鉴权
                }
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }
        }
        return mGetMessageInnerPageListBean
    }



    /**
     * dada
     */
    fun toUrlSearchUserBean(mSearchUserBean: SearchUserBean?, uid:String): SearchUserBean?{

        mSearchUserBean!!.list.forEach {
            try {
                it.avatarUrl= decodePicUrls(it.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }
        }

        return mSearchUserBean
    }


    /**
     *  图片视频鉴权
     */
    fun toUrlCommentListBean(mCommentListBean: CommentBean?, uid:String): CommentBean?{
        //图片
        mCommentListBean!!.list.forEach {
            try {
                it.commentUserInfo!!.avatarUrl= decodePicUrls(it.commentUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

            try {
                it.replyUserInfo!!.avatarUrl= decodePicUrls(it.replyUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

        }
        return mCommentListBean
    }

    fun toUrlChildCommentListBean(mChildCommentBean: ChildCommentBean?, uid:String): ChildCommentBean?{
        //图片
        mChildCommentBean!!.list.forEach {
            try {
                it.commentUserInfo!!.avatarUrl= decodePicUrls(it.commentUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

            try {
                it.replyUserInfo!!.avatarUrl= decodePicUrls(it.replyUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }
        }
        return mChildCommentBean
    }


    fun toUrlCommentBeanItemBean(mCommentBeanItemBean: CommentBean.Item?, uid:String): CommentBean.Item?{
        //图片
            try {
                mCommentBeanItemBean!!.commentUserInfo!!.avatarUrl= decodePicUrls(mCommentBeanItemBean!!.commentUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

            try {
                mCommentBeanItemBean!!.replyUserInfo!!.avatarUrl= decodePicUrls(mCommentBeanItemBean.replyUserInfo!!.avatarUrl,uid,true)//头像
            }catch (e:Exception){
                Log.e("zqr", e.toString())
            }

        return mCommentBeanItemBean
    }


    fun toUrlChildCommentBeanChildItem(mChildCommentBeanChildItem: ChildCommentBean.ChildItem?, uid:String): ChildCommentBean.ChildItem?{
        //图片
        try {
            mChildCommentBeanChildItem!!.commentUserInfo!!.avatarUrl= decodePicUrls(mChildCommentBeanChildItem.commentUserInfo!!.avatarUrl,uid,true)//头像
        }catch (e:Exception){
            Log.e("zqr", e.toString())
        }

        try {
            mChildCommentBeanChildItem!!.replyUserInfo!!.avatarUrl= decodePicUrls(mChildCommentBeanChildItem!!.replyUserInfo!!.avatarUrl,uid,true)//头像
        }catch (e:Exception){
            Log.e("zqr", e.toString())
        }

        return mChildCommentBeanChildItem
    }


}

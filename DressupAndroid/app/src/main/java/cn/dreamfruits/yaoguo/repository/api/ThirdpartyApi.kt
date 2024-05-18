package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.contacts.ContactsStateBean
import cn.dreamfruits.yaoguo.repository.bean.contacts.QrCodeShortUrlBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 18:29
 */
interface ThirdpartyApi {

    /**
     * 获取CDN的key和随机数
     */
    @POST("/thirdparty/api/getTencentCDNSecretKey")
    fun getTencentCDNSecretKey(): Observable<BaseResult<TencentCDNSecretKeyBean>>
    @POST("/app/api/getUsersig")
    fun getTencentUserSig(): Observable<BaseResult<TencentCDNSecretKeyBean>>


    /**
     * 获取cos 临时密钥
     */
    @POST("/thirdparty/api/getTencentTmpSecretKey")
    fun getTencentTmpSecretKey(): Observable<BaseResult<TencentTmpSecretKeyBean>>


    /**
     * 发现好友部分  也写在这里
     */
    /**
     * 上报通讯录-全量   api

     [{"name":"张三","phone":"18582998776","type":0}]
    name ----> 通讯录中的名字
    phone ----> 手机号
    type ----> 0-删除，1-新增

     */
    @FormUrlEncoded
    @POST("/usercenter/api/uploadAllContacts")
    fun uploadAllContacts(
        @Field("contacts") contacts: String
    ): Observable<BaseResult<ContactsStateBean>>


    /**
     * 上报通讯录-差量   api
    [{"name":"张三","phone":"18582998776","type":0}]
    name ----> 通讯录中的名字
    phone ----> 手机号
    type ----> 0-删除，1-新增
     */
    @FormUrlEncoded
    @POST("/usercenter/api/uploadContacts")
    fun uploadContacts(

        @Field("contacts") contacts: String


    ): Observable<BaseResult<ContactsStateBean>>

    /**
     * 获取通讯录列表   api
     */
    @POST("/usercenter/api/getContactsList")
    fun getContactsList(
    ): Observable<BaseResult<SearchUserBean>>

    /**
     * 获取短链接   api
     */
    @FormUrlEncoded
    @POST("/app/api/getShortUrl")
    fun getShortUrl(
        @Field("targetId") targetId: Long,//目标id 必须
        @Field("type") type: Int,//0-用户，1-帖子，2-单品
    ): Observable<BaseResult<QrCodeShortUrlBean>>


    /**
     * 获取短链接get   api
     */
    @GET("/app/api/resolveShortUrl/{code}")
    fun getShortGetUrl(
        @Path("code") code: String
    ): Observable<BaseResult<QrCodeShortUrlBean>>


    /**
     *感兴趣的人
     */
    @FormUrlEncoded
    @POST("/app/api/getHomeRecommendUserList")
    fun getHomeRecommendUserList(
        @Field("size") size: Int//
    ): Observable<BaseResult<SearchUserBean>>

//    @GET("https://dev.dreamfruits.cn/app/api/resolveShortUrl/2qrl")
//    fun getShortGetUrl(

//    ): Observable<BaseResult<QrCodeShortUrlBean>>

}
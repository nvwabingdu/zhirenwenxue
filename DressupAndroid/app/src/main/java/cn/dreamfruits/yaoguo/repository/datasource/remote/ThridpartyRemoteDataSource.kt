package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.ThirdpartyApi
import cn.dreamfruits.yaoguo.repository.bean.contacts.ContactsStateBean
import cn.dreamfruits.yaoguo.repository.bean.contacts.QrCodeShortUrlBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 18:33
 */
class ThridpartyRemoteDataSource : BaseRemoteDataSource() {

    private val thirdpartyApi: ThirdpartyApi = retrofitService(ThirdpartyApi::class.java)

    /**
     * 获取CDN的key和随机数
     */
    fun getTencentCDNSecretKey(
    ): Observable<TencentCDNSecretKeyBean> {
        return thirdpartyApi.getTencentCDNSecretKey()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取腾讯im userSig
     */
    fun getTencentUserSig(
    ): Observable<TencentCDNSecretKeyBean> {
        return thirdpartyApi.getTencentUserSig()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取cos临时密钥
     */
    fun getTencentTmpSecretKey(): Observable<TencentTmpSecretKeyBean> {
        return thirdpartyApi.getTencentTmpSecretKey()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 上报通讯录-全量   远程仓库
     */
    fun uploadAllContacts(

        contacts: String,

        ): Observable<ContactsStateBean> {
        return thirdpartyApi.uploadAllContacts(contacts)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 上报通讯录-差量   远程仓库
     */
    fun uploadContacts(

        contacts: String,

        ): Observable<ContactsStateBean> {
        return thirdpartyApi.uploadContacts(contacts)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取通讯录列表   远程仓库
     */
    fun getContactsList(


    ): Observable<SearchUserBean> {
        return thirdpartyApi.getContactsList()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取短链接   远程仓库
     */
    fun getShortUrl(

        targetId: Long,//目标id 必须
        type: Int,//0-用户，1-帖子，2-单品

    ): Observable<QrCodeShortUrlBean> {
        return thirdpartyApi.getShortUrl(targetId, type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取短链接   get   远程仓库
     */
    fun getShortGetUrl(
        code: String,//二维码内容 必须
    ): Observable<QrCodeShortUrlBean> {
        return thirdpartyApi.getShortGetUrl(code)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 感兴趣的人
     */
    fun getHomeRecommendUserList(
        size: Int
    ): Observable<SearchUserBean> {
        return thirdpartyApi.getHomeRecommendUserList(size).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

}
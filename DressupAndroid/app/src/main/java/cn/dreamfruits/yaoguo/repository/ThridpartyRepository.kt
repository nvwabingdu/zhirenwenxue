package cn.dreamfruits.yaoguo.repository

import android.util.Log
import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.code
import cn.dreamfruits.yaoguo.repository.bean.contacts.ContactsStateBean
import cn.dreamfruits.yaoguo.repository.bean.contacts.QrCodeShortUrlBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.ThridpartyRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 18:36
 */
class ThridpartyRepository(dataSource: ThridpartyRemoteDataSource) :
    BaseRemoteRepository<ThridpartyRemoteDataSource>(dataSource) {


    /**
     * 获取CDN的key和随机数
     */
    fun getTencentCDNSecretKey(): Observable<TencentCDNSecretKeyBean> {
        return remoteDataSource.getTencentCDNSecretKey().doOnNext {
            MMKVConstants.initData(
                MMKVConstants.GET_TENCENT_CDN_SECRETKEY,
                it
            )
        }
    }

    fun getTencentUserSig(): Observable<TencentCDNSecretKeyBean> {
        return remoteDataSource.getTencentUserSig()
    }

    /**
     * 获取cos临时密钥
     */
    fun getTencentTmpSecretKey(): Observable<TencentTmpSecretKeyBean> {
        return remoteDataSource.getTencentTmpSecretKey()
    }

    /**
     * 上报通讯录-全量  本地仓库
     */
    fun uploadAllContacts(

        contacts: String,

        ): Observable<ContactsStateBean> {
        return remoteDataSource.uploadAllContacts(contacts)
    }


    /**
     * 上报通讯录-差量  本地仓库
     */
    fun uploadContacts(

        contacts: String,

        ): Observable<ContactsStateBean> {
        return remoteDataSource.uploadContacts(contacts)
    }

    /**
     * 获取通讯录列表  本地仓库
     */
    fun getContactsList(


    ): Observable<SearchUserBean> {
        return remoteDataSource.getContactsList()
    }


    /**
     * 获取短链接  本地仓库
     */
    fun getShortUrl(

        targetId: Long,//目标id 必须
        type: Int,//0-用户，1-帖子，2-单品

    ): Observable<QrCodeShortUrlBean> {
        return remoteDataSource.getShortUrl(targetId, type)
    }

    /**
     * 获取短链接  本地仓库
     */
    fun getShortGetUrl(
        code: String,
    ): Observable<QrCodeShortUrlBean> {
        return remoteDataSource.getShortGetUrl(code)
    }


    /**
     * 感兴趣的人
     */
    fun getHomeRecommendUserList( size: Int): Observable<SearchUserBean> {
        return remoteDataSource.getHomeRecommendUserList(size).doOnNext {
//            MMKVConstants.initData(
//                MMKVConstants.GET_HOME_RECOMMEND_USERLIST,
//                it
//            )

        }
    }


}
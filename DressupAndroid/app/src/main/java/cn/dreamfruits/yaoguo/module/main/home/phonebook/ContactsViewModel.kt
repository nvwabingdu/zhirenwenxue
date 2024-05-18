package cn.dreamfruits.yaoguo.module.main.home.phonebook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.adapter.base.loadmore.LoadMoreStatus
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.code
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.login.state.GetContactsListState
import cn.dreamfruits.yaoguo.module.login.state.GetShortUrlGetState
import cn.dreamfruits.yaoguo.module.login.state.GetShortUrlState
import cn.dreamfruits.yaoguo.module.login.state.UploadAllContactsState
import cn.dreamfruits.yaoguo.module.login.state.UploadContactsState
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.*
import cn.dreamfruits.yaoguo.repository.bean.attention.FollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.attention.UnfollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import cn.dreamfruits.yaoguo.repository.bean.contacts.ContactsStateBean
import cn.dreamfruits.yaoguo.repository.bean.contacts.QrCodeShortUrlBean
import cn.dreamfruits.yaoguo.repository.bean.feed.*
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.UninterestedBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.find.IgnoreRecommendUserBean
import cn.dreamfruits.yaoguo.repository.bean.label.GetLabelListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.datasource.CollectRepository
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Tool
import com.xiaomi.push.it
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/3/11
 * @TIME 15:29
 */
class ContactsViewModel : BaseViewModel(){
    private val thridpartyRepository by inject<ThridpartyRepository>()

    /**
     * 上报通讯录-全量  viewModel
     */
    var mUploadAllContactsBean: ContactsStateBean? = null
    private val _uploadAllContactsState = MutableLiveData<UploadAllContactsState>()
    val uploadAllContactsState: MutableLiveData<UploadAllContactsState> get() = _uploadAllContactsState
    fun uploadAllContacts(
        contacts: String
    ) {
        val disposable = thridpartyRepository.uploadAllContacts(contacts)
            .subscribe({
                mUploadAllContactsBean= it
                _uploadAllContactsState.value = UploadAllContactsState.Success
                Log.e("zqr", "_uploadAllContactsState请求成功：$it")
            }, {
                _uploadAllContactsState.value = UploadAllContactsState.Fail(it.message)
                Log.e("zqr", "_uploadAllContactsState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 上报通讯录-差量  viewModel
     */
    var mUploadContactsBean: ContactsStateBean? = null
    private val _uploadContactsState = MutableLiveData<UploadContactsState>()
    val uploadContactsState: MutableLiveData<UploadContactsState> get() = _uploadContactsState
    fun uploadContacts(
        contacts: String
    ) {
        val disposable = thridpartyRepository.uploadContacts(contacts)
            .subscribe({
                mUploadContactsBean= it
                _uploadContactsState.value = UploadContactsState.Success
                Log.e("zqr", "_uploadContactsState请求成功：$it")
            }, {

                _uploadContactsState.value = UploadContactsState.Fail(it.message)
                Log.e("zqr", "_uploadContactsState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取通讯录列表  viewModel
     */
    var mGetContactsListBean: SearchUserBean? = null
    private val _getContactsListState = MutableLiveData<GetContactsListState>()
    val getContactsListState: MutableLiveData<GetContactsListState> get() = _getContactsListState
    fun getContactsList(
    ) {
        val disposable = thridpartyRepository.getContactsList()
            .subscribe({
                it.list.forEach {
                    it.avatarUrl=Singleton.getUrlX(it.avatarUrl,true,100)
                }
                mGetContactsListBean= it
                _getContactsListState.value = GetContactsListState.Success
                Log.e("zqr", "_getContactsListState请求成功：$it")
            }, {
                _getContactsListState.value = GetContactsListState.Fail(it.message)
                Log.e("zqr", "_getContactsListState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 获取短链接  viewModel
     */
    var mQrCodeShortUrlBean: QrCodeShortUrlBean? = null
    private val _getShortUrlState = MutableLiveData<GetShortUrlState>()
    val getShortUrlState: MutableLiveData<GetShortUrlState> get() = _getShortUrlState
    fun getShortUrl(

        targetId: Long,//目标id 必须
        type: Int,//0-用户，1-帖子，2-单品

    ) {
        val disposable = thridpartyRepository.getShortUrl(targetId,type)
            .subscribe({
                mQrCodeShortUrlBean= it
                _getShortUrlState.value = GetShortUrlState.Success
                Log.e("zqr", "_getShortUrlState请求成功：$it")
            }, {

                _getShortUrlState.value = GetShortUrlState.Fail(it.message)
                Log.e("zqr", "_getShortUrlState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 解析二维码 get viewModel
     */
    var mQrCodeShortUrlGetBean: QrCodeShortUrlBean? = null
    private val _getShortUrlGetState = MutableLiveData<GetShortUrlGetState>()
    val getShortUrlGetState: MutableLiveData<GetShortUrlGetState> get() = _getShortUrlGetState
    fun getShortGetUrl(
            code: String,//目标id 必须
    ) {
        val disposable = thridpartyRepository.getShortGetUrl(code)
            .subscribe({
                mQrCodeShortUrlGetBean= it
                _getShortUrlGetState.value = GetShortUrlGetState.Success
                Log.e("zqr", "_getShortUrlGetState请求成功：$it")
            }, {

                _getShortUrlGetState.value = GetShortUrlGetState.Fail(it.message)
                Log.e("zqr", "_getShortUrlGetState请求失败：$it")
            })
        addDisposable(disposable)
    }




    /**
     * 感兴趣的人
     */
    var mHomeRecommendUserListBean: SearchUserBean? = null
    private val _homeRecommendUserListState = MutableLiveData<HomeRecommendUserListState>()
    val homeRecommendUserListState: MutableLiveData<HomeRecommendUserListState> get() = _homeRecommendUserListState
    fun getHomeRecommendUserList(size: Int) {
        val disposable = thridpartyRepository.getHomeRecommendUserList(size)
            .subscribe({
                //取得的集合需要对图片地址处理鉴权，1、这里需要处理， 2、另外需要在对应的本地仓库处理 此处不做缓存不用处理
                it.list.forEach {
                    it.avatarUrl=Singleton.getUrlX(it.avatarUrl,true,100)
                }
                mHomeRecommendUserListBean=it
                _homeRecommendUserListState.value = HomeRecommendUserListState.Success
                Log.e("zqr", "_homeRecommendUserListState请求成功：$it")
            }, {

                _homeRecommendUserListState.value = HomeRecommendUserListState.Fail(it.message)
                Log.e("zqr", "_homeRecommendUserListState请求失败：$it")
            })
        addDisposable(disposable)
    }




}
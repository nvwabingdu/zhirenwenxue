package cn.dreamfruits.yaoguo.module.main.mine

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.repository.bean.CommonStateBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean2
import cn.dreamfruits.yaoguo.repository.bean.set.GetBindRelationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetNotificationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.set.NullBean
import cn.dreamfruits.yaoguo.repository.bean.set.SetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean
import cn.dreamfruits.yaoguo.repository.datasource.CollectRepository
import cn.dreamfruits.yaoguo.util.Tool
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 15:03
 */
class MineViewModel : BaseViewModel() {
    private val userRepository by inject<UserRepository>()
    private val collectRepository by inject<CollectRepository>()


    /**
     * 通过用户id获取用户信息  viewModel
     */
    var mUserInfoBean: UserInfoBean? = null
    private val _getUserInfoState = MutableLiveData<GetUserInfoState>()
    val getUserInfoState: MutableLiveData<GetUserInfoState> get() = _getUserInfoState
    fun getUserInfo(
        userId: Long?,//目标id 必须
    ) {
        val disposable = userRepository.getUserInfo(userId)
            .subscribe({
                mUserInfoBean= it
                _getUserInfoState.value = GetUserInfoState.Success
                Log.e("zqr", "_getUserInfoState请求成功：$it")
            }, {
                _getUserInfoState.value = GetUserInfoState.Fail(it.message)
                Log.e("zqr", "_getUserInfoState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 设置用户消息通知设置  viewModel
     */
    var mNullBean: NullBean? = null
    private val _setUserNotificationSettingState = MutableLiveData<SetUserNotificationSettingState>()
    val setUserNotificationSettingState: MutableLiveData<SetUserNotificationSettingState> get() = _setUserNotificationSettingState
    fun setUserNotificationSetting(
        type: Int,//0-点赞和收藏，1-新粉丝，2-评论，3-@，4-单聊，5-关注人发动态，6-官方
        isOpen: Int,//0-关闭，1-开起
    ) {
        val disposable = userRepository.setUserNotificationSetting(type,isOpen)
            .subscribe({
                mNullBean= it
                _setUserNotificationSettingState.value = SetUserNotificationSettingState.Success
                Log.e("zqr", "_setUserNotificationSettingState请求成功：$it")
            }, {

                _setUserNotificationSettingState.value = SetUserNotificationSettingState.Fail(it.message)
                Log.e("zqr", "_setUserNotificationSettingState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取用户消息通知设置列表  viewModel
     */
    var mGetNotificationBean: GetNotificationBean? = null
    private val _getUserNotificationSettingListState = MutableLiveData<GetUserNotificationSettingListState>()
    val getUserNotificationSettingListState: MutableLiveData<GetUserNotificationSettingListState> get() = _getUserNotificationSettingListState
    fun getUserNotificationSettingList(

    ) {
        val disposable = userRepository.getUserNotificationSettingList()
            .subscribe({
                mGetNotificationBean= it
                _getUserNotificationSettingListState.value = GetUserNotificationSettingListState.Success
                Log.e("zqr", "_getUserNotificationSettingListState请求成功：$it")
            }, {

                _getUserNotificationSettingListState.value = GetUserNotificationSettingListState.Fail(it.message)
                Log.e("zqr", "_getUserNotificationSettingListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取绑定关系  viewModel
     */
    var mGetBindRelationBean: GetBindRelationBean? = null
    private val _getBindRelationState = MutableLiveData<GetBindRelationState>()
    val getBindRelationState: MutableLiveData<GetBindRelationState> get() = _getBindRelationState
    fun getBindRelation(
    ) {
        val disposable = userRepository.getBindRelation()
            .subscribe({
                mGetBindRelationBean= it
                _getBindRelationState.value = GetBindRelationState.Success
                Log.e("zqr", "_getBindRelationState请求成功：$it")
            }, {

                _getBindRelationState.value = GetBindRelationState.Fail(it.message)
                Log.e("zqr", "_getBindRelationState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 绑定三方  viewModel
     */
    var mBindThreeAccountBean: BindThreeAccountBean2? = null
    private val _bindThirdPartyState = MutableLiveData<BindThirdPartyState>()
    val bindThirdPartyState: MutableLiveData<BindThirdPartyState> get() = _bindThirdPartyState
    fun bindThirdParty(
        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
        code: String?,//微信登录的code，qq登录的token
        openId: String?,//qq的openId
    ) {
        val disposable = userRepository.bindThirdParty(type,code,openId)
            .subscribe({
                mBindThreeAccountBean= it
                _bindThirdPartyState.value = BindThirdPartyState.Success
                Log.e("zqr", "_bindThirdPartyState请求成功：$it")
            }, {

                _bindThirdPartyState.value = BindThirdPartyState.Fail(it.message)
                Log.e("zqr", "_bindThirdPartyState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 解除绑定三方  viewModel
     */
    var mUnBindThreeAccountBean: BindThreeAccountBean2? = null
    private val _unbindThirdPartyState = MutableLiveData<UnbindThirdPartyState>()
    val unbindThirdPartyState: MutableLiveData<UnbindThirdPartyState> get() = _unbindThirdPartyState
    fun unbindThirdParty(
        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
    ) {
        val disposable = userRepository.unbindThirdParty(type)
            .subscribe({
                mUnBindThreeAccountBean= it
                _unbindThirdPartyState.value = UnbindThirdPartyState.Success
                Log.e("zqr", "_unbindThirdPartyState请求成功：$it")
            }, {

                _unbindThirdPartyState.value = UnbindThirdPartyState.Fail(it.message)
                Log.e("zqr", "_unbindThirdPartyState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 意见反馈  viewModel
     */
    var mCommonStateBean: CommonStateBean? = null
    private val _setFeedbackState = MutableLiveData<SetFeedbackState>()
    val setFeedbackState: MutableLiveData<SetFeedbackState> get() = _setFeedbackState
    fun feedback(

        content: String,//目标id
        picUrls: String?,//可选 图片数组，["a","b"]

    ) {
        val disposable = userRepository.feedback(content,picUrls)
            .subscribe({
                mCommonStateBean= it
                _setFeedbackState.value = SetFeedbackState.Success
                Log.e("zqr", "_setFeedbackState请求成功：$it")
            }, {

                _setFeedbackState.value = SetFeedbackState.Fail(it.message)
                Log.e("zqr", "_setFeedbackState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 获取用户个人主页展示设置  viewModel
     */
    var mGetUserHomePageSettingBean: GetUserHomePageSettingBean? = null
    private val _getUserHomePageSettingState = MutableLiveData<GetUserHomePageSettingState>()
    val getUserHomePageSettingState: MutableLiveData<GetUserHomePageSettingState> get() = _getUserHomePageSettingState
    fun getUserHomePageSetting(



    ) {
        val disposable = userRepository.getUserHomePageSetting()
            .subscribe({
                mGetUserHomePageSettingBean= it
                _getUserHomePageSettingState.value = GetUserHomePageSettingState.Success
                Log.e("zqr", "_getUserHomePageSettingState请求成功：$it")
            }, {

                _getUserHomePageSettingState.value = GetUserHomePageSettingState.Fail(it.message)
                Log.e("zqr", "_getUserHomePageSettingState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 设置用户个人主页展示设置  viewModel
     */
    var mSetUserHomePageSettingBean: SetUserHomePageSettingBean? = null
    private val _setUserHomePageSettingState = MutableLiveData<SetUserHomePageSettingState>()
    val setUserHomePageSettingState: MutableLiveData<SetUserHomePageSettingState> get() = _setUserHomePageSettingState
    fun setUserHomePageSetting(

        type: Int,//0:生日，1:地区
        isShow: Int,//是否显示；0-不展示，1-展示
        subType: Int?,//当type为0时，此字段必填；0:年龄，1-星座

    ) {
        val disposable = userRepository.setUserHomePageSetting(type,isShow,subType)
            .subscribe({
                mSetUserHomePageSettingBean= it
                _setUserHomePageSettingState.value = SetUserHomePageSettingState.Success
                Log.e("zqr", "_setUserHomePageSettingState请求成功：$it")
            }, {

                _setUserHomePageSettingState.value = SetUserHomePageSettingState.Fail(it.message)
                Log.e("zqr", "_setUserHomePageSettingState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取关注列表  viewModel
     */
    var mGetCareListBean: SearchUserBean? = null
    private val _getCareListState = MutableLiveData<GetCareListState>()
    val getCareListState: MutableLiveData<GetCareListState> get() = _getCareListState
    fun getUserFollowList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ) {
        val disposable = userRepository.getUserFollowList(size,lastTime,targetUid)
            .subscribe({
                mGetCareListBean=Tool().toUrlSearchUserBean(it,"0")
                _getCareListState.value = GetCareListState.Success
                Log.e("zqr", "_getCareListState请求成功：$it")
            }, {

                _getCareListState.value = GetCareListState.Fail(it.message)
                Log.e("zqr", "_getCareListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取粉丝列表  viewModel
     */
    var mGetFansListBean: SearchUserBean? = null
    private val _getFansListState = MutableLiveData<GetFansListState>()
    val getFansListState: MutableLiveData<GetFansListState> get() = _getFansListState
    fun getUserFollowerList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ) {
        val disposable = userRepository.getUserFollowerList(size,lastTime,targetUid)
            .subscribe({
                mGetFansListBean= Tool().toUrlSearchUserBean(it,"0")
                _getFansListState.value = GetFansListState.Success
                Log.e("zqr", "_getFansListState请求成功：$it")
            }, {

                _getFansListState.value = GetFansListState.Fail(it.message)
                Log.e("zqr", "_getFansListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 搜索关注用户  viewModel
     */
    var mSearchCareUserBean: SearchUserBean? = null
    private val _searchCareUserState = MutableLiveData<SearchCareUserState>()
    val searchCareUserState: MutableLiveData<SearchCareUserState> get() = _searchCareUserState
    fun searchFollow(

        size: Int,
        keyWord: String,//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ) {
        val disposable = userRepository.searchFollow(size,keyWord,lastTime)
            .subscribe({
                mSearchCareUserBean= Tool().toUrlSearchUserBean(it,"0")
                _searchCareUserState.value = SearchCareUserState.Success
                Log.e("zqr", "_searchCareUserState请求成功：$it")
            }, {

                _searchCareUserState.value = SearchCareUserState.Fail(it.message)
                Log.e("zqr", "_searchCareUserState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 更新用户信息
     */
    var mFixUserInfoBean: UserInfoBean? = null
    private val _fixUserInfoState = MutableLiveData<FixUserInfoState>()
    val fixUserInfoState: MutableLiveData<FixUserInfoState> get() = _fixUserInfoState
    fun updateUserInfo(gender:Int?=null,
                       nickName: String="",
                       avatarUrl: String="",
                       description: String?=null,
                       ygId: String?=null,
                       birthday: Long?=null,
                       country: String?=null,
                       province: String?=null,
                       city: String?=null,
                       inviteCode: String?=null
    ){
        val disposable = userRepository.updateUserInfo(gender,nickName, avatarUrl, description, ygId, birthday, country, province, city,inviteCode)
            .subscribe({

                if (!TextUtils.isEmpty(nickName)||!TextUtils.isEmpty(avatarUrl)){
                    editNameOrFaceurl(nickname = nickName, faceUrl = avatarUrl, userInfoBean = it)
                } else {
                    mFixUserInfoBean= it
                    _fixUserInfoState.value = FixUserInfoState.Success

                }
                Log.e("zqr", "_fixUserInfoState请求成功：$it")

            }, {

                _fixUserInfoState.value = FixUserInfoState.Fail(it.message)
                Log.e("zqr", "_fixUserInfoState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取黑名单列表  viewModel
     */
    var mBlackListBean: SearchUserBean? = null
    private val _getBlackListState = MutableLiveData<GetBlackListState>()
    val getBlackListState: MutableLiveData<GetBlackListState> get() = _getBlackListState
    fun getBlackList(
        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
    ) {
        val disposable = userRepository.getBlackList(size,lastTime)
            .subscribe({
                mBlackListBean=Tool().toUrlSearchUserBean(it,"0")
                _getBlackListState.value = GetBlackListState.Success
                Log.e("zqr", "_getBlackListState请求成功：$it")
            }, {

                _getBlackListState.value = GetBlackListState.Fail(it.message)
                Log.e("zqr", "_getBlackListState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 操作黑名单  viewModel
     */
    var mOperateBlackListBean: CommonStateBean? = null
    private val _operateBlackListState = MutableLiveData<OperateBlackListState>()
    val operateBlackListState: MutableLiveData<OperateBlackListState> get() = _operateBlackListState
    fun operateBlackList(

        operation: Int,//0-拉黑， 1-取消拉黑
        ids: String,//被拉黑用户id组成的字符串，用英文逗号隔开，例如：id1,id2,id3

    ) {
        val disposable = userRepository.operateBlackList(operation,ids)
            .subscribe({
                mOperateBlackListBean= it
                _operateBlackListState.value = OperateBlackListState.Success
                Log.e("zqr", "_operateBlackListState请求成功：$it")
            }, {

                _operateBlackListState.value = OperateBlackListState.Fail(it.message)
                Log.e("zqr", "_operateBlackListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    fun editNameOrFaceurl(nickname: String="", faceUrl: String="",userInfoBean: UserInfoBean) {
        // 设置个人资料
        // 设置个人资料
        val info = V2TIMUserFullInfo()
        if (!TextUtils.isEmpty(nickname))
            info.setNickname(nickname)
        if (!TextUtils.isEmpty(faceUrl))
            info.faceUrl = faceUrl
        V2TIMManager.getInstance().setSelfInfo(info, object : V2TIMCallback {
            override fun onSuccess() {
                // 设置个人资料成功
                mFixUserInfoBean= userInfoBean
                _fixUserInfoState.value = FixUserInfoState.Success
            }

            override fun onError(code: Int, desc: String) {
                // 设置个人资料失败
            }
        })

//
//// 监听个人资料变更回调
//        V2TIMManager.getInstance().addIMSDKListener(object : V2TIMSDKListener() {
//            override fun onSelfInfoUpdated(info: V2TIMUserFullInfo) {
//                // 收到个人资料变更回调
//            }
//        })

    }


}
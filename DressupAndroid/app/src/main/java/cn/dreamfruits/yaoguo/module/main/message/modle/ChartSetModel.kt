package cn.dreamfruits.yaoguo.module.main.message.modle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.code
import cn.dreamfruits.yaoguo.module.login.BlackListState
import cn.dreamfruits.yaoguo.module.login.DeleteFromBlackListState
import cn.dreamfruits.yaoguo.module.login.SetState
import cn.dreamfruits.yaoguo.module.login.TextMsgState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserInfoState
import cn.dreamfruits.yaoguo.module.main.home.state.IsStrangerBeanState
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean
import cn.dreamfruits.yaoguo.repository.datasource.MessageRepository
import com.blankj.utilcode.util.LogUtils
import com.tencent.imsdk.v2.*
import org.koin.core.component.inject


/**
 * @author Lee
 * @createTime 2023-06-29 20 GMT+8
 * @desc :
 */
class ChartSetModel : BaseViewModel() {

    private val _setState = MutableLiveData<SetState>()
    val setState: LiveData<SetState> get() = _setState

    private val _blackListState = MutableLiveData<BlackListState>()
    val blackListState: LiveData<BlackListState> get() = _blackListState

    private val _deleteFromBlackListState = MutableLiveData<DeleteFromBlackListState>()
    val deleteFromBlackListState: LiveData<DeleteFromBlackListState> get() = _deleteFromBlackListState

    private val messageRepository by inject<MessageRepository>()

    /**
     * 获取黑名单列表
     */
    fun getBlackList() {
        V2TIMManager.getFriendshipManager()
            .getBlackList(object : V2TIMValueCallback<List<V2TIMFriendInfo>> {
                override fun onSuccess(p0: List<V2TIMFriendInfo>) {
                    _blackListState.value = BlackListState.Success(p0)
                }

                override fun onError(code: Int, desc: String) {
                    _blackListState.value = BlackListState.Fail(code, desc)
                }

            })
    }


    fun isInBlackList(userId: String, list: List<V2TIMFriendInfo>): Boolean {
        val find = list.find { it.userID == userId }
        return find != null
    }


    fun deleteFromBlackList(userId: String) {

        var list: MutableList<String> = arrayListOf()
        list.add(userId)
        V2TIMManager.getFriendshipManager()
            .deleteFromBlackList(
                list,
                object : V2TIMValueCallback<List<V2TIMFriendOperationResult>> {
                    override fun onSuccess(p0: List<V2TIMFriendOperationResult>) {
                        _deleteFromBlackListState.value = DeleteFromBlackListState.Success(p0)
                    }

                    override fun onError(code: Int, desc: String) {
                        _deleteFromBlackListState.value = DeleteFromBlackListState.Fail(code, desc)
                    }

                })
    }

    fun addToBlackList(userId: String, position: Int = 0, operation: Int = 0) {

//        var list: MutableList<String> = arrayListOf()
//        list.add(userId)
//
//        V2TIMManager.getFriendshipManager()
//            .addToBlackList(list, object : V2TIMValueCallback<List<V2TIMFriendOperationResult>> {
//                override fun onSuccess(p0: List<V2TIMFriendOperationResult>) {
        localServerAddBlackList(operation, userId, null, position)
//                }
//
//                override fun onError(code: Int, desc: String) {
//                    _setState.value = SetState.Fail(code, desc)
//                }
//
//            })
    }

    //    _setState.value = SetState.Success(p0)
    fun localServerAddBlackList(
        operation: Int,
        userId: String,
        p0: List<V2TIMFriendOperationResult>?,
        position: Int = 0,
    ) {
        val disposable = messageRepository.operateBlackList(userId, operation)
            .subscribe({
                _setState.value = SetState.Success(p0, it, position, operation)
            }, {
                _setState.value = SetState.Fail(-1, it.message!!)
            })
        addDisposable(disposable)
    }

    private val userRepository by inject<UserRepository>()

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
                mUserInfoBean = it
                _getUserInfoState.value = GetUserInfoState.Success
                Log.e("zqr", "_getUserInfoState请求成功：$it")
            }, {
                _getUserInfoState.value = GetUserInfoState.Fail(it.message)
                Log.e("zqr", "_getUserInfoState请求失败：$it")
            })
        addDisposable(disposable)
    }
}
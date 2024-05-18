package cn.dreamfruits.yaoguo.module.login

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.module.main.home.state.HomeScrollHotWordsState
import cn.dreamfruits.yaoguo.repository.bean.message.BlackListBean
import com.tencent.imsdk.v2.*


sealed class SetState {

    data class Success(
        val message: List<V2TIMFriendOperationResult>?,
        val bean: BlackListBean,
        val position: Int,
        val operation: Int,
    ) :
        SetState()

    class Fail(val code: Int, val errorMsg: String) : SetState()
}

sealed class BlackListState {

    data class Success(val list: List<V2TIMFriendInfo>) : BlackListState()

    class Fail(val code: Int, val errorMsg: String) : BlackListState()
}

sealed class DeleteFromBlackListState {

    data class Success(val list: List<V2TIMFriendOperationResult>) : DeleteFromBlackListState()

    class Fail(val code: Int, val errorMsg: String) : DeleteFromBlackListState()
}

sealed class BlackListServerState {

    data class Success(val list: BasePageResult<BlackListBean>) : BlackListServerState()

    class Fail(val errorMsg: String) : BlackListServerState()
}

sealed class DeleteConversionState {

    data class Success(val position: Int) : DeleteConversionState()

    class Fail(val code: Int, val errorMsg: String) : DeleteConversionState()
}

sealed class ConversionChangedState {

    data class Success(val conversationList: List<V2TIMConversation>) :
        ConversionChangedState()

}
sealed class ConversionAddState {

    data class Success(val conversationList: List<V2TIMConversation>) :
        ConversionAddState()


}
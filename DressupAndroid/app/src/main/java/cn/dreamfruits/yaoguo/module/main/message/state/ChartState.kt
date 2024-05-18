package cn.dreamfruits.yaoguo.module.login

import cn.dreamfruits.yaoguo.module.main.home.state.HomeScrollHotWordsState
import cn.dreamfruits.yaoguo.module.main.message.bean.UserMsgCountEntity
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMUserFullInfo


sealed class TextMsgState {

    data class Success(val message: V2TIMMessage) : TextMsgState()

    class Fail(val code: Int, val errorMsg: String, val originMsg: V2TIMMessage) : TextMsgState()
}

sealed class MsgRevokedState {

    data class Success(val msgId: String) : MsgRevokedState()

}

sealed class ImUserInfoState {

    data class Success(val message: V2TIMUserFullInfo) : ImUserInfoState()

    class Fail(val code: Int, val errorMsg: String) : ImUserInfoState()
}

sealed class UserMsgCountState {

    data class Success(val count: UserMsgCountEntity) : UserMsgCountState()

    class Fail(val code: Int, val errorMsg: String) : UserMsgCountState()
}
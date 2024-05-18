package cn.dreamfruits.yaoguo.module.publish.state

import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean

//话题state
sealed class TopicState {

    data class Success(val list: List<TopicBean>?,val localTopic:String? =null) : TopicState()

    object Fail : TopicState()
}


//用户state
sealed class UserState {

    data class Success(val list: List<SearchUserBean.Item>?) : UserState()

    object Fail : UserState()

}
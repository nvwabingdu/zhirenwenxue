package cn.dreamfruits.yaoguo.module.main.home.state

import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.repository.bean.feed.DeleteFeedBean
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.repository.bean.message.IsStrangerBean

import com.tencent.imsdk.v2.V2TIMConversationResult

/**
 * @Author qiwangi
 * @Date 2023/3/18
 * @TIME 20:58
 */
//postDetailsViewModel.commentPublishBeanState.observe(this) { it ->
//    when (it) {
//        is CommentPublishBeanState.Success -> {
//
//        }
//        is CommentPublishBeanState.Fail -> {
//         //1.是否有断网提示
//         Singleton.isNetConnectedToast(this)
//        }
//    }
//}

sealed class HomeScrollHotWordsState {

    object Success : HomeScrollHotWordsState()

    class Fail(val errorMsg: String?) : HomeScrollHotWordsState()
}

sealed class GetStyleVersionListBeanState {

    object Success : GetStyleVersionListBeanState()

    class Fail(val errorMsg: String?) : GetStyleVersionListBeanState()
}

/**
 * 删除动态  状态接口类
 */
sealed class DeleteFeedState {

    object Success : DeleteFeedState()

    class Fail(val errorMsg: String?) : DeleteFeedState()
}


sealed class HomeRecommendFeedListState {

    object Success : HomeRecommendFeedListState()

    class Fail(val errorMsg: String?) : HomeRecommendFeedListState()
}

/**
 * 获取用户动态列表  状态接口类
 */
sealed class GetUserFeedListState {

    object Success : GetUserFeedListState()

    class Fail(val errorMsg: String?) : GetUserFeedListState()
}


sealed class HomeRecommendLabelListState {

    object Success : HomeRecommendLabelListState()

    class Fail(val errorMsg: String?) : HomeRecommendLabelListState()
}


sealed class HomeGetLabelFeedListState {

    object Success : HomeGetLabelFeedListState()

    class Fail(val errorMsg: String?) : HomeGetLabelFeedListState()
}


sealed class HomeFeedbackFeedState {

    object Success : HomeFeedbackFeedState()

    class Fail(val errorMsg: String?) : HomeFeedbackFeedState()
}

sealed class HomeUninterestedState {

    object Success : HomeUninterestedState()

    class Fail(val errorMsg: String?) : HomeUninterestedState()
}

/**
 * 获取版型分类列表APP（目前用于diy）  状态接口类
 */
sealed class GetStyleVersionTypeListBeanState {

    object Success : GetStyleVersionTypeListBeanState()

    class Fail(val errorMsg: String?) : GetStyleVersionTypeListBeanState()
}

sealed class GetStyleVersionTypeListBeanStateLeft {

    object Success : GetStyleVersionTypeListBeanStateLeft()

    class Fail(val errorMsg: String?) : GetStyleVersionTypeListBeanStateLeft()
}

/**
 * 重新上传制作服装单品  状态接口类
 */
sealed class RemakeClothesItem {

    object Success : RemakeClothesItem()

    class Fail(val errorMsg: String?) : RemakeClothesItem()
}

sealed class HomeGetCareFeedListState {

    object Success : HomeGetCareFeedListState()

    class Fail(val errorMsg: String?) : HomeGetCareFeedListState()
}

sealed class StyleVersionListByTypeBeanState {

    object Success : StyleVersionListByTypeBeanState()

    class Fail(val errorMsg: String?) : StyleVersionListByTypeBeanState()
}

/**
 * 根据类型获取版型列表2  逛逛  状态接口类
 */
sealed class GetStyleVersionTypeListStrollState {

    object Success : GetStyleVersionTypeListStrollState()

    class Fail(val errorMsg: String?) : GetStyleVersionTypeListStrollState()
}

/**
 * 根据类型获取版型列表2  逛逛  状态接口类
 */
sealed class GetStyleVersionTypeListStrollStateLeft {

    object Success : GetStyleVersionTypeListStrollStateLeft()

    class Fail(val errorMsg: String?) : GetStyleVersionTypeListStrollStateLeft()
}

/**
 * 根据二级分类获取服装列表 逛逛  状态接口类
 */
sealed class GetClothesItemListByTypeStrollStrollState {

    object Success : GetClothesItemListByTypeStrollStrollState()

    class Fail(val errorMsg: String?) : GetClothesItemListByTypeStrollStrollState()
}


/**
 * 根据分类id获取版型列表（目前用于diy）  状态接口类
 */
sealed class GetStyleVersionListByTypeState {

    object Success : GetStyleVersionListByTypeState()

    class Fail(val errorMsg: String?) : GetStyleVersionListByTypeState()
}

sealed class ClothesItemListBySvIdBeanState {

    object Success : ClothesItemListBySvIdBeanState()

    class Fail(val errorMsg: String?) : ClothesItemListBySvIdBeanState()
}

sealed class MyDiyListBeanBeanState {

    object Success : MyDiyListBeanBeanState()

    class Fail(val errorMsg: String?) : MyDiyListBeanBeanState()
}

/**
 * 获取服装单品详情  状态接口类
 */
sealed class GetClothesItemInfoBeanState {

    object Success : GetClothesItemInfoBeanState()

    class Fail(val errorMsg: String?) : GetClothesItemInfoBeanState()
}

/**
 * 潮流精选  状态接口类
 */
sealed class GetFeedListByCvIdBeanState {

    object Success : GetFeedListByCvIdBeanState()

    class Fail(val errorMsg: String?) : GetFeedListByCvIdBeanState()
}

/**
 * 相似穿搭  状态接口类
 */
sealed class GetSimilarRecommendBeanState {

    object Success : GetSimilarRecommendBeanState()

    class Fail(val errorMsg: String?) : GetSimilarRecommendBeanState()
}


/**
 * 修改单品可见状态  状态接口类
 */
sealed class ChangeClothesItemSeeLimitState {

    object Success : ChangeClothesItemSeeLimitState()

    class Fail(val errorMsg: String?) : ChangeClothesItemSeeLimitState()
}
/**
 * 修改单品名字  状态接口类
 */
sealed class ChangeClothesItemNameState {

    object Success : ChangeClothesItemNameState()

    class Fail(val errorMsg: String?) : ChangeClothesItemNameState()
}

/**
 * 删除单品  状态接口类
 */
sealed class DeleteClothesItemState {

    object Success : DeleteClothesItemState()

    class Fail(val errorMsg: String?) : DeleteClothesItemState()
}

sealed class GetLaudMessageListBeanState {

    object Success : GetLaudMessageListBeanState()

    class Fail(val errorMsg: String?) : GetLaudMessageListBeanState()
}

/**
 * 通过用户id获取用户信息  状态接口类
 */
sealed class GetUserInfoState {

    object Success : GetUserInfoState()

    class Fail(val errorMsg: String?) : GetUserInfoState()
}


/**
 * 设置用户消息通知设置  状态接口类
 */
sealed class SetUserNotificationSettingState {

    object Success : SetUserNotificationSettingState()

    class Fail(val errorMsg: String?) : SetUserNotificationSettingState()
}

/**
 * 获取用户消息通知设置列表  状态接口类
 */
sealed class GetUserNotificationSettingListState {

    object Success : GetUserNotificationSettingListState()

    class Fail(val errorMsg: String?) : GetUserNotificationSettingListState()
}

/**
 * 获取绑定关系  状态接口类
 */
sealed class GetBindRelationState {

    object Success : GetBindRelationState()

    class Fail(val errorMsg: String?) : GetBindRelationState()
}

/**
 * 绑定三方  状态接口类
 */
sealed class BindThirdPartyState {

    object Success : BindThirdPartyState()

    class Fail(val errorMsg: String?) : BindThirdPartyState()
}

/**
 * 解除绑定三方  状态接口类
 */
sealed class UnbindThirdPartyState {

    object Success : UnbindThirdPartyState()

    class Fail(val errorMsg: String?) : UnbindThirdPartyState()
}

/**
 * 意见反馈  状态接口类
 */
sealed class SetFeedbackState {

    object Success : SetFeedbackState()

    class Fail(val errorMsg: String?) : SetFeedbackState()
}

/**
 * 获取用户个人主页展示设置  状态接口类
 */
sealed class GetUserHomePageSettingState {

    object Success : GetUserHomePageSettingState()

    class Fail(val errorMsg: String?) : GetUserHomePageSettingState()
}

/**
 * 设置用户个人主页展示设置  状态接口类
 */
sealed class SetUserHomePageSettingState {

    object Success : SetUserHomePageSettingState()

    class Fail(val errorMsg: String?) : SetUserHomePageSettingState()
}

/**
 * 获取关注列表  状态接口类
 */
sealed class GetCareListState {

    object Success : GetCareListState()

    class Fail(val errorMsg: String?) : GetCareListState()
}

/**
 * 获取粉丝列表  状态接口类
 */
sealed class GetFansListState {

    object Success : GetFansListState()

    class Fail(val errorMsg: String?) : GetFansListState()
}
/**
 * 搜索关注用户  状态接口类
 */
sealed class SearchCareUserState {

    object Success : SearchCareUserState()

    class Fail(val errorMsg: String?) : SearchCareUserState()
}

/**
 * 修改用户信息
 */
sealed class FixUserInfoState {

    object Success : FixUserInfoState()

    class Fail(val errorMsg: String?) : FixUserInfoState()
}

/**
 * 获取黑名单列表  状态接口类
 */
sealed class GetBlackListState {

    object Success : GetBlackListState()

    class Fail(val errorMsg: String?) : GetBlackListState()
}

/**
 * 操作黑名单  状态接口类
 */
sealed class OperateBlackListState {

    object Success : OperateBlackListState()

    class Fail(val errorMsg: String?) : OperateBlackListState()
}

/**
 * 获取收藏列表  状态接口类
 */
sealed class GetCollectListState {

    object Success : GetCollectListState()

    class Fail(val errorMsg: String?) : GetCollectListState()
}

sealed class GetSysMessageListBeanState {

    object Success : GetSysMessageListBeanState()

    class Fail(val errorMsg: String?) : GetSysMessageListBeanState()
}

sealed class GetFollowMessageListBeanState {

    object Success : GetFollowMessageListBeanState()

    class Fail(val errorMsg: String?) : GetFollowMessageListBeanState()
}

sealed class GetCommentMessageListBeanState {

    object Success : GetCommentMessageListBeanState()

    class Fail(val errorMsg: String?) : GetCommentMessageListBeanState()
}


sealed class GetUnreadCountBeanState {

    object Success : GetUnreadCountBeanState()

    class Fail(val errorMsg: String?) : GetUnreadCountBeanState()
}

sealed class CheckMessageCountBeanState {

    object Success : CheckMessageCountBeanState()

    class Fail(val errorMsg: String?) : CheckMessageCountBeanState()
}

sealed class UpdateUnreadToReadBeanState {

    object Success : UpdateUnreadToReadBeanState()

    class Fail(val errorMsg: String?) : UpdateUnreadToReadBeanState()
}

sealed class IsStrangerBeanState {

    class Success(val isStrangerBean: IsStrangerBean,val list: MutableList<ConversionEntity>,val isAdd : Boolean) : IsStrangerBeanState()

    class Fail(val errorMsg: String?) : IsStrangerBeanState()
}


sealed class HomeRecommendUserListState {

    object Success : HomeRecommendUserListState()

    class Fail(val errorMsg: String?) : HomeRecommendUserListState()
}


sealed class IgnoreRecommendUserState {

    object Success : IgnoreRecommendUserState()

    class Fail(val errorMsg: String?) : IgnoreRecommendUserState()
}


sealed class FollowUserBeanState {

    object Success : FollowUserBeanState()

    class Fail(val errorMsg: String?) : FollowUserBeanState()
}


sealed class UnfollowUserBeanState {

    object Success : UnfollowUserBeanState()

    class Fail(val errorMsg: String?) : UnfollowUserBeanState()
}


sealed class LaudFeedBeanState {

    object Success : LaudFeedBeanState()

    class Fail(val errorMsg: String?) : LaudFeedBeanState()
}


sealed class UnLudFeedBeanState {

    object Success : UnLudFeedBeanState()

    class Fail(val errorMsg: String?) : UnLudFeedBeanState()
}


sealed class CollectBeanState {

    object Success : CollectBeanState()

    class Fail(val errorMsg: String?) : CollectBeanState()
}

sealed class UnCollectBeanState {

    object Success : UnCollectBeanState()

    class Fail(val errorMsg: String?) : UnCollectBeanState()
}


sealed class CommentDetailBeanState {

    object Success : CommentDetailBeanState()

    class Fail(val errorMsg: String?) : CommentDetailBeanState()
}

sealed class LabelDetailsBeanState {

    object Success : LabelDetailsBeanState()

    class Fail(val errorMsg: String?) : LabelDetailsBeanState()
}


sealed class FeedDetailsBeanState {

    object Success : FeedDetailsBeanState()

    class Fail(val errorMsg: String?) : FeedDetailsBeanState()
}

sealed class CommentListBeanState {

    object Success : CommentListBeanState()

    class Fail(val errorMsg: String?) : CommentListBeanState()
}

sealed class CommentPublishBeanState {

    object Success : CommentPublishBeanState()

    class Fail(val errorMsg: String?) : CommentPublishBeanState()
}

//========子级
sealed class ChildCommentBeanState {

    object Success : ChildCommentBeanState()

    class Fail(val errorMsg: String?) : ChildCommentBeanState()
}

sealed class CommentChildPublishBeanState {

    object Success : CommentChildPublishBeanState()

    class Fail(val errorMsg: String?) : CommentChildPublishBeanState()
}
//========子级


sealed class AtUserListBeanState {

    object Success : AtUserListBeanState()

    class Fail(val errorMsg: String?) : AtUserListBeanState()
}

sealed class SearchUserBeanState {

    object Success : SearchUserBeanState()

    class Fail(val errorMsg: String?) : SearchUserBeanState()
}


sealed class SearchSingleProductBeanState {

    object Success : SearchSingleProductBeanState()

    class Fail(val errorMsg: String?) : SearchSingleProductBeanState()
}
/**
 * 获取腰果热榜  状态接口类
 */
sealed class GetYgHotFeedListState {

    object Success : GetYgHotFeedListState()

    class Fail(val errorMsg: String?) : GetYgHotFeedListState()
}

/**
 * 猜你想搜  状态接口类
 */
sealed class GetGuessHotWordsState {

    object Success : GetGuessHotWordsState()

    class Fail(val errorMsg: String?) : GetGuessHotWordsState()
}


sealed class SearchFeedBeanState {

    object Success : SearchFeedBeanState()

    class Fail(val errorMsg: String?) : SearchFeedBeanState()
}

sealed class TopicBeanBeanState {

    object Success : TopicBeanBeanState()

    class Fail(val errorMsg: String?) : TopicBeanBeanState()
}

/**
 * 搜索模糊匹配  状态接口类
 */
sealed class GetSearchWordBeanState {

    object Success : GetSearchWordBeanState()

    class Fail(val errorMsg: String?) : GetSearchWordBeanState()
}


sealed class LaudCommentState {

    object Success : LaudCommentState()

    class Fail(val errorMsg: String?) : LaudCommentState()
}


sealed class UnLaudCommentState {

    object Success : UnLaudCommentState()

    class Fail(val errorMsg: String?) : UnLaudCommentState()
}

sealed class DeleteCommentState {

    object Success : DeleteCommentState()

    class Fail(val errorMsg: String?) : DeleteCommentState()
}

sealed class ConversationListState {

    data class Success(val v2TIMConversationResult: V2TIMConversationResult) :
        ConversationListState()

    class Fail(val code: Int, val errorMsg: String?) : ConversationListState()
}
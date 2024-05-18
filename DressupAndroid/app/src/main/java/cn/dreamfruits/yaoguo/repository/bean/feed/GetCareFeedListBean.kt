package cn.dreamfruits.yaoguo.repository.bean.feed

import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.util.Singleton

/**
 * 注意修改此类时  需要额外在item中添加一些变量
 */
data class GetCareFeedListBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 0
    val list: MutableList<WaterfallFeedBean.Item.Info>
)
//{
//    data class Item(
//        val address: String, // string
//        val atUser: List<Singleton.AtUser>,
//        val cityAdCode: String, // string
//        var collectCount: Int, // 0
//        val commentCount: Int, // 0
//        val commentList: List<Comment>,
//        val config: List<Singleton.Config>,
//        val content: String, // string
//        val createTime: Long, // 0
//        val id: Long, // 0
//        var isCollect: Int, // 0
//        var isLaud: Int, // 0
//        val latitude: Double, // 0
//        var laudCount: Int, // 0
//        val longitude: Double, // 0
//        val outfitId: Long, // 0
//        val picUrls: List<PicUrl>,
//        val provinceAdCode: String, // string
//        val relation: Int, // 0
//        val state: Int, // 0
//        val title: String, // string
//        val type: Int, // 0
//        val updateTime: Long, // 0
//        val userInfo: UserInfo,
//        val videoUrls: List<VideoUrls>,
//        val wearCount: Int, // 0
//        var homeRecommendUserListBean: HomeRecommendUserListBean,//添加为你推荐列表
//        var isShowForYouView:Boolean,//是否显示为你推荐布局
//        var muteFlag:Boolean//静音逻辑 默认是false  所以默认是有声音
//    ) {
//        data class Comment(
//            val commentUserInfo: CommentUserInfo,
//            val content: String, // string
//            val createTime: Long, // 0
//            val id: Long, // 0
//            val isLaud: Int, // 0
//            val laudCount: Int, // 0
//            val replyCount: Int, // 0
//            val replyUid: Any, // null
//            val replyUserInfo: Any, // null
//            val replys: Any, // null
//            val targetId: Long, // 0
//            val type: Int, // 0
//            val uid: Long // 0
//        ) {
//            data class CommentUserInfo(
//                val avatarUrl: String, // string
//                val id: Long, // 0
//                val nickName: String, // string
//                val relation: Int // 0
//            )
//        }
//        data class PicUrl(
//            val height: Int, // 0
//            var url: String, // string
//            val width: Int // 0
//        )
//
//        data class UserInfo(
//            var avatarUrl: String, // string
//            val id: Long, // 0
//            val nickName: String // string
//        )
//
//        data class VideoUrls(
//            val height: Int, // 0
//            var url: String, // string
//            val width: Int // 0
//        )
//    }
//}
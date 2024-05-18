package cn.dreamfruits.yaoguo.repository.bean.feed

import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.util.Singleton

data class WaterfallFeedBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 0
    val list: MutableList<Item>,

    //搜索动态独有
    val page: Int, // 2
    val size: Int,

    val totalCount: Int // 28
){
    data class Item(
        val info: Info,
        val type: Int // 0
    ) {
        data class Info(
            val address: String?, // null
            val atUser: MutableList<Singleton.AtUser>,
            val cityAdCode: String, // 028
            var collectCount: Int, // 0
            val commentCount: Int, // 0
            val commentList: List<Comment>, // null
            val config: MutableList<Singleton.Config>,
            val content: String,
            val createTime: Long, // 1679278652816
            val id: Long, // 465320778261072
            var isCollect: Int, // 0
            var isLaud: Int, // 0
            val latitude: Double, // 30.546203
            var laudCount: Int, // 0
            val longitude: Double, // 104.069204
            val outfitId: Any, // null
            val outfitInfo: Any, // null
            val picUrls:List<PicUrl>,
            val provinceAdCode: String, // 510000
            var relation: Int, //
            val seeLimit: Int, // 0
            val singleList: MutableList<Single>,
            val state: Int, // 1
            val title: String?=null, // 疯狂星期一
            val type: Int, // 0
            val updateTime: Long, // 1679278652816
            val userInfo: UserInfo,
            val videoUrls: List<VideoUrl>,
            val wearCount: Int, // 0

            //关注页面独有
            var homeRecommendUserListBean: HomeRecommendUserListBean,//添加为你推荐列表
            var isShowForYouView:Boolean,//是否显示为你推荐布局
            var muteFlag:Boolean//静音逻辑 默认是false  所以默认是有声音


        ) {
            data class Comment(//关注页面独有
                val commentUserInfo: CommentUserInfo,
                val content: String, // string
                val createTime: Long, // 0
                val id: Long, // 0
                val isLaud: Int, // 0
                val laudCount: Int, // 0
                val replyCount: Int, // 0
                val replyUid: Any, // null
                val replyUserInfo: Any, // null
                val replys: Any, // null
                val targetId: Long, // 0
                val type: Int, // 0
                val uid: Long // 0
            ) {
                data class CommentUserInfo(
                    val avatarUrl: String, // string
                    val id: Long, // 0
                    val nickName: String, // string
                    val relation: Int // 0
                )
            }
            data class PicUrl(
                val height: Int, // 591
                var url: String, // https://devfile.dreamfruits.cn/app/249501046271456/5cd8867acd0e950ca45bbdade01a2562
                var urlX:String,//鉴权后的字段
                val width: Int // 435
            )
            data class UserInfo(
                var avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/17c4e8e7a14e1368409c7cf8d97da40f.png
                var avatarUrlX: String, // 鉴权后的字段
                val id: Long, // 448606354679856
                val nickName: String // yg448606354679856
            )
            data class VideoUrl(
                val height: Int, // 1920
                var url: String, // https://devfile.dreamfruits.cn/app/367782168227296/3b605568b97cae28e507981bf74f0148.mp4
                var urlX: String, // https://devfile.dreamfruits.cn/app/367782168227296/3b605568b97cae28e507981bf74f0148.mp4
                val width: Int // 1080
            )

            data class Single(
                val id: Long, // 0
                val name: String, // string
                var coverUrl: String, // string

//                val appResourceUrl: String, // string
//                val cLength: Int, // 0
//                val collectCount: Int, // 0
//                val createTime: Long, // 0
//                val isCollect: Int, // 0
//                val labelsList: List<Labels>,
//                val modelList: List<Model>,
//                val picUrls: List<String>,
//                val position: Int, // 0
//                val state: Int, // 0
//                val tryOnCount: Int, // 0
//                val videoUrls: String // string
            ) {
                data class Labels(
                    val id: Long, // 0
                    val name: String, // string
                    val viewCount: Int // 0
                )
                data class Model(
                    val floor: Int, // 0
                    val id: Long, // 0
                    val resourceUrl: String, // string
                    val singleProductId: Long, // 0
                    val status: Int // 0
                )
            }
        }
    }
}
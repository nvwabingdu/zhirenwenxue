package cn.dreamfruits.yaoguo.repository.bean.comment

import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.ChildCommentListAdapter
import cn.dreamfruits.yaoguo.util.Singleton

/**
 * 有添加额外参数 删除时需要添加上
 */
data class CommentBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 1662629678933
    val list: MutableList<Item>,
    val totalCount: Int // 1
) {
    data class Item(//帖子评论发布可以用这个做bean
        val atUser: List<Singleton.AtUser>? = null,
        val commentUserInfo: CommentUserInfo? = null,
        val config: List<Singleton.Config>? = null,
        val content: String, // 牛哇牛哇
        val createTime: Long, // 1662101574713
        val id: Long, // 324606154451856
        var isLaud: Int, // 0
        var laudCount: Int, // 0
        var replyCount: Int, // 4
        val replyUid: Long? = 0, // null
        val replyUserInfo: CommentDetailBean.Reply.ReplyUserInfo? = null, // null
        var replys: MutableList<ChildCommentBean.ChildItem>? = null,//在适配器中处理加载列表
        val targetId: Long, // 323946011510144
        val type: Int, // 0-动态，1-评论  动态一级   评论2级
        val uid: Long,// 322590839375712
        //子类状态 用于请求数据啥的
        var hasNextChild: Int, // 0
        var lastTimeChild: Long, // 1662629678933
        var totalCountChild: Int, // 1
        var isChildNoMoreData: Boolean,
        var mChildCommentListAdapter: ChildCommentListAdapter? = null,
        // 发布评论独有
        val state: Int,
        var isChangeColor: Boolean = false,
    )
}

//子级 列表  子级评论发布可以用这个做bean
data class ChildCommentBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 1662629678933
    val list: MutableList<ChildItem>,
    val totalCount: Int // 1
) {
    data class ChildItem(//帖子评论发布可以用这个做bean
        val atUser: List<Singleton.AtUser>? = null,
        val commentUserInfo: CommentUserInfo? = null,
        val config: List<Singleton.Config>? = null,
        val content: String, // 哈哈哈哈哈
        val createTime: Long, // 1662109142454
        val id: Long, // 324668149385024
        var isLaud: Int, // 0
        var laudCount: Int, // 1
        val replyCount: Int, // 0
        val replyUid: Long, // 324647556115184
        val replyUserInfo: ReplyUserInfo? = null,
        val replys: List<Any>? = null,//子级评论的replys为空
        val targetId: Long, // 324606154451856
        val type: Int, // 1
        val uid: Long, // 324647556115184
    // 发布评论独有
        val state: Int,
        var isChangeColor: Boolean = false,
        )
}

data class ReplyUserInfo(
    var avatarUrl: String, // https://testfile.dreamfruits.cn/app/324647556115184/09513a0a8a0d2eb631dc84255ceebb66
    val id: Long, // 324647556115184
    val nickName: String, // yg324647556115184
    val relation: Int // 0
)

data class CommentUserInfo(
    var avatarUrl: String, // https://testfile.dreamfruits.cn/admin/10086/17c4e8e7a14e1368409c7cf8d97da40f.png
    val id: Long, // 322590839375712
    val nickName: String, // 成都梦果传媒科技有限公司
    var relation: Int // 0
)
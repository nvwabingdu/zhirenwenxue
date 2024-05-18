package cn.dreamfruits.yaoguo.repository.bean.comment

data class CommentDetailBean(
    val commentUserInfo: CommentUserInfo,
    val content: String, // 哈哈哈姐姐家
    val createTime: Long, // 1664270959335
    val id: Long, // 342377753275152
    val isLaud: Int, // 1
    val laudCount: Int, // 2
    val replyCount: Int, // 12
    val replyUid: Any, // null
    val replyUserInfo: Any, // null
    val replys: List<Reply>,
    val state: Int, // 3
    val targetId: Long, // 323779545489504
    val type: Int, // 0
    val uid: Long // 323944473208672
) {
    data class CommentUserInfo(
        val avatarUrl: String, // https://testfile.dreamfruits.cn/admin/10086/936cda026ef96e6225648cf5a3571b67.png
        val id: Long, // 323944473208672
        val nickName: String, // 郑强的爹
        val relation: Int // 0
    )

    data class Reply(
        val commentUserInfo: CommentUserInfo,
        val content: String, // 更何况机会
        val createTime: Long, // 1664273150505
        val id: Long, // 342395703337440
        val isLaud: Int, // 1
        val laudCount: Int, // 1
        val replyCount: Int, // 0
        val replyUid: Long, // 332988983232048
        val replyUserInfo: ReplyUserInfo,
        val replys: Any, // null
        val state: Int, // 1
        val targetId: Long, // 342377753275152
        val type: Int, // 1
        val uid: Long // 323944473208672
    ) {
        data class CommentUserInfo(
            val avatarUrl: String, // https://testfile.dreamfruits.cn/admin/10086/936cda026ef96e6225648cf5a3571b67.png
            val id: Long, // 323944473208672
            val nickName: String, // 郑强的爹
            val relation: Int // 0
        )

        data class ReplyUserInfo(
            var avatarUrl: String, // https://testfile.dreamfruits.cn/app/332988983232048/6ea244c1d9f6ccf9402575becbcda8b6
            val id: Long, // 332988983232048
            val nickName: String, // 穿搭达人
            val relation: Int // 0
        )
    }
}
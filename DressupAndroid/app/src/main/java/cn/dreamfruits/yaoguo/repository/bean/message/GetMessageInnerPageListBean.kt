package cn.dreamfruits.yaoguo.repository.bean.message

import com.tencent.imsdk.v2.V2TIMConversation

data class GetMessageInnerPageListBean(
        val hasNext: Int, // 1
        val lastTime: Long, // 1664266307270
        val list: MutableList<Item>
    ) {
        data class Item(
            val commentId: Long, // 329665692262960
            val content: String, // 你以为
            var coverUrl: String, // https://testfile.dreamfruits.cn/app/332988983232048/93aca638b1f033e2cdfeed63ac9d5206
            val createTime: Long, // 1665281477638
            val feedId: Long, // 339587744098176
            val id: Long, // 519
            val isDelete: Int, // 1
            val isRead: Int, // 0
            val refId: Long, // 339587744098176
            val refType: Int, // 0
            val replyId: Long?, // 342378218774992
            val state: Int, // 1
            val subType: Int, // 1
            val updateTime: Long, // 1665281477638
            val userInfo: UserInfo?,
            val feedType: Int?, // 1

            //系统消息额外添加的字段
            val title: String?, // 你的帖子违规啦！重新去发一篇更好的帖子吧！

            //评论消息独有的字段
            val commentIsDelete: Int?, // 0
            val firstCommentId: Long?, // 324606154451856
            var isLaud: Int?, // 0
            val replyContent: String?, // 牛哇牛哇
            val replyIsDelete: Int?, // 0
            val replyUserInfo: ReplyUserInfo?,
            var conversation: V2TIMConversation?,
        ) {
            data class UserInfo(
                var avatarUrl: String, // https://testfile.dreamfruits.cn/app/323258433931472/bf9b33f3c6030b11c4234aa216d45464
                val id: Long, // 323258433931472
                val nickName: String, // 家斌的爹
                var relation: Int // 3
            )
            data class ReplyUserInfo(
                var avatarUrl: String, // https://testfile.dreamfruits.cn/admin/10086/17c4e8e7a14e1368409c7cf8d97da40f.png
                val id: Long, // 322590839375712
                val nickName: String, // 成都梦果传媒科技有限公司
                val relation: Int // 3
            )
        }
    }

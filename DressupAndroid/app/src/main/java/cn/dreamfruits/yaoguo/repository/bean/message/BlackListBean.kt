package cn.dreamfruits.yaoguo.repository.bean.message

data class BlackListBean(
    var id: String,
    var nickName: String,
    var avatarUrl: String,
    var relation: Int,//0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
)
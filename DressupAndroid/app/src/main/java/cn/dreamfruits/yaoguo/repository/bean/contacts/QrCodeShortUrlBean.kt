package cn.dreamfruits.yaoguo.repository.bean.contacts

data class QrCodeShortUrlBean(
        val shortUrl: String, // https://dev.dreamfruits.cn/app/api/resolveShortUrl/sh

        //扫码结果  get请求下来的数据
        val isRedirect: Int, // 0
        val targetId: Long, // 448606354679856 type对应的id
        val type: Int, // 同获取短链接接口的入参type。0-用户，1-帖子，2-单品
        val url: String, // https://dev.dreamfruits.cn/h5/app/#/user?id=448606354679856

)
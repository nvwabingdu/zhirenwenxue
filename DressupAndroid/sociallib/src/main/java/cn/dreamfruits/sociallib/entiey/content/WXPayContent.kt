package cn.dreamfruits.sociallib.entiey.content

/**
 * description: 微信支付的实体
 */
data class WXPayContent (
    var params:String? = null,
    var appid:String,
    var out_trade_no:String? = null,
    var noncestr:String,
    var packageX:String? = "Sign=WXPay",
    var partnerid:String,
    var prepayid:String,
    var timestamp:String,
    var orderNumber:String? = null,
    var sign:String,
    var submitPayType:Int  // 提交订单type
):PayContent()
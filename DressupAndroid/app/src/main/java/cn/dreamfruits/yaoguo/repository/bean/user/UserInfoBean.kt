package cn.dreamfruits.yaoguo.repository.bean.user

/**
 * 用户实体信息
 */
data class UserInfoBean(
    val age: Long? = null,
    val avatarUrl: String = "",
    val backgroundUrl: String? = "",
    val birthday: String? = null,

    val canModifyYgId: Long = 0,
    val cityAdCode: Any? = null,
    val cityName: String? = null,
    val descript: String? = null,
    /**
     * 被收藏的帖子数
     */
    val feedCollectedCount: Long = 0,
    /**
     * 被点赞的帖子数
     */
    val feedLaudedCount: Long = 0,
    /**
     * 关注数
     */
    val followCount: Long = 0,
    /**
     * 粉丝数
     */
    val followerCount: Long = 0,

    /**
     * 是否点击过设置背景按钮，0-否，1-是
     */
    val isClickedBackground: Long? = null,
    val nickName: String = "",
    /**
     * 被收藏的穿搭方案数
     */
    val outFitCollectedCount: Long? = null,
    val provinceAdCode: Any? = null,
    /**
     * 关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
     */
    val relation: Long? = null,
    /**
     * 性别，0-女，1-男
     */
    val gender: Int? = null,
    /**
     * 被试穿数
     */
    val tryOnCount: Long? = null,
    val userId: Long? = null,
    val ygId: String? = null,

    //城市
    var city: String? = null,
    //省
    var province: String? = null,
    //国家
    var country: String? = null,

    //country":null,"province":null,"city":null

    //是否主页显示地址
//    var showLocation:Boolean= false,

    //显示生日标签
//    var showBirthdayTag:Boolean =false,

    /**
     * 生日标签
     * constellation 星座
     * age 年龄
     */
//    var birthdayTag: String ="age",

    val constellation: Any, // null
    val feedCount: Int = 0, // 1
    val phoneNumber: String, // 17323268010


    //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
    var blackRelation: Int = 0,
    var isPerfect: Int = 0,//是否完善性别，0-否，1-是
    var inviteCodePerfect: Int = 0,//是否完善邀请码，0-否，1-是


)
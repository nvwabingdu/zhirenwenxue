package cn.dreamfruits.yaoguo.repository.datasource.remote


import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.UserApi
import cn.dreamfruits.yaoguo.repository.bean.CommonStateBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean2
import cn.dreamfruits.yaoguo.repository.bean.set.GetBindRelationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetNotificationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.set.NullBean
import cn.dreamfruits.yaoguo.repository.bean.set.SetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean
import io.reactivex.rxjava3.core.Observable

/**
 * 用户远端数据源
 */
class UserRemoteDataSource : BaseRemoteDataSource(){

    private val userApi:UserApi = retrofitService(UserApi::class.java)


    /**
     * 获取用户信息
     * @param userId 用户id
     */
    fun getUserInfo(userId:Long?): Observable<UserInfoBean> {
        return userApi.getUserInfo(userId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     *更新用户信息
     */
    fun updateUserInfo(
        gender: Int?,
        nickName: String?,
        avatarUrl: String?,
        description: String?,
        ygId: String?,
        birthday: Long?,
        country: String?,
        province: String?,
        city: String?,
        inviteCode: String?
    ): Observable<UserInfoBean> {
        return userApi.updateUserInfo(
            gender, nickName, avatarUrl, description, ygId, birthday, country, province, city,inviteCode
        ).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 设置用户消息通知设置   远程仓库
     */
    fun setUserNotificationSetting(
        type: Int,//0-点赞和收藏，1-新粉丝，2-评论，3-@，4-单聊，5-关注人发动态，6-官方
        isOpen: Int,//0-关闭，1-开起
    ): Observable<NullBean> {
        return userApi.setUserNotificationSetting(type,isOpen)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取用户消息通知设置列表   远程仓库
     */
    fun getUserNotificationSettingList(
    ): Observable<GetNotificationBean> {
        return userApi.getUserNotificationSettingList()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取绑定关系   远程仓库
     */
    fun getBindRelation(
    ): Observable<GetBindRelationBean> {
        return userApi.getBindRelation()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 绑定三方   远程仓库
     */
    fun bindThirdParty(

        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
        code: String?,//微信登录的code，qq登录的token
        openId: String?,//qq的openId

    ): Observable<BindThreeAccountBean2> {
        return userApi.bindThirdParty(type,code,openId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 解除绑定三方   远程仓库
     */
    fun unbindThirdParty(

        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博

    ): Observable<BindThreeAccountBean2> {
        return userApi.unbindThirdParty(type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 意见反馈   远程仓库
     */
    fun feedback(

        content: String,//目标id
        picUrls: String?,//可选 图片数组，["a","b"]

    ): Observable<CommonStateBean> {
        return userApi.feedback(content,picUrls)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取用户个人主页展示设置   远程仓库
     */
    fun getUserHomePageSetting(



    ): Observable<GetUserHomePageSettingBean> {
        return userApi.getUserHomePageSetting()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 设置用户个人主页展示设置   远程仓库
     */
    fun setUserHomePageSetting(

        type: Int,//0:生日，1:地区
        isShow: Int,//是否显示；0-不展示，1-展示
        subType: Int?,//当type为0时，此字段必填；0:年龄，1-星座

    ): Observable<SetUserHomePageSettingBean> {
        return userApi.setUserHomePageSetting(type,isShow,subType)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取关注列表   远程仓库
     */
    fun getUserFollowList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ): Observable<SearchUserBean> {
        return userApi.getUserFollowList(size,lastTime,targetUid)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 获取粉丝列表   远程仓库
     */
    fun getUserFollowerList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ): Observable<SearchUserBean> {
        return userApi.getUserFollowerList(size,lastTime,targetUid)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 搜索关注用户   远程仓库
     */
    fun searchFollow(

        size: Int,
        keyWord: String,//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<SearchUserBean> {
        return userApi.searchFollow(size,keyWord,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取黑名单列表   远程仓库
     */
    fun getBlackList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<SearchUserBean> {
        return userApi.getBlackList(size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 操作黑名单   远程仓库
     */
    fun operateBlackList(

        operation: Int,//0-拉黑， 1-取消拉黑
        ids: String,//被拉黑用户id组成的字符串，用英文逗号隔开，例如：id1,id2,id3

    ): Observable<CommonStateBean> {
        return userApi.operateBlackList(operation,ids)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



}
package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
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
import cn.dreamfruits.yaoguo.repository.datasource.remote.UserRemoteDataSource
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.GsonUtils
import io.reactivex.rxjava3.core.Observable

/**
 * 用户的数据仓库
 */
class UserRepository(dataSource: UserRemoteDataSource) :
    BaseRemoteRepository<UserRemoteDataSource>(dataSource) {


    companion object {
        private const val KEY_CACHE_USER_BEAN = "key_cache_user_bean"

        var userInfo: UserInfoBean? = null
            set(value) {
                field = value
                field?.let { userInfo ->
                    cacheUserInfo(userInfo)
                }
            }


        /**
         * 初始化用户缓存信息
         */
        fun init() {
            val json = MMKVRepository.getCommonMMKV()
                .decodeString(KEY_CACHE_USER_BEAN, "")
            if (json!!.isNotBlank()) {
                try {
                    userInfo = GsonUtils.fromJson<UserInfoBean>(json, UserInfoBean::class.java)
                } catch (e: Exception) {
                }
            }
        }


        /**
         * 清除用户信息
         */
        fun clearUserInfo() {
            userInfo = null
            // 清除用户信息
            MMKVRepository.getCommonMMKV()
                .removeValueForKey(KEY_CACHE_USER_BEAN)
            OauthRepository.clearCache()
        }

        /**
         * 缓存用户信息
         */
        private fun cacheUserInfo(userBean: UserInfoBean) {
            val json = GsonUtils.toJson(userBean)
            MMKVRepository.getCommonMMKV()
                .encode(KEY_CACHE_USER_BEAN, json)
        }

    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(userId: Long?) : Observable<UserInfoBean> {
        return remoteDataSource.getUserInfo(userId).doOnNext {


            when (Singleton.isMine) {
                0 -> {
                    MMKVConstants.initData(MMKVConstants.GET_USER_INFO, it)
                    Singleton.isMine = 1
                }

                1 -> {
                    if (it.userId == Singleton.getUserInfo().userId) {
                        MMKVConstants.initData(MMKVConstants.GET_USER_INFO, it)
                    }
                }
            }
        }
    }


    /**
     * 更新用户信息
     */
    fun updateUserInfo(gender:Int?=null,
                       nickName: String?=null,
                       avatarUrl: String?=null,
                       description: String?=null,
                       ygId: String?=null,
                       birthday: Long?=null,
                       country: String?=null,
                       province: String?=null,
                       city: String?=null,
                               inviteCode: String?=null
    ):Observable<UserInfoBean>{
        return remoteDataSource.updateUserInfo(gender,nickName, avatarUrl, description, ygId, birthday, country, province, city,inviteCode)
    }

    /**
     * 设置用户消息通知设置  本地仓库
     */
    fun setUserNotificationSetting(
        type: Int,//0-点赞和收藏，1-新粉丝，2-评论，3-@，4-单聊，5-关注人发动态，6-官方
        isOpen: Int,//0-关闭，1-开起
    ): Observable<NullBean> {
        return remoteDataSource.setUserNotificationSetting(type,isOpen)
    }


    /**
     * 获取用户消息通知设置列表  本地仓库
     */
    fun getUserNotificationSettingList(
    ): Observable<GetNotificationBean> {
        return remoteDataSource.getUserNotificationSettingList()
    }

    /**
     * 获取绑定关系  本地仓库
     */
    fun getBindRelation(
    ): Observable<GetBindRelationBean> {
        return remoteDataSource.getBindRelation()
    }

    /**
     * 绑定三方  本地仓库
     */
    fun bindThirdParty(

        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
        code: String?,//微信登录的code，qq登录的token
        openId: String?,//qq的openId

    ): Observable<BindThreeAccountBean2> {
        return remoteDataSource.bindThirdParty(type,code,openId)
    }

    /**
     * 解除绑定三方  本地仓库
     */
    fun unbindThirdParty(

        type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博

    ): Observable<BindThreeAccountBean2> {
        return remoteDataSource.unbindThirdParty(type)
    }

    /**
     * 意见反馈  本地仓库
     */
    fun feedback(

        content: String,//目标id
        picUrls: String?,//可选 图片数组，["a","b"]

    ): Observable<CommonStateBean> {
        return remoteDataSource.feedback(content,picUrls)
    }

    /**
     * 获取用户个人主页展示设置  本地仓库
     */
    fun getUserHomePageSetting(



    ): Observable<GetUserHomePageSettingBean> {
        return remoteDataSource.getUserHomePageSetting()
    }

    /**
     * 设置用户个人主页展示设置  本地仓库
     */
    fun setUserHomePageSetting(

        type: Int,//0:生日，1:地区
        isShow: Int,//是否显示；0-不展示，1-展示
        subType: Int?,//当type为0时，此字段必填；0:年龄，1-星座

    ): Observable<SetUserHomePageSettingBean> {
        return remoteDataSource.setUserHomePageSetting(type,isShow,subType)
    }

    /**
     * 获取关注列表  本地仓库
     */
    fun getUserFollowList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ): Observable<SearchUserBean> {
        return remoteDataSource.getUserFollowList(size,lastTime,targetUid)
    }


    /**
     * 获取粉丝列表  本地仓库
     */
    fun getUserFollowerList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime
        targetUid: Long?,//目标用户id

    ): Observable<SearchUserBean> {
        return remoteDataSource.getUserFollowerList(size,lastTime,targetUid)
    }


    /**
     * 搜索关注用户  本地仓库
     */
    fun searchFollow(

        size: Int,
        keyWord: String,//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<SearchUserBean> {
        return remoteDataSource.searchFollow(size,keyWord,lastTime)
    }

    /**
     * 获取黑名单列表  本地仓库
     */
    fun getBlackList(

        size: Int,
        lastTime: Long?,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<SearchUserBean> {
        return remoteDataSource.getBlackList(size,lastTime)
    }

    /**
     * 操作黑名单  本地仓库
     */
    fun operateBlackList(

        operation: Int,//0-拉黑， 1-取消拉黑
        ids: String,//被拉黑用户id组成的字符串，用英文逗号隔开，例如：id1,id2,id3

    ): Observable<CommonStateBean> {
        return remoteDataSource.operateBlackList(operation,ids)
    }


}
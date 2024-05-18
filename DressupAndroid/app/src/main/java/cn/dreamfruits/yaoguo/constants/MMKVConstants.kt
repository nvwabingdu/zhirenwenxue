package cn.dreamfruits.yaoguo.constants

import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchScrollHotWordsBean
import com.blankj.utilcode.util.GsonUtils

/**
 * @Author qiwangi
 * @Date 2023/3/15
 * @TIME 15:20
 */
interface MMKVConstants {


    /**
     * 仅针对仓库对象缓存的数据
     */
    companion object{

         const val RECOMMEND_FEED_LIST = "recommend_feed_list"//获取推荐动态列表
         const val SEARCH_SCROLL_HOT_WORDS_BEAN = "search_scroll_hot_words_bean"//搜索滚动热搜词
         const val RECOMMEND_LABEL_LIST = "recommend_label_list"//首页tab-推荐话题
         const val LABEL_FEED_LIST = "label_feed_list"//获取话题动态列表
         const val FEED_BACK_FEED = "feed_back_feed"//动态反馈
         const val FEED_BACK = "feed_back"//动态反馈
         const val UN_INTERESTED = "un_interested"//不感兴趣
         const val FOLLOW_USER = "follow_user"//关注用户
         const val UN_FOLLOW_USER = "un_follow_user"//取消关注
         const val USER_FOLLOW_LIST = "user_follow_list"//获取关注列表
         const val USER_FOLLOW_FANS_LIST = "user_follow_fans_list"//获取粉丝列表
         const val RECOMMEND_USER_LIST = "recommend_user_list"//推荐关注
         const val AT_USER_LIST = "at_user_list"//获取at列表
         const val GET_CARE_FEED_LIST = "get_care_feed_list"//获取关注动态列表
         const val GET_TENCENT_CDN_SECRETKEY = "get_tencent_cdn_secretKey"//腾讯图片鉴权key
         const val GET_DISCOVER_USER_LIST = "get_discover_user_list"//发现用户
         const val GET_HOME_RECOMMEND_USERLIST = "get_home_recommend_userList"//感兴趣的人
         const val IGNORE_RECOMMEND_USER = "ignore_recommend_user"//忽略感兴趣的人
         const val FIND_OUTFIT = "find_outfit"//发现穿搭
         const val FIND_LABEL = "find_label"//发现话题

         const val GET_USER_INFO = "get_user_info"//用户信息

        const val UNITY_CROP_URL="unity_crop_url"//裁剪图片url

        //单条动态数据用于传递
         const val WATERFALL_FEED_BEAN_ITEM = "waterfall_feed_bean_item"//发现话题

         const val HISTORY_BEAN = "history_bean"//历史搜索数据 本地保存
         const val CONTACT_BEAN = "contact_bean"//通讯录bean









        /**
         * ---------------------------mmkv
         * 缓存且更新数据
         */
        fun initData(key:String,tempBean:Any?){
            try {
                //清除之前的缓存
                clearData(key)

                //重新缓存新数据
                tempBean?.let {
                    val json = GsonUtils.toJson(it)
                    MMKVRepository.getCommonMMKV().encode(key, json)
                }
            }catch (e:Exception){
            //Log.e("zqr--00",e.toString())
            }
        }

//        /**
//         * 取出缓存数据  暂未实现
//         */
//        fun  getCacheData(key:String, tempBean:Any): Any? {
//            //搜索滚动热搜词
//            val json = MMKVRepository.getCommonMMKV().decodeString(key, "")
//            var bean: Any? =null
//            if (json!!.isNotBlank()) {
//                try {
//                     bean = GsonUtils.fromJson(json,tempBean::class.java)//反序列化
//                } catch (e: Exception) {
//                    Log.e("zqr", "$key--:$e")
//                }
//            }
//            return bean
//        }

        /**
         * 清除缓存数据
         */
        fun clearData(key:String) {
            MMKVRepository.getCommonMMKV().removeValueForKey(key)
        }

    }
}
package cn.dreamfruits.yaoguo.constants


interface RouterConstants {


    /**
     * 绑定手机号
     */
    interface BindPhone {
        companion object {
            //绑定类型
            const val KEY_BIND_TYPE = "key_bind_type"
            //微信unionId或者qqopenId
            const val KEY_BIND_CODE = "key_bind_code"

        }
    }

    /**
     * 绑定性别
     */
    interface BindGender{
        companion object{
            //userid
           const val KEY_USER_ID = "key_user_id"

            //更新性别
           const val KEY_UPDATE_GENDER ="key_update_gender"

        }
    }


    /**
     * 三方登录
     */
    interface ThirdPartyLogin{
        companion object{
            //平台类型
            const val KEY_PLATFORM_TYPE = "key_platform_type"

            //进入类型
            const val KEY_INTO_TYPE = "key_into_type"
        }
    }


    /**
     * 发布动态
     */
    interface FeedPublish{
        companion object{
            //选择的单品
            const val KEY_CLOTHES_LIST = "key_clothes_list"
            const val KEY_SEL_MEDIA_LIST = "KEY_SEL_MEDIA_LIST"

            //单品属于什么性别
            const val KEY_SELECT_GENDER = "key_gender"

            //搜索周边
            const val REQUEST_CODE_POI_SEARCH = 100
            const val RESULT_CODE_POI_SEARCH = 101

            //已选择单品
            const val REQUEST_CODE_SELECT_CLOTHES = 111
            const val RESULT_SELECT_CLOTHES = 112

        }
    }


}
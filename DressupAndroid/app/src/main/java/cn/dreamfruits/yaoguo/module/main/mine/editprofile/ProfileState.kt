package cn.dreamfruits.yaoguo.module.main.mine.editprofile

import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean

sealed class ProfileState {

    data class Success(var data:UserInfoBean? =null) : ProfileState()

    object Fail : ProfileState()
}
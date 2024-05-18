package cn.dreamfruits.yaoguo.module.main

import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.OEMPush.pushnative.TUIOfflinePushManager

/**
 * @author Lee
 * @createTime 2023-07-11 10 GMT+8
 * @desc :
 */
class OpenActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_splash

    override fun initView() {
    }

    override fun initData() {
        TUIOfflinePushManager.getInstance().clickNotification(intent)
        finish()
    }
}
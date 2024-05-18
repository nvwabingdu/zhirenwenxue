package cn.dreamfruits.yaoguo.module.main.mine.otherusercenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.mine.MineFragment


/**
 * @Author qiwangi
 * @Date 2023/6/27
 * @TIME 12:10
 */
class UserCenterActivity : BaseActivity(){
    override fun initData() {}

    override fun layoutResId(): Int = R.layout.activity_user_center

    override fun initView() {

        val userId = intent.getLongExtra("userId",0L)
        Log.e("zqr  UserCenterActivity", "userId:$userId")
        //创建fragment
        val f = MineFragment()
        val bundle = Bundle()
        bundle.putLong("userId", userId)
        f.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.user_center_fragment_container, f).commit()
    }


}
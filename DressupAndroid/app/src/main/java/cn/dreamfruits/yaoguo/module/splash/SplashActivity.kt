package cn.dreamfruits.yaoguo.module.splash

import android.content.Intent
import android.text.TextUtils
import android.view.View
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.LoginHelper
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.module.main.message.chart.TUIConstants
import cn.dreamfruits.yaoguo.module.main.message.into.MessageInnerPageActivity
import cn.dreamfruits.yaoguo.repository.OauthRepository
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.util.SchedulersUtil
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {


    override fun layoutResId(): Int = R.layout.activity_splash

    override fun initView() {
        //隐藏虚拟按键，并且全屏
//        val decorView = window.decorView
//        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        decorView.systemUiVisibility = uiOptions

        BarUtils.transparentStatusBar(this)
    }

    @SuppressWarnings("all")
    override fun initData() {


        var ext = intent.getStringExtra(TUIConstants.TUIOfflinePush.NOTIFICATION_EXT_KEY)

        LogUtils.e(">>>> 推送点击获取数据 ext = " + ext)
//

        if (TextUtils.isEmpty(ext)) {
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(SchedulersUtil.applySchedulers())
                .subscribe {

                    LogUtils.e(">>> " + OauthRepository.isLogin())
                    LogUtils.e(">>> " + UserRepository.userInfo?.userId)

                    if (OauthRepository.isLogin() && UserRepository.userInfo?.userId != 0L) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        LoginHelper.startLogin()
                    }
                    finish()
                }
        } else {
            var jsonObject = JSONObject(ext)
//            0-点赞帖子 1-收藏帖子 2-评论帖子 3-新的关注 4-关注用户发布帖子 5-评论@ 6-帖子@
//            0、1：跳互动通知页-赞\收藏\引用 2、5、6：跳互动通知页-评论 3：跳互动通知页-新关注 4：跳动态详情页
//              >>>> 推送点击获取数据 ext = {"type":2,"targetId":2952}
//            TargetId
//            integer
//            必需
//            目标id，根据type跳转不同页面，根据id展示不同内容。
            if (OauthRepository.isLogin() && UserRepository.userInfo?.userId != 0L) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                var type = jsonObject.optInt("type")
                var targetId = jsonObject.optString("targetId")
//            0、1：跳互动通知页-赞\收藏\引用
                if (type == 0 || type == 1) {
                    val intent = Intent(this, MessageInnerPageActivity::class.java)//帖子详情页
                    intent.putExtra("title", "赞和收藏和引用")
                    intent.putExtra("tag", 0)
                    startActivity(intent)
//           2、5、6：跳互动通知页-评论
                } else if (type == 2 || type == 5 || type == 6) {
                    val intent = Intent(this, MessageInnerPageActivity::class.java)//帖子详情页
                    intent.putExtra("title", "评论和@")
                    intent.putExtra("tag", 2)
                    startActivity(intent)
//           3：跳互动通知页-新关注
                } else if (type == 3) {
                    val intent = Intent(this, MessageInnerPageActivity::class.java)//帖子详情页
                    intent.putExtra("title", "新增关注")
                    intent.putExtra("tag", 1)
                    startActivity(intent)
//           4：跳动态详情页
                } else if (type == 4) {

                }
            } else {
                LoginHelper.startLogin()
            }
            finish()
        }
    }

}
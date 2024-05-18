package cn.dreamfruits.sociallib.handler.wx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import cn.dreamfruits.sociallib.PlatformManager
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * 微信登录 activity
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var mWXHandler: WXHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Log.i("TAG123", "onCreate: ")

        initAndPerformIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initAndPerformIntent()
    }


    override fun onReq(req: BaseReq?) {
        Log.i(">>>> ", "onReq: ")
        Log.i(">>>> ", "onReq: " + req!!.transaction)
        mWXHandler.wxEventHandler.onReq(req)

        this.finish()

        overridePendingTransition(0, 0)
    }


    override fun onResp(resp: BaseResp?) {

        Log.e(">>>> ", "onResp")
        Log.e(">>>> ", "onResp = " + resp.toString())
        if (resp != null) {
            mWXHandler.wxEventHandler.onResp(resp)
        }

        this.finish()
        overridePendingTransition(0, 0)
    }


    private fun initAndPerformIntent() {
        Log.e(">>>> ", "initAndPerformIntent")
        Log.e(">>>> ", PlatformManager.currentHandler.toString())
        if (PlatformManager.currentHandler == null) {
            this.finish()
        }

        PlatformManager.currentHandler?.let {
            mWXHandler = it as WXHandler
            mWXHandler.wxAPI?.handleIntent(intent, this)
        }

    }
}
package cn.dreamfruits.yaoguo.module.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import cn.dreamfruits.yaoguo.module.main.MainActivity


import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginHelper{

    companion object : KoinComponent{

        /**
         * 打开登录页面
         */
        fun startLogin(){
            val context by inject<Context>()
            val intent = Intent(context,LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            context.startActivity(intent)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun startMain(){
            val context by inject<Context>()
            val intent = Intent(context,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            context.startActivity(intent)
        }

    }

}
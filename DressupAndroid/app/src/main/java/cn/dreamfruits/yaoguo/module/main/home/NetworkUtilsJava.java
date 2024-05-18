package cn.dreamfruits.yaoguo.module.main.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Author qiwangi
 * @Date 2023/3/8
 * @TIME 16:43
 */
public class NetworkUtilsJava {
    //注意工具类中使用静态方法
    public static boolean isNetworkConnected(Context context){
        //判断上下文是不是空的
        if (context!=null){
            //获取连接管理器
            ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态mConnectivityManager.getActiveNetworkInfo();
            NetworkInfo mNnetNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if (mNnetNetworkInfo!=null){
                //判断网络是否可用//如果可以用就是 true  不可用就是false
                return mNnetNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}

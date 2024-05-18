package cn.dreamfruits.yaoguo.module.OEMPush.interfaces;


import cn.dreamfruits.yaoguo.module.OEMPush.utils.TUIOfflinePushErrorBean;

public interface PushCallback {
    void onTokenCallback(String token);

    void onTokenErrorCallBack(TUIOfflinePushErrorBean errorBean);
}

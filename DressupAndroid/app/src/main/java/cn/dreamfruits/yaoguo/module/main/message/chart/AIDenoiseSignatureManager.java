package cn.dreamfruits.yaoguo.module.main.message.chart;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.HashMap;

public class AIDenoiseSignatureManager {
    private static final String TAG = AIDenoiseSignatureManager.class.getSimpleName();
    private String aiDenoiseSignature = "";
    private int expiredTime;

    private static class AIDenoiseSignatureManagerHolder {
        private static final AIDenoiseSignatureManager aiDenoiseSignatureManager = new AIDenoiseSignatureManager();
    }

    public static AIDenoiseSignatureManager getInstance() {
        return AIDenoiseSignatureManagerHolder.aiDenoiseSignatureManager;
    }

    public void updateSignature() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime < expiredTime) {
            return;
        }

        V2TIMManager.getInstance().callExperimentalAPI("getAIDenoiseSignature", null, new V2TIMValueCallback<Object>() {
            @Override
            public void onSuccess(Object object) {
                if (object == null) {
                    return;
                }

                if (!(object instanceof HashMap)) {
                    return;
                }

                HashMap<String, String> hashMap = (HashMap<String, String>) object;
                Object signatureObject = hashMap.get("signature");
                Object expiredTimeObject = hashMap.get("expired_time");
                if (signatureObject != null) {
                    aiDenoiseSignature = (String) signatureObject;
                }

                if (expiredTimeObject != null) {
                    String expiredTimeString = (String) expiredTimeObject;
                    expiredTime = Integer.parseInt(expiredTimeString);
                }
            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e(TAG, "getAIDenoiseSignature error, code:" + code + ", desc:" + desc);
            }
        });
    }

    public String getSignature() {
        updateSignature();
        return aiDenoiseSignature;
    }
}

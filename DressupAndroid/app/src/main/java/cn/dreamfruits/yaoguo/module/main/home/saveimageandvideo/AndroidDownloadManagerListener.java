package cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo;

public interface AndroidDownloadManagerListener {
    void onPrepare();

    void onSuccess(String path);

    void onFailed(Throwable throwable);
}

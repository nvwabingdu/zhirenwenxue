package cn.dreamfruits.yaoguo.module.publish.service


import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import cn.dreamfruits.yaoguo.global.FeedPublishGlobal
import cn.dreamfruits.yaoguo.global.FeedPublishState
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.ThridpartyRepository
import cn.dreamfruits.yaoguo.repository.bean.publish.FeedPubParamsBean
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean
import cn.dreamfruits.yaoguo.util.CosUpload
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import org.koin.android.ext.android.inject


class FeedPublishService : Service() {

    val thridpartyRepository by inject<ThridpartyRepository>()
    val feedRepository by inject<FeedRepository>()
    var tencentTmpSecretKeyBean: TencentTmpSecretKeyBean? = null

    companion object {

        @JvmStatic
        fun startService(context: Context) {
            val intent = Intent(context, FeedPublishService::class.java)
            context.startService(intent)
        }

        fun reStartService(context: Context, taskId: Long) {
            val intent = Intent(context, FeedPublishService::class.java)
            intent.putExtra("taskId", taskId)
            context.startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        val d = thridpartyRepository.getTencentTmpSecretKey()
            .subscribe({
                handleFeedPublishBean(it)
                tencentTmpSecretKeyBean = it
            }, {
                ToastUtils.showShort("获取腾讯云cos 临时密钥失败")
            })

    }

    var taskId = -1L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        taskId = intent!!.getLongExtra("taskId", -1)

        LogUtils.e(">>>>> taskId = $taskId")

        tencentTmpSecretKeyBean?.let {
            handleFeedPublishBean(it)
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun handleFeedPublishBean(tencentTmpSecretKeyBean: TencentTmpSecretKeyBean) {
        //如果正在发布就不处理
        if (FeedPublishGlobal.isReleasing) {
            return
        }
        //取最后一条数据
        var paramsBean = FeedPublishGlobal.paramsBeanList.last()
        //
        if (taskId != -1L) {
            val find = FeedPublishGlobal.paramsBeanList.find { it.taskId == taskId }
            if (find != null) {
                paramsBean = find
            }
        }

        //图文
        if (paramsBean.type == 0) {
            val filePathList = mutableListOf<String>()
            val picBeanList = paramsBean.picBean
            picBeanList?.forEach { picBean ->
                LogUtils.e(">>>> " + picBean.url)
                //图片如果已经上传过，则不用再重新上传
                if (!picBean.url.startsWith("https")) {
                    filePathList.add(picBean.url)
                }
            }
            if (filePathList.isEmpty()) {
                publish(paramsBean)
            } else
            //上传图片
                uploadFiles(
                    filePathList,
                    tencentTmpSecretKeyBean,
                    onSuccess = { resultUrlMap ->
                        picBeanList?.forEach {
                            resultUrlMap[it.url]?.let { resultUrl ->
                                it.url = resultUrl
                            }
                        }
                        publish(paramsBean)
                    },
                    onFail = {
                        FeedPublishGlobal.setValue(
                            FeedPublishState.FeedPublishFail(
                                paramsBean.taskId!!,
                                ""
                            )
                        )
                        FeedPublishGlobal.isReleasing = false
                    },
                    onProgress = { progress ->
                        FeedPublishGlobal.setValue(
                            FeedPublishState.FeedPublishProgress(
                                paramsBean.taskId!!,
                                progress
                            )
                        )
                    }
                )

            //视频
        } else {
            val filePathList = mutableListOf<String>()
            if (!paramsBean.picBean!!.first().url.startsWith("https")) {
                filePathList.add(paramsBean.picBean!!.first().url)
            }
            if (!paramsBean.videoBean!!.url.startsWith("https")) {
                filePathList.add(paramsBean.videoBean!!.url)
            }

            if (filePathList.isEmpty()) {
                publish(paramsBean)
            } else
                uploadFiles(
                    filePathList,
                    tencentTmpSecretKeyBean,
                    onSuccess = { resultUrlMap ->

                        resultUrlMap[paramsBean.picBean!!.first().url]?.let { resultUrl ->
                            paramsBean.picBean!!.first().url = resultUrl
                        }

                        resultUrlMap[paramsBean.videoBean!!.url]?.let { resultUrl ->
                            paramsBean.videoBean!!.url = resultUrl
                        }

                        publish(paramsBean)
                    },
                    onFail = {
                        FeedPublishGlobal.setValue(
                            FeedPublishState.FeedPublishFail(
                                paramsBean.taskId!!,
                                ""
                            )
                        )
                        FeedPublishGlobal.isReleasing = false
                    },
                    onProgress = { progress ->
                        FeedPublishGlobal.setValue(
                            FeedPublishState.FeedPublishProgress(
                                paramsBean.taskId!!,
                                progress
                            )
                        )
                    }
                )
        }
        FeedPublishGlobal.isReleasing = true

    }

    private fun publish(feedPubParamsBean: FeedPubParamsBean) {

        feedRepository.createFeed(
            title = feedPubParamsBean.title ?: "",
            content = feedPubParamsBean.content ?: "",
            type = feedPubParamsBean.type!!,
            picUrls = if (feedPubParamsBean.picBean.isNullOrEmpty()) "" else GsonUtils.toJson(
                feedPubParamsBean.picBean
            ),
            seeLimit = feedPubParamsBean.seeLimit!!,
            videoUrls = if (feedPubParamsBean.videoBean == null) "" else GsonUtils.toJson(
                arrayListOf(feedPubParamsBean.videoBean)
            ),
            topicIds = feedPubParamsBean.topicIds ?: "",
            atUser = feedPubParamsBean.atUser ?: "",
            provinceAdCode = if (feedPubParamsBean.provinceAdCode == null) "" else feedPubParamsBean.provinceAdCode.toString(),
            cityAdCode = if (feedPubParamsBean.cityAdCode == null) "" else feedPubParamsBean.cityAdCode.toString(),
            longitude = feedPubParamsBean.longitude,
            latitude = feedPubParamsBean.latitude,
            address = feedPubParamsBean.address ?: "",
            singleIds = feedPubParamsBean.singleIds ?: ""
        ).subscribe({
            val find =
                FeedPublishGlobal.paramsBeanList.find { it.taskId == feedPubParamsBean.taskId }
            if (find != null)
                FeedPublishGlobal.paramsBeanList.remove(find)

            FeedPublishGlobal.isReleasing = false

            tencentTmpSecretKeyBean?.let { tencentTmpSecretKey ->
                if (FeedPublishGlobal.paramsBeanList.size > 0) {
                    handleFeedPublishBean(tencentTmpSecretKey)
                }
            }

            FeedPublishGlobal.setValue(
                FeedPublishState.FeedPublishSuccess(
                    it,
                    feedPubParamsBean.taskId!!
                )
            )
        }, {
            ToastUtils.showShort("发布出现错误${it.message}")
            FeedPublishGlobal.setValue(
                if (it.message!!.contains("UnknownHostException")) {
                    FeedPublishState.FeedPublishFail(
                        feedPubParamsBean.taskId!!,
                        "网络恢复后，系统将重新上传"
                    )
                } else
                    FeedPublishState.FeedPublishFail(
                        feedPubParamsBean.taskId!!,
                        "发布出现错误${it.message}"
                    )
            )
            FeedPublishGlobal.isReleasing = false
        })

    }


    /**
     * 上传文件
     */
    fun uploadFiles(
        images: List<String>,
        tencentTmpSecretKeyBean: TencentTmpSecretKeyBean,
        onSuccess: (resultUrlMap: Map<String, String>) -> Unit,
        onProgress: (progress: Int) -> Unit,
        onFail: () -> Unit,
    ) {
        val cosUpload = CosUpload()
        cosUpload.initService(tencentTmpSecretKeyBean, applicationContext)

        val resultUrlMap = mutableMapOf<String, String>()

        images.forEach{
            Log.e("zqr", "uploadFiles: " + it)
        }

        cosUpload.uploadFiles(images,
            onSuccess = { filePath, resultUrl ->
                resultUrlMap[filePath] = resultUrl!!
            },
            onFail = onFail,
            onProgress = onProgress,
            onComplete = {
                onSuccess.invoke(resultUrlMap)
            }
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG11", "onDestroy: ")
    }

}
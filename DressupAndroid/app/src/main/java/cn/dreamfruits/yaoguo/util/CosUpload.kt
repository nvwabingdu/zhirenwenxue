package cn.dreamfruits.yaoguo.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.UriUtils
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.cos.xml.transfer.TransferState
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import com.tencent.qcloud.core.auth.SessionQCloudCredentials
import java.io.File

class CosUpload {

    private var context: Context? = null
    private var consXmlService: CosXmlService? = null

    // private var transferManager: TransferManager? = null
    private var tencentTmpSecretKeyBean: TencentTmpSecretKeyBean? = null


    private class ServerCredentialProvider(
        val tmpSecretId: String,
        val tmpSecretKey: String,
        val sessionToken: String,
        val startTime: Long,
        val expiredTime: Long,
    ) : BasicLifecycleCredentialProvider() {

        override fun fetchNewCredentials(): QCloudLifecycleCredentials {

            return SessionQCloudCredentials(
                tmpSecretId,
                tmpSecretKey,
                sessionToken,
                startTime,
                expiredTime
            )
        }
    }



    fun initService(
        tmpSecretKeyBean: TencentTmpSecretKeyBean,
        context: Context,
    ) {
        val cosXmlServiceConfig = CosXmlServiceConfig.Builder()
            .setRegion(tmpSecretKeyBean.region)
            .isHttps(true)
            .builder()
        this.context = context
        val serverCredentialProvider = ServerCredentialProvider(
            tmpSecretKeyBean.tmpSecretId,
            tmpSecretKeyBean.tmpSecretKey,
            tmpSecretKeyBean.sessionToken,
            tmpSecretKeyBean.startTime,
            tmpSecretKeyBean.expireTime
        )
        this.tencentTmpSecretKeyBean = tmpSecretKeyBean

        consXmlService = CosXmlService(context, cosXmlServiceConfig, serverCredentialProvider)
    }


    /**
     * 上传多个文件
     * @param success 上传成功
     * @param fail 上传失败
     * @param progress 上传进度
     */
    fun uploadFiles(
        paths: List<String>,
        onSuccess: ((filePath: String, resultUrl: String?) -> Unit)? = null,
        onFail: (() -> Unit)? = null,
        onProgress: ((progress: Int) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
    ) {
        val transferConfig = TransferConfig.Builder().build()
        val transferManager = TransferManager(consXmlService, transferConfig)
        val uploadId = null
        var completeCount = 0

        //总大小
        var totalSize = 0L
        //上一个文件完成的大小
        var preCompleteSize = 0L
        //总共完成的大小
        var totalCompleteSize = 0.0

        for (path in paths) {
            val srcPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                UriUtils.uri2File(Uri.parse(path)).absolutePath
            } else {
                path
            }
            totalSize += File(srcPath).length()
        }

        for (path in paths) {
            val srcPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                UriUtils.uri2File(Uri.parse(path)).absolutePath
            } else {
                path
            }

            val suffix = srcPath.substring(srcPath.lastIndexOf("."))
            val file = File(srcPath)
            val fileName = "${
                EncryptUtils.encryptMD5ToString(
                    file.name,
                    System.currentTimeMillis().toString()
                )
            }$suffix"

            val cosPath = "${tencentTmpSecretKeyBean!!.allowKey}$fileName"

            val cosXmlUploadTask =
                transferManager.upload(
                    tencentTmpSecretKeyBean!!.bucket,
                    cosPath,
                    srcPath,
                    uploadId
                )
            //设置上传进度监听
            cosXmlUploadTask.setCosXmlProgressListener { complete, target ->
                totalCompleteSize += (complete - preCompleteSize)

                onProgress?.invoke(((totalCompleteSize / totalSize) * 100).toInt())

                preCompleteSize = complete
                if (complete >= target) {
                    preCompleteSize = 0
                }
            }
            //设置上传结果监听
            cosXmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {

                override fun onSuccess(p0: CosXmlRequest?, result: CosXmlResult?) {

                    onSuccess?.invoke(path, "${tencentTmpSecretKeyBean?.cdnDomain}${cosPath}")
                }

                override fun onFail(
                    p0: CosXmlRequest?,
                    p1: CosXmlClientException?,
                    p2: CosXmlServiceException?,
                ) {
                    onFail?.invoke()
                    Log.i("TAG11", "onFail-:${p1?.message}  ${p2?.message}")
                }
            })

            cosXmlUploadTask.setTransferStateListener { state ->
                if (state == TransferState.COMPLETED || state == TransferState.FAILED) {
                    completeCount += 1
                    //
                    if (completeCount == paths.size) {
                        onComplete?.invoke()
                    }
                }
            }
        }
    }


    /**
     * 上传单个文件
     */
    fun uploadFile(
        path: String,
        onSuccess: ((filePath: String, resultUrl: String?) -> Unit)? = null,
        onProgress: ((Int) -> Unit)? = null,
        onFail: (() -> Unit)? = null,
    ) {
        val transferConfig = TransferConfig.Builder().build()
        val transferManager = TransferManager(consXmlService, transferConfig)

        val file = UriUtils.uri2File(Uri.parse(path))
        val cosPath = "${tencentTmpSecretKeyBean!!.allowKey}/${file.name}"
        val uploadId = "100"

        val cosXmlUploadTask = transferManager.upload(
            tencentTmpSecretKeyBean!!.bucket,
            cosPath,
            Uri.parse(path),
            uploadId
        )

        cosXmlUploadTask.setCosXmlProgressListener { complete, target ->
            onProgress?.invoke(((complete / target) * 100).toInt())
        }

        cosXmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(p0: CosXmlRequest?, p1: CosXmlResult?) {
                onSuccess?.invoke(path, p1!!.accessUrl)
            }

            override fun onFail(
                p0: CosXmlRequest?,
                p1: CosXmlClientException?,
                p2: CosXmlServiceException?,
            ) {
                onFail?.invoke()
            }
        })

    }


}
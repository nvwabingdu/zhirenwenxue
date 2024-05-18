package cn.dreamfruits.yaoguo.global

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import cn.dreamfruits.yaoguo.module.publish.service.FeedPublishService
import cn.dreamfruits.yaoguo.repository.bean.feed.FeedDetailsBean
import cn.dreamfruits.yaoguo.repository.bean.publish.FeedPubParamsBean
import cn.dreamfruits.yaoguo.repository.bean.publish.FeedPublishStateBean
import cn.dreamfruits.yaoguo.util.LiveDataBus
import com.blankj.utilcode.util.LogUtils

/**
 * 发布帖子的全局
 */
object FeedPublishGlobal {

    var paramsBeanList: ArrayList<FeedPubParamsBean> = arrayListOf()
        private set

    private const val LIVEDATA_KEY_DYN_PUB = "livedata_key_feed_publish"

    //是否正在发布中
    var isReleasing = false

    /**
     * 发布帖子
     */
    fun addObserve(
        lifecycleOwner: LifecycleOwner,
        context: Context,
        onStart: (FeedPublishStateBean) -> Unit,
        onSuccess: (taskId: Long) -> Unit,
        onProgress: (taskId: Long, Progress: Int) -> Unit,
        onFail: (taskId: Long, msg: String) -> Unit,
        onCancel: (taskId: Long) -> Unit,
        onTryAgain: (taskId: Long) -> Unit,
        onTryAll: (taskId: Long) -> Unit,
    ) {
        LiveDataBus.get()
            .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
            .observe(lifecycleOwner) { state ->
                when (state) {
                    //发布帖子开始
                    is FeedPublishState.FeedPublishStart -> {
                        paramsBeanList.add(state.feedPubParamsBean)

                        FeedPublishService.startService(context)

                        onStart.invoke(
                            FeedPublishStateBean(
                                state.feedPubParamsBean.taskId!!,
                                state.feedPubParamsBean.picBean!!.last().url,
                            )
                        )
                    }
                    //上传进度
                    is FeedPublishState.FeedPublishProgress -> {
                        onProgress.invoke(
                            state.taskId,
                            if (state.progress >= 100) 100 else state.progress
                        )
                    }
//                    content://media/external/images/media/1573
                    //发布帖子成功
                    is FeedPublishState.FeedPublishSuccess -> {
                        LogUtils.e(">>>> onSuccess = " + state.taskId)
                        onSuccess.invoke(state.taskId)
                    }

                    //发布帖子失败
                    is FeedPublishState.FeedPublishFail -> {
                        LogUtils.e(">>>> onFail = " + state.taskId)
                        onFail.invoke(state.taskId, state.msg)
                    }

                    is FeedPublishState.FeedPublishCancel -> {
                        val find = paramsBeanList.find { state.taskId == it.taskId }
                        if (find != null) {
                            paramsBeanList.remove(find)
                            onCancel.invoke(state.taskId)
                        }
                    }
                    is FeedPublishState.FeedPublishTryAgain -> {

                        val find = paramsBeanList.find { it.taskId == state.taskId }

                        FeedPublishService.reStartService(context, state.taskId)

                        onTryAgain.invoke(
                            find!!.taskId!!
                        )
                    }

                    is FeedPublishState.FeedPublishTryAll -> {
                        var paramsBean = FeedPublishGlobal.paramsBeanList.last()
                        onTryAll.invoke(paramsBean.taskId!!)
                        FeedPublishService.startService(context)
                    }
                }
            }
    }

    /**
     * 发布帖子
     */
    fun feedPublish(
        paramsBean: FeedPubParamsBean,
    ) {
        LiveDataBus.get()
            .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
            .value = FeedPublishState.FeedPublishStart(paramsBean)
    }


    /**
     * 取消发布帖子
     */
    fun cancelPublish(
        taskId: Long,
    ) {
        LiveDataBus.get()
            .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
            .value = FeedPublishState.FeedPublishCancel(taskId)
    }

    /**
     * 重新发布帖子
     */
    fun feedPublishTryAgain(
        taskId: Long,
    ) {
        LiveDataBus.get()
            .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
            .value = FeedPublishState.FeedPublishTryAgain(taskId)
    }

    fun feedPublishTryAll(

    ) {
        if (paramsBeanList.isNotEmpty())
            LiveDataBus.get()
                .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
                .value = FeedPublishState.FeedPublishTryAll()
    }


    fun setValue(state: FeedPublishState) {
        LiveDataBus.get()
            .with(LIVEDATA_KEY_DYN_PUB, FeedPublishState::class.java)
            .postValue(state)
    }

}


sealed class FeedPublishState {

    data class FeedPublishStart(val feedPubParamsBean: FeedPubParamsBean) : FeedPublishState()

    data class FeedPublishSuccess(val feedDetailsBean: FeedDetailsBean, val taskId: Long) :
        FeedPublishState()

    class FeedPublishProgress(val taskId: Long, val progress: Int) : FeedPublishState()

    class FeedPublishFail(val taskId: Long, val msg: String) : FeedPublishState()
    class FeedPublishCancel(val taskId: Long) : FeedPublishState()
    class FeedPublishTryAgain(val taskId: Long) : FeedPublishState()
    class FeedPublishTryAll() : FeedPublishState()
}
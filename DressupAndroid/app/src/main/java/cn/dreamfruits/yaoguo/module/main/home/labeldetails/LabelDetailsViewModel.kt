package cn.dreamfruits.yaoguo.module.main.home.labeldetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.LabelDetailsBeanState
import cn.dreamfruits.yaoguo.repository.LabelRepository
import cn.dreamfruits.yaoguo.repository.bean.label.LabelDetailsBean
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/5/19
 * @TIME 17:44
 */
class LabelDetailsViewModel : BaseViewModel(){
    private val labelRepository by inject<LabelRepository>()

    /**
     * 话题详情
     */
    var mLabelDetailsBean: LabelDetailsBean? = null
    private val _labelDetailsBeanState = MutableLiveData<LabelDetailsBeanState>()
    val labelDetailsBeanState: MutableLiveData<LabelDetailsBeanState> get() = _labelDetailsBeanState
    fun getLabelDetail( id: Long?) {
        val disposable = labelRepository.getLabelDetail(id)
            .subscribe({
                mLabelDetailsBean= it

                _labelDetailsBeanState.value = LabelDetailsBeanState.Success
                Log.e("zqr", "_labelDetailsBeanState请求成功：$it")
            }, {

                _labelDetailsBeanState.value = LabelDetailsBeanState.Fail(it.message)
                Log.e("zqr", "_labelDetailsBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

}
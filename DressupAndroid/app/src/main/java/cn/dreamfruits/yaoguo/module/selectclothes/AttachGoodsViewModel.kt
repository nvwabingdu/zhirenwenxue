package cn.dreamfruits.yaoguo.module.selectclothes


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.home.state.HomeGetCareFeedListState
import cn.dreamfruits.yaoguo.module.main.home.state.StyleVersionListByTypeBeanState
import cn.dreamfruits.yaoguo.module.selectclothes.fragment.DataCallBack
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesCategoryState
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesItemState
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesState
import cn.dreamfruits.yaoguo.repository.ClothesRepository
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesRootCategoryBean
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

class AttachGoodsViewModel(var call: DataCallBack) : BaseViewModel() {

    private val clothesRepository by inject<ClothesRepository>()

    private val _clothesItem = MutableLiveData<ClothesItemState>()
    val clothesItem: LiveData<ClothesItemState> get() = _clothesItem

    /**
     * 点击版型
     */
    private val _clothesCategoryId = MutableLiveData<Long>()
    val clothesCategoryId: LiveData<Long> get() = _clothesCategoryId

    //上一次获取获取我的服装列表的时间
    var mClothesItemLastTime: Long = 0

    /**
     *
     */
    fun getMyClothesItem(type: Int, size: Int = 9) {
        val disposable = clothesRepository.getMyClothesItem(size, type, mClothesItemLastTime)
            .subscribe({
                call.getdata(it)
            }, {
                call.fail(it.message)
            })
        addDisposable(disposable)
    }
}



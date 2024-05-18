package cn.dreamfruits.yaoguo.module.share


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
import cn.dreamfruits.yaoguo.module.selectclothes.state.SearchClothesItemState
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.module.share.state.ShareUserListState
import cn.dreamfruits.yaoguo.repository.ClothesRepository
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesRootCategoryBean
import cn.dreamfruits.yaoguo.repository.datasource.ShareRepository
import cn.dreamfruits.yaoguo.util.Tool
import com.blankj.utilcode.util.LogUtils
import org.koin.core.component.inject

class ShareViewModel() : BaseViewModel() {

    private val shareRepository by inject<ShareRepository>()
    private val _shareUserList = MutableLiveData<ShareUserListState>()
    val shareUserList: LiveData<ShareUserListState> get() = _shareUserList

    private val _recomendUserList = MutableLiveData<ShareUseRecomendListState>()
    val recomendUserList: LiveData<ShareUseRecomendListState> get() = _recomendUserList

    private val _shortUrl = MutableLiveData<ShareShortUrlState>()
    val shortUrl: LiveData<ShareShortUrlState> get() = _shortUrl

    fun getShareUserList() {
        val disposable = shareRepository.getShareUserList()
            .subscribe({
                _shareUserList.value = ShareUserListState.Success(it)
            }, {
                _shareUserList.value = ShareUserListState.Fail(it.message)
            })

        addDisposable(disposable)
    }

    fun getRecommendShareUserList() {
        val disposable = shareRepository.getRecommendShareUserList()
            .subscribe({
                _recomendUserList.value = ShareUseRecomendListState.Success(it.list)
            }, {
                _recomendUserList.value = ShareUseRecomendListState.Fail(it.message)
            })

        addDisposable(disposable)
    }

    fun getShortUrl(targetId: String, type: Int, shareType: Int) {
        val disposable = shareRepository.getShortUrl(targetId, type)
            .subscribe({
                _shortUrl.value = ShareShortUrlState.Success(it.shortUrl, shareType)
            }, {
                _shortUrl.value = ShareShortUrlState.Fail(it.message)
            })

        addDisposable(disposable)
    }

}

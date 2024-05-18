package cn.dreamfruits.yaoguo.base

import android.content.Context
import androidx.lifecycle.ViewModel
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.lxj.xpopup.XPopup
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.core.component.KoinComponent

open class BaseViewModel() : ViewModel(), KoinComponent {

    private var mCompositeDisposable: CompositeDisposable? = null


    protected fun addDisposable(disposable: Disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = CompositeDisposable()
        }
        this.mCompositeDisposable?.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable?.let { compositeDisposable ->

            if (!compositeDisposable.isDisposed) {
                compositeDisposable.clear()
            }
        }
    }

    var loadingPop: LoadingPop? = null
    fun showLoading(
        mContext: Context,
        title: String = "加载中...",
        style: LoadingPop.Style = LoadingPop.Style.Spinner,
    ) {
        loadingPop = LoadingPop(mContext).setTitle(title)
            .setStyle(style)

        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true)
            .asCustom(
                loadingPop
            )
            .show()
    }

    fun hideLoading() {
        if (loadingPop != null) {
            loadingPop!!.dismiss()
        }
    }

}
package cn.dreamfruits.yaoguo.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.MaybeTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *description: Rx线程切换工具类.
 */
object SchedulersUtil {

  fun <T> applySchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
      observable.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
    return FlowableTransformer { flowable ->
      flowable.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
    return SingleTransformer { flowable ->
      flowable.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
    return MaybeTransformer { flowable ->
      flowable.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  fun <T> observeSchedulersIO(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
      observable.observeOn(Schedulers.io())
    }
  }

  fun <T> observeSchedulersMain(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
      observable.observeOn(AndroidSchedulers.mainThread())
    }
  }
}
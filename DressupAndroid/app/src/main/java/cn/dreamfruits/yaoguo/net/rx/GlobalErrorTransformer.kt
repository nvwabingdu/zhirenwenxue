package cn.dreamfruits.yaoguo.net.rx


import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.net.rx.retry.RetryConfig
import cn.dreamfruits.yaoguo.net.rx.retry.RetryWithFunction
import cn.dreamfruits.yaoguo.util.SchedulersUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*

/**
 * 网络请求的全局拦截及异常处理.
 */
class GlobalErrorTransformer<T>(
    private val onNextInterceptor: (BaseResult<T>) -> Observable<T> = { Observable.just(it.data) },
    private val onErrorResumeNext: (Throwable) -> Observable<T> = { Observable.error(it) },
    private val retryConfigProvider: (Throwable) -> RetryConfig = { RetryConfig() },
    private val upStreamSchedulerProvider: () -> Scheduler = { AndroidSchedulers.mainThread() },
    private val downStreamSchedulerProvider: () -> Scheduler = { AndroidSchedulers.mainThread() }
) : ObservableTransformer<BaseResult<T>, T> {

    override fun apply(upstream: Observable<BaseResult<T>>) =
        upstream
            .flatMap { onNextInterceptor.invoke(it) }
            .onErrorResumeNext { throwable -> onErrorResumeNext.invoke(throwable) }
            .observeOn(upStreamSchedulerProvider.invoke())
            .retryWhen(RetryWithFunction(retryConfigProvider))
            .observeOn(downStreamSchedulerProvider.invoke())
            .compose(SchedulersUtil.applySchedulers())
}
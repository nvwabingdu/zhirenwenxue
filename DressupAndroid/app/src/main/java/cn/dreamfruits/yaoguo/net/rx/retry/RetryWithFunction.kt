package cn.dreamfruits.yaoguo.net.rx.retry


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit

/**
 * Observable的接口请求重连.
 */
class RetryWithFunction(
    val retryConfigProvider: (Throwable) -> RetryConfig

) : Function<Observable<Throwable>, ObservableSource<*>> {

    private var retryCount = 0


    override fun apply(t: Observable<Throwable>): ObservableSource<*> =
        t.flatMap {error ->
            val (maxRetries, delay, retryCondition) = retryConfigProvider(error)

            if (++retryCount <= maxRetries ){
               retryCondition().flatMapObservable { retry->
                   if (retry)
                       Observable.timer(delay.toLong(), TimeUnit.MILLISECONDS)
                   else
                       Observable.error(error)
               }
            }else
                Observable.error(error)
        }
}
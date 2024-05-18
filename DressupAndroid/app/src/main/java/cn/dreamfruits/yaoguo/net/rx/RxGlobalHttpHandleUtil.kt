package cn.dreamfruits.yaoguo.net.rx


import cn.dreamfruits.yaoguo.net.HttpResultCode
import cn.dreamfruits.yaoguo.net.exception.ApiException
import cn.dreamfruits.yaoguo.net.exception.LoginException
import cn.dreamfruits.yaoguo.net.exception.TokenExpiredException
import cn.dreamfruits.yaoguo.net.rx.retry.RetryConfig
import cn.dreamfruits.yaoguo.module.login.LoginHelper
import cn.dreamfruits.yaoguo.repository.OauthRepository
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.HttpException
import java.net.ConnectException
import java.util.concurrent.atomic.AtomicBoolean

//网络请求全局拦截及异常处理工具类.
object RxGlobalHttpHandleUtil : KoinComponent {

    private val tokenExpiredRetryConfig = RetryConfig { refreshToken() }

    private var refreshTokenPublishSubject = PublishSubject.create<Boolean>()
    private var isRefreshingToken = AtomicBoolean(false)


    /**
     * 刷新token
     */
    private fun refreshToken(): Single<Boolean> {
        if (isRefreshingToken.compareAndSet(false, true)) {
            val oauthRepository = get<OauthRepository>()

            oauthRepository.refreshToken()
                .map {
                    true
                }
                .doOnNext {
                    isRefreshingToken.set(false)
                }
                .doOnError {
                    isRefreshingToken.set(false)
                }
                .subscribe(refreshTokenPublishSubject)

        }
        return refreshTokenPublishSubject.single(false)
    }


    fun <T> globalHttpHandle(): GlobalErrorTransformer<T> {

        return GlobalErrorTransformer(
            onNextInterceptor = {

                when (it.code) {

                    HttpResultCode.SUCCESS -> Observable.just(it.data)

                    //HttpResultCode.TOKEN_EXPIRED -> Observable.error(TokenExpiredException())

                    HttpResultCode.NEED_LOGIN -> Observable.error(LoginException(it.code, it.msg))

                    else -> Observable.error(ApiException(it.code, it.msg))
                }
            },
            onErrorResumeNext = {
                when (it) {
                    is LoginException -> {
                        OauthRepository.clearCache()
                        LoginHelper.startLogin()
                        Observable.error(it)
                    }

                    is HttpException -> {
                        if (it.code() == HttpResultCode.TOKEN_EXPIRED) {
                            OauthRepository.clearCache()
                            LoginHelper.startLogin()
                            Observable.error(TokenExpiredException())
                        } else {
                            Observable.error(it)
                        }
                    }

                    else -> Observable.error(it)
                }

            },
            retryConfigProvider = {
                when (it) {
                    is ConnectException -> {
                        if (NetworkUtils.isConnected()) {
                            RetryConfig(maxRetries = 3)
                        } else {
                            RetryConfig()
                        }
                    }

                    is TokenExpiredException -> tokenExpiredRetryConfig

                    else -> RetryConfig()
                }
            }
        )
    }


}
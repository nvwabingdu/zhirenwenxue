package cn.dreamfruits.baselib.di


import cn.dreamfruits.baselib.network.GlobalHttpHandler
import cn.dreamfruits.baselib.network.RequestIntercept
import cn.dreamfruits.baselib.network.ssl.SSLManager
import com.blankj.utilcode.util.LogUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val KODEIN_MODULE_CLIENT_TAG = "koin_module_client_tag"

class ClientModule {

    companion object {

        var httpDebug: Boolean = true
        private const val TIME_OUT = 30L

        val clientModule = module {
            named(KODEIN_MODULE_APP_TAG)

            single<OkHttpClient.Builder> {
                provideClientBuilder()
            }

            single<Retrofit.Builder> {
                provideRetrofitBuilder()
            }

            single { RequestIntercept(get()) } bind Interceptor::class


            single<OkHttpClient> {
                provideClient(get(), getOrNull(), get(), get())
            }

            single<Retrofit> {
                provideRetrofit(get(), get(), get())
            }

        }


        private fun provideClientBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }

        private fun provideRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
        }

        private fun provideClient(
            builder: OkHttpClient.Builder,
            interceptors: MutableList<Interceptor>?,
            interceptor: Interceptor,
            handler: GlobalHttpHandler?,
        ): OkHttpClient {

            builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)

            handler?.let {
                builder.addNetworkInterceptor(interceptor)

                builder.addInterceptor { chain ->
                    chain.proceed(it.onHttpRequestBefore(chain, chain.request()))
                }
            }

            interceptors?.let {
                it.forEach { interceptor ->
                    builder.addInterceptor(interceptor)
                }
            }

//            if (httpDebug) {
//                // log日志拦截
//                val loggingInterceptor = HttpLoggingInterceptor { message: String? ->
//                    LogUtils.json(
//                        LogUtils.I,
//                        "json   ",
//                        message
//                    )
//                }
//                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//                builder.addInterceptor(
//                    loggingInterceptor
//                )
//
//                builder.sslSocketFactory(
//                    SSLManager.createSSLSocketFactory(),
//                    SSLManager.createX509TrustManager()
//                )
//                builder.hostnameVerifier { _, _ -> true }
//            }

            if (httpDebug) {
                builder.addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )//log

                builder.sslSocketFactory(
                    SSLManager.createSSLSocketFactory(),
                    SSLManager.createX509TrustManager()
                )
                builder.hostnameVerifier { _, _ -> true }
            }

            return builder.build()
        }

        private fun provideRetrofit(
            builder: Retrofit.Builder,
            httpUrl: HttpUrl,
            client: OkHttpClient,
        ): Retrofit {
            return builder
                .client(client)
                .baseUrl(httpUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
    }
}
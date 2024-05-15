package com.example.zrtool.net.retrofit

import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author qiwangi
 * @Date 2023/8/1
 * @TIME 22:12
 */
class MyRetrofit : AppCompatActivity() {


    /**retrofit请求 第一步：创建实体类*/
    class App(val id: String, val name: String, val version: String)


    /**retrofit请求 第二步：创建接口*/
    interface AppService {
        /**==========GET===============*/
        //GET http://example.com/get_data.json
        @GET("get_data.json")
        fun getAppData(): Call<List<App>>

        //GET http://example.com/<page>/get_data.json
        @GET("{page}/get_data.json")
        fun getAppData2(@Path("page") page: Int): Call<List<App>>

        //GET http://example.com/get_data.json?u=<user>&t=<token>
        @GET("get_data.json")
        fun getAppData3(@Query("user") user: String, @Query("token") token: String): Call<List<App>>

        //GET http://example.com/get_data.json
        //User-Agent: okhttp
        //Cache-Control: max-age=0
        @Headers("User-Agent: okhttp", "Cache-Control: max-age=0")//添加静态请求头
        @GET("get_data.json")
        fun getAppData4(): Call<List<App>>

        //接上面添加动态请求头
        @GET("get_data.json")
        fun getAppData5(
            @Header("User-Agent") userAgent: String,
            @Header("Cache-Control") cacheControl: String
        ): Call<List<App>>


        /**==========POST===============*/
        //POST http://example.com/data/create{"id":1,"content":"this description for this data"}
        @POST("data/create")
        fun createData(@Body data: ContactsContract.Contacts.Data): Call<ResponseBody>//返回值为ResponseBody 无需解析 且不用关心返回


        /**==========DELETE===============*/
        //DELETE http://example.com/data/<id>
        @DELETE("data/{id}")
        fun deleteData(@Path("id") id: String): Call<ResponseBody>//返回值为ResponseBody 无需解析 且不用关心返回
    }


    /**retrofit请求 第三步：封装 创建retrofit实例 统一baseurl*/
    object SerViceCreator {
        //baseUrl
        private const val BASE_URL = "https://www.baidu.com"

        //retrofit实例
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //创建retrofit实例
        fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

        //泛型实化
        inline fun <reified T> create(): T = create(T::class.java)
    }

    /**retrofit请求 第四步：使用测试*/
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        /**方式1：普通使用*/
        //创建retrofit实例
        /*val retrofit = Retrofit.Builder()
            .baseUrl("https://www.baidu.com")//baseUrl
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //创建接口实例
        val appService = retrofit.create(AppService::class.java)

        //调用接口方法
        appService.getAppData().enqueue(object : Callback<List<App>> {
            override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
                val list = response.body()
            }
            override fun onFailure(call: Call<List<App>>, t: Throwable) {
                t.printStackTrace()
            }
        })*/

        /**方式2：通过泛型实化简化接口使用 比如AppService这个接口可替换为其他的接口 而不用修改SerViceCreator中的代码*/
        //创建接口实例
        val appService2 = SerViceCreator.create<AppService>()

        //调用接口方法
        appService2.getAppData().enqueue(object : Callback<List<App>> {
            override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
                val list = response.body()
            }

            override fun onFailure(call: Call<List<App>>, t: Throwable) {
                t.printStackTrace()
            }
        })




        /**方式3：通过协程简化接口使用  示例如下*/

    }

    /**
     * await()是一个挂起函数，然后给他声明了一个泛型T，并将await()函数定义成了Call<T>的扩展函数。这样所有返回值是Call类型的Retrofit接口就都可以直接调用await()函数了。
     * 接着，await()函数中使用了suspendCoroutine函数来挂起当前协程，并且在其block中调用了Call.enqueue()方法来发起网络请求。
     */
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body!=null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    /**
     * 简化之后调用如下
     */
    suspend fun getAppData(){
        val appService = SerViceCreator.create<AppService>()
        try {
            val appData = appService.getAppData().await()
            //对服务器响应的数据进行处理
        }catch (e:Exception){
            e.printStackTrace()
            //对异常情况进行处理
        }
    }

}
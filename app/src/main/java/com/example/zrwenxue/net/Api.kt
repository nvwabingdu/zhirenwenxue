package com.example.zrwenxue.net

import android.provider.ContactsContract
import com.example.zrtool.net.retrofit.MyRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author qiwangi
 * @Date 2023/8/5
 * @TIME 21:35
 */
interface ApiBaseService{

}

interface ApiHomeService {

    /**==========GET===============*/
    //GET http://example.com/get_data.json
    @GET("get_data.json")
    fun getAppData(): Call<List<MyRetrofit.App>>

    //GET http://example.com/<page>/get_data.json
    @GET("{page}/get_data.json")
    fun getAppData2(@Path("page") page: Int): Call<List<MyRetrofit.App>>

    //GET http://example.com/get_data.json?u=<user>&t=<token>
    @GET("get_data.json")
    fun getAppData3(@Query("user") user: String, @Query("token") token: String): Call<List<MyRetrofit.App>>

    //GET http://example.com/get_data.json
    //User-Agent: okhttp
    //Cache-Control: max-age=0
    @Headers("User-Agent: okhttp", "Cache-Control: max-age=0")//添加静态请求头
    @GET("get_data.json")
    fun getAppData4(): Call<List<MyRetrofit.App>>

    //接上面添加动态请求头
    @GET("get_data.json")
    fun getAppData5(
        @Header("User-Agent") userAgent: String,
        @Header("Cache-Control") cacheControl: String
    ): Call<List<MyRetrofit.App>>


    /**==========POST===============*/
    //POST http://example.com/data/create{"id":1,"content":"this description for this data"}
    @POST("data/create")
    fun createData(@Body data: ContactsContract.Contacts.Data): Call<ResponseBody>//返回值为ResponseBody 无需解析 且不用关心返回


    /**==========DELETE===============*/
    //DELETE http://example.com/data/<id>
    @DELETE("data/{id}")
    fun deleteData(@Path("id") id: String): Call<ResponseBody>//返回值为ResponseBody 无需解析 且不用关心返回
}

interface ApiLoginService{

}
package com.example.zrtool.oldnet.zqrretrofit;

import com.example.zrtool.oldnet.beans.MainNewsBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-01
 * Time: 23:18
 */
public class MyRetrofit {

    /**
     * GET请求方式
     */
    public interface IpService {
        //headers为消息报头处理，此处为静态
        @Headers({
                "Accept-Encoding: application/json",
                "User-Agent: zqr"
        })
        @GET("getIpInfo.php?ip=59.108.54.37")
        Call<MainNewsBean> getIpMsg();
    }


    /**
     * retrofit的GET请求接口
     */
    public interface RetrofitGETInterface{
        @GET("index")
        Call<MainNewsBean> getRequestBody(@Query("type") String type, @Query("key") String key);
    }


//    //通过接口去规定请求方式和请求后的数据格式
//    //--》GET请求（注解）
//    //index?type=top&key=APPKEY
//    public interface Jiekou{
//        @GET("index")
//        Call<F_class> getBean(@Query("type") String type, @Query("key") String key);
//    }

    //动态配置添加消息报头
//    interface Someservice{
//        @GET("some/endpoint")
//        Call<ResponseBody> getCarType(@Headers("Location") String location;);
//    }


    /**
     * 动态配置方式的 GET请求
     */
    public interface IpServiceForPath {
        @GET("{path}/getIpInfo.php?ip=59.108.54.37")
        Call<?> getIpMsg(@Path("path") String path);
    }

    /**
     * 传输数据类型为键值对的POST请求
     */
    public interface IpServiceForPost {
        @FormUrlEncoded//标明为表单请求
        @POST("getIpInfo.php")
        Call<?> getIpMsg(@Field("ip") String first);
    }

    /**
     * 传输数据类型Json字符串的POST请求
     */
    public interface IpServiceForPostBody {
        @POST("getIpInfo.php")
        Call<String> getIpMsg(@Body Reviews reviews);//使用Body会转为字符串
    }

    class Reviews {
        public String book;
        public String title;
        public String content;
        public String rating;
    }


    public interface IpServiceForQuery{
        @GET("getIpInfo.php")
        Call<?> getIpMsg(@Query("ip") String ip);
    }

    //
    public  interface IpServiceForQueryMap {
        @GET("getIpInfo.php")
        Call<?> getIpMsg(@QueryMap Map<String, String> options);
    }


    public interface FileUploadService {
        // 上传单个文件
        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadFile(
                @Part("description") RequestBody description,
                @Part MultipartBody.Part file);

        // 上传多个文件
        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadMultipleFiles(
                @Part("description") RequestBody description,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2);

        // 上传多个文件
        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadMultipleFiles(
                @Part("description") RequestBody description,
                @PartMap Map<String,RequestBody> file1);

    }






}

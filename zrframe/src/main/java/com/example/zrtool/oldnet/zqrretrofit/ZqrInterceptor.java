package com.example.zrtool.oldnet.zqrretrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-02
 * Time: 8:25
 */
public class ZqrInterceptor implements Interceptor {
    String name;
    String value;

    public ZqrInterceptor(String name, String value) {
        this.name = name;
        this.value = value;
    }

    //通过实现Interceptor来设置请求参数
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url().newBuilder()
                .addQueryParameter(name, value)
                .build();
        request = request.newBuilder().url(httpUrl).build();
        return chain.proceed(request);
    }

    //addInterceptor(new CustomInterceptor())   使用方法



    //设置动态头部信息
//    public Response intercept2(Chain chain) throws IOException {
//        Request original = chain.request();
//        Request request = original.newBuilder()
//                .header("User-Agent", "Your-App-Name")
//                .header("Accept", "application/vnd.yourapi.v1.full+json")
//                .method(original.method(), original.body())
//                .build();
//        return chain.proceed(request);
//    }








}

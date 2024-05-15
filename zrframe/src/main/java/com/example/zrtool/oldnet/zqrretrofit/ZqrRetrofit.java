package com.example.zrtool.oldnet.zqrretrofit;


import com.example.zrtool.oldnet.beans.MainNewsBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-02
 * Time: 9:10
 */
public class ZqrRetrofit {

    /**
     * 普通GET请求
     */
    private void getIpInformation() {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyRetrofit.IpService ipService = retrofit.create(MyRetrofit.IpService.class);
        Call<MainNewsBean> call = ipService.getIpMsg();
        call.enqueue(new Callback<MainNewsBean>() {
            @Override
            public void onResponse(Call<MainNewsBean> call, Response<MainNewsBean> response) {
//                String country = response.body().getData().getCountry();
//                Log.i("wangshu", "country" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MainNewsBean> call, Throwable t) {

            }
        });
    }

}

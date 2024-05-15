//package com.example.zrtool.oldnet.basehttp;
//
//import android.text.TextUtils;
//import android.util.Log;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//
//import retrofit2.http.HTTP;
//
///**
// * Created by Android Studio.
// * User: 86182
// * Date: 2020-12-01
// * Time: 19:37
// */
//public class MyHttpActivity extends AppCompatActivity {
//    private static final String TAG="HttpUrl";
//
//    public void initView() {
////        注意点1：
////        从Android 6.0开始引入了对Https的推荐支持，与以往不同，Android P的系统上面默认所有Http的请求都被阻止了。
////        解决的办法简单来说可以通过在AnroidManifest.xml中的application显示设置：
////        <application android:usesCleartextTraffic="true">
////        注意点2：
////        <!--volley框架：在 Android 6.0 中，我们取消了对 Apache HTTP 客户端的支持  需要用的话 要添加如下-->
////        <uses-library
////        android:name="org.apache.http.legacy"
////        android:required="false" />
//
//        //---------------------HttpClient GET 请求方式
////          useHttpClientGetThread("http://www.baidu.com");
//        //---------------------HttpClient post 请求方式
////        useHttpClientPostThread();
//        //--------------------HttpUrlConnection   请求方式
////        useHttpUrlConnectionPostThread();
//
//    }
//    /**
//     * --------------------------------------------------------------HttpClient GET请求网络
//     */
//    private void useHttpClientGetThread(String url) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                useHttpClientGet(url);
//            }
//        }).start();
//    }
//    /**
//     * 设置默认请求参数，并返回HttpClient
//     *
//     *     //6.0版本要使用httpclient的话 需要如下设置
//     *     useLibrary 'org.apache.http.legacy'
//     *
//     * @return HttpClient
//     */
//    private HttpClient createHttpClient() {
//        HttpParams mDefaultHttpParams = new BasicHttpParams();
//        //设置连接超时
//        HttpConnectionParams.setConnectionTimeout(mDefaultHttpParams, 15000);
//        //设置请求超时
//        HttpConnectionParams.setSoTimeout(mDefaultHttpParams, 15000);
//        HttpConnectionParams.setTcpNoDelay(mDefaultHttpParams, true);
//        HttpProtocolParams.setVersion(mDefaultHttpParams, HttpVersion.HTTP_1_1);
//        HttpProtocolParams.setContentCharset(mDefaultHttpParams, HTTP.UTF_8);
//        //持续握手
//        HttpProtocolParams.setUseExpectContinue(mDefaultHttpParams, true);
//        HttpClient mHttpClient = new DefaultHttpClient(mDefaultHttpParams);
//        return mHttpClient;
//
//    }
//    /**
//     * 使用HttpClient的get请求网络
//     *
//     * @param url
//     */
//    private void useHttpClientGet(String url) {
//        HttpGet mHttpGet = new HttpGet(url);
//        mHttpGet.addHeader("Connection", "Keep-Alive");
//        try {
//            HttpClient mHttpClient = createHttpClient();
//            HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
//            HttpEntity mHttpEntity = mHttpResponse.getEntity();
//            int code = mHttpResponse.getStatusLine().getStatusCode();
//            if (null != mHttpEntity) {
//                InputStream mInputStream = mHttpEntity.getContent();
//                String respose = converStreamToString(mInputStream);
//                Log.e(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
//                mInputStream.close();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "请求异常" +e.toString() );
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 将请求结果装潢为String类型
//     *
//     * @param is InputStream
//     * @return String
//     * @throws IOException
//     */
//    private String converStreamToString(InputStream is) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuffer sb = new StringBuffer();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        String respose = sb.toString();
//        return respose;
//    }
//
//
//    /**
//     * ------------------------------------------------------------HttpClient POST请求网络
//     */
//    private void useHttpClientPostThread() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                useHttpClientPost("http://ip.taobao.com/service/getIpInfo.php");
//            }
//        }).start();
//    }
//
//    private void useHttpClientPost(String url) {
//        HttpPost mHttpPost = new HttpPost(url);
//        mHttpPost.addHeader("Connection", "Keep-Alive");
//        try {
//            HttpClient mHttpClient = createHttpClient();
//            List<NameValuePair> postParams = new ArrayList<>();
//            //要传递的参数
//            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
//            mHttpPost.setEntity(new UrlEncodedFormEntity(postParams));
//            HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
//            HttpEntity mHttpEntity = mHttpResponse.getEntity();
//            int code = mHttpResponse.getStatusLine().getStatusCode();
//            if (null != mHttpEntity) {
//                InputStream mInputStream = mHttpEntity.getContent();
//                String respose = converStreamToString(mInputStream);
//                Log.e(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
//                mInputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ---------------------------------------------------------HttpUrlConnection POST请求网络
//     */
//    private void useHttpUrlConnectionPostThread() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                useHttpUrlConnectionPost("http://ip.taobao.com/service/getIpInfo.php");
//            }
//        }).start();
//    }
//
//    private void useHttpUrlConnectionPost(String url) {
//        InputStream mInputStream = null;
//        HttpURLConnection mHttpURLConnection = getHttpURLConnection(url);
//        try {
//            List<NameValuePair> postParams = new ArrayList<>();
//            //要传递的参数
//            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
//            postParams(mHttpURLConnection.getOutputStream(), postParams);
//            mHttpURLConnection.connect();
//            mInputStream = mHttpURLConnection.getInputStream();
//            int code = mHttpURLConnection.getResponseCode();
//            String respose = converStreamToString(mInputStream);
//            Log.e(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
//            mInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 配置默认参数
//     * @param url
//     * @return HttpURLConnection
//     */
//    public static HttpURLConnection getHttpURLConnection(String url){
//        HttpURLConnection mHttpURLConnection=null;
//        try {
//            URL mUrl=new URL(url);
//            mHttpURLConnection=(HttpURLConnection)mUrl.openConnection();
//            //设置链接超时时间
//            mHttpURLConnection.setConnectTimeout(15000);
//            //设置读取超时时间
//            mHttpURLConnection.setReadTimeout(15000);
//            //设置请求参数
//            mHttpURLConnection.setRequestMethod("POST");
//            //添加Header
//            mHttpURLConnection.setRequestProperty("Connection","Keep-Alive");
//            //接收输入流
//            mHttpURLConnection.setDoInput(true);
//            //传递参数时需要开启
//            mHttpURLConnection.setDoOutput(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return mHttpURLConnection ;
//    }
//
//    /**
//     * 组织请求参数,并将参数写入到输出流
//     */
//    public static void postParams(OutputStream output, List<NameValuePair> paramsList) throws IOException{
//        StringBuilder mStringBuilder=new StringBuilder();
//        for (NameValuePair pair:paramsList){
//            if(!TextUtils.isEmpty(mStringBuilder)){
//                mStringBuilder.append("&");
//            }
//            mStringBuilder.append(URLEncoder.encode(pair.getName(),"UTF-8"));
//            mStringBuilder.append("=");
//            mStringBuilder.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
//        }
//        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
//        writer.write(mStringBuilder.toString());
//        writer.flush();
//        writer.close();
//    }
//
//}

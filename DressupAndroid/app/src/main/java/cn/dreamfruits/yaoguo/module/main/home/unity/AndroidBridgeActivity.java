package cn.dreamfruits.yaoguo.module.main.home.unity;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.GsonUtils;
import com.example.zrsinglephoto.easyphotos.EasyPhotos;
import com.example.zrsinglephoto.easyphotos.GlideEngine;
import com.example.zrsinglephoto.easyphotos.models.album.entity.Photo;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.dreamfruits.yaoguo.constants.MMKVConstants;
import cn.dreamfruits.yaoguo.module.main.home.cropbitmap.CropBitmapActivity;
import cn.dreamfruits.yaoguo.module.main.diy.mine.DiyMineActivity;
import cn.dreamfruits.yaoguo.repository.MMKVRepository;
import cn.dreamfruits.yaoguo.repository.OauthRepository;
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean;
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentCDNSecretKeyBean;

/**
 * @Author qiwangi
 * @Date 2023/5/30
 * @TIME 12:24
 */
public class AndroidBridgeActivity extends UnityPlayerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        if (!Singleton.INSTANCE.isNewScene()){//设置是否进入场景的开关
//            Log.e("unity", "不进入场景");
//            return;
//        }
        switch (getIntent().getStringExtra("entryType")) {
            case "EnterDIY":
                EnterDIY();
                Log.e("unity", "进入DIY");
                break;
            case "EnterMyWardrobe":
                EnterMyWardrobe();
                Log.e("unity", "进入我的衣橱");
                break;
            case "EnterOthersWardrobe":
                EnterOthersWardrobe();
                Log.e("unity", "进入他人衣橱");
                break;
            case "EnterTryMatchPlan":
                EnterTryMatchPlan();
                Log.e("unity", "套装试穿");
                //[154454,54544,11211] <多个单品的ID数组>
                break;
            case "EnterTrySingleProcuct":
                EnterTrySingleProcuct();
                Log.e("unity", "单品试穿");
                //5455445 <当前单品的ID>
                break;
        }
//        Singleton.INSTANCE.setNewScene(false);
    }

    /**
     * U->A 获取用户token
     * UnityPlayer.UnitySendMessage("AppManager", "ReciveUserToken", str);//A->U 返回用户token
     */
    private String getUserToken(String s) {
        Log.e("unity", "U->A 获取用户token");
        String temp = "{\"token\":\"" + OauthRepository.Companion.getToken() + "\",\"expireTime\":" +  OauthRepository.Companion.getExpireTime() + "\"}";
        String TempJsonOauth = MMKVRepository.INSTANCE.getCommonMMKV().decodeString("key_cache_oauth_bean", "");
        LoginResultBean loginResultBean = GsonUtils.fromJson(TempJsonOauth, LoginResultBean.class);
        String str = "{\"token\":\"" + loginResultBean.getToken() + "\",\"expireTime\":\"" + loginResultBean.getExpireTime() + "\"}";
        Log.e("unity--11111--", str);
        return str;
    }

    /**
     * U->A 获取CND秘钥
     * UnityPlayer.UnitySendMessage("AppManager", "ReciveCDNKey", str);//A->U 返回CDN秘钥
     */
    private String getCDNKey(String s) {
        Log.e("unity", "U->A 获取CND秘钥");
        String TempJsonSecretKey = MMKVRepository.INSTANCE.getCommonMMKV().decodeString(MMKVConstants.GET_TENCENT_CDN_SECRETKEY, "");
        TencentCDNSecretKeyBean tencentCDNSecretKeyBean = GsonUtils.fromJson(TempJsonSecretKey, TencentCDNSecretKeyBean.class);
        String str = "{\"secretKey\":\"" + tencentCDNSecretKeyBean.getSecretKey()
                + "\",\"secondSecretKey\":\"" + tencentCDNSecretKeyBean.getSecondSecretKey()
                + "\",\"randStr\":\"" + tencentCDNSecretKeyBean.getRandStr() + "\"}";
        Log.e("unity", str);
        return str;
    }


    /**
     * U->A 获取相册图片
     */
    private void getChangeFacePic(String s) {
        Log.e("unity", "U->A 获取相册图片");
        //链式调用相册 选择单张照片
        EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                .setFileProviderAuthority("com.example.zrsinglephoto.fileprovider")
                .start(101); //也可以选择链式调用写法
    }
    private RelativeLayout photosAdView, albumItemsAdView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (albumItemsAdView != null) {
            if (albumItemsAdView.getParent() != null) {
                ((FrameLayout) (albumItemsAdView.getParent())).removeAllViews();
            }
        }
        if (photosAdView != null) {
            if (photosAdView.getParent() != null) {
                ((FrameLayout) (photosAdView.getParent())).removeAllViews();
            }
        }
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
//                boolean selectedOriginal =
//                        data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
                Log.e("zqr", "onActivityResult: " + resultPhotos.get(0).path);

//                String path = "{\"path\": \""+resultPhotos.get(0).path+"\"}";
//                UnityPlayer.UnitySendMessage("AppManager", "ReceiveChangeFacePic", path);//A->U 返回照片路径
//                //跳转到裁剪页面
                Intent intent = new Intent(this, CropBitmapActivity.class);
                intent.putExtra("path", resultPhotos.get(0).path);//传递图片路径
                startActivity(intent);
//                return;
            }
        } else if (RESULT_CANCELED == resultCode) {
            Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
        }
    }
    //==========================以上图片裁剪
    /**
     * U->A 退出Unity
     */
    private void exitUnity(String s) {
        Log.e("unity", "U->A 退出Unity");
        finish();
//        switch (s){
//            case "1":{//主页
//                Intent intent = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    intent = new Intent(this, MainActivity.class);
//                }
//                startActivity(intent);
//            }
//            break;
//            case "2":{//帖子详情
//                Intent intent = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    intent = new Intent(this, PostDetailsActivity.class);
//                }
//                startActivity(intent);
//            }
//            break;
//            case "3":{//单品详情
//                Intent intent = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    intent = new Intent(this, SingleDetailsActivity.class);
//                }
//                startActivity(intent);
//            }
//            break;
//            case "4":{//diy
//                Intent intent = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    intent = new Intent(this, DiyStyleListActivity.class);
//                }
//                startActivity(intent);
//            }
//            break;
//            default://默认返回主页
//                Intent intent = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    intent = new Intent(this, MainActivity.class);
//                }
//                startActivity(intent);
//                break;
//        }
    }

    /**
     * A->U 进入DIY
     */
    private void EnterDIY() {
        // TODO: 2023/6/15 固定写一个id
        Log.e("unity", "A->U 进入DIY" + getIntent().getStringExtra("data"));
        UnityPlayer.UnitySendMessage("AppManager", "EnterDIY", getIntent().getStringExtra("data"));
    }

    /**
     * A->U 进入我的衣橱
     */
    private void EnterMyWardrobe() {
        Log.e("unity", "A->U 进入我的衣橱");
        UnityPlayer.UnitySendMessage("AppManager", "EnterMyWardrobe", "");
    }

    /**
     * A->U 进入他人衣橱
     */
    private void EnterOthersWardrobe() {
        String str=getIntent().getStringExtra("userId");
        Log.e("unity", "A->U 进入他人衣橱"+str);
        UnityPlayer.UnitySendMessage("AppManager", "EnterOthersWardrobe", str);
    }

    /**
     * U->A 版型制作完成
     */
    private void diyMakeComplete(String s) {
        Log.e("unity", "A->U 版型制作完成");
        Log.e("unity", "A->U 版型制作完成==="+s);
//        UnityPlayer.UnitySendMessage("AppManager", "DiyMakeComplete", s);
        Intent intent = new Intent(this, DiyMineActivity.class);
        intent.putExtra("key", "value");
        startActivity(intent);
        // TODO: 2023/6/20 杀掉进程
        finish();
    }



    /**
     * U->A 删除相册地址
     */
    private void PhotoImageDelete(String s) {
        Log.e("unity", "U->A 删除相册地址==="+s);

        try{
            File imageFile = new File(s);
            if (imageFile.exists()) {
               imageFile.delete();
                Log.e("unity", "U->A 删除相册地址成功");
            }
        }catch (Exception e){
            Log.e("unity", "U->A 删除相册地址---异常"+e.getMessage());
        }
    }

    /**
     * A->U 套装试穿
     */
    private void EnterTryMatchPlan() {
        Log.e("unity", "A->U 套装试穿");
        String str=getIntent().getStringExtra("tryClothes");
        UnityPlayer.UnitySendMessage("AppManager", "EnterTryMatchPlan", str);
    }

    /**
     * A->U 单品试穿
     */
    private void EnterTrySingleProcuct() {
        Log.e("unity", "A->U 单品试穿");
        String str=getIntent().getStringExtra("trySingleClothes");
        UnityPlayer.UnitySendMessage("AppManager", "EnterTrySingleProcuct", str);
    }

    /**
     * @param jsonString
     * @return
     */
    public static String parseJsonCmd(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String cmdValue = jsonObject.getString("cmd");
            return cmdValue;
        } catch (Exception e) {
            Log.e("unity", "parseJson方法: " + e.getMessage());
        }
        return "-";
    }

    /**
     * @param jsonString
     * @return
     */
    public static String parseJsonData(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            return dataObject.toString();
        } catch (Exception e) {
            Log.e("unity", "parseJson方法: " + e.getMessage());
        }
        return "-";
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("unity", "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("unity", "onPause: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("unity", "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("unity", "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("unity", "onDestroy: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("unity", "onBackPressed: ");
        finish();
    }


   }

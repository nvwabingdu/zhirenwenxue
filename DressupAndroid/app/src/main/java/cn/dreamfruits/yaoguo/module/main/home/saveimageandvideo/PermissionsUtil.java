package cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionsUtil {
    /**
     * 权限申请
     */
    public static String[] PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.REQUEST_INSTALL_PACKAGES
    };
    public static String[] PERMISSION_ALL = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.PHONE,
            Manifest.permission_group.MICROPHONE,
            Manifest.permission_group.CAMERA};

    public static String[] PERMISSION_CAMERA = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission_group.CAMERA,
            Manifest.permission_group.MICROPHONE};

    @SuppressLint("CheckResult") //多个申请暂时有问题
    public static void requestAll(FragmentActivity context, Consumer<Permission> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission_group.CAMERA,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.LOCATION,
                        Manifest.permission_group.PHONE,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult") //多个申请有问题
    public static void requestCamera(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(//Manifest.permission.REQUEST_INSTALL_PACKAGES,
                        Manifest.permission.READ_EXTERNAL_STORAGE,//允许应用程序读取设备存储器上的文件，例如照片、视频或音乐。
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//允许应用程序将文件写入设备存储器，例如保存照片或视频到相册中。
                        Manifest.permission.ACCESS_COARSE_LOCATION,//允许应用程序获取粗略的设备位置信息，例如城市或地区级别的位置信息。
                        Manifest.permission.CAMERA,//允许应用程序访问设备的摄像头，进行拍照或录制视频等操作。
                        Manifest.permission.READ_PHONE_STATE,//允许应用程序读取设备的通话状态和设备标识符等信息。
                        Manifest.permission.RECORD_AUDIO//允许应用程序录制音频。
                )
                .subscribe(accept);
    }

    @SuppressLint("CheckResult") //多个申请有问题
    public static void requestStorage(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestLocation(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission_group.LOCATION)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestPhone(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission_group.PHONE)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestPackages(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .subscribe(accept);
    }
}

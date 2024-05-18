package com.example.zrqrcode.zxing;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import com.example.zrqrcode.R;
import com.example.zrqrcode.zxing.util.LogUtils;
import com.example.zrqrcode.zxing.util.PermissionUtils;
import com.google.zxing.Result;


/**
 * 相机扫描基类；{@link CaptureFragment} 内部持有{@link CameraScan}，便于快速实现扫描识别。
 * <p>
 * 快速实现扫描识别主要有以下几种方式：
 * <p>
 * 1、通过继承 {@link CaptureActivity}或者{@link CaptureFragment}或其子类，可快速实现扫描识别。
 * （适用于大多数场景，自定义布局时需覆写getLayoutId方法）
 * <p>
 * 2、在你项目的Activity或者Fragment中实例化一个{@link DefaultCameraScan}。（适用于想在扫码界面写交互逻辑，又因为项目
 * 架构或其它原因，无法直接或间接继承{@link CaptureActivity}或{@link CaptureFragment}时使用）
 * <p>
 * 3、继承{@link CameraScan}自己实现一个，可参照默认实现类{@link DefaultCameraScan}，其他步骤同方式2。（高级用法，谨慎使用）
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CaptureFragment extends Fragment implements CameraScan.OnScanResultCallback {

    /**
     * 相机权限请求代码
     */
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0X86;
    /**
     * 根视图
     */
    private View mRootView;
    /**
     * 预览视图
     */
    protected PreviewView previewView;
    /**
     * 取景视图
     */
    protected ViewfinderView viewfinderView;
    /**
     * 手电筒视图
     */
    protected View ivFlashlight;
    /**
     * CameraScan
     */
    private CameraScan mCameraScan;

    public static CaptureFragment newInstance() {

        Bundle args = new Bundle();

        CaptureFragment fragment = new CaptureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isContentView()) {
            mRootView = createRootView(inflater, container);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    /**
     * 初始化
     */
    public void initUI() {
        previewView = mRootView.findViewById(getPreviewViewId());
        int viewfinderViewId = getViewfinderViewId();
        if (viewfinderViewId != 0 && viewfinderViewId != View.NO_ID) {
            viewfinderView = mRootView.findViewById(viewfinderViewId);
        }
        int ivFlashlightId = getFlashlightId();
        if (ivFlashlightId != 0 && ivFlashlightId != View.NO_ID) {
            ivFlashlight = mRootView.findViewById(ivFlashlightId);
            if (ivFlashlight != null) {
                ivFlashlight.setOnClickListener(v -> onClickFlashlight());
            }
        }
        initCameraScan();
        startCamera();
    }

    /**
     * 点击手电筒
     */
    protected void onClickFlashlight() {
        toggleTorchState();
    }

    /**
     * 初始化CameraScan
     */
    public void initCameraScan() {
        mCameraScan = new DefaultCameraScan(this, previewView);
        mCameraScan.setOnScanResultCallback(this);
    }

    /**
     * 启动相机预览
     */
    public void startCamera() {
        if (mCameraScan != null) {
            if (PermissionUtils.checkPermission(getContext(), Manifest.permission.CAMERA)) {
                mCameraScan.startCamera();
            } else {
                LogUtils.d("checkPermissionResult != PERMISSION_GRANTED");
                PermissionUtils.requestPermission(this, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * 释放相机
     */
    private void releaseCamera() {
        if (mCameraScan != null) {
            mCameraScan.release();
        }
    }

    /**
     * 切换闪光灯状态（开启/关闭）
     */
    protected void toggleTorchState() {
        if (mCameraScan != null) {
            boolean isTorch = mCameraScan.isTorchEnabled();
            mCameraScan.enableTorch(!isTorch);
            if (ivFlashlight != null) {
                ivFlashlight.setSelected(!isTorch);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            requestCameraPermissionResult(permissions, grantResults);
        }
    }

    /**
     * 请求Camera权限回调结果
     *
     * @param permissions  权限
     * @param grantResults 授权结果
     */
    public void requestCameraPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.requestPermissionsResult(Manifest.permission.CAMERA, permissions, grantResults)) {
            startCamera();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        releaseCamera();
        super.onDestroyView();
    }

    /**
     * 返回true时会自动初始化{@link #createRootView(LayoutInflater, ViewGroup)}，返回为false是需自己去初始化{@link #createRootView(LayoutInflater, ViewGroup)}
     *
     * @return 默认返回true
     */
    public boolean isContentView() {
        return true;
    }

    /**
     * 创建{@link #mRootView}
     *
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    public View createRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    /**
     * 布局ID；通过覆写此方法可以自定义布局
     *
     * @return 布局ID
     */
    public int getLayoutId() {
        return R.layout.zxl_capture;
    }

    /**
     * {@link #viewfinderView} 的 ID
     *
     * @return 默认返回{@code R.id.viewfinderView}, 如果不需要扫码框可以返回0
     */
    public int getViewfinderViewId() {
        return R.id.viewfinderView;
    }

    /**
     * 预览界面{@link #previewView} 的ID；可通过覆写此方法自定义ID
     *
     * @return 默认返回{@code R.id.previewView}
     */
    public int getPreviewViewId() {
        return R.id.previewView;
    }

    /**
     * 获取 {@link #ivFlashlight} 的ID
     *
     * @return 默认返回{@code R.id.ivFlashlight}, 如果不需要手电筒按钮可以返回0
     */
    public int getFlashlightId() {
        return R.id.ivFlashlight;
    }

    /**
     * 获取 {@link CameraScan}
     *
     * @return {@link #mCameraScan}
     */
    public CameraScan getCameraScan() {
        return mCameraScan;
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        return false;
    }

    //--------------------------------------------

    /**
     * 获取根视图
     *
     * @return {@link #mRootView}
     */
    public View getRootView() {
        return mRootView;
    }

}

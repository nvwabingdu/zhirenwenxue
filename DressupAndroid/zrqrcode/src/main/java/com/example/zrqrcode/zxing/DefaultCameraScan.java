package com.example.zrqrcode.zxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.zrqrcode.zxing.analyze.Analyzer;
import com.example.zrqrcode.zxing.analyze.MultiFormatAnalyzer;
import com.example.zrqrcode.zxing.config.AspectRatioCameraConfig;
import com.example.zrqrcode.zxing.config.CameraConfig;
import com.example.zrqrcode.zxing.config.ResolutionCameraConfig;
import com.example.zrqrcode.zxing.manager.AmbientLightManager;
import com.example.zrqrcode.zxing.manager.BeepManager;
import com.example.zrqrcode.zxing.util.LogUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.detector.MathUtils;

import java.util.concurrent.Executors;

/**
 * 相机扫描基类；{@link DefaultCameraScan} 为 {@link CameraScan} 的默认实现
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
public class DefaultCameraScan extends CameraScan {

    /**
     * Defines the maximum duration in milliseconds between a touch pad
     * touch and release for a given touch to be considered a tap (click) as
     * opposed to a hover movement gesture.
     */
    private static final int HOVER_TAP_TIMEOUT = 150;
    /**
     * Defines the maximum distance in pixels that a touch pad touch can move
     * before being released for it to be considered a tap (click) as opposed
     * to a hover movement gesture.
     */
    private static final int HOVER_TAP_SLOP = 20;
    /**
     * 每次缩放改变的步长
     */
    private static final float ZOOM_STEP_SIZE = 0.1F;

    private FragmentActivity mFragmentActivity;
    private Context mContext;
    private LifecycleOwner mLifecycleOwner;
    /**
     * 预览视图
     */
    private PreviewView mPreviewView;

    private ListenableFuture<ProcessCameraProvider> mCameraProviderFuture;
    /**
     * 相机
     */
    private Camera mCamera;
    /**
     * 相机配置
     */
    private CameraConfig mCameraConfig;
    /**
     * 分析器
     */
    private Analyzer mAnalyzer;
    /**
     * 是否分析
     */
    private volatile boolean isAnalyze = true;
    /**
     * 是否已经分析出结果
     */
    private volatile boolean isAnalyzeResult;
    /**
     * 闪光灯（手电筒）视图
     */
    private View flashlightView;

    private MutableLiveData<Result> mResultLiveData;
    /**
     * 扫描结果回调
     */
    private OnScanResultCallback mOnScanResultCallback;
    /**
     * 蜂鸣音效管理器：主要用于播放蜂鸣提示音和振动效果
     */
    private BeepManager mBeepManager;
    /**
     * 环境光线管理器：主要通过传感器来监听光线的亮度变化
     */
    private AmbientLightManager mAmbientLightManager;

    private int mOrientation;
    private int mImageWidth;
    private int mImageHeight;
    /**
     * 最后自动缩放时间
     */
    private long mLastAutoZoomTime;
    /**
     * 最后点击时间，根据两次点击时间间隔用于区分单机和触摸缩放事件
     */
    private long mLastHoveTapTime;
    /**
     * 是否是点击事件
     */
    private boolean isClickTap;
    /**
     * 按下时X坐标
     */
    private float mDownX;
    /**
     * 按下时Y坐标
     */
    private float mDownY;

    public DefaultCameraScan(@NonNull FragmentActivity activity, @NonNull PreviewView previewView) {
        this.mFragmentActivity = activity;
        this.mLifecycleOwner = activity;
        this.mContext = activity;
        this.mPreviewView = previewView;
        initData();
    }

    public DefaultCameraScan(@NonNull Fragment fragment, @NonNull PreviewView previewView) {
        this.mFragmentActivity = fragment.getActivity();
        this.mLifecycleOwner = fragment;
        this.mContext = fragment.getContext();
        this.mPreviewView = previewView;
        initData();
    }

    /**
     * 缩放手势检测
     */
    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            if (mCamera != null) {
                float ratio = mCamera.getCameraInfo().getZoomState().getValue().getZoomRatio();
                // 根据缩放的手势和当前比例进行缩放
                zoomTo(ratio * scale);
                return true;
            }
            return false;
        }

    };

    /**
     * 初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initData() {
        mResultLiveData = new MutableLiveData<>();
        mResultLiveData.observe(mLifecycleOwner, result -> {
            if (result != null) {
                handleAnalyzeResult(result);
            } else if (mOnScanResultCallback != null) {
                mOnScanResultCallback.onScanResultFailure();
            }
        });

        mOrientation = mContext.getResources().getConfiguration().orientation;

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(mContext, mOnScaleGestureListener);
        mPreviewView.setOnTouchListener((v, event) -> {
            handlePreviewViewClickTap(event);
            if (isNeedTouchZoom()) {
                return scaleGestureDetector.onTouchEvent(event);
            }
            return false;
        });

        mBeepManager = new BeepManager(mContext);
        mAmbientLightManager = new AmbientLightManager(mContext);
        mAmbientLightManager.register();
        mAmbientLightManager.setOnLightSensorEventListener((dark, lightLux) -> {
            if (flashlightView != null) {
                if (dark) {
                    if (flashlightView.getVisibility() != View.VISIBLE) {
                        flashlightView.setVisibility(View.VISIBLE);
                        flashlightView.setSelected(isTorchEnabled());
                    }
                } else if (flashlightView.getVisibility() == View.VISIBLE && !isTorchEnabled()) {
                    flashlightView.setVisibility(View.INVISIBLE);
                    flashlightView.setSelected(false);
                }
            }
        });
    }

    /**
     * 处理预览视图点击事件；如果触发的点击事件被判定对焦操作，则开始自动对焦
     *
     * @param event 事件
     */
    private void handlePreviewViewClickTap(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isClickTap = true;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mLastHoveTapTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isClickTap = MathUtils.distance(mDownX, mDownY, event.getX(), event.getY()) < HOVER_TAP_SLOP;
                    break;
                case MotionEvent.ACTION_UP:
                    if (isClickTap && mLastHoveTapTime + HOVER_TAP_TIMEOUT > System.currentTimeMillis()) {
                        // 开始对焦和测光
                        startFocusAndMetering(event.getX(), event.getY());
                    }
                    break;
            }
        }
    }

    /**
     * 开始对焦和测光
     *
     * @param x X轴坐标
     * @param y Y轴坐标
     */
    private void startFocusAndMetering(float x, float y) {
        if (mCamera != null) {
            MeteringPoint point = mPreviewView.getMeteringPointFactory().createPoint(x, y);
            FocusMeteringAction focusMeteringAction = new FocusMeteringAction.Builder(point).build();
            if (mCamera.getCameraInfo().isFocusMeteringSupported(focusMeteringAction)) {
                mCamera.getCameraControl().startFocusAndMetering(focusMeteringAction);
                LogUtils.d("startFocusAndMetering:" + x + "," + y);
            }
        }
    }

    @Override
    public CameraScan setCameraConfig(CameraConfig cameraConfig) {
        if (cameraConfig != null) {
            this.mCameraConfig = cameraConfig;
        }
        return this;
    }

    /**
     * 初始化相机配置
     */
    /**
     * 初始化相机配置
     */
    private void initCameraConfig(Context context) {
        if (mCameraConfig == null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int size = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            // 根据分辨率初始化缺省配置CameraConfig；在此前提下尽可能的找到比屏幕分辨率小一级的配置；在适配、性能与体验之间得有所取舍，找到平衡点。
            if (size > ResolutionCameraConfig.IMAGE_QUALITY_1080P) {
                mCameraConfig = new ResolutionCameraConfig(context);
            } else if (size > ResolutionCameraConfig.IMAGE_QUALITY_720P) {
                mCameraConfig = new ResolutionCameraConfig(context, ResolutionCameraConfig.IMAGE_QUALITY_720P);
            } else {
                mCameraConfig = new AspectRatioCameraConfig(context);
            }
        }
    }

    @Override
    public void startCamera() {
        initCameraConfig(mContext);
        if (mAnalyzer == null) {
            mAnalyzer = new MultiFormatAnalyzer();
        }
        mCameraProviderFuture = ProcessCameraProvider.getInstance(mContext);
        mCameraProviderFuture.addListener(() -> {
            try {
                Preview preview = mCameraConfig.options(new Preview.Builder());

                //相机选择器
                CameraSelector cameraSelector = mCameraConfig.options(new CameraSelector.Builder());
                //设置SurfaceProvider
                preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

                //图像分析
                ImageAnalysis imageAnalysis = mCameraConfig.options(new ImageAnalysis.Builder()
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST));
                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), image -> {
                    mImageWidth = image.getWidth();
                    mImageHeight = image.getHeight();
                    if (isAnalyze && !isAnalyzeResult && mAnalyzer != null) {
                        Result result = mAnalyzer.analyze(image, mOrientation);
                        mResultLiveData.postValue(result);
                    }
                    image.close();
                });
                if (mCamera != null) {
                    mCameraProviderFuture.get().unbindAll();
                }
                // 绑定到生命周期
                mCamera = mCameraProviderFuture.get().bindToLifecycle(mLifecycleOwner, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                LogUtils.e(e);
            }

        }, ContextCompat.getMainExecutor(mContext));
    }

    /**
     * 处理分析结果
     *
     * @param result
     */
    private synchronized void handleAnalyzeResult(Result result) {

        if (isAnalyzeResult || !isAnalyze) {
            return;
        }
        isAnalyzeResult = true;
        if (mBeepManager != null) {
            mBeepManager.playBeepSoundAndVibrate();
        }

        if (result.getBarcodeFormat() == BarcodeFormat.QR_CODE && isNeedAutoZoom() && mLastAutoZoomTime + 100 < System.currentTimeMillis()) {
            ResultPoint[] points = result.getResultPoints();
            if (points != null && points.length >= 2) {
                float distance1 = ResultPoint.distance(points[0], points[1]);
                float maxDistance = distance1;
                if (points.length >= 3) {
                    float distance2 = ResultPoint.distance(points[1], points[2]);
                    float distance3 = ResultPoint.distance(points[0], points[2]);
                    maxDistance = Math.max(Math.max(distance1, distance2), distance3);
                }
                if (handleAutoZoom((int) maxDistance, result)) {
                    return;
                }
            }
        }

        scanResultCallback(result);
    }

    /**
     * 处理自动缩放
     *
     * @param distance
     * @param result
     * @return
     */
    private boolean handleAutoZoom(int distance, Result result) {
        int size = Math.min(mImageWidth, mImageHeight);
        if (distance * 4 < size) {
            mLastAutoZoomTime = System.currentTimeMillis();
            zoomIn();
            scanResultCallback(result);
            return true;
        }
        return false;
    }

    /**
     * 扫描结果回调
     *
     * @param result 扫描结果
     */
    private void scanResultCallback(Result result) {
        if (mOnScanResultCallback != null && mOnScanResultCallback.onScanResultCallback(result)) {
            /*
             * 如果拦截了结果，则重置分析结果状态，并当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
             * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调用
             * setAnalyzeImage(false)，因为setAnalyzeImage方法能动态控制是否继续分析图像。
             */
            isAnalyzeResult = false;
            return;
        }

        if (mFragmentActivity != null) {
            Intent intent = new Intent();
            intent.putExtra(SCAN_RESULT, result.getText());
            mFragmentActivity.setResult(Activity.RESULT_OK, intent);
            mFragmentActivity.finish();
        }
    }

    @Override
    public void stopCamera() {
        if (mCameraProviderFuture != null) {
            try {
                mCameraProviderFuture.get().unbindAll();
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }

    @Override
    public CameraScan setAnalyzeImage(boolean analyze) {
        isAnalyze = analyze;
        return this;
    }

    @Override
    public CameraScan setAnalyzer(Analyzer analyzer) {
        mAnalyzer = analyzer;
        return this;
    }


    @Override
    public void zoomIn() {
        if (mCamera != null) {
            float ratio = getCameraInfo().getZoomState().getValue().getZoomRatio() + ZOOM_STEP_SIZE;
            float maxRatio = getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
            if (ratio <= maxRatio) {
                mCamera.getCameraControl().setZoomRatio(ratio);
            }
        }
    }

    @Override
    public void zoomOut() {
        if (mCamera != null) {
            float ratio = getCameraInfo().getZoomState().getValue().getZoomRatio() - ZOOM_STEP_SIZE;
            float minRatio = getCameraInfo().getZoomState().getValue().getMinZoomRatio();
            if (ratio >= minRatio) {
                mCamera.getCameraControl().setZoomRatio(ratio);
            }
        }
    }

    @Override
    public void zoomTo(float ratio) {
        if (mCamera != null) {
            ZoomState zoomState = getCameraInfo().getZoomState().getValue();
            float maxRatio = zoomState.getMaxZoomRatio();
            float minRatio = zoomState.getMinZoomRatio();
            float zoom = Math.max(Math.min(ratio, maxRatio), minRatio);
            mCamera.getCameraControl().setZoomRatio(zoom);
        }
    }

    @Override
    public void lineZoomIn() {
        if (mCamera != null) {
            float zoom = getCameraInfo().getZoomState().getValue().getLinearZoom() + ZOOM_STEP_SIZE;
            if (zoom <= 1f) {
                mCamera.getCameraControl().setLinearZoom(zoom);
            }
        }
    }

    @Override
    public void lineZoomOut() {
        if (mCamera != null) {
            float zoom = getCameraInfo().getZoomState().getValue().getLinearZoom() - ZOOM_STEP_SIZE;
            if (zoom >= 0f) {
                mCamera.getCameraControl().setLinearZoom(zoom);
            }
        }
    }

    @Override
    public void lineZoomTo(@FloatRange(from = 0.0, to = 1.0) float linearZoom) {
        if (mCamera != null) {
            mCamera.getCameraControl().setLinearZoom(linearZoom);
        }
    }

    @Override
    public void enableTorch(boolean torch) {
        if (mCamera != null && hasFlashUnit()) {
            mCamera.getCameraControl().enableTorch(torch);
        }
    }

    @Override
    public boolean isTorchEnabled() {
        if (mCamera != null) {
            return mCamera.getCameraInfo().getTorchState().getValue() == TorchState.ON;
        }
        return false;
    }

    @Override
    public boolean hasFlashUnit() {
        if (mCamera != null) {
            return mCamera.getCameraInfo().hasFlashUnit();
        }
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public CameraScan setVibrate(boolean vibrate) {
        if (mBeepManager != null) {
            mBeepManager.setVibrate(vibrate);
        }
        return this;
    }

    @Override
    public CameraScan setPlayBeep(boolean playBeep) {
        if (mBeepManager != null) {
            mBeepManager.setPlayBeep(playBeep);
        }
        return this;
    }

    @Override
    public CameraScan setOnScanResultCallback(OnScanResultCallback callback) {
        this.mOnScanResultCallback = callback;
        return this;
    }

    @Nullable
    @Override
    public Camera getCamera() {
        return mCamera;
    }

    /**
     * CameraInfo
     *
     * @return {@link CameraInfo}
     */
    private CameraInfo getCameraInfo() {
        return mCamera.getCameraInfo();
    }

    @Override
    public void release() {
        isAnalyze = false;
        flashlightView = null;
        if (mAmbientLightManager != null) {
            mAmbientLightManager.unregister();
        }
        if (mBeepManager != null) {
            mBeepManager.close();
        }
        stopCamera();
    }

    @Override
    public CameraScan bindFlashlightView(@Nullable View v) {
        flashlightView = v;
        if (mAmbientLightManager != null) {
            mAmbientLightManager.setLightSensorEnabled(v != null);
        }
        return this;
    }

    @Override
    public CameraScan setDarkLightLux(float lightLux) {
        if (mAmbientLightManager != null) {
            mAmbientLightManager.setDarkLightLux(lightLux);
        }
        return this;
    }

    @Override
    public CameraScan setBrightLightLux(float lightLux) {
        if (mAmbientLightManager != null) {
            mAmbientLightManager.setBrightLightLux(lightLux);
        }
        return this;
    }

}

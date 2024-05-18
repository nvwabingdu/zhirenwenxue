package cn.dreamfruits.yaoguo.module.main.home.vlistvideo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zr.video.config.ConstantKeys;
import com.zr.video.controller.GestureVideoController;
import com.zr.video.tool.BaseToast;
import com.zr.video.tool.PlayerUtils;
import com.zr.video.ui.view.CustomBottomView;
import com.zr.video.ui.view.CustomCompleteView;
import com.zr.video.ui.view.CustomErrorView;
import com.zr.video.ui.view.CustomGestureView;
import com.zr.video.ui.view.CustomLiveControlView;
import com.zr.video.ui.view.CustomOncePlayView;
import com.zr.video.ui.view.CustomPrepareView;
import com.zr.video.ui.view.CustomTitleView;

/**
 * @Author qiwangi
 * @Date 2023/3/27
 * @TIME 11:23
 */
public class ListVideoController extends GestureVideoController implements View.OnClickListener{

    private Context mContext;
    private ImageView mLockButton;
    private ProgressBar mLoadingProgress;
    private ImageView thumb;
    private CustomTitleView titleView;
    private CustomBottomView vodControlView;

    private CustomLiveControlView liveControlView;
    private CustomOncePlayView customOncePlayView;
    private TextView tvLiveWaitMessage;
    /**
     * 是否是直播，默认不是
     */
    public static boolean IS_LIVE = false;

    public ListVideoController(@NonNull Context context) {
        this(context, null);
    }

    public ListVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return com.yc.video.R.layout.custom_video_player_standard;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        this.mContext = context;
        initFindViewById();
        initListener();
        initConfig();
    }

    private void initFindViewById() {
        mLockButton = findViewById(com.yc.video.R.id.lock);
        mLoadingProgress = findViewById(com.yc.video.R.id.loading);
    }

    private void initListener() {
        mLockButton.setOnClickListener(this);
    }

    private void initConfig() {
        //根据屏幕方向自动进入/退出全屏
        setEnableOrientation(false);
        //设置可以滑动调节进度
        setCanChangePosition(false);
        //竖屏也开启手势操作，默认关闭
        setEnableInNormal(false);
        //滑动调节亮度，音量，进度，默认开启
        setGestureEnabled(false);
        //先移除多的视图view
        removeAllControlComponent();
        //添加视图到界面
        addDefaultControlComponent("");
    }


    /**
     * 快速添加各个组件
     * 需要注意各个层级
     * @param title                             标题
     */
    public void addDefaultControlComponent(String title) {
        //添加自动完成播放界面view
        CustomCompleteView completeView = new CustomCompleteView(mContext);//xml中隐藏分享按钮了
        completeView.setVisibility(GONE);
        this.addControlComponent(completeView);

        //添加错误界面view
        CustomErrorView errorView = new CustomErrorView(mContext);
        errorView.setVisibility(GONE);
        this.addControlComponent(errorView);

        //添加与加载视图界面view，准备播放界面
        CustomPrepareView prepareView = new CustomPrepareView(mContext);
        thumb = prepareView.getThumb();
        prepareView.setClickStart();
        this.addControlComponent(prepareView);

        //添加标题栏
        titleView = new CustomTitleView(mContext);
        titleView.setTitle(title);
        titleView.setVisibility(VISIBLE);
        this.addControlComponent(titleView);

        //添加直播/回放视频底部控制视图
        changePlayType();

        //添加滑动控制视图
        CustomGestureView gestureControlView = new CustomGestureView(mContext);
        this.addControlComponent(gestureControlView);
    }


    /**
     * 切换直播/回放类型
     */
    public void changePlayType(){
        if (IS_LIVE) {
            //添加底部播放控制条
            if (liveControlView==null){
                liveControlView = new CustomLiveControlView(mContext);
            }
            this.removeControlComponent(liveControlView);
            this.addControlComponent(liveControlView);

            //添加直播还未开始视图
            if (customOncePlayView==null){
                customOncePlayView = new CustomOncePlayView(mContext);
                tvLiveWaitMessage = customOncePlayView.getTvMessage();
            }
            this.removeControlComponent(customOncePlayView);
            this.addControlComponent(customOncePlayView);

            //直播视频，移除回放视图
            if (vodControlView!=null){
                this.removeControlComponent(vodControlView);
            }

        } else {
            //添加底部播放控制条
            if (vodControlView==null){
                vodControlView = new CustomBottomView(mContext);
//                vodControlView = new ListVideoCustomBottomView(mContext);
                //是否显示底部控制器。默认显示 这里显示
//                vodControlView.showBottomControl(true);
                //是否显示底部进度条。默认显示
                vodControlView.showBottomProgress(true);
            }
            this.removeControlComponent(vodControlView);
            this.addControlComponent(vodControlView);
            //正常视频，移除直播视图
            if (liveControlView!=null){
                this.removeControlComponent(liveControlView);
            }
            if (customOncePlayView!=null){
                this.removeControlComponent(customOncePlayView);
            }
        }
        setCanChangePosition(!IS_LIVE);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.yc.video.R.id.lock) {
            mControlWrapper.toggleLockState();
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            mLockButton.setSelected(true);
            String string = mContext.getResources().getString(com.yc.video.R.string.locked);
            BaseToast.showRoundRectToast(string);
        } else {
            mLockButton.setSelected(false);
            String string = mContext.getResources().getString(com.yc.video.R.string.unlocked);
            BaseToast.showRoundRectToast(string);
        }
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (mLockButton.getVisibility() == GONE) {
                    mLockButton.setVisibility(VISIBLE);
                    if (anim != null) {
                        mLockButton.startAnimation(anim);
                    }
                }
            } else {
                mLockButton.setVisibility(GONE);
                if (anim != null) {
                    mLockButton.startAnimation(anim);
                }
            }
        }
    }

    /**
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     * @param playerState                   播放模式
     */
    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case ConstantKeys.PlayMode.MODE_NORMAL:
                setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mLockButton.setVisibility(GONE);
                break;
            case ConstantKeys.PlayMode.MODE_FULL_SCREEN:
                if (isShowing()) {
                    mLockButton.setVisibility(VISIBLE);
                } else {
                    mLockButton.setVisibility(GONE);
                }
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                LayoutParams lblp = (LayoutParams) mLockButton.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }

    }

    /**
     * 播放状态
     * -1               播放错误
     * 0                播放未开始
     * 1                播放准备中
     * 2                播放准备就绪
     * 3                正在播放
     * 4                暂停播放
     * 5                正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     * 6                暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
     * 7                播放完成
     * 8                开始播放中止
     * @param playState                     播放状态，主要是指播放器的各种状态
     */
    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case ConstantKeys.CurrentState.STATE_IDLE:
                mLockButton.setSelected(false);
                mLoadingProgress.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_PLAYING:
            case ConstantKeys.CurrentState.STATE_PAUSED:
            case ConstantKeys.CurrentState.STATE_PREPARED:
            case ConstantKeys.CurrentState.STATE_ERROR:
            case ConstantKeys.CurrentState.STATE_COMPLETED:
                mLoadingProgress.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_PREPARING:
            case ConstantKeys.CurrentState.STATE_BUFFERING_PAUSED:
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                mLoadingProgress.setVisibility(GONE);
                mLockButton.setVisibility(GONE);
                mLockButton.setSelected(false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            String string = mContext.getResources().getString(com.yc.video.R.string.lock_tip);
            BaseToast.showRoundRectToast(string);
            return true;
        }
        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }
        Activity activity = PlayerUtils.scanForActivity(getContext());
        //如果不是全屏模式，则直接关闭页面activity
        if (PlayerUtils.isActivityLiving(activity)){
            activity.finish();
        }
        return super.onBackPressed();
    }

    /**
     * 刷新进度回调，子类可在此方法监听进度刷新，然后更新ui
     *
     * @param duration 视频总时长
     * @param position 视频当前时长
     */
    @Override
    protected void setProgress(int duration, int position) {
        super.setProgress(duration, position);
        Log.e("setProgress2222","duration:   "+duration+"    position:  "+position);
    }

    @Override
    public void destroy() {

    }

    public ImageView getThumb() {
        Log.e("setProgress2222","thumb:   "+thumb);
        return thumb;
    }

    public void setTitle(String title) {
        if (titleView!=null){
            titleView.setTitle(title);
        }
    }

    public CustomBottomView getBottomView() {
        return vodControlView;
    }


    public TextView getTvLiveWaitMessage() {
        return tvLiveWaitMessage;
    }
}

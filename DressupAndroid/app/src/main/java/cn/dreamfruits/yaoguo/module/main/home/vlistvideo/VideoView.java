package cn.dreamfruits.yaoguo.module.main.home.vlistvideo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zr.kernel.utils.VideoLogUtils;
import com.zr.video.bridge.ControlWrapper;
import com.zr.video.config.ConstantKeys;
import com.zr.video.tool.PlayerUtils;
import com.zr.video.ui.view.InterControlView;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.util.Singleton;

/**
 * @Author qiwangi
 * @Date 2023/5/11
 * @TIME 15:43
 */
public class VideoView extends FrameLayout implements InterControlView{
    private ImageView thumb;
    private ImageView mPlayBtn;
    private ControlWrapper mControlWrapper;
    private int mScaledTouchSlop;
    private int mStartX, mStartY;

    public VideoView(@NonNull Context context) {
        super(context);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.home_listvideo_layout_listvideo_controller, this, true);
        thumb = findViewById(R.id.iv_thumb);
        mPlayBtn = findViewById(R.id.play_btn);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mControlWrapper!=null){
                    mControlWrapper.togglePlay();
                }
            }
        });

        /**
         * 长按保存 倍数等
         */
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Singleton.INSTANCE.showSaveVideo(mControlWrapper.getUrl(), (Activity) getContext(),false,mControlWrapper);
                Singleton.INSTANCE.showLog(this,"图片地址------"+mControlWrapper.getUrl());
                return false;
            }
        });
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    long currentTimeMillis =0;
    /**
     * 解决点击和VerticalViewPager滑动冲突问题
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();

                //获取当前系统时间
                 currentTimeMillis = System.currentTimeMillis();

                return true;
            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                if (Math.abs(endX - mStartX) < mScaledTouchSlop && Math.abs(endY - mStartY) < mScaledTouchSlop) {
                    Long time = System.currentTimeMillis() - currentTimeMillis;
                    Singleton.INSTANCE.showLog(this,"时间==========="+time);
                    if (time <= 200){
                        performClick(); //点击播放暂停
                    }
                    if(time > 500){
                        performLongClick(); //弹窗倍数等
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {}

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case ConstantKeys.CurrentState.STATE_IDLE:
                VideoLogUtils.e("STATE_IDLE " + hashCode());
                thumb.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PLAYING:
                VideoLogUtils.e("STATE_PLAYING " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(GONE);
                //开始刷新进度
                mControlWrapper.startProgress();
                break;
            case ConstantKeys.CurrentState.STATE_PAUSED:
                VideoLogUtils.e("STATE_PAUSED " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PREPARED:
                VideoLogUtils.e("STATE_PREPARED " + hashCode());
                break;
            case ConstantKeys.CurrentState.STATE_ERROR:
                VideoLogUtils.e("STATE_ERROR " + hashCode());
                Toast.makeText(getContext(), "unknown err", Toast.LENGTH_SHORT).show();
                break;
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                if (mPbBottomProgress!=null){
                    mPbBottomProgress.setProgress(0);
                    mPbBottomProgress.setSecondaryProgress(0);
                }
                if (mSeekBar!=null){
                        mSeekBar.setProgress(0);
                        mSeekBar.setSecondaryProgress(0);
                    }
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {}

    /**
     * @param duration 视频总时长
     * @param position 视频当前时长
     */
    @Override
    public void setProgress(int duration, int position) {
        if (mIsDragging) {
            return;
        }

        if (mSeekBar != null) {
            if (duration > 0) {
                mSeekBar.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * mSeekBar.getMax());
                mSeekBar.setProgress(pos);
                mPbBottomProgress.setProgress(pos);
            } else {
                mSeekBar.setEnabled(false);
            }
            int percent = mControlWrapper.getBufferedPercentage();
            if (percent >= 95) {
                //解决缓冲进度不能100%问题
                mSeekBar.setSecondaryProgress(mSeekBar.getMax());
                mPbBottomProgress.setSecondaryProgress(mPbBottomProgress.getMax());
            } else {
                mSeekBar.setSecondaryProgress(percent * 10);
                mPbBottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (mTvTotalTime != null){
            mTvTotalTime.setText(PlayerUtils.formatTime(duration));
        }
        if (mTvCurrTime != null){
            mTvCurrTime.setText(PlayerUtils.formatTime(position));
        }
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {}
     void setFull(){
        mControlWrapper.startFullScreen();
    }

    //进度条相关
    private TextView mTvCurrTime;
    private SeekBar mSeekBar;
    private TextView mTvTotalTime;
    private ProgressBar mPbBottomProgress;
    private boolean mIsDragging;

    public ControlWrapper getControlWrapper() {
        return mControlWrapper;
    }

    public int getMax() {
        if (mPbBottomProgress == null) return 0;
        return mPbBottomProgress.getMax();
    }

    //设置这个
    public void setIsDragging(boolean mIsDragging) {
        this.mIsDragging = mIsDragging;
    }

    public void setInitView(SeekBar mSeekBar,
                           TextView mTvCurrTime,
                           TextView mTvTotalTime,
                           ProgressBar mPbBottomProgress) {
        this.mSeekBar = mSeekBar;
        this.mTvCurrTime = mTvCurrTime;
        this.mTvTotalTime = mTvTotalTime;
        this.mPbBottomProgress = mPbBottomProgress;
    }
}
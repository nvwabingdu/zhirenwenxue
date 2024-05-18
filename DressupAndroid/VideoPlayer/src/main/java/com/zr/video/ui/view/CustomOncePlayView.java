package com.zr.video.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.video.R;
import com.zr.video.bridge.ControlWrapper;
import com.zr.video.config.ConstantKeys;
import com.zr.video.tool.BaseToast;
import com.zr.video.tool.PlayerUtils;


/**
 * 即将开播视图
 */
public class CustomOncePlayView extends LinearLayout implements InterControlView {

    private Context mContext;
    private float mDownX;
    private float mDownY;
    private TextView mTvMessage;
    private TextView mTvRetry;
    private int playState;
    private ControlWrapper mControlWrapper;

    public CustomOncePlayView(Context context) {
        super(context);
        init(context);
    }

    public CustomOncePlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomOncePlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        this.mContext = context;
        setVisibility(GONE);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.custom_video_player_once_live, this, true);
        initFindViewById(view);
        initListener();
        setClickable(true);
    }

    private void initFindViewById(View view) {
        mTvMessage = view.findViewById(R.id.tv_message);
        mTvRetry = view.findViewById(R.id.tv_retry);
    }

    private void initListener() {
        mTvRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playState == ConstantKeys.CurrentState.STATE_ONCE_LIVE){
                    //即将开播
                    if (PlayerUtils.isConnected(mContext)){
                        mControlWrapper.start();
                    } else {
                        BaseToast.showRoundRectToast("请查看网络是否连接");
                    }
                } else {
                    BaseToast.showRoundRectToast("时间还未到，请稍后再试");
                }
            }
        });
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
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        this.playState = playState;
        if (playState == ConstantKeys.CurrentState.STATE_ONCE_LIVE) {
            //即将开播
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLock) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getX() - mDownX);
                float absDeltaY = Math.abs(ev.getY() - mDownY);
                if (absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public TextView getTvMessage() {
        return mTvMessage;
    }

}

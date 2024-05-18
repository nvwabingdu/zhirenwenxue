package com.zr.video.ui.pip;

import android.content.Context;
import android.view.View;

import com.zr.video.player.VideoPlayer;
import com.zr.video.player.VideoViewManager;
import com.zr.video.tool.PlayerUtils;


/**
 * 悬浮播放
 */
public class FloatVideoManager {

    //画中画
    public static final String PIP = "pip";
    private static FloatVideoManager instance;
    private VideoPlayer mVideoPlayer;
    private FloatVideoView mFloatView;
    private CustomFloatController mFloatController;
    private boolean mIsShowing;
    private int mPlayingPosition = -1;
    private Class mActClass;


    private FloatVideoManager(Context context) {
        mVideoPlayer = new VideoPlayer(context);
        VideoViewManager.instance().add(mVideoPlayer, PIP);
        mFloatController = new CustomFloatController(context);
        mFloatView = new FloatVideoView(context, 0, 0);
    }

    public static FloatVideoManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FloatVideoManager.class) {
                if (instance == null) {
                    instance = new FloatVideoManager(context);
                }
            }
        }
        return instance;
    }

    public void startFloatWindow() {
        if (mIsShowing) {
            return;
        }
        PlayerUtils.removeViewFormParent(mVideoPlayer);
        mVideoPlayer.setController(mFloatController);
        mFloatController.setPlayState(mVideoPlayer.getCurrentPlayState());
        mFloatController.setPlayerState(mVideoPlayer.getCurrentPlayerState());
        mFloatView.addView(mVideoPlayer);
        mFloatView.addToWindow();
        mIsShowing = true;
    }

    public void stopFloatWindow() {
        if (!mIsShowing) {
            return;
        }
        mFloatView.removeFromWindow();
        PlayerUtils.removeViewFormParent(mVideoPlayer);
        mIsShowing = false;
    }

    public void setPlayingPosition(int position) {
        this.mPlayingPosition = position;
    }

    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    public void pause() {
        if (mIsShowing) {
            return;
        }
        mVideoPlayer.pause();
    }

    public void resume() {
        if (mIsShowing) {
            return;
        }
        mVideoPlayer.resume();
    }

    public void reset() {
        if (mIsShowing){
            return;
        }
        PlayerUtils.removeViewFormParent(mVideoPlayer);
        mVideoPlayer.release();
        mVideoPlayer.setController(null);
        mPlayingPosition = -1;
        mActClass = null;
    }

    public boolean onBackPress() {
        return !mIsShowing && mVideoPlayer.onBackPressed();
    }

    public boolean isStartFloatWindow() {
        return mIsShowing;
    }

    /**
     * 显示悬浮窗
     */
    public void setFloatViewVisible() {
        if (mIsShowing) {
            mVideoPlayer.resume();
            mFloatView.setVisibility(View.VISIBLE);
        }
    }

    public void setActClass(Class cls) {
        this.mActClass = cls;
    }

    public Class getActClass() {
        return mActClass;
    }

}

package com.example.administrator.leehom.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;


import java.lang.ref.WeakReference;

import static com.example.administrator.leehom.service.MusicPlayService.LAST_MUSIC_DURATION;
import static com.example.administrator.leehom.service.MusicPlayService.LAST_MUSIC_POSITION;


/**
 * auther：wzy
 * date：2017/5/7 11 :53
 * desc: 用于展示音乐播放进度的seekbar
 * 功能有：
 * 1、sb能实时展示当前进度，能调节进度（提供一个进度手动改变的回调接口出来）
 * 2、sb能记录当前进度，下次进入时还在该进度，这需要media也记录上一次播放的曲目
 */

public class MusicSeekBar extends SeekBar {
    private static final String STATE_INSTANCE = "STATE_INSTANCE";
    /**
     * 当前播放位置 毫秒
     */
    private int mCuttentPosition;
    /**
     * 总播放长度 毫秒
     */
    private int mAllDuration;
    public static final String TAG = "MusicSeekBar";

    public MusicSeekBar(Context context) {
        super(context);
        init();
    }


    public MusicSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public MusicSeekBar setMusicProgressChangeListener(MusicProgressChangeListener musicProgressChangeListener) {
        mMusicProgressChangeListener = musicProgressChangeListener;
        return this;
    }

    public void reset() {
        mCuttentPosition = 0;
        mMusicHandler.removeCallbacksAndMessages(null);
        this.setProgress(mCuttentPosition);
    }

    private static class MusicHandler extends Handler {
        private WeakReference<MusicSeekBar> mMusicSeekBarWeakReference;

        private MusicHandler(MusicSeekBar musicSeekBar) {
            mMusicSeekBarWeakReference = new WeakReference<>(musicSeekBar);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mMusicSeekBarWeakReference == null) {
                return;
            }
            MusicSeekBar musicSeekBar = mMusicSeekBarWeakReference.get();
            if (musicSeekBar != null) {
                musicSeekBar.autoPlayAdd();
            }
        }
    }

    private static final int _1S = 1000;

    private void autoPlayAdd() {
        mCuttentPosition += _1S;
        play();
        Log.i(TAG, "autoPlayAdd mCuttentPosition:" + mCuttentPosition + ", mAllDuration:" + mAllDuration);
        if (mCuttentPosition < mAllDuration) {
            Message message = Message.obtain();
            mMusicHandler.sendMessageDelayed(message, _1S);
        } else {
            if (!Utils.checkNull(mMusicProgressChangeListener)) {
                mMusicProgressChangeListener.onProgressMoveEnd();
                Log.i(TAG, "current music play end");
            }
        }
    }

    private MusicHandler mMusicHandler = new MusicHandler(this);


    private void init() {
        this.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCuttentPosition = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!Utils.checkNull(mMusicProgressChangeListener)) {
                    mMusicProgressChangeListener.onProgressChangeOK(seekBar.getProgress());
                    Log.i(TAG, "from user move ok :" + seekBar.getProgress());
                }
            }
        });
    }


    public void pause() {
        mMusicHandler.removeCallbacksAndMessages(null);
    }

    public void continuePlay() {
        if (mIsDestory) {
            play();
        }
    }

    public MusicSeekBar play() {
        this.setProgress(mCuttentPosition);
        return this;
    }

    /**
     * 进度条开始
     *
     * @return
     */
    public MusicSeekBar start() {
        mMusicHandler.removeCallbacksAndMessages(null);
        Message message = Message.obtain();
        mMusicHandler.sendMessageDelayed(message, _1S);
        return this;
    }

    private boolean mIsDestory = false;

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean("mIsDestory", true);
        bundle.putInt("mCurrentPosition", mCuttentPosition);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.mIsDestory = bundle.getBoolean("mIsDestory");
            this.mCuttentPosition = bundle.getInt("mCurrentPosition");
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public int getCuttentPosition() {
        return mCuttentPosition;
    }

    public MusicSeekBar setCuttentPosition(int cuttentPosition) {
        mCuttentPosition = cuttentPosition;
        return this;
    }


    public MusicSeekBar setAllDuration(int allDuration) {
        mAllDuration = allDuration;
        this.setMax(allDuration);
        return this;
    }

    private MusicProgressChangeListener mMusicProgressChangeListener;

    public interface MusicProgressChangeListener {
        void onProgressChangeOK(int postion);
        void onProgressMoveEnd();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void onPause() {
        Log.i(TAG, "onPause");
        mMusicHandler.removeCallbacksAndMessages(null);
    }

    private int getLastMusicPositionFromSp() {
        return (int) Utils.SharedPreferencesUtils.getParam(getContext(), LAST_MUSIC_POSITION, 0);
    }

    private int getLastMusicDurationFromSp() {
        return (int) Utils.SharedPreferencesUtils.getParam(getContext(), LAST_MUSIC_DURATION, 0);
    }

}

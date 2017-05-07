package com.example.administrator.leehom.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.activity.MainActivity;
import com.example.administrator.leehom.model.AppContant;
import com.example.administrator.leehom.utils.MusicSeekBar;


/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc: 播放页面
 */

public class ThridFragment extends FragmentBase {
    public static final String TAG = "ThridFragment";
    private View pre_bt;
    private View next_bt;
    private TextView play_or_pause;
    private MusicSeekBar play_progress;
    private View musicBg;
    private ObjectAnimator mRotateAnimation;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            currentRotateValue = (float) animation.getAnimatedValue() % 360;
            Log.i(TAG, "mRotateAnimation animation :" + currentRotateValue);
        }
    };
    ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int currentPosition = getArguments().getInt(CURRENT_POSITION);
        int duration = getArguments().getInt(DURATION);
        View view = inflater.inflate(R.layout.fragment_thrid, container, false);
        pre_bt = view.findViewById(R.id.pre_bt);
        next_bt = view.findViewById(R.id.next_bt);
        play_or_pause = (TextView) view.findViewById(R.id.play_or_pause);
        play_progress = (MusicSeekBar) view.findViewById(R.id.play_progress);
        musicBg = view.findViewById(R.id.music_bg_container);
        /*
            音乐进度的显示
            1、用SeekBar显示，一下简称sb，
            2、sb能实时展示当前进度，能调节进度（提供一个进度手动改变的回调接口出来）
            3、sb能记录当前进度，下次进入时还在该进度，这需要media也记录上一次播放的曲目
         */
        play_progress = play_progress
                .setAllDuration(duration)
                .setCuttentPosition(currentPosition)
                .play()
                .start()
                .setMusicProgressChangeListener(new MusicSeekBar.MusicProgressChangeListener() {
                    @Override
                    public void onProgressChangeOK(int postion) {
                        // 手动调节
                        Log.i(TAG, "onProgressChangeOK:" + postion);
                        MainActivity activity = (MainActivity) getActivity();
                        activity.musicProgressMoveTo(postion);
                    }
                });
        play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayKey((TextView) v);
            }
        });

        initRotateAnimator();
        setPlayStateByMusicPlayMedia();
        return view;
    }

    private void initRotateAnimator() {
        mRotateAnimation = ObjectAnimator.
                ofFloat(musicBg, "rotation", currentRotateValue, currentRotateValue + 360);/*

        mRotateAnimation = ObjectAnimator.
                ofInt(musicBg, "Rotation", currentRotateValue - 360, currentRotateValue);*/
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        mRotateAnimation.setDuration(20000);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setInterpolator(lin);
        mRotateAnimation.addUpdateListener(mAnimatorUpdateListener);
    }

    private void clickPlayKey(TextView v) {
        int id = v.getId();
        if (id == R.id.play_or_pause) {
            MainActivity activity = (MainActivity) getActivity();
            int currentPlayState = activity.getCurrentPlayState();
            // 根据当前的播放状态来设置音乐播放的状态
            switch (currentPlayState) {
                case AppContant.PlayMessage.PLAY:
                    activity.pause();
                    break;
                case AppContant.PlayMessage.STOP:
                    // TODO 目前的场景只是再次进入的时候是停止状态，点击播放键，播放上次存储的音乐
                    activity.play(null);
                    break;
                case AppContant.PlayMessage.PAUSE:
                    activity.continuePlay();
                    break;
            }

            // 再根据音乐播放的状态来设置按键
            setPlayStateByMusicPlayMedia();
        }
    }

    private boolean mIsPlaying = false;
    private static final String CURRENT_POSITION = "currentPostion";
    private static final String DURATION = "durantion";

    private float currentRotateValue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ThridFragment getInstance(int currentPostion, int duration) {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_POSITION, currentPostion);
        bundle.putInt(DURATION, duration);
        ThridFragment thridFragment = new ThridFragment();
        thridFragment.setArguments(bundle);
        return thridFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "onHiddenChanged :" + hidden);
        if (hidden) {
            mRotateAnimation.cancel();
            mRotateAnimation.removeAllListeners();
            musicBg.clearAnimation();
        } else {
            Log.i(TAG, "onHiddenChanged");
            MainActivity activity = (MainActivity) getActivity();
            int currentPostion = activity.getcurrentPosition();
            int duration = activity.getDuration();
            Log.i(TAG, "onHiddenChanged  currentPostion:" + currentPostion + " ,duration :" + duration);
            if (currentPostion != -1 && duration != -1) {
                play_progress = play_progress
                        .setCuttentPosition(currentPostion)
                        .setAllDuration(duration)
                        .play()
                        .start();
            }
            setPlayStateByMusicPlayMedia();
        }
        super.onHiddenChanged(hidden);
    }

    private void setPlayStateByMusicPlayMedia() {
        MainActivity activity = (MainActivity) getActivity();
        int currentPlayState = activity.getCurrentPlayState();
        // 根据当前的播放状态来设置音乐播放的状态
        switch (currentPlayState) {
            case AppContant.PlayMessage.PLAY:
                startRotateAnimation();
                play_or_pause.setText("暂停");
                play_progress.start();
                play_progress.onPause();
                break;
            case AppContant.PlayMessage.STOP:
                // TODO 目前的场景只是再次进入的时候是停止状态，点击播放键，播放上次存储的音乐
                play_or_pause.setText("播放");
                play_progress.onPause();
                mRotateAnimation.removeAllListeners();
                mRotateAnimation.end();
                break;
            case AppContant.PlayMessage.PAUSE:
                play_or_pause.setText("播放");
                play_progress.onPause();
                mRotateAnimation.cancel();
                mRotateAnimation.removeAllListeners();
                musicBg.clearAnimation();
                break;
        }
    }

    private void startRotateAnimation() {
        mRotateAnimation.setFloatValues(currentRotateValue, currentRotateValue + 360);
        mRotateAnimation.addUpdateListener(mAnimatorUpdateListener);
        mRotateAnimation.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        play_progress.onPause();
    }

}

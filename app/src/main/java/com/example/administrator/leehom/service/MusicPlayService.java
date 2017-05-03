package com.example.administrator.leehom.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.leehom.model.AppContant;
import com.example.administrator.leehom.utils.Utils;


/**
 * auther：wzy
 * date：2017/5/2 23 :50
 * desc:音乐播放服务
 */

public class MusicPlayService extends Service {
    private MediaPlayer mMediaPlayer;

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setCurrentPlayStaus(int currentPlayStaus) {
        mCurrentPlayStaus = currentPlayStaus;
    }

    public void setOldUrl(String oldUrl) {
        mOldUrl = oldUrl;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    private String mUrl;
    private static final String TAG = "MusicPlayService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 第一次绑定服务的时候创建
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*int message = intent.getIntExtra(AppContant.StringFlag.PLAY_MESSAGE, AppContant.PlayMessage.ERROR);
        mUrl = intent.getStringExtra(AppContant.StringFlag.PLAY_URL);
        switch (message) {
            case AppContant.PlayMessage.PLAY:
                // 从0开始播放
                play();
                break;
            case AppContant.PlayMessage.PAUSE:
                pause();
                break;
            case AppContant.PlayMessage.STOP:
                stop();
                break;
            case AppContant.PlayMessage.ERROR:
                // TODO 处理异常情况
                error();
                break;

        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    private void error() {

    }

    private void stop() {
        if (Utils.checkNull(mMediaPlayer)) {
            Log.e(TAG, "stop  mMediaPlayer == null");
            return;
        }
        mMediaPlayer.stop();
        mCurrentPlayStaus = AppContant.PlayMessage.STOP;
        mCurrentPosition = 0;
    }

    private void pause() {
        if (Utils.checkNull(mMediaPlayer)) {
            Log.e(TAG, "pause  mMediaPlayer == null");
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mCurrentPlayStaus = AppContant.PlayMessage.PAUSE;
        }
    }

    /**
     * 当前播放状态默认停止
     */
    private int mCurrentPlayStaus = AppContant.PlayMessage.STOP;
    /**
     * 上一个播放的url音乐地址
     */
    private String mOldUrl;
    /**
     * 存储暂停后的播放进度
     */
    private int mCurrentPosition;

    private void play() {
        /*
         播放有多重场景，
         1、正在播放的状态下，点另一首歌播放，
         2、正在播放的状态下，再点当前歌曲播放，
         3、暂停状态下，再点当前歌曲，
         4、暂停状态下，点其他歌曲，
         5、TODO 记录当前播放的歌曲，和播放位置到sp，下次进入可以直接读取，
         6、停止状态下，点某个歌曲，
          */
        if (Utils.checkNull(mMediaPlayer) || Utils.checkNull(mUrl)) {
            Log.e(TAG, "mMediaPlayer == null || mUrl == null");
        }
        switch (mCurrentPlayStaus) {
            case AppContant.PlayMessage.PLAY:
                // 正在播放的状态下,同一首歌(以url是否相同来区分是否是同一首歌曲)
                if (TextUtils.equals(mOldUrl, mUrl)) {
                    // 同一首歌则暂停
                    pause();
                } else {
                    stop();
                    play(0);
                }
                break;
            case AppContant.PlayMessage.PAUSE:
                // 暂停状态下
                if (TextUtils.equals(mOldUrl, mUrl)) {
                    // 同一首歌则继续
                    // play(mCurrentPosition);
                    continuePlay();
                } else {
                    stop();
                    play(0);
                }
                break;
            case AppContant.PlayMessage.STOP:
                // 停止状态下,直接播放
                play(0);
                break;
        }

    }

    /**
     * 暂停后的继续播放
     */
    public void continuePlay() {
        if (Utils.checkNull(mMediaPlayer)) {
            Log.e(TAG, "continuePlay :  mMediaPlayer == null");
            return;
        }
        mMediaPlayer.start();
        mCurrentPlayStaus = AppContant.PlayMessage.PLAY;
    }

    public void play(int position) {
        if (Utils.checkNull(mMediaPlayer)) {
            Log.e(TAG, "play(int position) :  mMediaPlayer == null");
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepare();  //进行缓冲
            mMediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }

    private class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int mPosition;

        public PreparedListener(int position) {
            mPosition = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mCurrentPlayStaus = AppContant.PlayMessage.PLAY;
            mOldUrl = mUrl;
            if (mPosition > 0) {
                mp.seekTo(mPosition);
            }
        }
    }

    public static class MusicBind extends Binder implements IService {
        private MusicPlayService mService;

        public MusicBind(MusicPlayService service) {
            mService = service;
        }

        @Override
        public void musicPlay(String url) {
            if (Utils.checkNull(mService)) {
                return;
            }
            mService.setUrl(url);
            mService.play();
        }

        @Override
        public void musicPause() {
            if (Utils.checkNull(mService)) {
                return;
            }
            mService.pause();
        }

        @Override
        public void musicContinuePlay() {
            if (Utils.checkNull(mService)) {
                return;
            }
            mService.continuePlay();
        }

        @Override
        public void musicStop() {
            if (Utils.checkNull(mService)) {
                return;
            }
            mService.stop();
        }
    }
}


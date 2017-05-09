package com.example.administrator.leehom.service;

/**
 * auther：wzy
 * date：2017/5/4 00 :16
 * desc: service给activity使用的方法
 */

public interface IService {
    void musicPlay(String url);

    void musicPause();

    void musicContinuePlay();

    void musicStop();

    void musicProgressMoveTo(int position);

    int getcurrentPosition();

    int getDuration();

    void continuePlay();

    void pause();

    void pauseOrResumeMusic();

    int getCurrentPlayState();

    void currentMusicPlayOver();
}

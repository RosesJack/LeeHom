package com.example.administrator.leehom.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.leehom.LoadingView;
import com.example.administrator.leehom.R;
import com.example.administrator.leehom.fragment.MainFragment;
import com.example.administrator.leehom.fragment.SecondFragment;
import com.example.administrator.leehom.fragment.ThridFragment;
import com.example.administrator.leehom.service.IService;
import com.example.administrator.leehom.service.MusicPlayService;
import com.example.administrator.leehom.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.leehom.service.MusicPlayService.LAST_MUSIC_DURATION;
import static com.example.administrator.leehom.service.MusicPlayService.LAST_MUSIC_POSITION;


/**
 * TODO 在该页面后，点击该页面所属按钮不应再有动作
 */
public class MainActivity extends BaseActivity<LoadingView> implements View.OnClickListener {

    private TextView mFirst;
    private TextView mSecond;
    private TextView mThird;
    private final static String TAG = "MainActivity";
    private Context mContext = this;
    /**
     * 音乐播放的binder
     */
    private IService mMusicBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder = (IService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(TAG, "service connected failed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindMusicService();
        initMainView();
        initMainListener();
        if (mFirst != null) {
            mFirst.performClick();
        }
    }

    private boolean bindMusicService() {
        if (Utils.checkNull(mConnection) || Utils.checkNull(mContext)) {
            return true;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, MusicPlayService.class);
        mContext.bindService(intent, mConnection, BIND_AUTO_CREATE);
        return false;
    }

    private void initMainListener() {
        mFirst.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mThird.setOnClickListener(this);
    }

    private void initMainView() {
        mFirst = (TextView) findViewById(R.id.first);
        mSecond = (TextView) findViewById(R.id.second);
        mThird = (TextView) findViewById(R.id.third);
    }

    /**
     * 用于记录当前正在显示的Fragment
     */
    private Fragment mCurrentShowingFragment;

    private Map<String, Fragment> mAllFragments = new HashMap<>();

    @Override
    public void onClick(View v) {
        if (!Utils.checkNull(mCurrentClickView)) {
            mCurrentClickView.setEnabled(true);
        }
        switch (v.getId()) {
            case R.id.first:
                gotoFragment(MainFragment.FRAGMENT_TAG);
                break;
            case R.id.second:
                gotoFragment(ThridFragment.FRAGMENT_TAG);
                break;
            case R.id.third:
                gotoFragment(SecondFragment.FRAGMENT_TAG);
                break;
        }
        v.setEnabled(false);
        mCurrentClickView = v;
    }

    private View mCurrentClickView;

    public IService getMusicBinder() {
        return mMusicBinder;
    }

    public void play(String url) {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
            return;
        }
        mMusicBinder.musicPlay(url);
    }

    public void musicProgressMoveTo(int position) {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
            return;
        }
        mMusicBinder.musicProgressMoveTo(position);
    }

    public int getcurrentPosition() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
            return -1;
        }
        return mMusicBinder.getcurrentPosition();
    }

    public int getDuration() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
            return -1;
        }
        return mMusicBinder.getDuration();
    }

    public void pauseOrResumeMusic() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
        }
        mMusicBinder.pause();
    }

    public int getCurrentPlayState() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
        }
        return mMusicBinder.getCurrentPlayState();
    }

    public void pause() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
        }
        mMusicBinder.pause();
    }

    public void continuePlay() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
        }
        mMusicBinder.continuePlay();
    }


    private int getLastMusicPositionFromSp() {
        return (int) Utils.SharedPreferencesUtils.getParam(this, LAST_MUSIC_POSITION, 0);
    }

    private int getLastMusicDurationFromSp() {
        return (int) Utils.SharedPreferencesUtils.getParam(this, LAST_MUSIC_DURATION, 0);
    }

    public void currentMusicPlayOver() {
        if (Utils.checkNull(mMusicBinder)) {
            Log.e(TAG, "mMusicBinder is null");
        }
        mMusicBinder.currentMusicPlayOver();
    }


    /**
     * 1、当前展示的fragment与新加入的Fragment相同
     * 2、....不同
     * 3、当前fragment之前已经创建过 add and show and hide
     * 4、fragment没有创建过 show hide
     *
     * @param tag
     */
    public void gotoFragment(String tag) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            // fragment第一次创建
            if (TextUtils.equals(tag, MainFragment.FRAGMENT_TAG)) {
                fragment = MainFragment.getInstance();
            } else if (TextUtils.equals(tag, SecondFragment.FRAGMENT_TAG)) {
                fragment = SecondFragment.getInstance();
            } else if (TextUtils.equals(tag, ThridFragment.FRAGMENT_TAG)) {
                fragment = ThridFragment.getInstance();
            }
            if (fragment == mCurrentShowingFragment) {
                return;
            } else {
                if (fragment != null) {
                    if (mCurrentShowingFragment != null) {
                        fragmentTransaction.hide(mCurrentShowingFragment);
                    }
                    fragmentTransaction.add(R.id.fragment_container, fragment, tag);
                    fragmentTransaction.show(fragment);
                    mCurrentShowingFragment = fragment;
                }
            }
        } else {
            // 非第一创建
            if (fragment == mCurrentShowingFragment) {
                return;
            } else {
                if (mCurrentShowingFragment != null) {
                    fragmentTransaction.hide(mCurrentShowingFragment);
                    fragmentTransaction.show(fragment);
                    mCurrentShowingFragment = fragment;
                }
            }
        }
        fragmentTransaction.commit();
    }
}

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

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
public class MainActivity extends Activity implements View.OnClickListener {

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

    /**
     * 防止Fragment的生命周期重新走一遍
     * TODO 可能存在按下home进程进入后台再进入 内存中的fragment对象被清空 会有异常bug
     *
     * @param clazz
     * @param isNewStart
     */
    private void setFragment(Class clazz, boolean isNewStart) {
        Fragment fragment = createFragment(clazz, isNewStart);
        if (Utils.checkNull(fragment)) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String simpleName = clazz.getSimpleName();
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(simpleName);
        if (!Utils.checkNull(mCurrentShowingFragment)) {
            fragmentTransaction.hide(mCurrentShowingFragment);
        }
        if (Utils.checkNull(fragmentByTag)) {
            fragmentTransaction.add(R.id.fragment_container, fragment, simpleName);
            mCurrentShowingFragment = fragment;
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.show(fragmentByTag);
            mCurrentShowingFragment = fragmentByTag;
        }
        fragmentTransaction.commit();
    }

    private void setDefaultFragment() {
        setFragment(MainFragment.class, true);
    }

    private Map<String, Fragment> mAllFragments = new HashMap<>();

    /**
     * 创建Fragment
     *
     * @param fragment
     * @param isNewStart 是否是重新new的Fragment对象
     */
    private Fragment createFragment(Class fragment, boolean isNewStart) {
        // 重新开启fragment
        if (isNewStart) {
            if (fragment == MainFragment.class) {
                return MainFragment.getInstance();
            } else if (fragment == SecondFragment.class) {
                return SecondFragment.getInstance();

            } else if (fragment == ThridFragment.class) {
                return ThridFragment.getInstance(getLastMusicPositionFromSp(), getLastMusicDurationFromSp());
            }
        }
        // 沿用老的fragment
        String key = fragment.getSimpleName();
        if (mAllFragments.containsKey(key)) {
            return mAllFragments.get(key);
        } else {
            if (fragment == MainFragment.class) {
                return MainFragment.getInstance();
            } else if (fragment == SecondFragment.class) {
                return SecondFragment.getInstance();

            } else if (fragment == ThridFragment.class) {
                return ThridFragment.getInstance(getLastMusicPositionFromSp(), getLastMusicDurationFromSp());
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                setFragment(MainFragment.class, false);
                break;
            case R.id.second:
                setFragment(ThridFragment.class, false);
                break;
            case R.id.third:
                setFragment(SecondFragment.class, false);
                break;
        }
    }

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
}

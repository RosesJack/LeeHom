package com.example.administrator.leehom.activity;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
            try {
                Fragment newInstance = (Fragment) fragment.newInstance();
                mAllFragments.put(fragment.getSimpleName(), newInstance);
                return newInstance;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        // 沿用老的fragment
        String key = fragment.getSimpleName();
        if (mAllFragments.containsKey(key)) {
            return mAllFragments.get(key);
        } else {
            Fragment newInstance2 = null;
            try {
                newInstance2 = (Fragment) fragment.newInstance();
                mAllFragments.put(fragment.getSimpleName(), newInstance2);
                return newInstance2;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
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

}

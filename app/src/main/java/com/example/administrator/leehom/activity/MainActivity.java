package com.example.administrator.leehom.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.fragment.MainFragment;
import com.example.administrator.leehom.fragment.SecondFragment;
import com.example.administrator.leehom.fragment.ThridFragment;
import com.example.administrator.leehom.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mFirst;
    private TextView mSecond;
    private TextView mThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainView();
        initMainListener();
        if (mFirst != null) {
            mFirst.performClick();
        }
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

    private void setFragment(Class clazz, boolean isNewStart) {
        Fragment fragment = createFragment(clazz, isNewStart);
        if (Utils.checkNull(fragment)) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
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
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
                setFragment(SecondFragment.class, false);
                break;
            case R.id.third:
                setFragment(ThridFragment.class, false);
                break;
        }
    }
}

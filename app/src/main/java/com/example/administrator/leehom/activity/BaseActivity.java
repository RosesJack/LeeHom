package com.example.administrator.leehom.activity;

import android.app.Activity;

import com.example.administrator.leehom.LoadingView;

/**
 * auther：wzy
 * date：2017/6/10 22 :05
 * desc:
 */

public class BaseActivity<T> extends Activity {
    private T mT;

    public T getT() {
        return mT;
    }

    public void setT(T t) {
        mT = t;
    }
}

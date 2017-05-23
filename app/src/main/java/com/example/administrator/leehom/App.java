package com.example.administrator.leehom;

import android.app.Application;
import android.content.Context;

/**
 * auther：wzy
 * date：2017/5/15 22 :25
 * desc:
 */

public class App extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }
}

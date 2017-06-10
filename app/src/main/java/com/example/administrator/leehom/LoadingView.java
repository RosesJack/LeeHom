package com.example.administrator.leehom;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.administrator.leehom.activity.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * auther：wzy
 * date：2017/6/10 19 :39
 * desc:
 */

public class LoadingView {
    private static final String TAG = "LoadingView";
    private WeakReference<Context> mRef;
    private WindowManager mWindowManager;
    private View mErrorView;
    private WindowManager.LayoutParams mLayoutParams;

    private LoadingView(Context context) {
        mRef = new WeakReference<>(context);
        init();
    }

    private void init() {
        initView();
        initEvent();
    }

    private void initEvent() {

    }

    private View mRootView;

    private void initView() {
        if (mRef == null) {
            Log.w(TAG, "mRef is null");
            return;
        }
        Context context = mRef.get();
        if (context == null) {
            Log.w(TAG, "context is null");
            return;
        }
        mRootView = LayoutInflater.from(context).inflate(R.layout.loadding_view_page, null);
    }

    private ViewGroup mContainer;

    public static LoadingView build(BaseActivity<LoadingView> activity) {
        if (activity == null) {
            Log.w(TAG, "context is null");
            return null;
        }
        LoadingView t = activity.getT();
        if (t == null) {
            t = new LoadingView(activity);
            activity.setT(t);
        }
        return t;
    }


    public void showLoading() {
        if (mRef == null) {
            Log.w(TAG, "mRef is null");
            return;
        }
        Context context = mRef.get();
        if (context == null) {
            Log.w(TAG, "context is null");
            return;
        }
        if (mContainer == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            // 设置为无焦点状态
            // 没有边界
            // 半透明效果
            createWindowManagerLayoutparams();

            mWindowManager.addView
                    (mRootView,
                            mLayoutParams);
        } else {
            removeViewFromParent(mRootView);
            mContainer.addView(mRootView);
        }
    }

    private void removeViewFromParent(View view) {
        if (view == null) {
            Log.w(TAG, "view is null");
            return;
        }
        ViewParent parent = view.getParent();
        if (parent != null) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(mRootView);
        }
    }

    private void createWindowManagerLayoutparams() {
        mLayoutParams = new WindowManager.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        // 设置为无焦点状态
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, // 没有边界
                        // 半透明效果
                        PixelFormat.TRANSLUCENT);
    }

    private boolean isSetedContainer = false;

    public void hideLoading() {

        if (mRootView == null) {
            Log.w(TAG, "mRootView is null");
            return;
        }
        if (isSetedContainer) {
            if (mContainer == null) {
                Log.w(TAG, "mContainer is null");
                return;
            }
            mContainer.removeView(mRootView);
        } else {
            if (mWindowManager == null) {
                Log.w(TAG, "mWindowManager is null");
                return;
            }
            mWindowManager.removeView(mRootView);
        }
    }

    public LoadingView setContainer(ViewGroup container) {
        isSetedContainer = true;
        mContainer = container;
        return this;
    }

    public void showErrorView() {
        if (mRef == null) {
            Log.w(TAG, "mRef is null");
            return;
        }
        Context context = mRef.get();
        if (context == null) {
            Log.w(TAG, "context is null");
            return;
        }
        if (mRootView == null) {
            Log.w(TAG, "mRootView is null");
            return;
        }
        if (isSetedContainer) {
            if (mContainer == null) {
                Log.w(TAG, "mContainer is null");
                return;
            }
            mContainer.removeView(mRootView);
            mErrorView = LayoutInflater.from(context).inflate(R.layout.error_view_page, null);
            mContainer.addView(mErrorView);
        } else {
            if (mWindowManager == null) {
                Log.w(TAG, "mWindowManager is null");
                return;
            }
            mWindowManager.removeView(mRootView);
            mErrorView = LayoutInflater.from(context).inflate(R.layout.error_view_page, null);
            if (mLayoutParams == null) {
                createWindowManagerLayoutparams();
            }
            mWindowManager.addView(mErrorView, mLayoutParams);
        }
    }
}

package com.example.administrator.leehom.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.leehom.LoadingView;
import com.example.administrator.leehom.R;
import com.example.administrator.leehom.activity.BaseActivity;
import com.example.administrator.leehom.activity.MainActivity;
import com.example.administrator.leehom.db.dao.MusicDao;
import com.example.administrator.leehom.fragment.adapter.MainFragmentAdapter;
import com.example.administrator.leehom.fragment.adapter.RecyclerViewItemClickListener;
import com.example.administrator.leehom.fragment.adapter.RecyclerViewItemLongClickListener;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.service.IService;
import com.example.administrator.leehom.thread.ThreadPoolProxyFactory;
import com.example.administrator.leehom.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc:music list fragment
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainFragment extends FragmentBase {
    public static final String FRAGMENT_TAG = "MainFragment";
    private final static String TAG = "MainFragment";
    private RecyclerView mRecyclerView;
    private MainActivity mActivity;
    private List<MusicModel> mData;
    private MainFragmentAdapter mFragmentAdapter;
    private IService mMusicBinder;
    private WindowManager mWmManager;
    private RelativeLayout mRelativeLayout;
    private long mFirstTouchTime;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        LoadingView.build((BaseActivity<LoadingView>) this.getActivity())
                .setContainer(container)
                .showLoading();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.music_list);
        initData();
        initListener();
        return mView;
    }

    private void initListener() {
        if (Utils.checkNull(mFragmentAdapter)) {
            Log.e(TAG, "mRecyclerView == null");
            return;
        }
        mFragmentAdapter.setItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Object obj = view.getTag();
                if (!Utils.checkNull(obj) && obj instanceof MusicModel) {
                    MusicModel musicModel = (MusicModel) obj;
                    Log.i(TAG, "musicModel :" + musicModel);
                    if (!Utils.checkNull(mActivity)) {
                        mActivity.play(musicModel.getUrl());
                    }
                }
            }
        });

        // 长按事件
        mFragmentAdapter.setRecyclerViewItemLongClickListener(new RecyclerViewItemLongClickListener() {
            @Override
            public void onLongClick(View v) {
                Log.i(TAG, "onItemClick: ");
                // doLongClickEvent(v);
            }

            @Override
            public void onTouching(View v) {
                Log.i(TAG, "onTouching");
                doLongClickEvent(v);
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void afterTouch(View v) {
                if (mRelativeLayout != null) {
                    if (mWmManager == null) {
                        mWmManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
                    }
                    mRelativeLayout.clearAnimation();
                    if (mRelativeLayout.isAttachedToWindow()) {
                        mWmManager.removeView(mRelativeLayout);
                    }
                }
            }
        });
    }

    private void doLongClickEvent(View view) {
        if (view == null) {
            Log.w(TAG, "view is null");
            return;
        }
        Object obj = view.getTag();
        MusicModel musicModel = (MusicModel) obj;
        Log.i(TAG, "musicModel :" + musicModel);
        if (mActivity == null) {
            Log.e(TAG, "mActivity==null");
            return;
        }
        // 模糊展开
        blurOpenDetail(view, musicModel);
    }

    private void blurOpenDetail(View view, MusicModel musicModel) {
        int[] intXy = new int[2];
        view.getLocationOnScreen(intXy);
        int originWidth = view.getMeasuredWidth();
        Log.i(TAG, "getMeasuredWidth1: " + originWidth);
        ViewGroup.LayoutParams originWidthLayoutParams = view.getLayoutParams();
        originWidth = originWidthLayoutParams.width;

        RelativeLayout.LayoutParams viewLayoutLayoutParams =
                new RelativeLayout.LayoutParams(originWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewLayoutLayoutParams.leftMargin = intXy[0];
        viewLayoutLayoutParams.topMargin = intXy[1];
        final ViewGroup viewLayout = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.item_main, null);
        LinearLayout.LayoutParams imageLayoutParameter =
                new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int measuredHeight1 = viewLayout.getMeasuredHeight();
        TextView tv_name = (TextView) viewLayout.findViewById(R.id.music_name);
        TextView tv_owner = (TextView) viewLayout.findViewById(R.id.music_owner);
        tv_name.setText(musicModel.getTilte());
        tv_owner.setText(musicModel.getArtist());

        TextView tv1 = new TextView(view.getContext());
        tv1.setLayoutParams(imageLayoutParameter);
        tv1.setText(mActivity.getResources().getString(R.string.music_from, musicModel.getAlbum()));
        TextView tv2 = new TextView(view.getContext());
        tv2.setLayoutParams(imageLayoutParameter);
        tv2.setText(mActivity.getResources().getString(R.string.music_duration, musicModel.getDuration()));
        tv1.setTextColor(R.color.colorBlack);
        tv2.setTextColor(R.color.colorBlack);

        View rootView = view.getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        Bitmap bitmap = rootView.getDrawingCache();
        Bitmap blurBitmap = Utils.blurBitmap(bitmap);
        mRelativeLayout = new RelativeLayout(mActivity);
        mRelativeLayout.setBackground(null);
        Drawable drawable = new BitmapDrawable(blurBitmap);
        mRelativeLayout.addView(viewLayout, viewLayoutLayoutParams);

        viewLayout.addView(tv1);
        viewLayout.addView(tv2);
        viewLayout.measure(0, 0);
        int measuredHeight2 = viewLayout.getMeasuredHeight();

        Log.i(TAG, "measuredHeight2 :" + measuredHeight2);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(view.getMeasuredHeight(), measuredHeight2).setDuration(500);
        valueAnimator.start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = viewLayout.getLayoutParams();
                float height = (float) animation.getAnimatedValue();
                layoutParams.height = (int) height;
                viewLayout.setLayoutParams(layoutParams);
            }
        });

        mRelativeLayout.setBackground(drawable);
        ObjectAnimator.ofFloat(mRelativeLayout, "alpha", 0.1f, 1f).setDuration(500).start();
        mWmManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams =
                new WindowManager.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.TYPE_APPLICATION,
                                // 设置为无焦点状态
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, // 没有边界
                                // 半透明效果
                                PixelFormat.TRANSLUCENT);
        mWmManager.addView(mRelativeLayout, layoutParams);
        // 不设置以下属性，每次得到的bitmap都与第一次得到的相同
        rootView.setDrawingCacheEnabled(false);
    }

    private void initData() {
        if (Utils.checkNull(mRecyclerView) || Utils.checkNull(mActivity)) {
            return;
        }
        LinearLayoutManager layout = new LinearLayoutManager(mActivity);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);
        mFragmentAdapter = new MainFragmentAdapter(mData);
        mRecyclerView.setAdapter(mFragmentAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mFragmentAdapter, getActivity()));
        updateRecyclerView();
    }

    private static class SpaceItemDecoration  extends RecyclerView.ItemDecoration{
        private RecyclerView.Adapter mAdapter;
        private Context mContext;
        private SpaceItemDecoration(RecyclerView.Adapter adapter, Context context) {
            mAdapter = adapter;
            mContext = context;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mAdapter == null || mContext == null) {
                Log.e(TAG, "mAdapter==null or mContext == null");
                return;
            }
            int itemCount = mAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            Log.d(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);
            outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.ui_10_dp);
            outRect.right = outRect.left;
            if (pos == 0) {
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.ui_4_dp);
            } else {
                outRect.top = 0;
            }
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.ui_4_dp);
        }
    }


    private void updateRecyclerView() {
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(queryDbRunable);
    }

    // TODO 可能会造成内存泄漏
    private Runnable queryDbRunable = new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(3000);
            List<MusicModel> models = MusicDao.getInstance(mActivity).queryAll();
            Log.i(TAG, "query all :" + models);
            if (!Utils.checkNull(mDbHandler)) {
                Message message = Message.obtain();
                message.obj = models;
                message.what = DbHandler.QUERY_ALL;
                mDbHandler.sendMessage(message);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mMusicBinder = mActivity.getMusicBinder();
    }

    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().remove(queryDbRunable);
    }

    private DbHandler mDbHandler = new DbHandler(this);

    private static class DbHandler extends Handler {
        private WeakReference<MainFragment> mMainFragmentWf;
        public static final int QUERY_ALL = 1;

        public DbHandler(MainFragment fragment) {
            mMainFragmentWf = new WeakReference<MainFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_ALL:
                    if (!Utils.checkNull(mMainFragmentWf)) {
                        List<MusicModel> musicList = (List<MusicModel>) msg.obj;
                        MainFragment mainFragment = mMainFragmentWf.get();
                        if (mainFragment == null) {
                            Log.w(TAG,"mainFragment is null");
                            return;
                        }
                        Activity activity = mainFragment.getActivity();
                        LoadingView.build((BaseActivity<LoadingView>) activity).hideLoading();
                        mainFragment.refreshListUI(musicList);
                    }
                    break;
            }
        }
    }

    public void refreshListUI(List<MusicModel> modelList) {
        Log.i(TAG, "refreshListUI modelList :" + modelList);
        if (!Utils.checkNull(mFragmentAdapter)) {
            mFragmentAdapter.updataRefresh(modelList);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        } else {
            updateRecyclerView();
        }
    }

}

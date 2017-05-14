package com.example.administrator.leehom.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.activity.MainActivity;
import com.example.administrator.leehom.db.dao.MusicDao;
import com.example.administrator.leehom.fragment.adapter.MainFragmentAdapter;
import com.example.administrator.leehom.fragment.adapter.RecyclerViewItemClickListener;
import com.example.administrator.leehom.model.AppContant;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.service.IService;
import com.example.administrator.leehom.service.MusicPlayService;
import com.example.administrator.leehom.thread.ThreadPoolProxyFactory;
import com.example.administrator.leehom.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.os.Build.VERSION_CODES.M;
import static com.example.administrator.leehom.model.AppContant.StringFlag.PLAY_URL;

/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc:music list fragment
 */

public class MainFragment extends FragmentBase {
    public static final String FRAGMENT_TAG = "MainFragment";
    private final static String TAG = "MainFragment";
    private RecyclerView mRecyclerView;
    private MainActivity mActivity;
    private List<MusicModel> mData;
    private MainFragmentAdapter mFragmentAdapter;
    private IService mMusicBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.music_list);
        initData();
        initListener();
        return view;
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
                        mMainFragmentWf.get().refreshListUI(musicList);
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

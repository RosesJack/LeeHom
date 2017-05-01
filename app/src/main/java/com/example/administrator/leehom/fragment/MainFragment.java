package com.example.administrator.leehom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.administrator.leehom.db.dao.MusicDao;
import com.example.administrator.leehom.fragment.adapter.MainFragmentAdapter;
import com.example.administrator.leehom.fragment.adapter.RecyclerViewItemClickListener;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.thread.ThreadPoolProxyFactory;
import com.example.administrator.leehom.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc:
 */

public class MainFragment extends FragmentBase {
    private final static String TAG = "MainFragment";
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<MusicModel> mData;
    private MainFragmentAdapter mFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.music_list);
        mContext = container.getContext();
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
                    Toast.makeText(mContext, " " + musicModel, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "musicModel :" + musicModel);
                }
            }
        });
    }

    private void initData() {
        if (Utils.checkNull(mRecyclerView) || Utils.checkNull(mContext)) {
            return;
        }
        LinearLayoutManager layout = new LinearLayoutManager(mContext);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);
        mFragmentAdapter = new MainFragmentAdapter(mData);
        mRecyclerView.setAdapter(mFragmentAdapter);
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(queryDbRunable);
    }

    // TODO 可能会造成内存泄漏
    private Runnable queryDbRunable = new Runnable() {
        @Override
        public void run() {
            List<MusicModel> models = MusicDao.getInstance(mContext).queryAll();
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
}

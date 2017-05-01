package com.example.administrator.leehom.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.engine.LocalMusicSearcher;
import com.example.administrator.leehom.engine.SearchListener;
import com.example.administrator.leehom.utils.Utils;

import java.lang.ref.WeakReference;

import static android.R.attr.path;

/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc:
 */

public class SecondFragment extends FragmentBase implements View.OnClickListener {
    private Button mSearchButton;
    private TextView mShowTextView;
    private Context mContext;
    private static final String TAG = "SecondFragment";
    private LocalMusicSearcher mLocalMusicSearcher;
    private final SearcherHandler searcherHandler = new SearcherHandler(this, Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sec, container, false);
        mContext = container.getContext();
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        mSearchButton.setOnClickListener(this);
    }

    private void initView(View view) {
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mShowTextView = (TextView) view.findViewById(R.id.show_tv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SecondFragment getInstance() {
        return new SecondFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                doLocalSearch();
                v.setClickable(false);
                break;
        }
    }

    private void doLocalSearch() {
        if (Utils.checkNull(mContext)) {
            Log.e(TAG, "mContext == null");
            return;
        }
        mLocalMusicSearcher = new LocalMusicSearcher();

        mLocalMusicSearcher.search(mContext, new SearchListener() {
            @Override
            public void start() {
                Log.i(TAG, "start");
            }

            @Override
            public void searching(String path) {
                Message message = Message.obtain();
                message.obj = path;
                message.what = SearcherHandler.MUSIC_SEARCHING;
                searcherHandler.sendMessage(message);
                Log.i(TAG, "searching :" + path);
            }

            @Override
            public void stop() {
                Message message = Message.obtain();
                message.obj = path;
                message.what = SearcherHandler.MUSIC_SEARCHING_STOP;
                searcherHandler.sendMessage(message);
                Log.i(TAG, "stop ");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (!Utils.checkNull(mLocalMusicSearcher)) {
            mLocalMusicSearcher.stop();
            Log.i(TAG, "search stop");
        }
    }

    private static class SearcherHandler extends Handler {
        private WeakReference<SecondFragment> mSecondFragmentR;
        public static final int MUSIC_SEARCHING = 1;
        public static final int MUSIC_SEARCHING_STOP = 2;

        public SearcherHandler(SecondFragment secondFragment, Looper looper) {
            super(looper);
            mSecondFragmentR = new WeakReference<SecondFragment>(secondFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MUSIC_SEARCHING:
                    String url = (String) msg.obj;
                    mSecondFragmentR.get().showSearching(url);
                    break;
                case MUSIC_SEARCHING_STOP:
                    mSecondFragmentR.get().searchOver();
                    break;
            }
        }
    }

    public void showSearching(final String url) {
        Log.i("wzy", "showSearching() :" + url);
        if (!Utils.checkNull(mShowTextView)) {
            mShowTextView.setText(url);
            Log.i("wzy", "mShowTextView.setText:" + url);
        }
    }

    public void searchOver() {
        if (!Utils.checkNull(mSearchButton) && !Utils.checkNull(mShowTextView)) {
            mSearchButton.setClickable(true);
            mShowTextView.setText("查询结束");
        }
    }
}

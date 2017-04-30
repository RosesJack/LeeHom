package com.example.administrator.leehom.engine;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.example.administrator.leehom.db.dao.MusicDao;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.thread.ThreadPoolProxyFactory;
import com.example.administrator.leehom.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * auther：wzy
 * date：2017/4/30 18 :18
 * desc: search music in sdcard
 */

public class LocalMusicSearcher {
    private SearchListener mListener;
    private Context mContext;
    private final static String TAG = "LocalMusicSearcher";

    public void search(Context context, SearchListener listener) {
        mListener = listener;
        mContext = context;
        ThreadPoolProxyFactory.getDbThreadPoolProxy().execute(searcheRunable);
    }

    private Runnable searcheRunable = new Runnable() {
        @Override
        public void run() {
            if (!Utils.checkNull(mListener)) {
                mListener.start();
            }
            doLocalSearch();
        }
    };

    private void doLocalSearch() {
        File rootDir = Environment.getExternalStorageDirectory();
        MusicDao.getInstance(mContext).deleteAll();
        getPath(rootDir);
        saveToDb(musicPaths);
        if (!Utils.checkNull(mListener)) {
            mListener.stop();
        }
    }

    private List<String> musicPaths = new ArrayList<>();

    public void getPath(File rootFile) {
        // 扫描太快的话，TextView内容更新速度跟不上 空100毫秒用于TextView绘制
        SystemClock.sleep(50);
        // Log.i("wzy", "getPath() :" + rootFile != null ? rootFile.getAbsolutePath() : null);
        if (mListener != null) {
            mListener.searching(rootFile != null ? rootFile.getAbsolutePath() : null);
        }
        if (Utils.checkNull(rootFile)) {
            Log.e(TAG, "rootFile is null");
            return;
        }

        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            if (Utils.checkNull(files)) {
                return;
            }
            for (File file : files) {
                getPath(file);
            }
        } else {
            String name = rootFile.getName();
            if (!Utils.checkNull(name)) {
                if (name.contains(".mp3") ||
                        name.contains(".wma") ||
                        name.contains(".wav")) {
                    // 目前根据文件大小来判断 < 1m的音乐文件不加入
                    long length = rootFile.length();
                    if (length > 1024) {
                        // musicPaths.add(rootFile.getAbsolutePath());
                        saveToDb(rootFile.getAbsolutePath());
                    }
                }
            }

        }
    }

    private void saveToDb(List<String> paths) {
        if (Utils.checkNull(mContext)) {
            Log.e(TAG, "mContext == null");
            return;
        }
        // TODO 数据库现在虽然没有owner内容，但后期可能会加上
        long insert = MusicDao.getInstance(mContext).deleteAllAndInsertNew(paths);
        if (insert <= 0) {
            Log.e(TAG, "saveToDb insert fail");
        }
    }

    private void saveToDb(String path) {
        if (Utils.checkNull(mContext)) {
            Log.e(TAG, "mContext == null");
            return;
        }
        Log.i("wzy", "saveToDb :" + path);
        long insert = MusicDao.getInstance(mContext).insert(path);
        if (insert <= 0) {
            Log.e(TAG, "saveToDb insert fail");
        }
    }

    public void stop() {
        ThreadPoolProxyFactory.getDbThreadPoolProxy().remove(searcheRunable);
        searcheRunable = null;
    }
}

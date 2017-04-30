package com.example.administrator.leehom.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.leehom.db.MusicDbHelp;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.utils.Utils;

import java.util.List;


/**
 * auther：wzy
 * date：2017/4/30 13 :57
 * desc:
 */

public class MusicDao implements DbDao<MusicModel> {
    private static MusicDao mMusicDao;
    private static final String TAG = "MusicDao";
    private MusicDbHelp mMusicDbHelp;

    private MusicDao(Context context) {
        mMusicDbHelp = new MusicDbHelp(context);
    }


    public static MusicDao getInstance(Context context) {
        if (mMusicDao == null) {
            synchronized (MusicDao.class) {
                if (mMusicDao == null) {
                    mMusicDao = new MusicDao(context);
                }
            }
        }
        return mMusicDao;
    }

    @Override
    public List<MusicModel> queryAll() {
        if (Utils.checkNull(mMusicDbHelp)) {
            Log.e(TAG, "mMusicDbHelp == null");
            return null;
        }
        Cursor cursor = mMusicDbHelp.query(MusicDbHelp.TABLE_NAME, null, null, null, null, null, null, null);
        return MusicModel.fromCusor(cursor);
    }

    @Override
    public long insert(List<MusicModel> list) {
        if (Utils.checkNull(list) || Utils.checkNull(mMusicDbHelp)) {
            return -1;
        }
        long insertNum = 0;
        SQLiteDatabase writableDatabase = mMusicDbHelp.getWritableDatabase();
        try {
            writableDatabase.beginTransaction();
            // 事务批量插入
            for (MusicModel musicModel : list) {
                ContentValues values = musicModel.toContentValue();
                insertNum = writableDatabase.insert(MusicDbHelp.TABLE_NAME, null, values);
                Log.i("wzy", "values :" + values);
                Log.i("wzy", "insertNum :" + insertNum);
            }
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
        return insertNum;
    }


    @Override
    public int delete(List<MusicModel> list) {
        if (Utils.checkNull(mMusicDbHelp) || Utils.checkNull(list)) {
            return -1;
        }
        int deleteNum = 0;
        SQLiteDatabase writableDatabase = mMusicDbHelp.getWritableDatabase();
        try {

            writableDatabase.beginTransaction();
            for (MusicModel musicModel : list) {
                String name = musicModel.getName();
                String url = musicModel.getUrl();
                if (!Utils.checkNull(name)) {
                    deleteNum = writableDatabase.delete(MusicDbHelp.TABLE_NAME, "name=?", new String[]{name});
                    continue;
                }
                if (!Utils.checkNull(url)) {
                    deleteNum = writableDatabase.delete(MusicDbHelp.TABLE_NAME, "url=?", new String[]{url});
                    continue;
                }
                Log.e(TAG, "delete fail model id = " + musicModel.getId());
            }
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }

        writableDatabase.close();
        mMusicDbHelp.close();
        return deleteNum;
    }

    @Override
    public int update() {
        return 0;
    }
}

package com.example.administrator.leehom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * auther：wzy
 * date：2017/4/30 13 :49
 * desc:
 */

public class MusicDbHelp extends SQLiteOpenHelper {

    /**
        tilte
        album
        artist
        url
        duration
        size
     */
    public static final String TABLE_NAME = "music_list";
    private final static String CREATE_DB_SQL =
            "create table " + TABLE_NAME + "" +
                    " (_id integer primary key autoincrement" +
                    ",tilte varchar(100)" +
                    ",album varchar(100)" +
                    ",artist varchar(100)" +
                    ",url varchar(255)" +
                    ",duration varchar(100)" +
                    ",size bigint " +
                    // 插入时间戳
                    ",time TimeStamp NOT NULL DEFAULT CURRENT_TIMESTAMP " +
                    ")";

    public MusicDbHelp(Context context) {
        super(context, "LeeHom", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query
            (String tableName, String[] clumns, String selection, String[] selectionArgs,
             String groupBy, String having, String orderBy, String limit) {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        return readableDatabase.query(tableName, clumns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public long insert(String tableName, String nullColumnHack, ContentValues values) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.insert(tableName, nullColumnHack, values);
    }

    public void delete(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        writableDatabase.delete(tableName, whereClause, whereArgs);
    }
}

package com.example.administrator.leehom.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 13 :46
 * desc:
 */

public class MusicModel {
    private int id;
    private String name;
    private String url;
    private String time;

    @Override
    public String toString() {
        return "MusicModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<MusicModel> fromCusor(Cursor cursor) {
        List<MusicModel> models = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            MusicModel musicModel = new MusicModel();
            musicModel.setName(name);
            musicModel.setUrl(url);
            musicModel.setId(id);
            musicModel.setTime(time);
            models.add(musicModel);
        }
        return models;
    }

    public ContentValues toContentValue() {
        ContentValues values = new ContentValues();
        String name = getUrl();
        int index = name.lastIndexOf("/");
        name = name.substring(index + 1, name.length());
        values.put("name", name);
        values.put("url", this.getUrl());
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

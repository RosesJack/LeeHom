package com.example.administrator.leehom.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;

/**
 * auther：wzy
 * date：2017/4/30 13 :46
 * desc:
 */

public class MusicModel {
    private int id;
    private String time;
    private String tilte;
    private String album;
    private String artist;
    private String url;
    private int duration;
    private long size;

    @Override
    public String toString() {
        return "MusicModel{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", tilte='" + tilte + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
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

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<MusicModel> fromCusor(Cursor cursor) {
        List<MusicModel> models = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("tilte"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String album = cursor.getString(cursor.getColumnIndex("album"));
            String artist = cursor.getString(cursor.getColumnIndex("artist"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            long size = cursor.getLong(cursor.getColumnIndex("size"));

            MusicModel musicModel = new MusicModel();
            musicModel.size = size;
            musicModel.url = url;
            musicModel.duration = duration;
            musicModel.time = time;
            musicModel.tilte = title;
            musicModel.artist = artist;
            musicModel.album = album;
            musicModel.id = id;

            models.add(musicModel);
        }
        return models;
    }

    public ContentValues toContentValue() {
        ContentValues values = new ContentValues();
        values.put("tilte", tilte);
        values.put("album", album);
        values.put("artist", artist);
        values.put("url", url);
        values.put("duration", duration);
        values.put("size", size);
        return values;
    }

}

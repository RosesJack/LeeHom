package com.example.administrator.leehom.db.dao;

import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 13 :58
 * desc:
 */

public interface DbDao<T> {
    public List<T> queryAll();

    public long insert(List<T> list);

    public int delete(List<T> list);

    public int update();
}

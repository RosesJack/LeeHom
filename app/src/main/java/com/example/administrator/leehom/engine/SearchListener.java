package com.example.administrator.leehom.engine;

/**
 * auther：wzy
 * date：2017/4/30 18 :40
 * desc:
 */

public interface SearchListener {
    void start();

    void searching(String path);

    void stop();
}

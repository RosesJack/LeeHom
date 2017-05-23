package com.example.administrator.leehom.fragment.adapter;

import android.view.View;

/**
 * auther：wzy
 * date：2017/5/15 22 :12
 * desc:
 */

public interface RecyclerViewItemLongClickListener {
    void onLongClick(View v);

    void onTouching(View v);

    void afterTouch(View v);
}

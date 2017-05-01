package com.example.administrator.leehom.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 16 :07
 * desc:
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bindView(T t) {
        itemView.setTag(t);
    }
}

package com.example.administrator.leehom.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.administrator.leehom.fragment.viewholder.BaseViewHolder;

import java.util.List;

/**
 * auther：wzy
 * date：2017/4/30 16 :07
 * desc:
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> mData;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void updataRefresh(List<T> list) {
        mData = list;
        notifyDataSetChanged();
    }
}

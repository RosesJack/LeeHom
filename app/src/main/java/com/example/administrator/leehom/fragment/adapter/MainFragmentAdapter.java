package com.example.administrator.leehom.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.fragment.viewholder.BaseViewHolder;
import com.example.administrator.leehom.fragment.viewholder.MainViewHolder;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.utils.Utils;

import java.util.List;


/**
 * auther：wzy
 * date：2017/4/30 16 :06
 * desc:
 */

public class MainFragmentAdapter extends BaseAdapter<MusicModel> {
    private final static String TAG = "MainFragmentAdapter";
    private RecyclerViewItemClickListener mItemClickListener;

    public MainFragmentAdapter(List<MusicModel> modelList) {
        mData = modelList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater f = LayoutInflater.from(parent.getContext()).cloneInContext(parent.getContext());
        View itemView = f.inflate(R.layout.item_main, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.checkNull(mItemClickListener)) {
                    mItemClickListener.onItemClick(v);
                }
            }
        });
        MainViewHolder mainViewHolder = new MainViewHolder(itemView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (Utils.checkNull(mData)) {
            Log.e(TAG, "mData == null");
            return;
        }
        if (holder instanceof MainViewHolder) {
            MainViewHolder mainViewHolder = (MainViewHolder) holder;
            MusicModel musicModel = mData.get(position);
            if (Utils.checkNull(musicModel)) {
                Log.e(TAG, "musicModel == null");
                return;
            }
            holder.bindView(musicModel);
            mainViewHolder.mNameTextView.setText(musicModel.getTilte());
            // TODO 作者信息需要从文件的exif中提取
            mainViewHolder.mOwnerTextView.setText(musicModel.getArtist());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public List<MusicModel> getData() {
        return mData;
    }

    public void setData(List<MusicModel> data) {
        mData = data;
    }

    public void setItemClickListener(RecyclerViewItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}

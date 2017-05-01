package com.example.administrator.leehom.fragment.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.administrator.leehom.R;
import com.example.administrator.leehom.model.MusicModel;

/**
 * auther：wzy
 * date：2017/4/30 16 :18
 * desc:
 */

public class MainViewHolder extends BaseViewHolder<MusicModel> {
    public TextView mNameTextView;
    public TextView mOwnerTextView;

    public MainViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.music_name);
        mOwnerTextView = (TextView) itemView.findViewById(R.id.music_owner);
    }
}

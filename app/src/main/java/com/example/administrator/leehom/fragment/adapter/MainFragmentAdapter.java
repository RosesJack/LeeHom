package com.example.administrator.leehom.fragment.adapter;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.leehom.App;
import com.example.administrator.leehom.R;
import com.example.administrator.leehom.fragment.MainFragment;
import com.example.administrator.leehom.fragment.viewholder.BaseViewHolder;
import com.example.administrator.leehom.fragment.viewholder.MainViewHolder;
import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.message;


/**
 * auther：wzy
 * date：2017/4/30 16 :06
 * desc:
 */

public class MainFragmentAdapter extends BaseAdapter<MusicModel> {
    private final static String TAG = "MainFragmentAdapter";
    private RecyclerViewItemClickListener mItemClickListener;
    private long mFirstTouchTime;

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
                if (!Utils.checkNull(mItemClickListener) && !isLongClickSoDonotClick) {
                    mItemClickListener.onItemClick(v);
                }
            }
        });
        /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!Utils.checkNull(mRecyclerViewItemLongClickListener)) {
                    mRecyclerViewItemLongClickListener.onLongClick(v);
                }
                return true;
            }
        });*/
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "event :" + event.getAction());
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mCurrentClickView = v;
                        Message message = Message.obtain();
                        message.what = TimeHandler.TIME_EMPTY;
                        message.obj =v;
                        mTimeHandler.sendMessageDelayed(message, 800);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mRecyclerViewItemLongClickListener != null) {
                            mRecyclerViewItemLongClickListener.afterTouch(v);
                        }
                        mTimeHandler.removeMessages(TimeHandler.TIME_EMPTY);
                        if (isLongClickSoDonotClick) {
                            isLongClickSoDonotClick = false;
                        }
                        mCurrentClickView = null;
                        break;
                }
                return false;
            }
        });
        MainViewHolder mainViewHolder = new MainViewHolder(itemView);
        return mainViewHolder;
    }
private View mCurrentClickView;
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

    private boolean isLongClickSoDonotClick = false;

    private TimeHandler mTimeHandler = new TimeHandler(this);

    private class TimeHandler extends Handler {
        private WeakReference<MainFragmentAdapter> mRef;
        public static final int TIME_EMPTY = 1;

        private TimeHandler(MainFragmentAdapter adapter) {
            this.mRef = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIME_EMPTY:
                    Log.i(TAG, "time empty");
                    MainFragmentAdapter mainFragmentAdapter = mRef.get();
                    if (mRecyclerViewItemLongClickListener != null) {
                        Log.i(TAG, " set mRecyclerViewItemLongClickListener");
                        mRecyclerViewItemLongClickListener.onTouching(mCurrentClickView);
                        isLongClickSoDonotClick = true;
                    }
                    break;
            }
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
    private RecyclerViewItemLongClickListener mRecyclerViewItemLongClickListener;

    public void setRecyclerViewItemLongClickListener(RecyclerViewItemLongClickListener recyclerViewItemLongClickListener) {
        mRecyclerViewItemLongClickListener = recyclerViewItemLongClickListener;
    }
}

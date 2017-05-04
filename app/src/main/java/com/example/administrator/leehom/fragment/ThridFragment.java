package com.example.administrator.leehom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.leehom.R;

/**
 * auther：wzy
 * date：2017/4/30 01 :05
 * desc:
 */

public class ThridFragment extends FragmentBase {
    public static final String TAG = "ThridFragment";
    private View pre_bt;
    private View next_bt;
    private View play_or_pause;
    private ProgressBar play_progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thrid, container, false);
        pre_bt = view.findViewById(R.id.pre_bt);
        next_bt = view.findViewById(R.id.next_bt);
        play_or_pause = view.findViewById(R.id.play_or_pause);
        play_progress = (ProgressBar) view.findViewById(R.id.play_progress);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ThridFragment getInstance() {
        return new ThridFragment();
    }
}

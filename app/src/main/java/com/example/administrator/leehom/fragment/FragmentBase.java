package com.example.administrator.leehom.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.leehom.model.MusicModel;
import com.example.administrator.leehom.utils.Utils;

import java.util.List;


/**
 * auther：wzy
 * date：2017/4/30 01 :28
 * desc:
 */

public class FragmentBase extends Fragment {
    private final static String TAG = "FragmentBase";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

}

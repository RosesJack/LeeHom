package com.example.administrator.leehom.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;

import com.example.administrator.leehom.R;

import java.util.ArrayList;

/**
 * Created by 31844 on 2018/1/23.
 */

public class PermissionActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 0x01;
    private static final String TAG = "PermissionActivity";
    private String[] mPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = this.getIntent();
        mPermissions = intent.getStringArrayExtra("permission_strings");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
        } else {
            Log.i(TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.M");
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult :" + permissions + "  requestCode:" + requestCode);
        if (requestCode == PERMISSION_REQUEST_CODE) {

        }
        finish();
    }
}

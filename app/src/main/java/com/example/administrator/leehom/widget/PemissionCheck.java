package com.example.administrator.leehom.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.example.administrator.leehom.model.AppContant;

/**
 * Created by 31844 on 2018/1/23.
 */

public class PemissionCheck {
    private static final String TAG = "PemissionCheck";

    public static boolean checkPermission(String[] permissions) {
        return false;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean permissionResult = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int resultCode = context.checkSelfPermission(permission);
            permissionResult = resultCode == PackageManager.PERMISSION_GRANTED;
            if (!permissionResult) {
                Intent intent = new Intent(AppContant.StringFlag.PERMISSION_ACTION);
                intent.putExtra(AppContant.StringFlag.PERMISSION_FLAG, new String[]{
                        permission
                });
                context.startActivity(intent);
            }
        }
        Log.i(TAG,"checkPermission :" + permissionResult);
        return permissionResult;
    }
}

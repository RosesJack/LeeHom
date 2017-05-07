package com.example.administrator.leehom.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.id.list;

/**
 * auther：wzy
 * date：2017/4/30 01 :40
 * desc:
 */

public class Utils {
    public static boolean checkNull(Object obj) {
        return obj == null;
    }

    public static boolean checkNull(File file) {
        return file == null || !file.exists() || file.length() <= 0;
    }

    public static boolean checkNull(Set set) {
        if (set == null || set.size() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean checkNull(Map map) {
        if (map == null || map.size() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean checkNull(List list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean checkNull(Object[] objs) {
        if (objs == null || objs.length <= 0) {
            return true;
        }
        return false;
    }

    public static boolean checkNull(String strs) {
        return TextUtils.isEmpty(strs);
    }


    public static class SharedPreferencesUtils {
        /**
         * 保存在手机里面的文件名
         */
        private static final String FILE_NAME = "share_date";


        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         *
         * @param context
         * @param key
         * @param object
         */
        public static void setParam(Context context, String key, Object object) {

            String type = object.getClass().getSimpleName();
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) object);
            }

            editor.commit();
        }


        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param context
         * @param key
         * @param defaultObject
         * @return
         */

        public static Object getParam(Context context, String key, Object defaultObject) {
            String type = defaultObject.getClass().getSimpleName();
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if ("String".equals(type)) {
                return sp.getString(key, (String) defaultObject);
            } else if ("Integer".equals(type)) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if ("Boolean".equals(type)) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if ("Float".equals(type)) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if ("Long".equals(type)) {
                return sp.getLong(key, (Long) defaultObject);
            }

            return null;
        }
    }
}

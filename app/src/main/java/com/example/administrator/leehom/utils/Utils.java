package com.example.administrator.leehom.utils;

import android.text.TextUtils;

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
}

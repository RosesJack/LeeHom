package com.example.administrator.leehom.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.example.administrator.leehom.App;

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap) {
        //先将图片缩小
        bitmap = fitBitmap(bitmap, bitmap.getWidth() / 3);
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(App.getContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(25.0f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        //将图片放大
        return fitBitmap(outBitmap, bitmap.getWidth() * 3);
    }

    /**
     * 尺寸压缩，在内存中的大小变化
     *
     * @param target
     * @param newWidth
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        return bmp;
    }
}

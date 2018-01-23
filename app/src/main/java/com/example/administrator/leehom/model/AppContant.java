package com.example.administrator.leehom.model;

/**
 * auther：wzy
 * date：2017/5/2 23 :55
 * desc: 常量
 */

public class AppContant {
    /**
     * 指明音乐播放状态
     */
    public static class PlayMessage {
        /**
         * 异常code
         */
        public static final int ERROR = -1;
        /**
         * 播放
         */
        public static final int PLAY = 0;
        /**
         * 暂停
         */
        public static final int PAUSE = 1;
        /**
         * 停止
         */
        public static final int STOP = 2;
    }

    /**
     * 常用字符串标记
     */
    public static class StringFlag {
        /**
         * 用于intent的IntExtra
         */
        public static final String PLAY_MESSAGE = "message";
        /**
         * 用于intent的StringExtra
         */
        public static final String PLAY_URL = "url";
        /**
         * 用于intent的StringExtra（权限）
         */
        public static final String PERMISSION_FLAG = "permission_strings";

        public static final String PERMISSION_ACTION = "android.intent.action.check_permission";
    }

    public static class PermissionStr{
        public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    }
}

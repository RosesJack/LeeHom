package com.example.administrator.leehom.thread;

/**
 * Created by Administrator on 2016/10/14.
 */
public class ThreadPoolProxyFactory {
    /**
     * 普通线程池
     */
    static ThreadPoolProxy mNormalThreadPoolProxy;
    /**
     * 下载线程
     */
    static ThreadPoolProxy mDownLoadThreadPoolProxy;

    public static ThreadPoolProxy getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(1, 1);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    /**
     * 单线程线程池用于操作数据库 防止并发
     *
     * @return
     */
    public static ThreadPoolProxy getDbThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(1, 1);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    public static ThreadPoolProxy getDownLoadThreadPoolProxy() {
        if (mDownLoadThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mDownLoadThreadPoolProxy == null) {
                    mDownLoadThreadPoolProxy = new ThreadPoolProxy(3, 3);
                }
            }
        }
        return mDownLoadThreadPoolProxy;
    }
}

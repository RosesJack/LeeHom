package com.example.administrator.leehom.thread;

import android.renderscript.ScriptGroup;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/10/14.
 */
public class ThreadPoolProxy {

    private ThreadPoolExecutor mThreadPoolExecutor;
    /**
     * 核心线程池大小
     */
    private int mCorePoolSize;
    /**
     *
     */
    private int mMaximumPoolSize;

    public ThreadPoolProxy(int mCorePoolSize, int mMaximumPoolSize) {
        this.mMaximumPoolSize = mMaximumPoolSize;
        this.mCorePoolSize = mCorePoolSize;
    }

    public void initThreadPoolExecutor() {
        //双重检查加锁，只有第一次实例化启用同步机制，提高了性能
        if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
                    long keepAliveTime = 0;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    //无界队列
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                    mThreadPoolExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
    }

    /**
     * 提交任务
     *
     * @return
     */

    public Future submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> future = mThreadPoolExecutor.submit(task);
        return future;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.remove(task);
    }
}

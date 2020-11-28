package com.winter.library;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 高磊
 * @Time: 2020/11/23 5:59 PM
 * @Description:
 */
public class ThreadUtils {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }
    private static ExecutorService compressImage = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), threadFactory("onCompress", false));

    public static void compress(Runnable runnable) {
        compressImage.execute(runnable);
    }
    public static void operate(Runnable r){
        compressImage.execute(r);
    }
    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }
}

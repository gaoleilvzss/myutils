package com.winter.myutils;

import android.app.Application;

import com.winter.library.log.LogX;
import com.winter.library.log.LogXAdapter;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/22
 * desc :
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogXAdapter adapter = new LogXAdapter.Builder().setShowCurrentThread(true)
                .setShowLogSwitch(true)
                .setTag("winter").build();
        LogX.addLogAdapter(adapter);

    }
}

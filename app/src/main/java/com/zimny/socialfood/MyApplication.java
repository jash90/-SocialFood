package com.zimny.socialfood;

import android.app.Application;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;


/**
 * Created by ideo7 on 08.08.2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        XLog.init(LogLevel.ALL);
    }
}

package com.zimny.socialfood;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;


/**
 * Created by ideo7 on 08.08.2017.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        XLog.init(LogLevel.ALL);
        Iconics.init(getApplicationContext());
        Iconics.registerFont(new GoogleMaterial());
        Iconics.registerFont(new FontAwesome());
    }
}

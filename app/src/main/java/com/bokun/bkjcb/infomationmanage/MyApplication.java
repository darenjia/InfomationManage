package com.bokun.bkjcb.infomationmanage;

import android.app.Application;

/**
 * Created by DengShuai on 2017/8/18.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }
}

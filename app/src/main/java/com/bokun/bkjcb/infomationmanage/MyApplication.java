package com.bokun.bkjcb.infomationmanage;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.facebook.stetho.Stetho;

/**
 * Created by DengShuai on 2017/8/18.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        checkSoftVersion();
        Stetho.initializeWithDefaults(this);
    }

    private void checkSoftVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            VersionInfo versionInfo = DBManager.newInstance(getApplicationContext()).getVersionInfo();
            if (!version.equals(versionInfo)) {
                versionInfo.setProgramV(version);
                DBManager.newInstance(getApplicationContext()).setVersionInfo(versionInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}

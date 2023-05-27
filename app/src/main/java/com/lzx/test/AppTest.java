package com.lzx.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppTest extends AppCompatActivity {

//    private LauncherApps mLauncherApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_test);

//        mLauncherApps = (LauncherApps) getSystemService(Context.LAUNCHER_APPS_SERVICE);

//        List<PackageInfo> mPackageInfo = getPackageManager().getInstalledPackages(0);
//        Log.d("lzx0", "onCreate: mPackageInfo.size() = "+mPackageInfo.size());
//        for (int i = 0; i < mPackageInfo.size(); i++) {
//            Log.d("lzx0", "onCreate: mPackageInfo"+mPackageInfo.get(i).packageName);
//        }
        initAppList(this);
    }

    private static ArrayList<AppInformation> initAppList(Context context){
        ArrayList<AppInformation> appDataList = new ArrayList<>();
        LauncherApps mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        final List<LauncherActivityInfo> launcherAppList = mLauncherApps.getActivityList(null, android.os.Process.myUserHandle());
        Log.d("lzx1", "initAppList: launcherAppList.size = "+launcherAppList.size());

        for (LauncherActivityInfo activityInfo : launcherAppList) {
            if(isSystemApp(context,activityInfo.getComponentName().getPackageName())){
                Log.d("lzx1", "initAppList: systemApp = "+activityInfo.getComponentName().getPackageName());
                continue;
            }
            AppInformation appInfo = new AppInformation();
            appInfo.appIcon = activityInfo.getIcon(0);
            appInfo.appName = (String) activityInfo.getLabel();
            appInfo.appComponentName = activityInfo.getComponentName();
            Log.d("lzx1", "initAppList: appName | pkgName= "+activityInfo.getLabel()+" | "+activityInfo.getComponentName().getPackageName()+" | "+activityInfo.getComponentName().getClassName());
            appDataList.add(appInfo);
        }
        return appDataList;
    }

    public static boolean launcherApp(Context context, ComponentName component) {
        try {
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(component);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSystemApp(Context context,String pkgName) {
        if (pkgName != null) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pkgInfo = pm.getPackageInfo(pkgName, 0);
                return (pkgInfo != null) && (pkgInfo.applicationInfo != null)
                        && ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

}
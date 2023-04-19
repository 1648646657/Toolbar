package com.example.toolbar;

import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityListenSevice extends Service {

    private static final String packageName = "com.sf.ssct_pad";
//    private static final String packageName = "com.example.toolbar";
    private static final String SYSTEM_KEY = "sys.nabar";
    private RecentUseComparator mRecentComp;
    private boolean flag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mRecentComp = new RecentUseComparator();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static class RecentUseComparator implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }

    public String getTopPackage(){
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 3 * 60 * 1000;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime);
        if (usageStats == null || usageStats.size() == 0) {
            return "";
        }
        Collections.sort(usageStats, mRecentComp);
        return usageStats.get(0).getPackageName();
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lzx", "ActivityListenSevice.onStartCommand: package = "+getTopPackage());

//        new Thread(){
//            @Override
//            public void run() {
//                flag = true;
//                while (flag){
//                    if (needPermissionForBlocking(getApplicationContext())) {
//                        Intent intent0 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                        intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else {
//                        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
//                        String currentPackageName = "";
//                        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
//                            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                                currentPackageName = processInfo.processName;
//                                break;
//                            }
//                        }
//                        if(currentPackageName.equals(packageName)){
//                            Log.d("lzx", "ActivityListenSevice.onStartCommand: PackageName = "+currentPackageName);
//                            Intent intent0 =new Intent();
//                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                            intent0.putExtra("SHOW_NAV_BAR", 0);
//                            sendBroadcast(intent0);
//                        }else {
//                            Log.d("lzx", "ActivityListenSevice.onStartCommand: Another PackageName = "+currentPackageName);
//                            Intent intent0 =new Intent();
//                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                            intent0.putExtra("SHOW_NAV_BAR", 1);
//                            sendBroadcast(intent0);
//                        }
//                    }
//                    SystemClock.sleep(1000);
//                }
//            }
//        }.start();

        new Thread() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//版本小于lollipop的
//                        ActivityManager am = ((ActivityManager) getSystemService(ACTIVITY_SERVICE));
//                        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(5);
//                        if(taskInfo.get(0).topActivity.getPackageName().equals(packageName)){
//                            Log.d("lzx", "ActivityListenSevice.onStartCommand: PackageName = "+packageName);
//                            Intent intent0 =new Intent();
//                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                            intent0.putExtra("SHOW_NAV_BAR", 0);
//                            sendBroadcast(intent0);
//                        }else {
//                            Log.d("lzx", "ActivityListenSevice.onStartCommand: Another PackageName");
//                            Intent intent0 =new Intent();
//                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                            intent0.putExtra("SHOW_NAV_BAR", 0);
//                            sendBroadcast(intent0);
//                        }
//                    } else {
                    if (needPermissionForBlocking(getApplicationContext())) {
                        Intent intent0 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent0);
                    } else {
                        if(getTopPackage().equals(packageName)){
                            Log.d("lzx", "ActivityListenSevice.onStartCommand: PackageName = "+getTopPackage());
                            Intent intent0 =new Intent();
                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
                            intent0.putExtra("SHOW_NAV_BAR", 0);
                            sendBroadcast(intent0);
                        }else {
                            Log.d("lzx", "ActivityListenSevice.onStartCommand: Another PackageName = "+getTopPackage());
                            Intent intent0 =new Intent();
                            intent0.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
                            intent0.putExtra("SHOW_NAV_BAR", 1);
                            sendBroadcast(intent0);
                        }
                    }
//                    }
                    SystemClock.sleep(1000);
                }
            }
        }.start();

//        if(getTopPackage().equals(packageName)){
//            Log.d("lzx", "ActivityListenSevice.onStartCommand: PackageName = "+packageName);
//            Intent intent0 =new Intent();
//            intent.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//            intent.putExtra("SHOW_NAV_BAR", 0);
//            sendBroadcast(intent0);
//        }else {
//            Log.d("lzx", "ActivityListenSevice.onStartCommand: Another PackageName");
//            Intent intent0 =new Intent();
//            intent.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//            intent.putExtra("SHOW_NAV_BAR", 1);
//            sendBroadcast(intent0);
//        }
//        getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                if(activity.getPackageName().equals(packageName)){
//                    Log.d("lzx", "onActivityResumed: PackageName = "+activity.getPackageName());
//                    Intent intent =new Intent();
//                    intent.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                    intent.putExtra("SHOW_NAV_BAR", 0);
//                    sendBroadcast(intent);
//                }
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                if(activity.getPackageName().equals(packageName)){
//                    Log.d("lzx", "onActivityStopped: PackageName = "+activity.getPackageName());
//                    Intent intent =new Intent();
//                    intent.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//                    intent.putExtra("SHOW_NAV_BAR", 1);
//                    sendBroadcast(intent);
//                }
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("lzx", "ActivityListenSevice.onDestroy: ");
        flag = false;
    }

}

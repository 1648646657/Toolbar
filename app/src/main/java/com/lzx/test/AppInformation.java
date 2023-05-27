package com.lzx.test;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;

/**
 * @author:
 * @date:
 * @description:
 */
public class AppInformation {
    public String appName;
    public String packageName;
    public String activityName;
    public Drawable appIcon;
    public boolean isSystemApp;
    public boolean isSelected = false;
    public ComponentName appComponentName;

    public AppInformation(){}

    public AppInformation(String appName, String packageName, String activityName, Drawable appIcon, boolean isSystemApp, boolean isSelected) {
        this.appName = appName;
        this.packageName = packageName;
        this.activityName = activityName;
        this.appIcon = appIcon;
        this.isSystemApp = isSystemApp;
        this.isSelected = isSelected;
    }
    public AppInformation(String appName, String packageName, boolean isSelected) {
        this.appName = appName;
        this.packageName = packageName;
        this.isSelected = isSelected;
    }

    public AppInformation(ComponentName componentName, boolean isSelected) {
        this.appComponentName = componentName;
        this.isSelected = isSelected;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}

package com.example.zsarsenbayev.applicationtracker;

/**
 * Created by zsarsenbayev on 2/23/18.
 */

public class AppInfo {

    private String appName;
    private String packageName;
    private boolean isSystemApp;


    public String getAppName(){
        return  appName;
    }

    public String getPackageName(){
        return packageName;
    }

    public void setAppName(String appName){
        this.appName = appName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }
    public void setSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

}

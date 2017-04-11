package com.miguo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class AppStateUtil {

    /**
     * 判断App是否在前台工作
     * 如果在前台工作，并且没有推送权限，就要推送应用内弹窗
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
            }
        }
        return false;
    }

}

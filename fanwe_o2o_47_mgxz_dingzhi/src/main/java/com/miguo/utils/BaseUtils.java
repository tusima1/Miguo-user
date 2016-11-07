package com.miguo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import com.fanwe.o2o.miguo.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 狗蛋哥 on 16/3/7.
 */
public class BaseUtils {


    /**
     * 获取宽度
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static String getNumberWithFriendWay(long count) {
        int cou = (int) count;
        return getNumberWithFriendWay(cou);
    }

    public static String getNumberWithFriendWay(int count) {
        DecimalFormat df = new DecimalFormat("0.0");
        String number;
        if (count < 1000) {
            number = count + "";
        } else if (count < 10000) {
            number = df.format((double) count / 1000) + "k";
        } else if (count < 10000000) {
            number = df.format((double) count / 10000) + "w";
        } else if (count < 100000000) {
            number = df.format((double) count / 10000000) + "kw";
        } else {
            number = df.format((double) count / 100000000) + "mn";
        }
        return number;
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivity(Activity activity, Intent intent) {
        intent.putExtra("index", 0);
//        activity.startParallaxSwipeBackActivty(activity, intent);
        activity.startActivity(intent);
//        ((Activity)activity).overridePendingTransition(R.anim.translate_left,R.anim.translate_right);
    }

    /**
     * 跳转到新到activit并且结束当前Activity
     */
    public static void jumpToNewActivityWithFinish(Activity activity, Intent intent) {
//        activity.startParallaxSwipeBackActivty(activity, intent);
        activity.startActivity(intent);
        activity.finish();
//        activity.overridePendingTransition(R.anim.translate_left, R.anim.translate_right);
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivityWithBackway(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.translate_left_out, R.anim.translate_right_out);
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivityForResult(Activity activity, Intent intent, int requestCode) {
//        activity.startParallaxSwipeBackActivty(activity, intent, requestCode);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.translate_left, R.anim.translate_right);
    }

    /**
     * 跳转到新到activity
     * 速度更快
     */
    public static void jumpToNewActivityForResult2(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.translate_left2, R.anim.translate_right2);
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivityForResultWithAplhaAnimation(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
    }

    /**
     * 销毁activity
     */
    public static void finishActivity(Activity activity) {
//        VoicePlayer.creat(activity).stop();
        activity.finish();
        activity.overridePendingTransition(R.anim.translate_left_out, R.anim.translate_right_out);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置为横屏
     *
     * @param activity
     */
    public static void setScreenLandscape(Activity activity) {
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 设置为竖屏
     *
     * @param activity
     */
    public static void setScreenPortrait(Activity activity) {
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 判断微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if ("com.tencent.mm".equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}

package com.miguo.live.views.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fanwe.app.App;

import java.text.DecimalFormat;

/**
 * Created by 狗蛋哥 on 16/3/7.
 */
public class BaseUtils {


    public static int getWidth(){
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

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
//        activity.overridePendingTransition(R.anim.translate_left_out, R.anim.translate_right_out);
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
//        activity.startActivity();
//        activity.overridePendingTransition(R.anim.translate_left, R.anim.translate_right);
    }

    /**
     * 跳转到新到activity
     * 速度更快
     */
    public static void jumpToNewActivityForResult2(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
//        activity.overridePendingTransition(R.anim.translate_left2, R.anim.translate_right2);
    }

    /**
     * 跳转到新到activity
     */
    public static void jumpToNewActivityForResultWithAplhaAnimation(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
//        activity.overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
    }

    /**
     * 销毁activity
     */
    public static void finishActivity(Activity activity) {
//        VoicePlayer.creat(activity).stop();
        activity.finish();
//        activity.overridePendingTransition(R.anim.translate_left_out,R.anim.translate_right_out);
    }

    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = App.getInstance().getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int dip2px(float dpValue) {
        return dip2px(null, dpValue);
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

}

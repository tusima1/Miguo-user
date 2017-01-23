package com.fanwe.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.utils.ActivityNameUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Stack;

/**
 * Created by didik on 2016/8/31.
 */
public class ActivityLifeManager {
    public static final String TAG = "ActivityLifeManager";
    private static Stack<Activity> mStackActivity;

    private ActivityLifeManager() {
        mStackActivity = new Stack<>();
    }

    public void init(Application weApp) {
        weApp.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public static ActivityLifeManager getInstance() {
        return SingletonHolder.alm;
    }

    private static class SingletonHolder {
        private static final ActivityLifeManager alm = new ActivityLifeManager();
    }


    private Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application
            .ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            String pageName = ActivityNameUtil.getInstance().getPageName(activity.getLocalClassName());
            if (!TextUtils.isEmpty(pageName)) {
                Log.d(TAG, "onActivityCreated:" + pageName);
                MobclickAgent.onPageStart(pageName);
            } else {
                Log.d(TAG, "onActivityCreated:unknownPage");
            }
            addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            addActivity(activity);
            MobclickAgent.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            MobclickAgent.onPause(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            String pageName = ActivityNameUtil.getInstance().getPageName(activity.getLocalClassName());
            if (!TextUtils.isEmpty(pageName)) {
                Log.d(TAG, "onActivityDestroyed:" + pageName);
                MobclickAgent.onPageEnd(pageName);
            } else {
                Log.d(TAG, "onActivityDestroyed:unknownPage");
            }
            removeActivity(activity);
        }
    };


    /***********************
     * Activity Manager Tools
     ***********************/
    public Activity getLastActivity() {
        Activity activity = null;
        try {
            activity = mStackActivity.lastElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

    public boolean isLastActivity(Activity activity) {
        if (activity != null) {
            return getLastActivity() == activity;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return mStackActivity.isEmpty();
    }

    private void addActivity(Activity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.add(activity);
        }
    }

    /**
     * 移除指定的Activity
     */
    private void removeActivity(Activity activity) {
        if (activity != null) {
            mStackActivity.remove(activity);
        }
    }

    public void finishLastActivity() {
        Activity activity = getLastActivity();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mStackActivity.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mStackActivity) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        for (Activity activity : mStackActivity) {
            if (activity != null) {
                activity.finish();
            }
        }
        mStackActivity.clear();
    }

}

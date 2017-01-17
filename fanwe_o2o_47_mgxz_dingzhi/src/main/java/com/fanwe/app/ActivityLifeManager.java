package com.fanwe.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

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
            Log.d(TAG, "onActivityCreated");
            addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed");
            addActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, "onActivitySaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed");
            removeActivity(activity);
        }
    };


    /*********************** Activity Manager Tools ***********************/
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

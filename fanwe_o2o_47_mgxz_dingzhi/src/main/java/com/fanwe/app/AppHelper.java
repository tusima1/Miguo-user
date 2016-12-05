package com.fanwe.app;

import android.app.Activity;
import android.content.Intent;

import com.fanwe.model.LocalUserModel;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;

public class AppHelper {

    public static boolean isLogin() {
        return isLogin(null);
    }

    public static boolean isLogin(Activity activity) {
        if (getLocalUser() == null) {
            if (activity != null) {
                Intent intent = new Intent(activity, ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
                activity.startActivity(intent);
            }
            return false;
        } else {
            return true;
        }
    }

    public static LocalUserModel getLocalUser() {
        return App.getApplication().getmLocalUser();
    }

    /**
     * 更新数据库中用户表中用户数据。
     * @param localUser
     */

    public static void updateLocalUser(LocalUserModel localUser) {
        App.getApplication().setmLocalUser(localUser);
    }

}

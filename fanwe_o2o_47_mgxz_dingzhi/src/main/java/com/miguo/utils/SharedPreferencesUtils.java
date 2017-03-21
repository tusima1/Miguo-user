package com.miguo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.miguo.definition.SharedPreferencesConfig;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * SharedPreferences单例类
 */
public class SharedPreferencesUtils {

    static SharedPreferencesUtils instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;

    String tag = getClass().getSimpleName();

    /**
     * 防止默认构造器被调用需私有化
     */
    private SharedPreferencesUtils(){
    }

    private SharedPreferencesUtils(Context context){
        this.context = context;
        sp = context.getSharedPreferences(SharedPreferencesConfig.NAME, SharedPreferencesConfig.MODE);
        editor = sp.edit();
    }

    public static SharedPreferencesUtils getInstance(){
        /**
         * 防止异步请求和用户操作同时访问
         */
        synchronized (SharedPreferencesUtils.class){
            if(instance == null){
                instance = new SharedPreferencesUtils(App.getInstance());
            }
            return instance;
        }
    }

    /**
     * 登录
     * @param username 手机号
     * @param password 密码 md5
     */
    public void saveUserNameAndUserPassword(String username, String password){
        editor.putString(SharedPreferencesConfig.MOBILE, username);
        editor.putString(SharedPreferencesConfig.PASSWORD, password);
        editor.commit();
    }

    /**
     * 获取本地保存的用户名
     * @return
     */
    public String getUserName(){
        return sp.getString(SharedPreferencesConfig.MOBILE, "");
    }

    /**
     * 获取本地保存的密码
     * @return
     */
    public String getPassword(){
        return sp.getString(SharedPreferencesConfig.PASSWORD, "");
    }

    /**
     * 退出登录
     */
    public void clearUserNameAndUserPassword(){
        editor.putString(SharedPreferencesConfig.MOBILE, "");
        editor.putString(SharedPreferencesConfig.PASSWORD, "");
        editor.commit();
    }

    /**
     * 是否有本地用户
     * @return
     */
    public boolean hasSave(){
        return !"".equals(getUserName()) && !"".equals(getUserName());
    }

    public boolean hasImei(){
        return !TextUtils.isEmpty(getImei());
    }

    public String getImei(){
        return sp.getString(SharedPreferencesConfig.IMEI, "");
    }

    public String setImei(String imei){
        editor.putString(SharedPreferencesConfig.IMEI, imei);
        editor.commit();
        return imei;
    }

}

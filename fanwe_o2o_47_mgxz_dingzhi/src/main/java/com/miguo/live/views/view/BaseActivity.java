package com.miguo.live.views.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fanwe.app.App;
import com.miguo.live.views.category.Category;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.utils.ToasUtil;


/**
 * Created by 狗蛋哥 on 16/3/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 是否按两次返回退出程序
     */
    protected boolean keyDownToCloseActivity = false;

    /**
     * 按返回两次间隔时间小于DOUBALE_TIME即退出Activity
     */
    protected long DOUBLE_TIME = 2000;

    /**
     * 退出时间
     */
    protected long mExitTime;

    /**
     *  全局Application
     */
    protected App app;

    /**
     *
     *
     */

    protected Category category;

    protected boolean isSavedInstanceStateNull = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSavedInstanceStateNull = savedInstanceState == null ? true : false;
        init();
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState){
        setContentView(savedInstanceState);
    }

    protected void setContentView(Bundle savedInstanceState){
    }

    /**
     * Activity初始化操作
     */
    protected void init(){
//        SmartBarUtils.hide(this, getWindow(), SmartBarUtils.SMART_BAR_HEIGH);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
//        setBar(R.color.status_bar);
        setActivityParams();
        setContentView();
        initApp();
        initCategory();
    }

    public void setContentView(View view){
        super.setContentView(view);
    }

    private void setBar(int resColor){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(false);
//        tintManager.setStatusBarTintResource(resColor);//通知栏所需颜色
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置Activity的根布局
     */
    protected abstract void setContentView();

    /**
     * UI绑定/监听器初始化/UI设置监听
     */
    protected abstract void initCategory();

    /**
     * Activity初始化前的一些基础参数
     */
    protected void setActivityParams(){
        BaseUtils.setScreenPortrait(this);
    }


    /**
     * 引用Application对象
     */
    private void initApp(){
        this.app = (App) getApplicationContext();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        UmengUtils.onResume(this);
        doOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        UmengUtils.onPause(this);
        doOnPause();
    }



    @Override
    protected void onStop() {
        super.onStop();
        doOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        getCategory().destory();
        doOnDestory();
    }

    protected void doOnResume(){

    }
    protected void doOnPause(){

    }

    protected void doOnStop(){

    }

    protected void doOnDestory(){

    }

    /**
     * 监听设备返回键,按两次后退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**按两次后退出程序*/
        if(keyDownToCloseActivity){
            keyDown2CloseApplication();
            return true;
        }
        /**按一次后退出Activity*/
        if(!keyDownToCloseActivity){
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 判断点击两次退出Activity
     */
    private void keyDown2CloseApplication(){
        /**第一次点击返回||两次间隔时间 > DOUBLE_TIME*/
        if(System.currentTimeMillis() - mExitTime > DOUBLE_TIME){
            mExitTime = System.currentTimeMillis();
            showToastWithShortTime("再按一次退出程序!");
        }else{
//            finishActivity();
            System.exit(0);
        }

    }

    /**
     * 退出Activity
     */
    protected void finishActivity(){
        BaseUtils.finishActivity(this);
    }

    /**
     * 获取BaseApplication对象
     * @return
     */
    public App getApp(){
        return app;
    }

    /**
     * 获取Category
     */
    public Category getCategory(){
        return category;
    }

    /**
     * 通过设置这个参数来调控是否点击返回键两次后退出Activity
     * @param onKeyDownToCloseActivity
     */
    protected void setTwiceKeyDownToCloseActivity(boolean onKeyDownToCloseActivity){
        this.keyDownToCloseActivity = onKeyDownToCloseActivity;
    }

    /**
     * show toast 时间为short
     * @param msg
     */
    public void showToastWithShortTime(String msg){
        ToasUtil.showToastWithShortTime(this, msg);
    }

    public boolean isSavedInstanceStateNull() {
        return isSavedInstanceStateNull;
    }
}

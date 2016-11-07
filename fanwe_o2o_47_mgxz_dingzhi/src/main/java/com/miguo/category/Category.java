package com.miguo.category;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanwe.app.App;
import com.miguo.app.HiBaseActivity;
import com.miguo.listener.Listener;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.BaseView;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public abstract class Category implements BaseView {

    protected Category category;
    protected String title;

    protected Listener listener;
    protected HiBaseActivity activity;
    protected App app;
//    protected ImageView back;
    protected String tag = this.getClass().getSimpleName();

    protected View view;
    protected LayoutInflater inflater;

    protected String lastRefreshTime;

    /**
     * View
     * @param category
     */
//    protected RelativeLayout top;

    public Category(Category category){
        this(category.getActivity());
    }


    public Category(Category category, View view, String title) {
        this.title =title;
        this.view = view;
        this.activity = category.getActivity();
        this.app = activity.getApp();
        initFirst();
        findViews();
        initListener();
        setListener();
        initViews();
        init();
    }

    public Category(Category category, View view) {
        this.view = view;
        this.activity = category.getActivity();
        this.app = activity.getApp();
        initFirst();
        findViews();
        initListener();
        setListener();
        initViews();
        init();
    }

    public Category(HiBaseActivity activity){
        this.activity = activity;
        this.app = activity.getApp();
        initFirst();
        findViews();
        initListener();
        setListener();
        initViews();
        init();
        setTitlePadding();
    }


    /**
     * 绑定View
     */
    protected void findViews(){
//        top = findViewById(R.id.title_layout);
//        back = findViewById(R.id.back);
        findCategoryViews();
    }

    /**
     * 初始化Listener
     */
    protected void initListener(){
        initThisListener();
    }

    /**
     * 设置监听时间(Controller)
     */
    protected  void setListener(){
//        if(back!=null){
//            back.setOnClickListener(listener);
//        }
        setThisListener();
    }


    public App getApp(){
        return app;
    }

    public HiBaseActivity getActivity(){
        return activity;
    }

    /**通过 findViewById(R.id.resId) 来获取xml 中的id 资源*/
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int resId){
        return (T) getActivity().findViewById(resId);
    }
    public <T extends View> T findViewById(View view, int resId){
        return (T) view.findViewById(resId);
    }

    /**
     * 沉浸式标题栏效果需要设置padding
     */
    protected void setTitlePadding() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
    }

    /**
     * 设置标题栏的透明度
     * 一般用于滑动时候的处理
     * @param radio
     */
    protected void setTitleAlpha(float radio){
//        top.setAlpha(radio);
    }

    /**
     * 沉浸式标题栏效果需要设置padding
     */
    protected void setTitlePadding(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (view != null) {
            view.setPadding(0, BaseUtils.getStatusBarHeight(getActivity()) * 3, 0, 0);
        }
    }

    /**
     * 获取Drawable对象
     * @param drawbleId
     * @return
     */
    public Drawable getDrawable(int drawbleId){
        return getResources().getDrawable(drawbleId);
    }

    /**
     * 通过资源id获取Color
     * @param colorId
     * @return
     */
    public int getColor(int colorId){
        return getResources().getColor(colorId);
    }

    /**
     * 获取dimen中的数值
     * @param dimensId
     * @return
     */
    public int getDimensionPixelSize(int dimensId){
        return getResources().getDimensionPixelSize(dimensId);
    }

    /**
     * 通过资源id获取Stirng
     * @param stringId
     * @return
     */
    public String getString(int stringId){
        return getResources().getString(stringId);
    }

    /**
     * 获取Resources对象
     * @return
     */
    public Resources getResources(){
        return getActivity().getResources();
    }


    /**
     * 统一showToast
     * @param msg
     */
    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出Activity
     */
    public void finish() {
        BaseUtils.finishActivity(getActivity());
    }

//    public BaseUser getCurrentUser(){
//        return getApp().getCurrentUser();
//    }


    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public View getView(){
        return view;
    }


    public int getScreenWidth(){
        return BaseUtils.getWidth(getActivity());
    }

    public int getScreenHeight(){
        return BaseUtils.getHeight(getActivity());
    }

    public RelativeLayout.LayoutParams getRelativeLayoutParams(int width, int height){
        return new RelativeLayout.LayoutParams(width, height);
    }

    public LinearLayout.LayoutParams getLineaLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    /**
     * 获取Listener对象
     * @return
     */
    public Listener getListener(){
        return listener;
    }

    public Intent getIntent(Context context, Class<?> clz){
        return new Intent(context, clz);
    }

    public void destory(){

    }

    public LayoutInflater getInflater(){
        return getActivity().getLayoutInflater();
    }

    protected abstract void initFirst();
    protected abstract void findCategoryViews();
    protected abstract void initThisListener();
    protected abstract void setThisListener();
    protected abstract void init();
    protected abstract void initViews();
}

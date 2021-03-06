package com.miguo.category.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanwe.app.App;
import com.miguo.app.HiBaseActivity;
import com.miguo.dialog.HiBaseDialog;
import com.miguo.listener.dialog.DialogFragmentListener;
import com.miguo.utils.BaseUtils;


/**
 * Created by zlh on 16/3/16.
 */
public abstract class DialogFragmentCategory {

    protected View view;
    protected HiBaseDialog fragment;
    String tag = this.getClass().getSimpleName();
    DialogFragmentListener listener;

    public DialogFragmentCategory(View view, HiBaseDialog fragment){
        this.view = view;
        this.fragment = fragment;
        initFirst();
        findViews();
        initFragmentListener();
        setFragmentListener();
        init();
    }

    protected abstract void initFirst();

    protected void findViews(){
        findFragmentViews();
    }

    protected abstract void findFragmentViews();

    protected abstract void initFragmentListener();

    protected abstract void setFragmentListener();


    protected abstract void init();

    public HiBaseActivity getActivity(){
        return fragment.getIActivity();
    }

    public App getApp(){
        return fragment.getApp();
    }

    public <T extends View> T findViewById(View view, int resId){
        return (T) view.findViewById(resId);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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



    public <T extends View> T findViewById(int resId){
        return (T) view.findViewById(resId);
    }

    public Intent getIntent(Context context, Class<?> clz){
        return new Intent(context, clz);
    }

    /**
     * utils
     */
    /**
     * 获取Drawable对象
     * @param drawbleId
     * @return
     */
    public Drawable getDrawable(int drawbleId){
        return fragment.getResources().getDrawable(drawbleId);
    }

    /**
     * 通过资源id获取Color
     * @param colorId
     * @return
     */
    public int getColor(int colorId){
        return fragment.getResources().getColor(colorId);
    }

    /**
     * 获取dimen中的数值
     * @param dimensId
     * @return
     */
    public int getDimensionPixelSize(int dimensId){
        return fragment.getResources().getDimensionPixelSize(dimensId);
    }

    /**
     * 统一showToast
     * @param msg
     */
    public void showToast(String msg){
        Toast.makeText(App.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

//    public BaseUser getCurrentUser(){
//        return getApp().getCurrentUser();
//    }


    public void dismiss(){
        fragment.dismiss();
    }



    public int getScreenWidth(){
        return BaseUtils.getWidth();
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



    public HiBaseDialog getDialog(){
        return fragment;
    }

}

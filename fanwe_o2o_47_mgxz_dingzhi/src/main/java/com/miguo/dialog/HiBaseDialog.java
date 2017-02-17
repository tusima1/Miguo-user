package com.miguo.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiBaseActivity;
import com.miguo.category.dialog.DialogFragmentCategory;


/**
 * Created by zlh on 16/3/23.
 */
public abstract class HiBaseDialog extends DialogFragment {

    DialogFragmentCategory category;

    String tag = this.getClass().getSimpleName();
    /**
     * View 缓存View;
     */
    View cacheView;

    public HiBaseDialog(){
        setStyle(STYLE_NO_FRAME, R.style.BaseDialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        initBundleData(savedInstanceState);
        if(cacheView == null) {
            cacheView = createView(inflater, container, savedInstanceState);
        }else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) cacheView.getParent();
            if(parent != null){
                parent.removeView(cacheView);
            }
        }
//        setBar(R.color.base_color);
        initFragmentCategory();
        return cacheView;
    }

    protected void initBundleData(Bundle savedInstanceState){

    }

    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected abstract void initFragmentCategory();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        animAppear();
        super.onViewCreated(view, savedInstanceState);
    }


    protected void animAppear() {
        animFadeIn();
    }

    protected void animFadeIn() {
        if (null == cacheView) {
            return;
        }
        cacheView.startAnimation(AnimationUtils.loadAnimation(getIActivity(), R.anim.alpha_in));
    }

    @Override
    public void dismiss() {
//        animDisAppear();
        super.dismiss();
    }

    protected void animDisAppear() {
        animFadeOut();
    }

    protected void animPopDown() {
        if (null != cacheView) {
            cacheView.startAnimation(AnimationUtils.loadAnimation(getIActivity(), R.anim.out_to_bottom));
        }
        animFadeOut();
    }

    protected void animPopup() {
        animFadeIn();
        if (null != cacheView) {
            cacheView.startAnimation(AnimationUtils.loadAnimation(getIActivity(), R.anim.in_from_bottom));
        }
    }

    private void animFadeOut() {
        if (null == cacheView) {
            dismiss();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(getIActivity(), R.anim.alpha_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cacheView.startAnimation(anim);
    }

    public App getApp(){
        return (App) getActivity().getApplicationContext();
    }


    public HiBaseActivity getIActivity(){
        return (HiBaseActivity)super.getActivity();
    }


    public DialogFragmentCategory getCategory(){
        return category;
    }

}

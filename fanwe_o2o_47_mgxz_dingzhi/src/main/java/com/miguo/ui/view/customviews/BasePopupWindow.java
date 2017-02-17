package com.miguo.ui.view.customviews;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.didikee.uilibs.utils.DisplayUtil;

/**
 * Created by didik 
 * Created time 2017/2/13
 * Description: 
 */

public abstract class BasePopupWindow extends PopupWindow {

    protected final Activity mHoldActivity;
    protected final View mAnchor;

    public BasePopupWindow(Activity mHoldActivity, View mAnchor) {
        this.mHoldActivity = mHoldActivity;
        this.mAnchor = mAnchor;
        if (checkParams()) {
            startFlow();
        } else {
            Log.e("BasePopupWindow", "Activity 或 Anchor 不能为空!");
        }
    }

    protected boolean checkParams() {
        return !(mHoldActivity == null || mAnchor == null);
    }

    protected void startFlow() {
        View contentView = setPopupWindowContentView();
        if (contentView == null) return;
        setPopupWindowParams();
        setContentView(contentView);
    }

    protected void setPopupWindowParams() {
        setWidth(DisplayUtil.dp2px(mHoldActivity,312));
        setHeight(DisplayUtil.dp2px(mHoldActivity,415));
        setAnimationStyle(android.R.style.Animation);//clear pop default animation
        setOutsideTouchable(false);
        setTouchable(true);
    }

    protected abstract View setPopupWindowContentView();

    protected boolean isAlready2ShowPopupWindow() {
        return mHoldActivity != null && (!mHoldActivity.isFinishing());
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (isAlready2ShowPopupWindow())
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isAlready2ShowPopupWindow())
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (isAlready2ShowPopupWindow())
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (isAlready2ShowPopupWindow())
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}

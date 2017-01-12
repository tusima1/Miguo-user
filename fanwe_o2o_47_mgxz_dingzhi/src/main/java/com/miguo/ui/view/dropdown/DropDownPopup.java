package com.miguo.ui.view.dropdown;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.didikee.uilibs.utils.DisplayUtil;
import com.didikee.uilibs.utils.UIColor;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.floatdropdown.helper.DropDownHelper2;

/**
 * Created by didik 
 * Created time 2017/1/12
 * Description: 
 */

public class DropDownPopup extends PopupWindow {

    private final Activity mHoldActivity;
    private View anchor;
    private int bgColor = UIColor.getAlphaColor(66,Color.BLACK);

    public DropDownPopup(Activity mHoldActivity,View anchor) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        initDropDownLayout();
    }

    private void initDropDownLayout() {
        if (mHoldActivity == null){
            return;
        }
        int[] anchorLocation=new int[2];
        anchor.getLocationOnScreen(anchorLocation);
        LinearLayout rootLayout=new LinearLayout(mHoldActivity);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout contentLayout=new FrameLayout(mHoldActivity);
        DropDown dropDownView=new DropDown(mHoldActivity);
        DropDownHelper2 helper2=new DropDownHelper2(mHoldActivity,dropDownView);

        contentLayout.addView(dropDownView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLayout.setBackgroundColor(Color.YELLOW);
        int height = DisplayUtil.dp2px(mHoldActivity, 395);

        View fakeView =new View(mHoldActivity);
        fakeView.setBackgroundColor(bgColor);
        fakeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootLayout.addView(contentLayout,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        rootLayout.addView(fakeView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(rootLayout);
        setOutsideTouchable(false);
        setTouchable(true);
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    public void show(){
        if (!mHoldActivity.isFinishing()){
            int height = anchor.getHeight();
            showAsDropDown(anchor);
//            showAsDropDown(anchor,0,-height);
        }else {
            MGToast.showToast("Activity is processing or finishing ~~");
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

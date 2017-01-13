package com.miguo.ui.view.floatdropdown.helper;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.miguo.ui.view.dropdown.DropDownPopup;
import com.miguo.ui.view.dropdown.interf.PopupWindowLike;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

/**
 * Created by didik 
 * Created time 2017/1/13
 * Description: 
 */

public class DropDownPopHelper implements PopupWindowLike{
    private final Activity mHoldActivity;
    private final FakeDropDownMenu anchor;
    private DropDownPopup popup;

    public DropDownPopHelper(Activity mHoldActivity, FakeDropDownMenu anchor) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        initPopup();
    }

    private void initPopup() {
        anchor.setFakeTitleTabClickListener(new FakeDropDownMenu.OnFakeTitleTabClickListener() {
            @Override
            public void onFakeClick(View v, int index) {
                Log.e("test","index: "+index);
                if (popup.isShowing()){
                    popup.handleClick(index);
                }else {
                    popup.show();
                    popup.handleClick(index);
                }

            }
        });
        popup = new DropDownPopup(mHoldActivity,anchor);
    }

    @Override
    public void show() {
        popup.show();
    }

    @Override
    public void dismiss() {
        popup.dismiss();
    }

    public void setOnDropDownListener(OnDropDownListener dropDownListener) {
        popup.setDropDownListener(dropDownListener);
    }
}

package com.miguo.ui.view.floatdropdown.helper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.miguo.ui.view.dropdown.DropDownPopup;
import com.miguo.ui.view.dropdown.interf.PopupWindowLike;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/13
 * Description: 
 */

public class DropDownPopHelper implements PopupWindowLike{
    private final Activity mHoldActivity;
    private final FakeDropDownMenu anchor;
    private FakeDropDownMenu fakeDDM;
    private DropDownPopup popup;

    public DropDownPopHelper(Activity mHoldActivity, FakeDropDownMenu anchor) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        initPopup();
    }
    public DropDownPopHelper(Activity mHoldActivity, FakeDropDownMenu anchor,FakeDropDownMenu fakeDDM) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        this.fakeDDM = fakeDDM;
        initPopup();
    }

    private void initPopup() {
        anchor.setIsFake(false);
        if (fakeDDM != null){
            fakeDDM.setIsFake(true);
            fakeDDM.setFakeTitleTabClickListener(new FakeDropDownMenu.OnFakeTitleTabClickListener() {
                @Override
                public void onFakeClick(View v, final int index) {
                    anchor.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            anchor.performIndexClick(index);
                        }
                    },300);

                }
            });
        }
        //事件最终都是由真的anchor处理
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
        popup.setTextChangedListener(new DropDownPopup.OnTitleTextChangedListener() {
            @Override
            public void onTitleTextChange(int index, @NonNull String text) {
                setTitleText(index,text);
            }
        });
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

    public void setTitleText(int index,String text) {
        text = TextUtils.isEmpty(text) ? " " : text;
        int length = text.length();
        if (length>4){
            text = text.substring(0,4);
            text +="...";
        }
        anchor.setTitleText(index,text);
        if (fakeDDM!=null){
            fakeDDM.setTitleText(index,text);
        }
    }

    public void performMarkIds(String id){
        //36531bd0-51c0-4f88-bb41-f2534b986118
        if (TextUtils.isEmpty(id)){
            return;
        }
        List<String> ids=new ArrayList<>();
        ids.add(id);
        performMarkIds(ids);
    }

    /**
     * 你希望选中哪些item,把他们的 id 传进去就好了
     * @param ids
     */
    public void performMarkIds(List<String> ids){
        //36531bd0-51c0-4f88-bb41-f2534b986118
        if (ids!=null && ids.size()>0){
            popup.performMarkIds(ids);
        }

    }
}

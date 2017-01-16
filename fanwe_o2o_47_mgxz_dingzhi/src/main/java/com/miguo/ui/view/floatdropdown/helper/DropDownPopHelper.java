package com.miguo.ui.view.floatdropdown.helper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.SingleMode;
import com.miguo.ui.view.dropdown.DropDownPopup;
import com.miguo.ui.view.dropdown.interf.PopupWindowLike;
import com.miguo.ui.view.floatdropdown.interf.OnCallDismissPopListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/13
 * Description: 
 */

public class DropDownPopHelper implements PopupWindowLike {
    private final Activity mHoldActivity;
    private final FakeDropDownMenu anchor;
    private FakeDropDownMenu fakeDDM;
    private DropDownPopup popup;

    public DropDownPopHelper(Activity mHoldActivity, FakeDropDownMenu anchor) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        initPopup();
    }

    public DropDownPopHelper(Activity mHoldActivity, FakeDropDownMenu anchor, FakeDropDownMenu
            fakeDDM) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        this.fakeDDM = fakeDDM;
        initPopup();
    }

    private void initPopup() {
        anchor.setIsFake(false);
        if (fakeDDM != null) {
            fakeDDM.setIsFake(true);
            fakeDDM.setFakeTitleTabClickListener(new FakeDropDownMenu.OnFakeTitleTabClickListener
                    () {
                @Override
                public void onFakeClick(View v, final int index) {
                    anchor.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            anchor.performIndexClick(index);
                        }
                    }, 300);

                }
            });
        }
        //事件最终都是由真的anchor处理
        anchor.setFakeTitleTabClickListener(new FakeDropDownMenu.OnFakeTitleTabClickListener() {
            @Override
            public void onFakeClick(View v, int index) {
                if (popup.isShowing()) {
                    popup.handleClick(index);
                } else {
                    popup.show();
                    popup.handleClick(index);
                }

            }
        });

        anchor.setOnFakeCallDismissPopListener(new OnCallDismissPopListener() {
            @Override
            public void callDismissPop(boolean immediately) {
                callDismiss(immediately);
            }
        });

        popup = new DropDownPopup(mHoldActivity, anchor);
        popup.setTextChangedListener(new DropDownPopup.OnTitleTextChangedListener() {
            @Override
            public void onTitleTextChange(int index, @NonNull String text) {
                setTitleText(index, text);
            }
        });
        popup.setOnCallDismissPopListener(new OnCallDismissPopListener() {
            @Override
            public void callDismissPop(boolean immediately) {
                callDismiss(immediately);
            }
        });
    }
    private void callDismiss(boolean isImmediately){
        if (isImmediately){
            popup.dismiss();
        }else {
            anchor.handleArrowImageAnim(0);
            anchor.resetLastPosition();
        }
    }

    @Override
    public void show() {
        popup.show();
    }

    @Override
    public void dismiss() {
        callDismiss(false);
    }

    public void setOnDropDownListener(final OnDropDownListener dropDownListener) {
        popup.setDropDownListener(new OnDropDownListener() {
            @Override
            public void onItemSelected(int index, Pair<SingleMode, SingleMode> pair, List
                    <SingleMode> items) {
                if (pair != null) {
                    String titleName = "";
                    if (pair.second != null) {
                        titleName = pair.second.getName();
                    } else {
                        titleName = pair.first.getName();
                    }
                    setTitleText(index, titleName);
                }

                if (dropDownListener != null) {
                    dropDownListener.onItemSelected(index, pair, items);
                }
                //if need,dismiss pop here.
                //popHelper.dismiss();
            }
        });
    }


    public void setTitleText(int index, String text) {
        text = TextUtils.isEmpty(text) ? " " : text;
        int length = text.length();
        if (length > 4) {
            text = text.substring(0, 4);
            text += "...";
        }
        anchor.setTitleText(index, text);
        if (fakeDDM != null) {
            fakeDDM.setTitleText(index, text);
        }
    }

    public void updateData(SearchCateConditionBean.ResultBean.BodyBean newBody){
        if (newBody!=null){
            popup.updateData(newBody);
        }
    }

    /**
     * 选中默认的位置
     */
    public void performDefaultMarkPositions(){
        popup.performDefaultMarkPositions();
    }

    public void performMarkIds(String levelOneId,String levelTwoId) {
        StringBuilder sb=new StringBuilder();
        if (TextUtils.isEmpty(levelOneId)) {
            return;
        }
        if (TextUtils.isEmpty(levelTwoId)){
            sb.append(levelOneId);
        }else {
            sb.append(levelOneId);
            sb.append("-&-");
            sb.append(levelTwoId);
        }
        List<String> ids = new ArrayList<>();
        ids.add(sb.toString());
        performMarkIds(ids);
    }

    /**
     * 你希望选中哪些item,把他们的 id 传进去就好了
     * @param ids
     */
    public void performMarkIds(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            popup.performMarkIds(ids);
        }

    }
}

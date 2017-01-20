package com.miguo.ui.view.floatdropdown.helper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.fanwe.utils.MGStringFormatter;
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
    private final String QiuDaiYan="求代言";
    private final String Collection="收藏";
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
        performDefaultMarkPositions();
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
                    String oneTitleName="";
                    String twoTitleName="";
                    String titleName="";
                    if (pair.first!=null){
                        oneTitleName =pair.first.getName();
                    }
                    if (pair.second!=null){
                        twoTitleName=pair.second.getName();
                    }
                    if (!TextUtils.isEmpty(twoTitleName)){
                        titleName = twoTitleName;
                    }
                    if (!TextUtils.isEmpty(oneTitleName) && (QiuDaiYan.equalsIgnoreCase(oneTitleName) || Collection.equalsIgnoreCase(oneTitleName))){
                        titleName = oneTitleName;
                    }
                    if (!TextUtils.isEmpty(oneTitleName) && TextUtils.isEmpty(titleName)){
                        titleName = oneTitleName;
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
        String limitedChinese = MGStringFormatter.getLimitedChinese(text, 5);
        anchor.setTitleText(index, limitedChinese);
        if (fakeDDM != null) {
            fakeDDM.setTitleText(index, limitedChinese);
        }
    }

    public void updateData(SearchCateConditionBean.ResultBean.BodyBean newBody){
        if (newBody!=null){
            popup.updateData(newBody);
            performDefaultMarkPositions();
        }
    }

    /**
     * 选中默认的位置
     */
    public void performDefaultMarkPositions(){
        popup.performDefaultMarkPositions();
    }

    public void performMarkIds(List<Pair<String,String>> pairsIds){
        if (pairsIds==null || pairsIds.size()<=0){
            performDefaultMarkPositions();
            return;
        }
        List<String> ids = new ArrayList<>();
        StringBuilder sb;
        for (Pair<String, String> pairsId : pairsIds) {
            if (pairsId==null)continue;
            String one=pairsId.first;
            String two=pairsId.second;

            sb=new StringBuilder();
            if (TextUtils.isEmpty(one)) {
                continue;
            }
            if (TextUtils.isEmpty(two)){
                sb.append(one);
            }else {
                sb.append(one);
                sb.append("-&-");
                sb.append(two);
            }
            ids.add(sb.toString());
        }
        innerPerformMarkIds(ids);
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
        innerPerformMarkIds(ids);
    }

    /**
     * 你希望选中哪些item,把他们的 id 传进去就好了
     * @param ids
     */
    private void innerPerformMarkIds(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            popup.performMarkIds(ids);
        }

    }
}

package com.miguo.ui.view.dropdown;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.floatdropdown.helper.DropDownMarkBean;
import com.miguo.ui.view.floatdropdown.interf.OnCallDismissPopListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener2;
import com.miguo.ui.view.floatdropdown.view.FilterView;
import com.miguo.ui.view.floatdropdown.view.SingleSideListView;
import com.miguo.ui.view.floatdropdown.view.TwoSideListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by didik 
 * Created time 2017/1/12
 * Description: 
 */

public class DropDownPopup extends PopupWindow{

    private final Activity mHoldActivity;
    private View anchor;
    private String bgColor = "#4D000000";
    private DropDown dropDownView;
    private OnDropDownListener dropDownListener;

    private int animIn= 200;
    private int animOut=250;

    private ValueAnimator fadeIn;
    private ValueAnimator fadeOut;
    private View fakeView;

    //------------------------------
    private ValueAnimator expandAnimator;
    private ValueAnimator reverseAnimator;

    private int expandDuration = 250;
    private int reverseDuration = 300;

    public DropDownPopup(Activity mHoldActivity,View anchor) {
        this.mHoldActivity = mHoldActivity;
        this.anchor = anchor;
        initDropDownLayout();
    }

    private void initDropDownLayout() {
        if (mHoldActivity == null){
            return;
        }
        RelativeLayout root=new RelativeLayout(mHoldActivity);

        LinearLayout rootLayout=new LinearLayout(mHoldActivity);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        dropDownView = new DropDown(mHoldActivity);
//        dropDownView.setBackgroundColor(Color.parseColor(bgColor));
        dropDownView.setBackgroundColor(Color.TRANSPARENT);

        initDDData();

        int height = DisplayUtil.dp2px(mHoldActivity, 355);

        fakeView = new View(mHoldActivity);
        fakeView.setBackgroundColor(Color.parseColor(bgColor));
        fakeView.setAlpha(0f);
        fakeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callDismissPopListener!=null){
                    callDismissPopListener.callDismissPop(false);
                }
            }
        });
        dropDownView.setId(R.id.TOP);
        fakeView.setId(R.id.BOTTOM);
        RelativeLayout.LayoutParams ddParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        ddParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams fakeParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        fakeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fakeParams.addRule(RelativeLayout.BELOW,R.id.TOP);

        root.addView(dropDownView,ddParams);
        root.addView(fakeView,fakeParams);
//        rootLayout.addView(dropDownView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
//        rootLayout.addView(fakeView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(root);
        setAnimationStyle(android.R.style.Animation);//clear pop default animation
        setOutsideTouchable(false);
        setTouchable(true);

        initListener();
        initAnimations();
    }

    private void initAnimations() {
        fadeIn = ValueAnimator.ofFloat(0f,1.0f);
        fadeIn.setDuration(animIn);
        fadeIn.setTarget(fakeView);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                fakeView.setAlpha(animatedValue);
            }
        });

        fadeOut = ValueAnimator.ofFloat(1.0f,0f);
        fadeOut.setDuration(animOut);
        fadeOut.setTarget(fakeView);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                fakeView.setAlpha(animatedValue);
            }
        });
    }
    private OnCallDismissPopListener callDismissPopListener;
    public void setOnCallDismissPopListener(OnCallDismissPopListener callDismissPopListener) {
        this.callDismissPopListener = callDismissPopListener;
    }

    private void initListener() {
        dropDownView.setDismissFinishListener(new DropDown.OnInnerAnimateListener() {

            @Override
            public void startExpand() {
                fadeIn.start();
            }

            @Override
            public void endExpand() {

            }

            @Override
            public void startReverse() {
                fadeOut.start();
            }

            @Override
            public void endReverse() {
                DropDownPopup.super.dismiss();
            }
        });
    }


    public void handleClick(int index){
        if (this.isShowing()){
            dropDownView.onClick(index);
        }
    }


    public void show(){
        if (!mHoldActivity.isFinishing()){
            showAsDropDown(anchor);
        }else {
            MGToast.showToast("Activity is processing or finishing ~~");
        }
    }

    @Override
    public void dismiss() {
        dropDownView.dismiss();
    }

    private SearchCateConditionBean.ResultBean.BodyBean saveBody;
    private void initDDData() {
        SearchCateConditionBean.ResultBean.BodyBean body =  SearchCateConditionFactory.get();
        if (body==null){
            Log.e("test","筛选条初始化失败,没有取到数据!");
            return;
        }
        this.saveBody = body;
        List<TwoMode> item1 = mergeDataForItem1(body.getNearByList(), body
                .getHotAreaList1(), body.getAdminAreaList());
        List<TwoMode> item2 = mergeDataForItem1(body.getCategoryList());
        List<SingleMode> item3 = mergeDataForItem3(body.getIntelList1().get(0).getIntelList2());
        List<TwoMode> item4 = mergeDataForItem1(body.getFilterList1().get(0).getFilterList2());
        handleData(item1,item2,item3,item4);
    }

    private boolean isDataOk(List list){
        return !(list == null || list.size()<=0);
    }

    /**
     * 准备要展示的ContentLayout 里面的View
     */
    protected ArrayList<View> prepareContentView(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4){
        ArrayList<View> views=new ArrayList<>();

        TwoSideListView index1=new TwoSideListView(mHoldActivity);
        TwoSideListView index2=new TwoSideListView(mHoldActivity);
        SingleSideListView index3=new SingleSideListView(mHoldActivity);
        FilterView index4=new FilterView(mHoldActivity);

        index1.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                handleResult(1,new Pair<SingleMode, SingleMode>(levelOne,levelTwo),null);
                dropDownView.dismiss();
            }
        });
        index2.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                handleResult(2,new Pair<SingleMode, SingleMode>(levelOne,levelTwo),null);
                dropDownView.dismiss();
            }
        });
        index3.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                handleResult(3,new Pair<SingleMode, SingleMode>(levelOne,levelTwo),null);
                dropDownView.dismiss();
            }
        });

        index4.setOnDropDownSelectedListener(new OnDropDownSelectedListener2<SingleMode>() {
            @Override
            public void onDropDownSelected(List<SingleMode> singleModes) {
                handleResult(4,null,singleModes);
                dropDownView.dismiss();
            }
        });

        index1.setData(item1);
        index2.setData(item2);
        index3.setData(item3);
        index4.setData(item4);//list<list<singleMode>>

        views.add(index1);
        views.add(index2);
        views.add(index3);
        views.add(index4);
        return views;
    }

    public interface OnTitleTextChangedListener{
        void onTitleTextChange(int index,@NonNull String text);
    }
    private OnTitleTextChangedListener textChangedListener;

    public void setTextChangedListener(OnTitleTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    //------------------------------------------- data ---------------------------------------------
    private void handleData(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4) {
        if (isDataOk(item1) && isDataOk(item2) && isDataOk(item3) && isDataOk(item4)){
            dropDownView.prepareContentView(prepareContentView(item1,item2,item3,item4));
            dropDownView.setInitOk(true);
        }else {
            dropDownView.setInitOk(false);
        }
    }
    private List<TwoMode> mergeDataForItem1(List... array){
        if (array == null || array.length==0)return null;
        List<TwoMode> list=new ArrayList<>();
        for (List<TwoMode> anArray : array) {
            list.addAll(anArray);
        }
        return list;
    }

    private List<SingleMode> mergeDataForItem3(List... array){
        if (array == null || array.length==0)return null;
        List<SingleMode> list=new ArrayList<>();
        for (List<SingleMode> anArray : array) {
            list.addAll(anArray);
        }
        return list;
    }

    private void handleResult(int index, Pair<SingleMode,SingleMode> pair, List<SingleMode> items){
        if (dropDownListener==null){
            return;
        }
        if (index == 4){
            dropDownListener.onItemSelected(4,null,items);
        }else {
            dropDownListener.onItemSelected(index,pair,null);
        }
    }

    public void setDropDownListener(OnDropDownListener dropDownListener) {
        this.dropDownListener=dropDownListener;
    }

    private DropDownMarkBean location1;
    private DropDownMarkBean location2;
    private DropDownMarkBean location3;
    private List<String> location4 =new ArrayList<>();

    public void performMarkIds(List<String> ids) {
        location1 = null;
        location2= null;
        location3 = null;
        location4.clear();
        for (String id : ids) {
            findItemLocation(id);
        }
        if (location1!=null){
            ((TwoSideListView)dropDownView.getContentViewList().get(1)).performPosition(location1.getLevelOne(),location1.getLevelTwo());
            handleTextChange(1,location1.getName());
        }
        if (location2!=null){
            ((TwoSideListView)dropDownView.getContentViewList().get(2)).performPosition(location2.getLevelOne(),location2.getLevelTwo());
            handleTextChange(2,location2.getName());
        }
        if (location3 !=null){
            ((SingleSideListView)dropDownView.getContentViewList().get(3)).performPosition(location3.getLevelOne());
            handleTextChange(3,location3.getName());
        }
        if (location4!=null && location4.size()>0){
            //Do Not Need Text Change
            ((FilterView)dropDownView.getContentViewList().get(4)).performSelectedItems(location4);
        }

    }

    private void handleTextChange(int index,String text){
        if (textChangedListener!=null){
            textChangedListener.onTitleTextChange(index,text);
        }
    }

    /**
     * 找到item的位置
     * @param id 没有二级:返回一级的数据
     *           有二级: 返回二级的id数据
     */
    private void findItemLocation(String id) {
        List<TwoMode> item1 = mergeDataForItem1(saveBody.getNearByList(), saveBody
                .getHotAreaList1(), saveBody.getAdminAreaList());
        List<TwoMode> item2 = mergeDataForItem1(saveBody.getCategoryList());
        List<SingleMode> item3 = mergeDataForItem3(saveBody.getIntelList1().get(0).getIntelList2());
        List<TwoMode> item4 = mergeDataForItem1(saveBody.getFilterList1().get(0).getFilterList2());

        //1
        for (int i = 0; i < item1.size(); i++) {
            List<SingleMode> singleModeList = item1.get(i).getSingleModeList();
            for (int j = 0; j < singleModeList.size(); j++) {
                SingleMode mode = singleModeList.get(j);
                if (id.equalsIgnoreCase(mode.getSingleId())){
                    if (location1==null){
                        location1 = new DropDownMarkBean(i,j,mode.getName());
                    }
                    return;
                }
            }
        }
        //2
        for (int i = 0; i < item2.size(); i++) {
            List<SingleMode> singleModeList = item2.get(i).getSingleModeList();
            for (int j = 0; j < singleModeList.size(); j++) {
                SingleMode mode = singleModeList.get(j);
                if (id.equalsIgnoreCase(singleModeList.get(j).getSingleId())){
                    if (location2==null){
                        location2 = new DropDownMarkBean(i,j,mode.getName());
                    }
                    return;
                }
            }
        }
        //3
        for (int i = 0; i < item3.size(); i++) {
            SingleMode mode = item3.get(i);
            if (id.equalsIgnoreCase(mode.getSingleId())){
                if (location3 == null){
                    location3 = new DropDownMarkBean(i,-1,mode.getName());
                }
                return;
            }

        }

        //4
        for (int i = 0; i < item4.size(); i++) {
            List<SingleMode> singleModeList = item4.get(i).getSingleModeList();
            for (int j = 0; j < singleModeList.size(); j++) {
                SingleMode mode = singleModeList.get(j);
                if (id.equalsIgnoreCase(mode.getSingleId())){
                    location4.add(id);
                    return;
                }
            }
        }

    }
}

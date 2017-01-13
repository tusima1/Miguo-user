package com.miguo.ui.view.dropdown;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.didikee.uilibs.utils.DisplayUtil;
import com.didikee.uilibs.utils.UIColor;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.live.views.customviews.MGToast;
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

public class DropDownPopup extends PopupWindow {

    private final Activity mHoldActivity;
    private View anchor;
    private int bgColor = UIColor.getAlphaColor(66,Color.BLACK);
    private DropDown dropDownView;
    private OnDropDownListener dropDownListener;

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
        rootLayout.setBackgroundColor(Color.WHITE);
        dropDownView = new DropDown(mHoldActivity);
//        DropDownHelper2 helper2=new DropDownHelper2(mHoldActivity, dropDownView);
        
        initDDListener();

        int height = DisplayUtil.dp2px(mHoldActivity, 355);

        View fakeView =new View(mHoldActivity);
        fakeView.setBackgroundColor(bgColor);
        fakeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootLayout.addView(dropDownView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        rootLayout.addView(fakeView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(rootLayout);
        setOutsideTouchable(false);
        setTouchable(true);
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    

    public void handleClick(int index){
        if (this.isShowing()){
            dropDownView.onClick(index);
        }
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

    private void initDDListener() {
        initDDData();
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


    private void handleData(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4) {
        if (isDataOk(item1) && isDataOk(item2) && isDataOk(item3) && isDataOk(item4)){
            dropDownView.prepareContentView(prepareContentView(item1,item2,item3,item4));
            dropDownView.setInitOk(true);
        }else {
            dropDownView.setInitOk(false);
        }
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
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("选中: "+levelOne.getName() +"  id: "+levelOne.getSingleId() +two);
                dropDownView.dismiss();
            }
        });
        index2.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                handleResult(2,new Pair<SingleMode, SingleMode>(levelOne,levelTwo),null);
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("选中: "+levelOne.getName() +"  id: "+levelOne.getSingleId() +two);
                dropDownView.dismiss();
            }
        });
        index3.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                handleResult(3,new Pair<SingleMode, SingleMode>(levelOne,levelTwo),null);
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("一级: "+levelOne.getName() +"  id: "+levelOne.getSingleId()  +two);
                dropDownView.dismiss();
            }
        });

        index4.setOnDropDownSelectedListener(new OnDropDownSelectedListener2<SingleMode>() {
            @Override
            public void onDropDownSelected(List<SingleMode> singleModes) {
                handleResult(4,null,singleModes);
                StringBuilder sb=new StringBuilder();
                sb.append("数量: "+singleModes.size());
                sb.append("\n");
                for (SingleMode mode : singleModes) {
                    String name = mode.getName();
                    sb.append("name: "+ name +"  id: "+mode.getSingleId());
                    sb.append("\n");
                }
                MGToast.showToast(sb.toString());
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
}

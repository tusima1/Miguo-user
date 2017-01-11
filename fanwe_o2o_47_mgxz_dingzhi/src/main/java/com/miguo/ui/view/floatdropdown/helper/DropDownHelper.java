package com.miguo.ui.view.floatdropdown.helper;

import android.content.Context;
import android.view.View;

import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.dropdown.DropDownMenu;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener2;
import com.miguo.ui.view.floatdropdown.view.FilterView;
import com.miguo.ui.view.floatdropdown.view.SingleSideListView;
import com.miguo.ui.view.floatdropdown.view.TwoSideListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/9
 * Description: 
 */

public class DropDownHelper {
    private final Context context;
    private DropDownMenu ddm;

    public DropDownHelper(Context context, DropDownMenu ddm) {
        this.context = context;
        this.ddm = ddm;
    }

    public void setData(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4) {
        if (isDataOk(item1) && isDataOk(item2) && isDataOk(item3) && isDataOk(item4)){
            ddm.prepareContentView(prepareContentView(item1,item2,item3,item4));
        }else {
            ddm.setInitOk(false);
        }
    }

    private boolean isDataOk(List list){
        return !(list == null || list.size()<=0);
    }

    /**
     * 准备要展示的ContentLayout 里面的View
     */
    protected ArrayList<View> prepareContentView(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4){
        ArrayList<View> views=new ArrayList<>();

        TwoSideListView index1=new TwoSideListView(context);
        TwoSideListView index2=new TwoSideListView(context);
        SingleSideListView index3=new SingleSideListView(context);
        FilterView index4=new FilterView(context);

        index1.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode singleMode) {
                MGToast.showToast("选中: "+singleMode.getName());
                ddm.dismiss();
            }
        });
        index2.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode singleMode) {
                MGToast.showToast("选中: "+singleMode.getName());
                ddm.dismiss();
            }
        });
        index3.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode singleMode) {
                MGToast.showToast("选中: "+singleMode.getName());
                ddm.dismiss();
            }
        });

        index4.setOnDropDownSelectedListener2(new OnDropDownSelectedListener2<SingleMode>() {
            @Override
            public void onDropDownSelected(List<SingleMode> t) {
                StringBuilder sb=new StringBuilder();
                for (SingleMode mode : t) {
                    String name = mode.getName();
                    sb.append("name: "+ name);
                    sb.append("\n");
                }
                MGToast.showToast(sb.toString());
                ddm.dismiss();
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

}

package com.miguo.ui.view.floatdropdown.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.dropdown.DropDownMenu;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener2;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;
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
    private FakeDropDownMenu fddm;
    private SearchCateConditionBean.ResultBean.BodyBean saveBody;
    private boolean isFirst= true;

    public DropDownHelper(Context context, DropDownMenu ddm) {
        this.context = context;
        this.ddm = ddm;
        setData();
    }
    public DropDownHelper(Context context, DropDownMenu ddm, FakeDropDownMenu fddm) {
        this.context = context;
        this.ddm = ddm;
        this.fddm = fddm;
        setData();
    }


    private void setData(){
        //SearchCateConditionBean.ResultBean.BodyBean body
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

        ddm.setOnDropDownTitleClickListener(new DropDownMenu.OnDropDownTitleClickListener() {
            @Override
            public void onAfter(View v, int index) {
//                if (isFirst && index == 2){
//                    Pair<Integer, Integer> integerIntegerPair = handleItemId2Check("14");
//                    TwoSideListView view = (TwoSideListView) ddm.getContentViewList().get(2);
//                    if (integerIntegerPair!=null){
//                        view.handlePerformClick(integerIntegerPair.first,integerIntegerPair.second);
//                    }else {
//                        Log.e("test","-----------------------------");
//                    }
//                    isFirst = false;
//                }
                //...
            }
        });
    }

    public void handleItem(String id){
        //
        ((TwoSideListView)ddm.getContentViewList().get(2)).performPosition(2,0);
    }


    private boolean isDataOk(List list){
        return !(list == null || list.size()<=0);
    }


    private void handleData(List<TwoMode> item1,List<TwoMode> item2 ,List<SingleMode> item3,List item4) {
        if (isDataOk(item1) && isDataOk(item2) && isDataOk(item3) && isDataOk(item4)){
            ddm.prepareContentView(prepareContentView(item1,item2,item3,item4));
            ddm.setInitOk(true);
        }else {
            ddm.setInitOk(false);
        }
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
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("选中: "+levelOne.getName() +"  id: "+levelOne.getSingleId() +two);
                setDropDownMenuText(1,levelOne.getName());
                ddm.dismiss();
            }
        });
        index2.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("选中: "+levelOne.getName() +"  id: "+levelOne.getSingleId() +two);
                setDropDownMenuText(2,levelOne.getName());
                ddm.dismiss();
            }
        });
        index3.setOnDropDownSelectedListener(new OnDropDownSelectedListener<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, SingleMode levelTwo) {
                String two = "";
                if (levelTwo !=null){
                    two =" \n"+ "二级: "+levelTwo.getName() +"  id: "+ levelTwo.getSingleId();
                }
                MGToast.showToast("一级: "+levelOne.getName() +"  id: "+levelOne.getSingleId()  +two);
                setDropDownMenuText(3,levelOne.getName());
                ddm.dismiss();
            }
        });

        index4.setOnDropDownSelectedListener(new OnDropDownSelectedListener2<SingleMode>() {
            @Override
            public void onDropDownSelected(SingleMode levelOne, List<SingleMode> levelTwo) {
                StringBuilder sb=new StringBuilder();
                for (SingleMode mode : levelTwo) {
                    String name = mode.getName();
                    sb.append("name: "+ name +"  id: "+mode.getSingleId());
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

    private void setDropDownMenuText(int index,String text){
        ddm.setTitleTabText(index-1,text);
    }

    /**
     * 目前只有item2 是会传id 过来,先只做2的判断
     * @param id 14
     */
    public Pair<Integer,Integer> handleItemId2Check(String id){
        int left = -1;
        int right = -1;
        if (TextUtils.isEmpty(id)){
            Log.e("test","handleItemId22Check: "+id);
            return null;
        }
        List<TwoMode> item2 = mergeDataForItem1(saveBody.getCategoryList());
        //二级分类的数据
//        for (TwoMode twoMode : item2) {
//            List<SingleMode> singleModeList = twoMode.getSingleModeList();
//            for (SingleMode mode : singleModeList) {
//                if (id.equalsIgnoreCase(mode.getSingleId())){
//                    twoMode.setChecked(true);
//                    mode.setChecked(true);
//                    return item2;
//                }
//            }
//        }

        for (int i = 0; i < item2.size(); i++) {
            List<SingleMode> singleModeList = item2.get(i).getSingleModeList();
            for (int j = 0; j < singleModeList.size(); j++) {
                SingleMode mode = singleModeList.get(j);
                if (id.equalsIgnoreCase(mode.getSingleId())){
//                    twoMode.setChecked(true);
//                    setChecked(true);
                    return new Pair<>(i,j);
                }
            }
        }
        return null;
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
}

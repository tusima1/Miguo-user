package com.miguo.live.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguo.live.views.customviews.GiftItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/9/11.
 */
public class GiftViewPagerAdapter extends PagerAdapter {

    private boolean isSelected=false;
    private List<View> viewList=new ArrayList<>();

    public GiftViewPagerAdapter(Context mContext,List data,TextView tv_send) {
        if (data!=null && data.size()>0){
            for (int i = 0; i < data.size(); i++) {
                //TODO 装载数据
            }
        }

        GiftItemView item1=new GiftItemView(mContext);
        item1.setText(tv_send);

        GiftItemView item2=new GiftItemView(mContext);
        item2.setText(tv_send);

        GiftItemView item3=new GiftItemView(mContext);
        item3.setText(tv_send);
        viewList.add(item1);
        viewList.add(item2);
        viewList.add(item3);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view,params);
        return view;
    }

    @Override
    public int getCount() {
        return viewList==null ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public boolean getSelected(int position){
        View view = viewList.get(position);
        boolean selected = false;
        if (view instanceof GiftItemView){
            selected=((GiftItemView)view).getSelected();
            Log.e("test","view instanceof GiftItemView :"+selected);
        }
        return selected;
    }

    public Object getSelectedItemInfo(){

        return "hh";
    }
}

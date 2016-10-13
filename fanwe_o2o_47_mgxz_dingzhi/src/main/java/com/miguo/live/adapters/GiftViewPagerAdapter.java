package com.miguo.live.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.views.customviews.GiftItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/9/11.
 */
public class GiftViewPagerAdapter extends PagerAdapter {

    private TextView tv_send;
    private Context mContext;
    private boolean isSelected=false;
    private List<View> viewList=new ArrayList<>();
    private List<GiftListBean> mData;

    public GiftViewPagerAdapter(Context mContext,List<GiftListBean> data,TextView tv_send) {
        this.mContext=mContext;
        this.tv_send=tv_send;
        this.mData=data;
        createView();
    }

    private void createView(){
        viewList.clear();
        int count;
        if (mData!=null && mData.size()>0){
            int size = mData.size();
            if (size%8!=0){
                count=size/8+1;
            }else {
                count=size/8;
            }
            for (int i = 0; i < count; i++) {
                //TODO 装载数据
                GiftItemView item=new GiftItemView(mContext);
                List<GiftListBean> temp=new ArrayList<>();
                for (int j = i*8; j < (i+1)*8; j++) {
                    if (j>=size){
                        break;
                    }
                    temp.add(mData.get(j));
                }
                item.setData(temp);
                item.setText(tv_send);
                viewList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void setData(List<GiftListBean> data){
        this.mData=data;
        createView();
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

    public GiftListBean getSelectedItemInfo(int position){
        GiftItemView view = (GiftItemView) viewList.get(position);
        return view.getSelectedItemInfo();
    }
}

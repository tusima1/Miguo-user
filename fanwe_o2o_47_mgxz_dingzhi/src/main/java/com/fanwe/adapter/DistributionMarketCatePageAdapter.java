package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.library.adapter.SDBasePagerAdapter;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.customview.SDGridLinearLayout.OnItemClickListener;

import java.util.List;

public class DistributionMarketCatePageAdapter extends SDBasePagerAdapter<List<ModelHomeClassifyList>> {

    private OnClickCateItemListener mListenerOnClickCateItem;

    public void setmListenerOnClickCateItem(OnClickCateItemListener listenerOnClickCateItem) {
        this.mListenerOnClickCateItem = listenerOnClickCateItem;
    }

    public DistributionMarketCatePageAdapter(List<List<ModelHomeClassifyList>> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getView(View container, int position) {
        final SDGridLinearLayout ll = new SDGridLinearLayout(mActivity);
        ll.setmColNumber(5);
        final DistributionMarketCateAdapter adapter = new DistributionMarketCateAdapter(getItemModel(position), mActivity);
        ll.setmListenerOnItemClick(new OnItemClickListener() {

            @Override
            public void onItemClick(int position, View view, ViewGroup parent) {
                if (mListenerOnClickCateItem != null) {
                    mListenerOnClickCateItem.onClickItem(position, view, adapter.getItem(position));
                }
            }
        });
        ll.setAdapter(adapter);
        return ll;
    }

    public interface OnClickCateItemListener {
        void onClickItem(int position, View view, ModelHomeClassifyList model);
    }

}

package com.miguo.adapter.base;

import android.content.Context;
import android.view.View;

import com.fanwe.library.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/11/11.
 */

public abstract class SimpleRVAdapter<T> extends BaseRVAdapter<T,BaseRVViewHolder> {
    private List<T> preData =new ArrayList<>();
    private int page =1;

    public SimpleRVAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleRVAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    public SimpleRVAdapter(Context context, int layoutResId, List<T> data, View emptyLayout) {
        super(context, layoutResId, data, emptyLayout);
    }

    @Override
    protected abstract void bindView(BaseRVViewHolder helper, T item);

    private void clear(){
        preData.clear();
        data.clear();
    }

    public void setData(List<T> body) {
        if (page==1){
            clear();
        }
        if (compareOldNewData(body)){
            notifyDataSetChanged();
        }else {
            preData.clear();
            SDToast.showToast("没有更多数据");
        }
        toggleEmptyLayout();
    }

    /**
     *
     * @param news 新数据集
     * @return false 没有新数据
     *          true 有新数据
     */
    private boolean compareOldNewData(List<T> news){
        if (news ==null)return false;
        if (news.isEmpty())return false;
        if (news.size() ==10){
            page++;
            data.addAll(news);
        }else {
            data.removeAll(preData);
            data.addAll(news);
            preData.clear();
            preData.addAll(news);
        }
        return true;
    }
    public int getRequestPage(){
        return page;
    }

    public void resetPage(){
        page=1;
    }
}

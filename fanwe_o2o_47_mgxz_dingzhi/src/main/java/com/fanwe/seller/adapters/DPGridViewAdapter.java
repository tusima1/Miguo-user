package com.fanwe.seller.adapters;

/**
 * Created by zhouhy on 2017/1/11.
 */

import android.content.Context;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.TypeModel;
import com.fanwe.seller.views.customize.DPAdapterViewHolder;
import com.miguo.entity.SearchCateConditionBean;

import java.util.List;

public class DPGridViewAdapter extends DPBaseAdapter<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>{

    public DPGridViewAdapter(Context context, List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void convert(DPAdapterViewHolder holder, SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean model) {
        if(model==null){
            return;
        }
        if(model.isChecked()) {
            holder.setImageResourceAndText(R.id.gridtextview,R.drawable.btn_orange_shape7, model.getName(),"#FFFFFFFF");
        }else{
            holder.setImageResourceAndText(R.id.gridtextview,R.drawable.btn_white_shape, model.getName(),"#FFCCCCCC");
        }
    }

}

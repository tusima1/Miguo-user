package com.fanwe.seller.adapters;

/**
 * Created by zhouhy on 2017/1/11.
 */

import android.content.Context;
import android.widget.TextView;


import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.TypeModel;
import com.fanwe.seller.views.customize.DPAdapterViewHolder;

import java.util.List;

public class DPGridViewAdapter extends DPBaseAdapter<TypeModel>{

    public DPGridViewAdapter(Context context, List<TypeModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void convert(DPAdapterViewHolder holder, TypeModel model) {
        if(model==null){
            return;
        }
        if(model.isIfSelected()) {
            holder.setImageResourceAndText(R.id.gridtextview,R.drawable.btn_orange_shape7, model.getContent(),"#FFFFFFFF");
        }else{
            holder.setImageResourceAndText(R.id.gridtextview,R.drawable.btn_white_shape, model.getContent(),"#FFCCCCCC");
        }
    }

}

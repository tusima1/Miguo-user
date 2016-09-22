package com.fanwe.reward.adapters;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.reward.model.DiamondTypeEntity;

public class DiamondGridAdapter extends BaseAdapter {

    private List<DiamondTypeEntity> datas = null;
    private LayoutInflater mInflater;
    private Context mContext;


    public DiamondGridAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }
    @Override
    public Object getItem(int position) {
        if(datas==null){
            return null;
        }
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recharge_diamond_item, null);
            viewTag = new ItemViewTag((LinearLayout) convertView.findViewById(R.id.diamond_line),(TextView) convertView.findViewById(R.id.diamond_value), (TextView) convertView.findViewById(R.id.money_value));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
       final DiamondTypeEntity entity = datas.get(position);
        if(entity == null){
            return convertView;
        }
        if(entity.isChecked()){
            setCheckedColor(viewTag,true);
        }else{
            setCheckedColor(viewTag,false);
        }
        // set name
        viewTag.mDiamond.setText(entity.getDiamond());
        // set icon
        viewTag.mMoney.setText("￥ "+entity.getPrice());


        return convertView;
    }


    public void setCheckedColor(ItemViewTag viewTag,boolean checked){
        if(checked) {
            viewTag.mDiamond.setTextColor(mContext.getResources().getColor(R.color.white));
            viewTag.mMoney.setTextColor(mContext.getResources().getColor(R.color.white));
            viewTag.diamond_line.setBackground(mContext.getResources().getDrawable(R.drawable.bg_orange_small));
        }else{
            viewTag.mDiamond.setTextColor(mContext.getResources().getColor(R.color.main_color));
            viewTag.mMoney.setTextColor(mContext.getResources().getColor(R.color.main_color_press));
            viewTag.diamond_line.setBackground(mContext.getResources().getDrawable(R.drawable.bg_orange_smallline));
        }
    }

    public List<DiamondTypeEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<DiamondTypeEntity> datas) {
        this.datas = datas;
    }

    class ItemViewTag {
        protected  LinearLayout diamond_line;
        protected TextView mDiamond;
        protected TextView mMoney;

        /**
         *
         * @param diamond_line
         * @param mDiamond
         * @param mMoney
         */
        public ItemViewTag( LinearLayout diamond_line,TextView mDiamond, TextView mMoney) {
            this.diamond_line = diamond_line;
            this.mDiamond = mDiamond;
            this.mMoney = mMoney;
        }
    }


}

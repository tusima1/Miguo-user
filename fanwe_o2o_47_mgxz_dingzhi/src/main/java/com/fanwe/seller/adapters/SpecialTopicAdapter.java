package com.fanwe.seller.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.Text.UICharacterCount;
import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getSpecialTopic.DetailListBean;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/10/18.
 */

public class SpecialTopicAdapter extends BaseAdapter {

    private List<DetailListBean> mData=new ArrayList<>();

    public void updateData(List<DetailListBean> newData){
        if (newData!=null && newData.size()>0){
            mData.addAll(newData);
            notifyDataSetChanged();
        }
    }

    public void setData(List<DetailListBean> data){
        mData.clear();
        updateData(data);
    }

    @Override
    public int getCount() {
        return mData==null ? 0:mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        STViewHolder holder;
        if (convertView==null){
            holder=new STViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specail_topic_listview,null);
            holder.iv_img= (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_location= (TextView) convertView.findViewById(R.id.tv_location);
            holder.tv_type= (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_price_tuan= (TextView) convertView.findViewById(R.id.tv_price_tuan);
            holder.tv_price_original= (TextView) convertView.findViewById(R.id.tv_price_original);
            holder.layout_tags= (LinearLayout) convertView.findViewById(R.id.layout_tags);
            convertView.setTag(holder);
        }
        holder= (STViewHolder) convertView.getTag();
        DetailListBean detailListBean = mData.get(position);
        final int margin = DisplayUtil.dp2px(parent.getContext(), 5);
        final int padding = DisplayUtil.dp2px(parent.getContext(), 1);
        if (detailListBean!=null){
            SDViewBinder.setImageView(detailListBean.getIcon(),holder.iv_img);
            holder.tv_location.setText(getLocationInfo(detailListBean.getArea_name(),detailListBean.getDistance(),""));
            holder.tv_name.setText(detailListBean.getTitle());
            String type = detailListBean.getType();
            //TODO 类型

            holder.tv_price_original.setText(detailListBean.getOrigin_price()+"元");
            holder.tv_price_original.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

            holder.tv_price_tuan.setText(detailListBean.getTuan_price()+"元/张");

            //添加tags
            List<DetailListBean.TagListBean> tag_list = detailListBean.getTag_list();
            holder.layout_tags.removeAllViews();
            if (tag_list!=null && tag_list.size()>0){
                int size = tag_list.size();
                size=size>5? 5:size;
                for (int i = 0; i < size; i++) {
                    DetailListBean.TagListBean tagListBean = tag_list.get(i);
                    TextView item=new TextView(parent.getContext());
                    item.setBackgroundResource(R.drawable.shape_solid_f5b830_cricle_small);
                    item.setTextColor(Color.WHITE);
                    item.setTextSize(12);
                    item.setGravity(Gravity.CENTER);
                    item.setText(tagListBean.getTitle());
                    item.setPadding(margin,padding,margin,padding);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                            .LayoutParams.WRAP_CONTENT,ViewGroup
                            .LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity= Gravity.CENTER_VERTICAL;
                    layoutParams.setMargins(0,0,margin,0);
                    item.setLayoutParams(layoutParams);
                    int childCount = holder.layout_tags.getChildCount();
                    if (childCount<5){
                        holder.layout_tags.addView(item);
                    }
                }
            }
        }

        return convertView;
    }

    private String getLocationInfo(String mapLocation,String distance,String location){
        return getLimitedString(mapLocation,6)+" | "+ SDDistanceUtil.getFormatDistance(DataFormat.toDouble(distance)) +" | "+getLimitedString(location,4);
    }

    private String getLimitedString(String text,int limitNum){
        if (TextUtils.isEmpty(text)){
            return "";
        }
        int length = Math.round(UICharacterCount.getCount(text));
        if (length<=limitNum){
            return text;
        }else if (length>limitNum){
            return text.substring(0,limitNum-1)+"...";
        }
        return "";
    }

    private class STViewHolder{
        public ImageView iv_img;//图片-

        public TextView tv_name;//标题
        public TextView tv_location;//位置
        public TextView tv_type;//类型
        public TextView tv_price_tuan;//团购价
        public TextView tv_price_original;//原价
        public LinearLayout layout_tags;//tags
    }
}

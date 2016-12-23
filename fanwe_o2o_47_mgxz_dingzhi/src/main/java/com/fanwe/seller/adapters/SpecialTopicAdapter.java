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
import com.fanwe.utils.MGStringFormatter;
import com.fanwe.utils.SDDistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/10/18.
 */

public class SpecialTopicAdapter extends BaseAdapter {

    private List<DetailListBean> mData=new ArrayList<>();

    public void addData(List<DetailListBean> newData){
        if (newData!=null && newData.size()>0){
            mData.addAll(newData);
            notifyDataSetChanged();
        }
    }

    public void removeData(List<DetailListBean> sameData){
        if (sameData!=null && sameData.size()>0){
            mData.removeAll(sameData);
//            notifyDataSetChanged();
        }
    }

    public void setData(List<DetailListBean> data){
        mData.clear();
        addData(data);
    }

    public List<DetailListBean> getData(){
        return mData;
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_groupon,null);
            holder.iv_img= (ImageView) convertView.findViewById(R.id.iv_img_item_featured_groupon);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name_item_featured_groupon);
            holder.tv_location= (TextView) convertView.findViewById(R.id.tv_location_item_featured_groupon);
            holder.tv_price_tuan= (TextView) convertView.findViewById(R.id.tv_price_tuan_item_featured_groupon);
            holder.tv_price_original= (TextView) convertView.findViewById(R.id.tv_price_original_item_featured_groupon);
            holder.tv_salary= (TextView) convertView.findViewById(R.id.tv_salary_item_featured_groupon);
            holder.layout_tags= (LinearLayout) convertView.findViewById(R.id.layout_tags_item_featured_groupon);
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
            holder.tv_price_original.setText(detailListBean.getOrigin_price()+"元");
            holder.tv_price_original.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);


            String float2 = MGStringFormatter.getFloat2(detailListBean.getSalary());
            float aFloat = MGStringFormatter.getFloat(float2);
            if (aFloat <=0){
                holder.tv_salary.setVisibility(View.GONE);
            }else {
                holder.tv_salary.setVisibility(View.VISIBLE);
                holder.tv_salary.setText(float2+"元佣金");
            }

            holder.tv_price_tuan.setText(detailListBean.getTuan_price_with_unit());

            //添加tags
            List<DetailListBean.TagListBean> tag_list = detailListBean.getTag_list();
            holder.layout_tags.removeAllViews();
            if (tag_list!=null && tag_list.size()>0){
                int size = tag_list.size();
                size=size>5? 5:size;
                for (int i = 0; i < size; i++) {
                    DetailListBean.TagListBean tagListBean = tag_list.get(i);
                    TextView item=new TextView(parent.getContext());
                    item.setBackgroundResource(R.drawable.bg_small_f5b830);
                    item.setTextColor(Color.parseColor("#f5b830"));
                    item.setTextSize(10);
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
        String tempMapLocation = getLimitedString(mapLocation, 6);
        String tempDistance = SDDistanceUtil.getFormatDistance(DataFormat.toDouble(distance));
        String tempLocation = getLimitedString(location, 4);
        if (!TextUtils.isEmpty(tempMapLocation)){
            tempMapLocation+=" | ";
        }else {
            tempMapLocation=".. | ";
        }
//        if (!TextUtils.isEmpty(tempLocation)&&!TextUtils.isEmpty(tempDistance)){
//            tempDistance+=" | ";
//        }
        if (TextUtils.isEmpty(tempLocation)){
            tempLocation="..";
        }
        if (TextUtils.isEmpty(tempDistance)){
            tempDistance="..";
        }
        return tempMapLocation+ tempDistance +" | "+tempLocation;
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
        public TextView tv_salary;//佣金
        public LinearLayout layout_tags;//tags
    }
}

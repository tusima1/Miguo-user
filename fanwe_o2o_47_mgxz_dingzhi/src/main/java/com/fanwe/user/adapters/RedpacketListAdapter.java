package com.fanwe.user.adapters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getUserRedpackets.ModelUserRedPacket;
import com.fanwe.utils.MGStringFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class RedpacketListAdapter extends BaseAdapter {

    private int orangeColor=Color.parseColor("#ff6600");
    private int tempYellowColor=Color.parseColor("#ffd1b2");
    private int grayColor=Color.parseColor("#cccccc");

    private boolean isCheckMode;//为true可选

    List<ModelUserRedPacket> mData;

    private String mRedIds;


    public RedpacketListAdapter(List<ModelUserRedPacket> mData,boolean isCheckMode) {
        this.mData = mData;
        this.isCheckMode=isCheckMode;
    }

    /**
     * 更新数据
     * @param mData ModelUserRedPacket
     */
    public void update(List<ModelUserRedPacket> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData==null?0:mData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_redpacket_positive,null);
            holder.mTv_Time = ((TextView) convertView.findViewById(R.id.tv_time));
            holder.mTv_TitleTag = ((TextView) convertView.findViewById(R.id.tv_tag));
            holder.mTv_BigNum = ((TextView) convertView.findViewById(R.id.tv_big_num));
            holder.packet_type = ((TextView) convertView.findViewById(R.id.packet_type));
            holder.mTv_Title = ((TextView) convertView.findViewById(R.id.tv_title));
            holder.mTv_Content = ((TextView) convertView.findViewById(R.id.tv_content));
            holder.mIv_WaterMark = ((ImageView) convertView.findViewById(R.id.iv_watermark));
            holder.fr_bg_down= (FrameLayout) convertView.findViewById(R.id.fr_bg_down);
            holder.mCb_check = (CheckBox) convertView.findViewById(R.id.iv_check);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();


        //bind data
        ModelUserRedPacket modelUserRedPacket = mData.get(position);
        final boolean check = modelUserRedPacket.isChecked();
        final String shopId = modelUserRedPacket.getId();
        final String redId = modelUserRedPacket.getRed_packet_id();
        //确定模式
        if (isCheckMode){
            holder.mCb_check.setVisibility(View.VISIBLE);
            holder.mCb_check.setChecked(check);
            holder.mCb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    mData.get(position).setChecked(checked);
                    updateCheckList(shopId,redId);
                }
            });
        }else {
            holder.mCb_check.setVisibility(View.GONE);
        }
        //两种状态
        String event_flag = modelUserRedPacket.getEvent_flag();//红包过期标志
        String is_used = modelUserRedPacket.getIs_used();//是否使用
        if ("0".equals(event_flag)){
            //已经无效 已过期
            holder.packet_type.setTextColor(grayColor);
            holder.mTv_BigNum.setTextColor(grayColor);
            holder.mTv_TitleTag.setTextColor(grayColor);
            holder.mIv_WaterMark.setImageResource(R.drawable.bg_red_disable);
            holder.fr_bg_down.setBackground(parent.getResources().getDrawable(R.drawable.shape_live_bg_red_down));
            holder.mIv_WaterMark.setVisibility(View.VISIBLE);
        }else if ("1".equals(is_used)){
            //已经使用了
            holder.packet_type.setTextColor(grayColor);
            holder.mTv_BigNum.setTextColor(grayColor);
            holder.mTv_TitleTag.setTextColor(grayColor);
            holder.mIv_WaterMark.setImageResource(R.drawable.bg_red_has_used);
            holder.fr_bg_down.setBackground(parent.getResources().getDrawable(R.drawable.shape_live_bg_red_down));
            holder.mIv_WaterMark.setVisibility(View.VISIBLE);
        }else {
            //可用的
            holder.packet_type.setTextColor(orangeColor);
            holder.mTv_BigNum.setTextColor(orangeColor);
            holder.mTv_TitleTag.setTextColor(tempYellowColor);
            holder.fr_bg_down.setBackground(parent.getResources().getDrawable(R.drawable.shape_live_bg_orange_down));
            holder.mIv_WaterMark.setVisibility(View.GONE);
        }
        holder.mTv_Title.setText(modelUserRedPacket.getRed_packet_name());
        holder.mTv_Content.setText(modelUserRedPacket.getSpecial_note());
        String dateStart = MGStringFormatter.getDate(modelUserRedPacket.getEvent_start());
        String dateEnd = MGStringFormatter.getDate(modelUserRedPacket.getEvent_end());
        if (TextUtils.isEmpty(dateStart)|| TextUtils.isEmpty(dateEnd)){
            holder.mTv_Time.setText("永久有效");
        }else {
            StringBuilder sb=new StringBuilder();
            sb.append("有效期:");
            sb.append(dateStart);
            sb.append("--");
            sb.append(dateEnd);
            holder.mTv_Time.setText(sb.toString());
        }
        //bind content
        String red_packet_amount = modelUserRedPacket.getRed_packet_amount();
        String red_packet_type = modelUserRedPacket.getRed_packet_type();
        //红包类型 1:打折  2:现金券
        if ("1".equals(red_packet_type)){
            holder.mTv_BigNum.setText(MGStringFormatter.getFloat2(red_packet_amount));
            holder.packet_type.setText("折");
            holder.mTv_TitleTag.setText("折扣券");
        }else if ("2".equals(red_packet_type)){
            holder.mTv_BigNum.setText(MGStringFormatter.getFloat2(red_packet_amount));
            holder.packet_type.setText("元");
            holder.mTv_TitleTag.setText("现金券");
        }
        return convertView;
    }


    /**
     * 设置红包被 选择列表
     * @param id  商家 ID
     * @param redId 红包ID
     */
    public void updateCheckList(String id, String redId){
        if(TextUtils.isEmpty(redId)){
            return;
        }
        int size = mData.size();
        for(int i = 0 ; i < size;i++){
            ModelUserRedPacket modelUserRedPacket = mData.get(i);
            if(modelUserRedPacket.getId().equals(id)&&!modelUserRedPacket.getRed_packet_id().equals(redId)){
                modelUserRedPacket.setChecked(false);
            }
        }
        notifyDataSetChanged();

    }
    /**
     * 获取被选中的item
     * @return 被选中的集合
     */
    public List<ModelUserRedPacket> getSelectedItem(){
        List<ModelUserRedPacket> selectedItems=new ArrayList<>();
        for (ModelUserRedPacket modelUserRedPacket : mData) {
            if (modelUserRedPacket.isChecked()){
                selectedItems.add(modelUserRedPacket);
            }
        }
        return selectedItems;
    }

    /**
     * 获取被选中的item ids.
     * @return 被选中的集合
     */
    public ArrayList<String>  getSelectedItemIds(){
        ArrayList<String> selectedItems= new ArrayList<>();

        for (ModelUserRedPacket modelUserRedPacket : mData) {
            if (modelUserRedPacket.isChecked()){
                selectedItems.add(modelUserRedPacket.getRed_packet_id());
            }
        }
        return selectedItems;

    }


    public String getmRedIds() {
        return mRedIds;
    }

    public void setmRedIds(String mRedIds) {
        this.mRedIds = mRedIds;
    }

    private class ViewHolder{
        public TextView mTv_Time;//有效期
        public TextView mTv_TitleTag;//类型(专属优惠)
        public TextView mTv_Title;//标题
        public TextView mTv_Content;//内容
        public TextView packet_type;//红包类型
        /*金额 内容*/
        public TextView mTv_BigNum;//打几折?例如8折,就是8,只能是数字
        public ImageView mIv_WaterMark;//水印
        public FrameLayout fr_bg_down;//下部分的背景
        public CheckBox mCb_check;//是否选中
    }
}

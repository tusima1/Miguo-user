package com.fanwe.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getAttentionFocus.ModelAttentionFocus;
import com.fanwe.utils.MGStringFormatter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 充值记录适配器
 */

public class AttentionListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelAttentionFocus> datas;

    public AttentionListAdapter(Context mContext, LayoutInflater layoutInflater, List<ModelAttentionFocus> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(R.layout.item_attention_list, null);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_attention);
            mHolder.tvSign = (TextView) convertView.findViewById(R.id.tv_signature_item_attention);
            mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status_item_attention);
            mHolder.ivIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon_item_attention);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    ModelAttentionFocus currModle;

    private void setData(Holder mHolder, final int position) {
        currModle = datas.get(position);
        mHolder.tvName.setText(currModle.getNick());
        mHolder.tvSign.setText(MGStringFormatter.getLimitedString(currModle.getPersonality(), 13));
        if ("1".equals(currModle.getAttention_status())) {
            mHolder.tvStatus.setText("未关注");
        } else if ("2".equals(currModle.getAttention_status())) {
            mHolder.tvStatus.setText("已关注");
        } else if ("3".equals(currModle.getAttention_status())) {
            mHolder.tvStatus.setText("互相关注");
        } else {
            mHolder.tvStatus.setText("");
        }
        ImageLoader.getInstance().displayImage(currModle.getIcon(), mHolder.ivIcon);
    }


    private static class Holder {
        TextView tvName, tvSign, tvStatus;
        CircleImageView ivIcon;
    }

}

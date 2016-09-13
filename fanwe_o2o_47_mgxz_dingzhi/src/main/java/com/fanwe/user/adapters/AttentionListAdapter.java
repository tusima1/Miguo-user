package com.fanwe.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getAttentionFocus.ModelAttentionFocus;

import java.util.List;

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
        Holder mHolder = null;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(R.layout.item_attention_list, null);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_attention);

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
    }


    private static class Holder {
        TextView tvName;
    }

}

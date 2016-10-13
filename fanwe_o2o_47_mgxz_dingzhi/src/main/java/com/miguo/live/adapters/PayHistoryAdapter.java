package com.miguo.live.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PinnedSectionListView;
import com.miguo.live.model.payHistory.ModelPayHistory;

import java.util.List;

/**
 * 充值记录适配器
 */

public class PayHistoryAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelPayHistory> datas;
    private final int ITEM = 0;
    private final int TITLE = 1;

    public PayHistoryAdapter(Context mContext, LayoutInflater layoutInflater, List<ModelPayHistory> datas) {
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
            convertView = inflater.inflate(R.layout.item_pay_history, null);
            mHolder.viewLine = (View) convertView.findViewById(R.id.view_line);
            mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_item_pay_history);
            mHolder.layoutItem = (RelativeLayout) convertView.findViewById(R.id.layout_item_pay_history);
            mHolder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money_item_pay_history);
            mHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num_item_pay_history);
            mHolder.tvOrder = (TextView) convertView.findViewById(R.id.tv_order_item_pay_history);
            mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status_item_pay_history);
            mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time_item_pay_history);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    ModelPayHistory currModle;

    private void setData(Holder mHolder, final int position) {
        currModle = datas.get(position);
        if (TITLE == currModle.getType()) {
            mHolder.tvTitle.setVisibility(View.VISIBLE);
            mHolder.layoutItem.setVisibility(View.GONE);
            mHolder.viewLine.setVisibility(View.GONE);

            mHolder.tvTitle.setText(currModle.getDate());
        } else {
            mHolder.tvTitle.setVisibility(View.GONE);
            mHolder.layoutItem.setVisibility(View.VISIBLE);
            mHolder.viewLine.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(currModle.getPrice())) {
                SDViewBinder.setTextView(mHolder.tvMoney, "支付¥" + currModle.getPrice());
            } else {
                SDViewBinder.setTextView(mHolder.tvMoney, "");
            }
            if (!TextUtils.isEmpty(currModle.getDiamond_count())) {
                SDViewBinder.setTextView(mHolder.tvNum, "购买" + currModle.getDiamond_count() + "钻");
            } else {
                SDViewBinder.setTextView(mHolder.tvNum, "");
            }
            if (!TextUtils.isEmpty(currModle.getOrder_id())) {
                SDViewBinder.setTextView(mHolder.tvOrder, "订单号：" + currModle.getOrder_id());
            } else {
                SDViewBinder.setTextView(mHolder.tvOrder, "");
            }
            if (!TextUtils.isEmpty(currModle.getOrder_id())) {
                if ("1".equals(currModle.getStatus())) {
                    //成功
                    mHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_home_recommend));
                    SDViewBinder.setTextView(mHolder.tvStatus, "充值成功");
                } else {
                    //失败
                    mHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.c_FF3000));
                    SDViewBinder.setTextView(mHolder.tvStatus, "充值失败");
                }
            } else {
                SDViewBinder.setTextView(mHolder.tvStatus, "");
            }

            SDViewBinder.setTextView(mHolder.tvTime, currModle.getDate());

        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TITLE;
    }

    private static class Holder {
        TextView tvTitle, tvMoney, tvNum, tvOrder, tvStatus, tvTime;
        RelativeLayout layoutItem;
        View viewLine;
    }

}

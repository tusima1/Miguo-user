package com.fanwe.mine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.handmark.pulltorefresh.library.PinnedSectionListView;
import com.miguo.definition.IntentKey;
import com.miguo.live.views.utils.BaseUtils;

import java.util.List;

/**
 * 代言收益适配器
 */

public class RepresentIncomeAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelCommissionLog> datas;
    private final int ITEM = 0;
    private final int TITLE = 1;

    public RepresentIncomeAdapter(Context mContext, LayoutInflater layoutInflater, List<ModelCommissionLog> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder mHolder = null;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(R.layout.item_represent_income, null);
            mHolder.viewLine = (View) convertView.findViewById(R.id.view_line);
            mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_item_represent_income);
            mHolder.layoutItem = (RelativeLayout) convertView.findViewById(R.id.layout_item_represent_income);
            mHolder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money_item_represent_income);
            mHolder.tvOrder = (TextView) convertView.findViewById(R.id.tv_order_item_represent_income);
            mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time_item_represent_income);
            mHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type_item_represent_income);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelCommissionLog currModle;
                currModle = datas.get(position);
                if(TITLE == currModle.getType()){
                    return;
                }

                if(currModle.isJumpToGoodsDetail() && !TextUtils.isEmpty(currModle.getJump_id())){
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, currModle.getJump_id());
                    BaseUtils.jumpToNewActivity((Activity) mContext, intent);
                }

            }
        });

        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        ModelCommissionLog currModle;

        currModle = datas.get(position);
        if (TITLE == currModle.getType()) {
            mHolder.tvTitle.setVisibility(View.VISIBLE);
            mHolder.layoutItem.setVisibility(View.GONE);
            mHolder.viewLine.setVisibility(View.GONE);

            mHolder.tvTitle.setText(currModle.getInsert_time());
        } else {
            mHolder.tvTitle.setVisibility(View.GONE);
            mHolder.layoutItem.setVisibility(View.VISIBLE);
            mHolder.viewLine.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(currModle.getMoney())) {
                SDViewBinder.setTextView(mHolder.tvMoney, currModle.getMoney() + "元");
            } else {
                SDViewBinder.setTextView(mHolder.tvMoney, "");
            }
            if (!TextUtils.isEmpty(currModle.getOrder_sn())) {
                SDViewBinder.setTextView(mHolder.tvOrder, "订单号：" + currModle.getOrder_sn());
            } else {
                SDViewBinder.setTextView(mHolder.tvOrder, "");
            }
            SDViewBinder.setTextView(mHolder.tvTime, currModle.getInsert_time(), "");
            String comeFrom = "";
            String mobile = currModle.getMobile();
            String money_type = currModle.getMoney_type();
            if (TextUtils.isEmpty(money_type)){
                comeFrom+=mobile;
            }else {
                comeFrom=money_type+"       "+mobile;
            }
            SDViewBinder.setTextView(mHolder.tvType, comeFrom, "");
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
        TextView tvTitle, tvMoney, tvType, tvOrder, tvTime;
        RelativeLayout layoutItem;
        View viewLine;
    }

}

package com.fanwe.mine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.user.model.wallet.RefundModel;
import com.handmark.pulltorefresh.library.PinnedSectionListView;
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
            convertView = inflater.inflate(R.layout.item_refund, null);
            mHolder.title_line = (LinearLayout) convertView.findViewById(R.id.title_line);
            mHolder.month_text = (TextView) convertView.findViewById(R.id.month_text);
            mHolder.data_line = (LinearLayout) convertView.findViewById(R.id.data_line);
            mHolder.time_txt = (TextView) convertView.findViewById(R.id.time_txt);
            mHolder.month_date = (TextView) convertView.findViewById(R.id.month_date);
            mHolder.value_txt = (TextView) convertView.findViewById(R.id.value_txt);
            mHolder.order_str = (TextView) convertView.findViewById(R.id.order_str);
            mHolder.order_id = (TextView) convertView.findViewById(R.id.order_id);
            mHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

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
            mHolder.title_line.setVisibility(View.VISIBLE);

            String value=currModle.getYear_month();
            mHolder.month_text.setText(value);
            mHolder.data_line.setVisibility(View.GONE);


        } else {
            mHolder.title_line.setVisibility(View.GONE);
            mHolder.data_line.setVisibility(View.VISIBLE);
            mHolder.time_txt.setText(currModle.getTime_str());
            mHolder.month_date.setText(currModle.getMonth_date());
            if (!TextUtils.isEmpty(currModle.getMoney())) {
                SDViewBinder.setTextView(mHolder.value_txt, currModle.getMoney() );
            } else {
                SDViewBinder.setTextView(mHolder.value_txt, "");
            }
            if(!TextUtils.isEmpty(currModle.getOrder_sn())) {
                mHolder.order_id.setText(currModle.getOrder_sn());
                mHolder.order_str.setVisibility(View.VISIBLE);
            }else{
                mHolder.order_id.setText("");
                mHolder.order_str.setVisibility(View.GONE);
            }

            SDViewBinder.setTextView(mHolder.month_text, currModle.getInsert_time(), "");
            String comeFrom = "";
            String mobile = currModle.getMobile();
            String money_type = currModle.getMoney_type();
            if (TextUtils.isEmpty(money_type)){
                comeFrom+=mobile;
            }else {
                comeFrom=money_type+"       "+mobile;
            }
            mHolder.tv_desc.setText(comeFrom);
        }
    }

    public void setmDatas(List<ModelCommissionLog> mDatas) {
        this.datas = mDatas;
    }

    public void addMoreMDatas(List<ModelCommissionLog> mDatas) {
        if (mDatas != null&&this.datas!=null) {
            this.datas.addAll(mDatas);
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


    public static  class Holder {
        LinearLayout title_line;
        TextView month_text;
        LinearLayout data_line;
        TextView time_txt;
        TextView month_date;
        TextView value_txt;
        TextView order_str;
        TextView order_id;
        TextView tv_desc;
    }

}

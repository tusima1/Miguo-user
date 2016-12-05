package com.fanwe.user.adapters;


import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.wallet.ExchangeDiamondHistoryModel;
import com.handmark.pulltorefresh.library.PinnedSectionListView;

import java.util.List;

/**
 * 兑换记录adapter.
 * Created by zhouhy on 2016/11/24.
 */

public class ExchangeHistoryAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private List<ExchangeDiamondHistoryModel> mDatas;
    private LayoutInflater inflater;

    private Context context;
    private final int TITLE = 1;

    public ExchangeHistoryAdapter(Activity activity, Context context,List<ExchangeDiamondHistoryModel> mDatas) {
        this.context = context;
        this.inflater = activity.getLayoutInflater();
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private void setData(ExchangeHistoryAdapter.MyViewHolder holder, final int position) {
        final ExchangeDiamondHistoryModel model = mDatas.get(position);
        if (TITLE == model.getType()) {
            holder.title_line.setVisibility(View.VISIBLE);
            String value=model.getYear_month();
            holder.month_text.setText(value);
            holder.data_line.setVisibility(View.GONE);
        } else {
            holder.title_line.setVisibility(View.GONE);
            holder.data_line.setVisibility(View.VISIBLE);
            holder.time_txt.setText(model.getTime_str());
            holder.month_date.setText(model.getMonth_date());
            holder.value_txt.setText(model.getBean());
        }
    }



    public void setmDatas(List<ExchangeDiamondHistoryModel> mDatas) {
        this.mDatas = mDatas;
    }

    public void addMoreMDatas(List<ExchangeDiamondHistoryModel> mDatas) {
        if (mDatas != null&&this.mDatas!=null) {
            this.mDatas.addAll(mDatas);
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExchangeHistoryAdapter.MyViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ExchangeHistoryAdapter.MyViewHolder();
            convertView = inflater.inflate(R.layout.item_exchangehistory, null);
            mHolder.title_line = (LinearLayout) convertView.findViewById(R.id.title_line);
            mHolder.month_text = (TextView) convertView.findViewById(R.id.month_text);
            mHolder.data_line = (RelativeLayout) convertView.findViewById(R.id.data_line);
            mHolder.time_txt = (TextView) convertView.findViewById(R.id.time_txt);
            mHolder.month_date = (TextView) convertView.findViewById(R.id.month_date);
            mHolder.value_txt = (TextView) convertView.findViewById(R.id.value_txt);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ExchangeHistoryAdapter.MyViewHolder) convertView.getTag();
        }
        setData(mHolder, position);

        return convertView;
    }


   public static  class MyViewHolder {
        LinearLayout title_line;
        TextView month_text;
        RelativeLayout data_line;
        TextView time_txt;
        TextView month_date;
        TextView value_txt;
    }
}

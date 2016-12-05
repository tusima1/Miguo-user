package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.wallet.RefundModel;
import com.handmark.pulltorefresh.library.PinnedSectionListView;

import java.util.List;

/**
 * 退款及其它的adapter.
 * Created by zhouhy on 2016/11/28.
 */

public class RefundAdapter  extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private List<RefundModel> mDatas;
    private LayoutInflater inflater;

    private Context context;
    private final int TITLE = 1;

    public RefundAdapter(Activity activity, Context context, List<RefundModel> mDatas) {
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
    private void setData(RefundAdapter.MyViewHolder holder, final int position) {
        final RefundModel model = mDatas.get(position);
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

            if(!TextUtils.isEmpty(model.getMoney())) {
                if(Float.valueOf(model.getMoney())>0) {
                    holder.value_txt.setText("+" +model.getMoney());
                }else{
                    holder.value_txt.setText("-" +model.getMoney());
                }
            }else{
                holder.value_txt.setText("");
            }
            if(!TextUtils.isEmpty(model.getOrder_sn())) {
                holder.order_id.setText(model.getOrder_sn());
                holder.order_str.setVisibility(View.VISIBLE);
            }else{
                holder.order_id.setText("");
                holder.order_str.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(model.getDescription())) {
                holder.tv_desc.setText(model.getDescription());
            }else{
                holder.tv_desc.setVisibility(View.GONE);
            }
        }
    }



    public void setmDatas(List<RefundModel> mDatas) {
        this.mDatas = mDatas;
    }

    public void addMoreMDatas(List<RefundModel> mDatas) {
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
        RefundAdapter.MyViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new RefundAdapter.MyViewHolder();
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
            mHolder = (RefundAdapter.MyViewHolder) convertView.getTag();
        }
        setData(mHolder, position);

        return convertView;
    }


    public static  class MyViewHolder {
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


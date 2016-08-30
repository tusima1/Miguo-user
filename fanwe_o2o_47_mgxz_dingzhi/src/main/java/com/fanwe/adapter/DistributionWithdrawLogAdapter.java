package com.fanwe.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.commission.model.getWithdrawLog.ModelWithdrawLog;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DistributionWithdrawLogAdapter extends SDBaseAdapter<ModelWithdrawLog> {

    public DistributionWithdrawLogAdapter(List<ModelWithdrawLog> listModel, Activity
            activity) {
        super(listModel, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent,
                        ModelWithdrawLog model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_distribution_withdraw_log, null);
        }

        TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
        TextView tv_withdraw_type = ViewHolder.get(convertView, R.id.tv_withdraw_type);
        TextView tv_status = ViewHolder.get(convertView, R.id.tv_status);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);

        if (model != null) {
            BigDecimal bd = new BigDecimal((model.getMoney()));
            bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            SDViewBinder.setTextView(tv_money, "￥" + String.valueOf(bd));
            String bank_card = model.getBank_card();
            int length = bank_card.length();
            if (length > 4) {
                SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name() + " 尾号" + bank_card.substring(length - 4, length));
            } else {
                SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name());
            }
            String  status = model.getWd_status();
            //提现状态，1审核中，2提现成功，3未通过
            if ("1".equals(status)) {
                SDViewBinder.setTextView(tv_status, "审核中");
            } else if ("2".equals(status)) {
                SDViewBinder.setTextView(tv_status, "提现成功");
            } else if ("3".equals(status)) {
                SDViewBinder.setTextView(tv_status, "未通过");
            }
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (TextUtils.isEmpty(model.getWd_time())){
                SDViewBinder.setTextView(tv_time, "");
            }else {
                Long updateTime = Long.valueOf(model.getWd_time());
                SDViewBinder.setTextView(tv_time, form.format(new Date(updateTime)));
            }

        }
        return convertView;
    }

}

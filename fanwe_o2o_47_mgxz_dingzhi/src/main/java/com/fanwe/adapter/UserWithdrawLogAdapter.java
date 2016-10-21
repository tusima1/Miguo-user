package com.fanwe.adapter;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.UserWithdrawLogActivity;
import com.fanwe.commission.model.getWithdrawLog.ModelWithdrawLog;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.o2o.miguo.R;

import java.util.List;

public class UserWithdrawLogAdapter extends SDBaseAdapter<ModelWithdrawLog> {
    public UserWithdrawLogAdapter(List<ModelWithdrawLog> mListModel,
                                  UserWithdrawLogActivity activity) {
        super(mListModel, activity);
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

            SDViewBinder.setTextView(tv_money, "￥" + TextMoney.textFarmat(model.getMoney()));
            String bank_card = model.getBank_card();
            if (!TextUtils.isEmpty(bank_card) && bank_card.length()>4){
                String substring = bank_card.substring(bank_card.length() - 4, bank_card.length());
                SDViewBinder.setTextView(tv_withdraw_type,"提现至尾号为"+substring+"的银行卡");
            }else {
                SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name());
            }

            String wd_status = model.getWd_status();
            if ("1".equals(wd_status)) {
                SDViewBinder.setTextView(tv_status, "审核中");
            } else if ("2".equals(wd_status)) {
                SDViewBinder.setTextView(tv_status, "提现成功");
            }else if ("3".equals(wd_status)){
                SDViewBinder.setTextView(tv_status, "未通过");
            }else {
                SDViewBinder.setTextView(tv_status, "");
            }
            String apply_time = model.getApply_time();
            if (TextUtils.isEmpty(apply_time)){
                SDViewBinder.setTextView(tv_time,"");
            }else {
                CharSequence formatDate = DateFormat.format("yyyy-MM-dd kk:mm:ss", Long.decode(apply_time));
                SDViewBinder.setTextView(tv_time,formatDate);
            }

        }
        return convertView;
    }

}


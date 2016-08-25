package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.Member;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;

import java.util.List;

public class MyXiaoMiDetailAdapter extends SDBaseAdapter<Member> {

    public MyXiaoMiDetailAdapter(List<Member> listModel, Activity activity) {
        super(listModel, activity);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final Member model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dist_lv, null);
        }
        ImageView iv_user = ViewHolder.get(convertView, R.id.iv_item_user_avatar);
        TextView tv_username = ViewHolder.get(convertView, R.id.tv_item_username);
        TextView tv_date = ViewHolder.get(convertView, R.id.tv_item_date);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_item_phone);
        TextView tv_momey = ViewHolder.get(convertView, R.id.tv_item_momey);
        LinearLayout ll_number = ViewHolder.get(convertView, R.id.ll_number);
        TextView tv_number = ViewHolder.get(convertView, R.id.tv_item_mumber);

        if (model != null) {
            ll_number.setBackgroundResource(R.drawable.my_xiaomi_second);
            SDViewBinder.setImageView(iv_user, model.getAvatar());

            SDViewBinder.setTextView(tv_username, model.getUser_name());
            SDViewBinder.setTextView(tv_date, model.getCreate_time());
            if (!TextUtils.isEmpty(model.getMobile())) {
                if (model.getMobile().length() > 7) {
                    String zh = model.getMobile().replace(model.getMobile().substring(3, 7), "****");
                    SDViewBinder.setTextView(tv_phone, zh);
                }
            } else {
                SDViewBinder.setTextView(tv_phone, "");
            }
            tv_number.setBackgroundResource(R.drawable.my_xiaomi_second);
            tv_number.setGravity(Gravity.CENTER);
            if (DataFormat.toInt(model.getSalary()) == 0) {
                SDViewBinder.setTextView(tv_momey, "+0.00");
            } else {
                SDViewBinder.setTextView(tv_momey, "+" + TextMoney.textFarmat(model.getSalary()), "+0.00");
            }
            SDViewBinder.setTextView(tv_number, model.getUser_num() + "个成员");
            iv_user.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", model.getUid());
                    mActivity.startActivity(intent);
                }
            });

            tv_username.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", model.getUid());
                    mActivity.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    @Override
    public void updateData(List<Member> listModel) {

        super.updateData(listModel);
    }

    @Override
    public int getCount() {

        return super.getCount();
    }

}

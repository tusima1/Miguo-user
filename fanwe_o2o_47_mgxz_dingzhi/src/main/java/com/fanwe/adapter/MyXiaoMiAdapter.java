package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.MyXiaomiDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Member;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyXiaoMiAdapter extends SDBaseAdapter<Member> {
    private int mType;
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private List<Member> listModels;

    public MyXiaoMiAdapter(List<Member> listModels, Activity activity, int type) {
        super(listModels, activity);
        this.listModels = listModels;
        this.mType = type;
        simpleDateFormat = new SimpleDateFormat("dd");
        date = new Date();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final Member model) {
        final Member bean = listModels.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dist_lv, null);
        }
        ImageView iv_user = ViewHolder.get(convertView, R.id.iv_item_user_avatar);
        LinearLayout ll_number = ViewHolder.get(convertView, R.id.ll_number);
        TextView tv_username = ViewHolder.get(convertView, R.id.tv_item_username);
        TextView tv_date = ViewHolder.get(convertView, R.id.tv_item_date);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_item_phone);
        TextView tv_momey = ViewHolder.get(convertView, R.id.tv_item_momey);
        TextView tv_number = ViewHolder.get(convertView, R.id.tv_item_mumber);
        if (bean != null) {
            SDViewBinder.setImageView(iv_user, bean.getAvatar());
            SDViewBinder.setTextView(tv_username, bean.getUser_name());
            SDViewBinder.setTextView(tv_date, SDDateUtil.milToStringlongPoint(DataFormat.toLong(bean.getCreate_time())));
            if (!TextUtils.isEmpty(bean.getMobile())) {
//                if (bean.getMobile().length() > 7) {
//                    String zh = bean.getMobile().replace(bean.getMobile().substring(3, 7), "****");
//                    SDViewBinder.setTextView(tv_phone,zh);
//                }
                SDViewBinder.setTextView(tv_phone, bean.getMobile());
            } else {
                SDViewBinder.setTextView(tv_phone, "");
            }
            String salary = "";
            if(!TextUtils.isEmpty(bean.getSalary())){
                salary= bean.getSalary();
                SDViewBinder.setTextView(tv_momey, DataFormat.toDoubleTwo(salary));
            }else{
                SDViewBinder.setTextView(tv_momey, "+0.00");
            }

            SDViewBinder.setTextView(tv_number, bean.getUser_num() + "个成员");
            if (bean.getUser_num() == 0 || mType == 2) {
                ll_number.setBackgroundResource(R.drawable.my_xiaomi_second);
            }else{
                ll_number.setBackgroundResource(R.drawable.bg_xiaomi_press);
            }
            iv_user.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getId());
                    mActivity.startActivity(intent);
                }
            });

            tv_username.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getId());
                    mActivity.startActivity(intent);
                }
            });

            if (bean.getUser_num() > 0 && mType == 1) {
                ll_number.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mActivity, MyXiaomiDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getId());
                        bundle.putString("user", bean.getUser_name());
                        bundle.putInt("number", bean.getUser_num());
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }
                });
            }
            if (bean.getRank() == 1 && !"".equals(bean)) {
                tv_phone.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        if (insertHistory(String.valueOf(bean.getId()), simpleDateFormat.format(date))) {
//                            clickAlert(bean.getId());
//                        } else {
//                            MGToast.showToast("您已经提醒过该成员，明天再试试吧!");
//                        }
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public void updateData(List<Member> listModel) {
        super.updateData(listModel);
    }

}

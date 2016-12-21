package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getMyDistributionCorps.ModelMyDistributionCorps;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDateUtil;

import java.util.List;

public class MineTeamAdapter extends SDBaseAdapter<ModelMyDistributionCorps> {
    private List<ModelMyDistributionCorps> listModels;

    public MineTeamAdapter(List<ModelMyDistributionCorps> listModels, Activity activity) {
        super(listModels, activity);
        this.listModels = listModels;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final ModelMyDistributionCorps model) {
        final ModelMyDistributionCorps bean = listModels.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dist_lv, null);
        }
        ImageView iv_user = ViewHolder.get(convertView, R.id.iv_item_user_avatar);
        TextView tv_username = ViewHolder.get(convertView, R.id.tv_item_username);
        TextView tv_date = ViewHolder.get(convertView, R.id.tv_item_date);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_item_phone);
        TextView tv_momey = ViewHolder.get(convertView, R.id.tv_item_momey);
        if (bean != null) {
            SDViewBinder.setImageView(iv_user, bean.getIcon());
            SDViewBinder.setTextView(tv_username, bean.getNick());
            SDViewBinder.setTextView(tv_date, SDDateUtil.milToStringlongPoint(DataFormat.toLong(bean.getFx_time())));
            SDViewBinder.setTextView(tv_phone, bean.getMobile(), "");
            SDViewBinder.setTextView(tv_momey, DataFormat.toDoubleTwo(bean.getFx_total_commission()), "+0.00");
            iv_user.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getUser_id());
                    mActivity.startActivity(intent);
                }
            });
            tv_username.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getUser_id());
                    mActivity.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    @Override
    public void updateData(List<ModelMyDistributionCorps> listModel) {
        super.updateData(listModel);
    }

}

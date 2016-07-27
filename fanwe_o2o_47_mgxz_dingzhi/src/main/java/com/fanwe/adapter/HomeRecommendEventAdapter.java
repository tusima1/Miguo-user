package com.fanwe.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.EventDetailActivity;
import com.fanwe.app.App;
import com.fanwe.customview.SnapUpCountDownTimerView;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.EventModel2;
import com.fanwe.model.EventModel_List;
import com.fanwe.o2o.miguo.R;

public class HomeRecommendEventAdapter extends SDBaseAdapter<EventModel_List> {
    public HomeRecommendEventAdapter(List<EventModel_List> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_home_recommend_event, null);

        }
        ImageView ivImage = ViewHolder.get(convertView, R.id.item_home_recommend_event_iv_image);
        TextView tv_title = ViewHolder.get(convertView, R.id.item_event_title);
        TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
        TextView tv_totalMoney = ViewHolder.get(convertView, R.id.tv_totalmoney);
        //倒计时
        SnapUpCountDownTimerView tv_time = ViewHolder.get(convertView, R.id.item_home_recommend_event_tv_time);

        final EventModel_List model = getItem(position);
        if (model != null) {
            SDViewBinder.setImageView(ivImage, model.getSpecial_icon());
            SDViewBinder.setTextView(tv_title, model.getSpecial_name());
            BigDecimal bd1 = new BigDecimal(model.getSpecial_price());
            bd1 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
            SDViewBinder.setTextView(tv_money, bd1 + "");
            BigDecimal bd2 = new BigDecimal(model.getCurrent_price());
            bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
            tv_totalMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            SDViewBinder.setTextView(tv_totalMoney, bd2 + "");

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.getApplication(), EventDetailActivity.class);
                    intent.putExtra(EventDetailActivity.EXTRA_EVENT_ID, 1);
                    mActivity.startActivity(intent);
                }
            });
            setTime(tv_time, model);
        }
        return convertView;
    }

    private void setTime(SnapUpCountDownTimerView tv_time, EventModel_List model) {
        tv_time.setTime(1, 55, 3);//设置小时，分钟，秒。注意不能大于正常值，否则会抛出异常
        tv_time.start();//开始倒计时
    }

}
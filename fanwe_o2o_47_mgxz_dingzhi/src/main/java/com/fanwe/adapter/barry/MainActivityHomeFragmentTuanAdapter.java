package com.fanwe.adapter.barry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.CommandGroupBuyBean;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.MGStringFormatter;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.utils.BaseUtils;

import java.util.List;

/**
 * Created by 狗蛋哥/zlh on 2016/9/23.
 */
public class MainActivityHomeFragmentTuanAdapter extends BarryBaseRecyclerAdapter {

    public MainActivityHomeFragmentTuanAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_main_fragment_home_tuan_item, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new MainActivityHomeFragmentTuanAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).item.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        setNormalPriceText(holder, position);
    }

    private void setNormalPriceText(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).normalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        if (getItem(position).getIcon().contains("http://")) {
            SDViewBinder.setImageView(getItem(position).getIcon(), getHolder(holder).image);
        }


        getHolder(holder).title.setText(MGStringFormatter.getLimitedString(getItem(position).getName(), 13));
        getHolder(holder).describe.setText(MGStringFormatter.getLimitedString(getItem(position).getBrief(), 46));
        getHolder(holder).price.setText(SDFormatUtil.formatNumberString(getItem(position).getTuan_price(), 2) + "元");
        getHolder(holder).normalPrice.setText(SDFormatUtil.formatNumberString(getItem(position).getOrigin_price(), 2) + "元");
        getHolder(holder).sell.setText("售出" + getItem(position).getBuy_count());
        getHolder(holder).freeReservation.setText(getItem(position).getTuan_property_name());
        getHolder(holder).distance.setText(getDistance(position));
    }

    private String getDistance(int position) {
        float distanceInt = DataFormat.toFloat(getItem(position).getDistance());
        if (distanceInt <= 1000) {
            return ">" + distanceInt + "m";
        } else if (distanceInt < 1000000) {
            float y = distanceInt / 1000;
            return ">" + y + "km";
        }
        float y = distanceInt / 1000;
        return ">" + y + "km";
    }

    public int getHeight() {
        return dip2px(100 + 10 * 2) * getItemCount();
    }

    @Override
    public CommandGroupBuyBean.Result.Body getItem(int position) {
        return (CommandGroupBuyBean.Result.Body) super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder) super.getHolder(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.item_layout)
        RelativeLayout item;

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.title)
        TextView title;

        @ViewInject(R.id.free_reservation)
        TextView freeReservation;

        @ViewInject(R.id.describe)
        TextView describe;

        @ViewInject(R.id.distance)
        TextView distance;

        @ViewInject(R.id.price)
        TextView price;

        @ViewInject(R.id.price_normal)
        TextView normalPrice;

        @ViewInject(R.id.sell)
        TextView sell;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MainActivityHomeFragmentTuanAdapterListener extends BarryListener {

        public MainActivityHomeFragmentTuanAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_layout:
                    clickItem();
                    break;
            }
            super.onClick(v);
        }

        private void clickItem() {
            Intent intent = new Intent(getActivity(), TuanDetailActivity.class);
            intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, getAdapter().getItem(position).getId());
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

        @Override
        public MainActivityHomeFragmentTuanAdapter getAdapter() {
            return (MainActivityHomeFragmentTuanAdapter) super.getAdapter();
        }
    }

}

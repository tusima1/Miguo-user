package com.fanwe.adapter.barry;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by barry/狗蛋哥 on 2016/10/11.
 */
public class TimeLimitAdapter extends BarryBaseRecyclerAdapter{

    public TimeLimitAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_time_limit_item, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return null;
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {

    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).normalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        if (getItem(position).getSpecial_icon().contains("http://")) {
            SDViewBinder.setImageView(getItem(position).getSpecial_icon(), getHolder(holder).image);

        }
        getHolder(holder).title.setText(getItem(position).getSpecial_name());
        getHolder(holder).describe.setText(getItem(position).getSpecial_dec());
        getHolder(holder).distance.setText(getDistance(position));
        getHolder(holder).price.setText(getItem(position).getSpecial_price());
        getHolder(holder).normalPrice.setText(getItem(position).getOrigin_price());
        getHolder(holder).sell.setText("售出" + getItem(position).getBuy_count());
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

    public ViewHolder getHolder(RecyclerView.ViewHolder holder){
        return (ViewHolder)holder;
    }

    public SpecialListModel.Result.Body getItem(int position){
        return (SpecialListModel.Result.Body)super.getItem(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.title)
        TextView title;

        @ViewInject(R.id.describe)
        TextView describe;

        @ViewInject(R.id.distance)
        TextView distance;

        @ViewInject(R.id.sell)
        TextView sell;

        @ViewInject(R.id.price)
        TextView price;

        @ViewInject(R.id.price_normal)
        TextView normalPrice;

        @ViewInject(R.id.buy_layout)
        LinearLayout buy;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

package com.fanwe.adapter.barry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.utils.BaseUtils;

import java.util.List;

/**
 * Created by barry/狗蛋哥 on 2016/10/11.
 */
public class TimeLimitAdapter extends BarryBaseRecyclerAdapter{

    OnTimeLimitClickListener onTimeLimitClickListener;

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
        return new TimeLimitListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).item.setOnClickListener(listener);
        getHolder(holder).buy.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).normalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        getHolder(holder).buy.setBackgroundResource(isSelling(position) ? R.drawable.shape_cricle_solid_white_stoke_orange : R.drawable.shape_cricle_solid_white_stoke_orange_unuseless);
        getHolder(holder).buyNow.setBackgroundResource(isSelling(position) ? R.drawable.shape_cricle_solid_orange_bottom_radius : R.drawable.shape_cricle_solid_orange_bottom_radius_unuseless);
        getHolder(holder).price.setTextColor(getColor(isSelling(position) ? R.color.orange : R.color.gray_cc));
        getHolder(holder).normalPrice.setTextColor(getColor(isSelling(position) ? R.color.text_base_color_2 : R.color.gray_cc));
        getHolder(holder).buyNowText.setText(getBuyText(position));
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

    private String getBuyText(int position){
        return isSelling(position) ? "立即抢购" : isEnding(position) ? "已结束" : "即将开抢";
    }

    private boolean isSelling(int position){
        return DataFormat.toLong(getItem(position).getCount_down()) > 0;
    }

    private boolean isEnding(int position){
        return DataFormat.toLong(getItem(position).getCount_down()) == 0;
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

        @ViewInject(R.id.item_layout)
        RelativeLayout item;

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

        @ViewInject(R.id.buy_now_layout)
        LinearLayout buyNow;

        @ViewInject(R.id.buy_now_text)
        TextView buyNowText;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class TimeLimitListener extends BarryListener{

        public TimeLimitListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_layout:
                    clickItem();
                    break;
                case R.id.buy_layout:
                    clickBuy();
                    break;
            }
        }

        private void clickItem(){
            Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
            intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, getAdapter().getItem(position).getTuan_id());
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

        private void clickBuy(){
            /**
             * 可购买
             */
            if(isSelling(position)){
                if(onTimeLimitClickListener != null){
                    if(TextUtils.isEmpty(App.getInstance().getToken())){
                        goLogin();
                        return;
                    }
                    onTimeLimitClickListener.addToShoppingCart(getItem(position).getTuan_id(), "");
                }
            }
        }

        public void goLogin(){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

        public TimeLimitAdapter getAdapter(){
            return (TimeLimitAdapter)super.getAdapter();
        }
    }

    public void setOnTimeLimitClickListener(OnTimeLimitClickListener onTimeLimitClickListener) {
        this.onTimeLimitClickListener = onTimeLimitClickListener;
    }

    public interface OnTimeLimitClickListener{
        void addToShoppingCart(String goodsId,String fx_user_id);
    }

}

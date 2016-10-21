package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.entity.HiShopDetailBean;
import com.miguo.live.views.utils.BaseUtils;

import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 * 商家详情 精选推荐列表
 */
public class HiShopDetailRecommendAdapter extends BarryBaseRecyclerAdapter{

    public HiShopDetailRecommendAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_hishop_detail_recommend_item_featured_groupon, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new TuanListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).image.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        if(getItem(position).getImg().contains("http://")){
            SDViewBinder.setImageView(getItem(position).getImg(), getHolder(holder).image);
        }

        getHolder(holder).title.setText(getItem(position).getShort_name());
        getHolder(holder).location.setText(getItem(position).getLocation());
        getHolder(holder).oringePrice.setText(getItem(position).getOrigin_price() + "元");
        getHolder(holder).tuanPrice.setText(getItem(position).getTuan_price() + getItem(position).getUnit());
        getHolder(holder).salary.setText(getItem(position).getSalary() + "元佣金");

        setTags(holder,position);
    }

    /**
     * 设置标签(最多5个)
     *
     * @param holder
     * @param position
     */
    private void setTags(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).tags.removeAllViews();
        if ((!SDCollectionUtil.isEmpty(getItem(position).getTag_list()))) {
            getHolder(holder).tags.setVisibility(View.VISIBLE);
            for (int i = 0; i < getItem(position).getTag_list().size(); i++) {
                HiShopDetailBean.Result.Tuan.TuanTag tag = getItem(position).getTag_list().get(i);
                if (i == 5) {
                    break;
                }
                if (!TextUtils.isEmpty(tag.getTitle())) {
                    getHolder(holder).tags.addView(generalTag(tag.getTitle()));
                }
            }
        } else {
            getHolder(holder).tags.setVisibility(View.GONE);
        }
    }

    /**
     * 生成标签
     *
     * @param tag
     * @return
     */
    private View generalTag(String tag) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = activity.getLayoutInflater().inflate(R.layout.activity_hishop_detail_recommend_tv_tag_item_featured_groupon, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_describe_tag_featured_groupon);
        tvTag.setText(tag);
        lp.setMargins(0, 0, BaseUtils.dip2px(activity, 5), 0);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public HiShopDetailBean.Result.Tuan getItem(int position) {
        return (HiShopDetailBean.Result.Tuan)super.getItem(position);
    }

    public int getItemHeight(){
        int imageHeight = dip2px(196);
        int titleHeight = dip2px(35);
        int addressHeight = dip2px(25);
        int tagHeight = dip2px(35);
        int spaceHeight = dip2px(30);
        int itemHeight = imageHeight + titleHeight + addressHeight + tagHeight + spaceHeight;
        return getItemCount() * itemHeight;
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.iv_img_item_featured_groupon)
        ImageView image;

        /**
         * 标签
         */
        @ViewInject(R.id.tv_name_item_featured_groupon)
        TextView title;

        /**
         * 标签
         */
        @ViewInject(R.id.layout_tags_item_featured_groupon)
        LinearLayout tags;

        /**
         * 位置
         */
        @ViewInject(R.id.tv_location_item_featured_groupon)
        TextView location;

        /**
         * 团购价
         * 95元/张
         */
        @ViewInject(R.id.tv_price_tuan_item_featured_groupon)
        TextView tuanPrice;

        /**
         * 104元
         */
        @ViewInject(R.id.tv_price_original_item_featured_groupon)
        TextView oringePrice;


        /**
         * 佣金
         */
        @ViewInject(R.id.tv_salary_item_featured_groupon)
        TextView salary;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TuanListener extends BarryListener{

        public TuanListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_img_item_featured_groupon:
                    clickImage();
                    break;
            }
        }

        private void clickImage(){
            Intent intent = new Intent(getActivity(), TuanDetailActivity.class);
            intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, getItem(position).getId());
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

    }

}
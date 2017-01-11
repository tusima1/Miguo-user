package com.miguo.groupon.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.StringTool;
import com.miguo.entity.ModelFeaturedGroupBuy;
import com.miguo.entity.Tag;
import com.miguo.utils.BaseUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 今日精选适配器
 * Created by Administrator on 2016/10/17.
 */
public class GrouponFeaturedAdapter extends RecyclerView.Adapter<GrouponFeaturedAdapter.MViewHolder> {

    private Activity activity;
    private List<ModelFeaturedGroupBuy> listData;

    public GrouponFeaturedAdapter(Activity activity, List<ModelFeaturedGroupBuy> mList) {
        super();
        this.activity = activity;
        this.listData = mList;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {

        View view = View.inflate(viewGroup.getContext(),
                R.layout.item_featured_groupon, null);
        // 创建一个ViewHolder
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MViewHolder mViewHolder, int position) {
        final ModelFeaturedGroupBuy currBean = listData.get(position);
        mViewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, currBean.getId());
                activity.startActivity(intent);
            }
        });
        mViewHolder.tvOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        ImageLoader.getInstance().displayImage(currBean.getImg(), mViewHolder.image);
        SDViewBinder.setTextView(mViewHolder.tvName, currBean.getName(), "");
        //位置信息
        String strLocation = "";
        if (!TextUtils.isEmpty(currBean.getArea_name())) {
            strLocation = currBean.getArea_name();
            strLocation = StringTool.getStringFixed(strLocation, 6, "");
            if (!TextUtils.isEmpty(currBean.getDistance()) || !TextUtils.isEmpty(currBean.getCate_name())) {
                strLocation = strLocation + " | ";
            }
        }
        if (!TextUtils.isEmpty(currBean.getDistance())) {
            strLocation = strLocation + StringTool.getDistance(currBean.getDistance());
            if (!TextUtils.isEmpty(currBean.getCate_name())) {
                strLocation = strLocation + " | ";
            }
        }
        if (!TextUtils.isEmpty(currBean.getCate_name())) {
            strLocation = strLocation + currBean.getCate_name();
        }
        SDViewBinder.setTextView(mViewHolder.tvLocation, strLocation, "");
        //原价
        if (!TextUtils.isEmpty(currBean.getOrigin_price())) {
            String temp = currBean.getOrigin_price() + "元";
            SDViewBinder.setTextView(mViewHolder.tvOriginal, temp, "");
        } else {
            SDViewBinder.setTextView(mViewHolder.tvOriginal, "");
        }
        //团购价
        if (!TextUtils.isEmpty(currBean.getTuan_price())) {
            String temp = currBean.getTuan_price() + "元/张";
            SDViewBinder.setTextView(mViewHolder.tvTuan, temp, "");
        } else {
            SDViewBinder.setTextView(mViewHolder.tvTuan, "");
        }
        //佣金
        if (!TextUtils.isEmpty(currBean.getSalary()) && DataFormat.toDouble(currBean.getSalary()) != 0) {
            String temp = currBean.getSalary() + "元佣金";
            mViewHolder.tvSalary.setVisibility(View.VISIBLE);
            SDViewBinder.setTextView(mViewHolder.tvSalary, temp);
        } else {
            mViewHolder.tvSalary.setVisibility(View.GONE);
        }
        //标签
        setTags(mViewHolder, currBean);
    }

    /**
     * 设置标签(最多5个)
     *
     * @param mViewHolder
     * @param currBean
     */
    private void setTags(MViewHolder mViewHolder, ModelFeaturedGroupBuy currBean) {
        mViewHolder.layoutTags.removeAllViews();
        if ((!SDCollectionUtil.isEmpty(currBean.getTag_list()))) {
            mViewHolder.layoutTags.setVisibility(View.VISIBLE);
            for (int i = 0; i < currBean.getTag_list().size(); i++) {
                if (i == 5) {
                    break;
                }
                Tag tag = currBean.getTag_list().get(i);
                if (!TextUtils.isEmpty(tag.getTitle())) {
                    mViewHolder.layoutTags.addView(generalTag(tag.getTitle()));
                }
            }
        } else {
            mViewHolder.layoutTags.setVisibility(View.GONE);
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
        View view = activity.getLayoutInflater().inflate(R.layout.tv_tag_item_featured_groupon, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_describe_tag_featured_groupon);
        tvTag.setText(tag);
        lp.setMargins(0, 0, BaseUtils.dip2px(activity, 5), 0);
        view.setLayoutParams(lp);
        return view;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvName, tvLocation, tvOriginal, tvTuan, tvSalary;
        public LinearLayout layoutTags, layoutItem;

        public MViewHolder(View view) {
            super(view);
            this.image = (ImageView) itemView.findViewById(R.id.iv_img_item_featured_groupon);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name_item_featured_groupon);
            this.tvLocation = (TextView) itemView.findViewById(R.id.tv_location_item_featured_groupon);
            this.tvOriginal = (TextView) itemView.findViewById(R.id.tv_price_original_item_featured_groupon);
            this.tvTuan = (TextView) itemView.findViewById(R.id.tv_price_tuan_item_featured_groupon);
            this.tvSalary = (TextView) itemView.findViewById(R.id.tv_salary_item_featured_groupon);
            this.layoutTags = (LinearLayout) itemView.findViewById(R.id.layout_tags_item_featured_groupon);
            this.layoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item_featured_groupon);
        }
    }

}
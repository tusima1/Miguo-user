package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.fanwe.utils.StringTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.ClassPath;
import com.miguo.entity.model.getFeaturedGroupBuy.ModelFeaturedGroupBuy;
import com.miguo.entity.model.getFeaturedGroupBuy.Tag;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 */
public class HiGrouponFeaturedAdapter extends BarryBaseRecyclerAdapter{

    List<Boolean> lines;
    public static final int SINGLE_LINE_HEIGHT = 35;
    public static final int DOUBBLE_LINE_HEIGHT = 60;
    OnItemDataChangedListener onItemDataChangedListener;

    public HiGrouponFeaturedAdapter(Activity activity, List datas) {
        super(activity, datas);
        lines = new ArrayList<>();
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.item_featured_groupon, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new GrouponFeaturedAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).layoutItem.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        handlerTextViewLines(holder, position);
        getHolder(holder).tvOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }


    private void handlerTextViewLines(final RecyclerView.ViewHolder holder, final int position){
        getHolder(holder).tvName.post(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "tvName line count: " + getHolder(holder).tvName.getLineCount());
                if(getHolder(holder).tvName.getLineCount() > 1){
                    lines.set(position, true);
                    getHolder(holder).tvName.setMaxLines(2);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(matchParent(), dip2px(DOUBBLE_LINE_HEIGHT));
                    params.setMargins(dip2px(12), 0, dip2px(12), 0);
                    getHolder(holder).tvName.setLayoutParams(params);
                    getHolder(holder).tvName.setGravity(Gravity.CENTER_VERTICAL);
                    Log.d(tag, "two lines..." + lines.get(position));
                    if(onItemDataChangedListener != null){
                        onItemDataChangedListener.onItemChanged();
                    }
                }
            }
        });
    }


    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        /**
         * 主图
         */
        ImageLoader.getInstance().displayImage(getItem(position).getImg(), getHolder(holder).image);

        /**
         * 名字
         */
        SDViewBinder.setTextView(getHolder(holder).tvName, getItem(position).getName(), "");

        /**
         * 位置信息
         */
        StringBuffer strLocation = new StringBuffer("");
        if (!TextUtils.isEmpty(getItem(position).getArea_name())) {
            String str = getItem(position).getArea_name();
            strLocation.append(StringTool.getStringFixed(str, 6, "")+" |");
        }else{
            strLocation.append(".. | ");
        }
        if (!TextUtils.isEmpty(getItem(position).getDistance())) {
            double distanceValue = Double.valueOf(getItem(position).getDistance());
            strLocation.append(SDDistanceUtil.getMGDistance(distanceValue)+" | ");
        }else{
            strLocation.append(".. | ");
        }
        if (!TextUtils.isEmpty(getItem(position).getCate_name())) {
            strLocation.append(getItem(position).getCate_name());
        }else{
            strLocation.append(" ..");
        }
        SDViewBinder.setTextView(getHolder(holder).tvLocation, strLocation.toString(), "");

        /**
         * 原价
         */
        if (!TextUtils.isEmpty(getItem(position).getOrigin_price())) {
            String temp = DataFormat.toDoubleTwo(getItem(position).getOrigin_price()) + "元";
            SDViewBinder.setTextView(getHolder(holder).tvOriginal, temp, "");
        } else {
            SDViewBinder.setTextView(getHolder(holder).tvOriginal, "");
        }

        /**
         * 团购价
         */
        String tuanPrice = getItem(position).getTuan_price_with_unit();
        if (!TextUtils.isEmpty(tuanPrice)) {
            SDViewBinder.setTextView(getHolder(holder).tvTuan, tuanPrice, "");
        } else {
            SDViewBinder.setTextView(getHolder(holder).tvTuan, "");
        }

        /**
         * 佣金
         */
        if (!TextUtils.isEmpty(getItem(position).getSalary()) && DataFormat.toDouble(getItem(position).getSalary()) != 0) {
            String temp =  DataFormat.toDoubleTwo(getItem(position).getSalary())  + "元佣金";
            getHolder(holder).tvSalary.setVisibility(View.GONE);
            SDViewBinder.setTextView(getHolder(holder).tvSalary, temp);
        } else {
            getHolder(holder).tvSalary.setVisibility(View.GONE);
        }

        setTags(holder, position);

    }

    /**
     * 设置标签(最多5个)
     *
     * @param holder
     * @param position
     */
    private void setTags(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).layoutTags.removeAllViews();
        if ((!SDCollectionUtil.isEmpty(getItem(position).getTag_list()))) {
            getHolder(holder).layoutTags.setVisibility(View.VISIBLE);
            for (int i = 0; i < getItem(position).getTag_list().size(); i++) {
                if (i == 5) {
                    break;
                }
                Tag tag = getItem(position).getTag_list().get(i);
                if (!TextUtils.isEmpty(tag.getTitle())) {
                    getHolder(holder).layoutTags.addView(generalTag(tag.getTitle()));
                }
            }
        } else {
            getHolder(holder).layoutTags.setVisibility(View.GONE);
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

    public int getItemHeight(){
        int allHeight = 0;
        for(int i = 0; i<getItemCount(); i++){
            int imageHeight = dip2px(196);
            int tagHeight = dip2px(35);
            int addressHeight = dip2px(25);
            int titleHeight = lines.get(i) ? dip2px(DOUBBLE_LINE_HEIGHT) : dip2px(SINGLE_LINE_HEIGHT);
            int spaceHeight = dip2px(30);
            int itemHeight = imageHeight + titleHeight + addressHeight + tagHeight + spaceHeight;
            allHeight += itemHeight;
            Log.d(tag, "get item height: " + allHeight + " ,is two lines : " + lines.get(i));
        }
        return allHeight;
    }


    @Override
    public void notifyDataSetChanged(List datas) {
        this.lines.clear();
        for(int i = 0; i<datas.size(); i++){
            lines.add(false);
        }
        super.notifyDataSetChanged(datas);
    }

    @Override
    public void notifyDataSetChangedLoadmore(List datas) {
        for(int i = getItemCount() - 1; i < getItemCount() - 1 + datas.size(); i++){
            lines.add(false);
        }
        super.notifyDataSetChangedLoadmore(datas);
    }

    @Override
    public ModelFeaturedGroupBuy getItem(int position) {
        return (ModelFeaturedGroupBuy)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.iv_img_item_featured_groupon)
        ImageView image;

        @ViewInject(R.id.tv_name_item_featured_groupon)
        TextView tvName;

        @ViewInject(R.id.tv_location_item_featured_groupon)
        TextView tvLocation;

        @ViewInject(R.id.tv_price_original_item_featured_groupon)
        TextView tvOriginal;

        @ViewInject(R.id.tv_price_tuan_item_featured_groupon)
        TextView tvTuan;

        @ViewInject(R.id.tv_salary_item_featured_groupon)
        TextView tvSalary;

        @ViewInject(R.id.layout_tags_item_featured_groupon)
        LinearLayout layoutTags;

        @ViewInject(R.id.layout_item_featured_groupon)
        LinearLayout layoutItem;


        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public interface OnItemDataChangedListener{
        void onItemChanged();
    }

    public void setOnItemDataChangedListener(OnItemDataChangedListener onItemDataChangedListener) {
        this.onItemDataChangedListener = onItemDataChangedListener;
    }

    class GrouponFeaturedAdapterListener extends BarryListener{

        public GrouponFeaturedAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_item_featured_groupon:
                    clickItem();
                    break;
            }
        }

        private void clickItem(){
            Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.GOODS_DETAIL_ACTIVITY));
            intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, getItem(position).getId());
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

    }

}

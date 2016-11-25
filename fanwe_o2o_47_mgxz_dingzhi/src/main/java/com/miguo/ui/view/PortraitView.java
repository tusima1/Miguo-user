package com.miguo.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getShopMemberInfo.ModelTag;
import com.miguo.live.views.base.BaseLinearLayout;

import java.util.List;


/**
 * Created by Administrator on 2016/11/24.
 */

public class PortraitView extends BaseLinearLayout {

    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
    }

    List<ModelTag> datas;

    public void initData(List<ModelTag> datasList) {
        datas = datasList;
        removeAllViews();
        if (SDCollectionUtil.isEmpty(datas)) {
            return;
        }
        doData();
        for (int i = 0; i < datas.size(); i++) {
            addView(generalTag(i, datas.size()));
        }
    }

    /**
     * data不能超过5个
     */
    private void doData() {
        if (datas.size() > 5) {
            datas = datas.subList(0, 5);
        }
    }

    private boolean flagFans;

    public void initDataFans(List<ModelTag> datasList) {
        datas = datasList;
        flagFans = true;
        removeAllViews();
        if (SDCollectionUtil.isEmpty(datas)) {
            return;
        }
        doData();
        for (int i = 0; i < datas.size(); i++) {
            addView(generalTag(i, datas.size()));
        }
    }

    private View generalTag(int i, int count) {
        ModelTag bean = datas.get(i);
        LayoutParams lp = getLinearLayoutParams(wrapContent(), wrapContent());
        int left = 0;
        if (i != 0) {
            left = getMarginLeft(count);
        }
        lp.setMargins(left, 0, 0, 0);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_portrait_shop_fans, null);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.iv_portrait_item_shop_fans);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_portrait_item_shop_fans);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name_fans_item_shop_fans);
        if (flagFans) {
            //粉丝模块
            tvTag.setVisibility(GONE);
            if (TextUtils.isEmpty(bean.getIconUrl()) && !TextUtils.isEmpty(bean.getTagName())) {
                //显示灰色图标
                circleImageView.setBackgroundResource(R.drawable.bg_grey_shop_fans);
                tvName.setVisibility(VISIBLE);
                SDViewBinder.setTextView(tvName, bean.getTagName(), "");
            } else {
                //显示头像
                SDViewBinder.setImageView(bean.getIconUrl(), circleImageView);
            }
        } else {
            //员工模块
            if (TextUtils.isEmpty(bean.getIconUrl()) && "神秘员工".equals(bean.getTagName())) {
                tvTag.setVisibility(GONE);
                //显示灰色图标
                circleImageView.setBackgroundResource(R.drawable.bg_grey_shop_fans);
                tvName.setVisibility(VISIBLE);
                SDViewBinder.setTextView(tvName, bean.getTagName(), "");
            } else {
                tvTag.setVisibility(VISIBLE);
                SDViewBinder.setTextView(tvTag, bean.getTagName(), "");
                SDViewBinder.setImageView(bean.getIconUrl(), circleImageView);
            }
        }
        view.setLayoutParams(lp);
        return view;
    }

    public int getAvatarWidth() {
        return dip2px(44);
    }

    public int getMarginLeft(int count) {
        switch (count) {
            case 2:
                return dip2px(77);
            case 3:
                return (getScreenWidth() - dip2px(138) - getAvatarWidth() * 3) / 2;
            case 4:
            case 5:
                return (getScreenWidth() - dip2px(50) - getAvatarWidth() * count) / (count - 1);
        }
        return dip2px(25);
    }

    @Override
    public int getScreenWidth() {
        return super.getScreenWidth() - dip2px(24);
    }
}

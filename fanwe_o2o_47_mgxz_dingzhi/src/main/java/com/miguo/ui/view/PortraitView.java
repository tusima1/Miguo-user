package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseLinearLayout;


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

    public void initData(int count) {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            addView(generalTag(i, count));
        }
    }

    private boolean flagFans;

    public void initDataFans(int count) {
        flagFans = true;
        removeAllViews();
        for (int i = 0; i < count; i++) {
            addView(generalTag(i, count));
        }
    }

    private View generalTag(int i, int count) {
        LayoutParams lp = getLinearLayoutParams(wrapContent(), wrapContent());
        int left = 0;
        if (i != 0) {
            left = getMarginLeft(count);
        }
        lp.setMargins(left, 0, 0, 0);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_portrait_shop_fans, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_portrait_item_shop_fans);
        if (flagFans) {
            tvTag.setVisibility(GONE);
        } else {
            tvTag.setVisibility(VISIBLE);
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

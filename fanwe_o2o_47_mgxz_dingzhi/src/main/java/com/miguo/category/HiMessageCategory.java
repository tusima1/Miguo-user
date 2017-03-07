package com.miguo.category;

import com.lidroid.xutils.ViewUtils;
import com.miguo.app.HiBaseActivity;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/6.
 */

public class HiMessageCategory extends Category {

    public HiMessageCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {

    }

    @Override
    protected void setThisListener() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {

    }
}

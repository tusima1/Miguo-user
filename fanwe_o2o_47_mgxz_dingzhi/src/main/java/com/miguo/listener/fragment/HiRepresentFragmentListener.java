package com.miguo.listener.fragment;

import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiRepresentFragmentCategory;
import com.miguo.definition.ClassPath;
import com.miguo.entity.SingleMode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.utils.BaseUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class HiRepresentFragmentListener extends FragmentListener {

    public HiRepresentFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.menu:
                clickMenu();
                break;
            case R.id.search_bar:
                clickSearch();
                break;
        }
    }

    private void clickSearch(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.SEARCH_GUIDE));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void clickMenu(){
        getCategory().clickMenu();
    }

    @Override
    public HiRepresentFragmentCategory getCategory() {
        return (HiRepresentFragmentCategory)super.getCategory();
    }
}

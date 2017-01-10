package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiRepresentCateFragmentCategory;
import com.miguo.definition.IntentKey;
import com.miguo.entity.SearchCateConditionBean;

import java.util.List;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentCateFragment extends HiBaseFragment {

    List<SearchCateConditionBean.Result.Body.Categories> categories;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hihome_fragment_hirepresent_banner, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        getIntentData();
        category = new HiRepresentCateFragmentCategory(cacheView, this);
    }

    private void getIntentData(){
        categories = (List<SearchCateConditionBean.Result.Body.Categories>)getArguments().getSerializable(IntentKey.REPRESENT_CATEGORYS);
    }

    public List<SearchCateConditionBean.Result.Body.Categories> getCategories() {
        return categories;
    }
}

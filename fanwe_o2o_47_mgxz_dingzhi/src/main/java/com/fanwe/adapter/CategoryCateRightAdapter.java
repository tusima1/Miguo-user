package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.SD2LvCategoryViewHelper.SD2LvCategoryViewHelperAdapterInterface;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getClassifyList.ModelClassifyList;

import java.util.List;

public class CategoryCateRightAdapter extends SDBaseAdapter<ModelClassifyList> implements SD2LvCategoryViewHelperAdapterInterface {

    private int mDefaultIndex;

    public CategoryCateRightAdapter(List<ModelClassifyList> listModel, Activity activity) {
        super(listModel, activity);
    }

    public void setmDefaultIndex(int rightIndex) {
        this.mDefaultIndex = rightIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_category_right, null);
        }

        TextView tvTitle = ViewHolder.get(convertView, R.id.item_category_right_tv_title);

        ModelClassifyList model = getItem(position);
        if (model != null) {
            SDViewBinder.setTextView(tvTitle, model.getName());
            if (model.isSelect()) {
                convertView.setBackgroundResource(R.drawable.choose_item_right);
            } else {
                convertView.setBackgroundColor(SDResourcesUtil.getColor(R.color.bg_gray_categoryview_item_select));
            }
        }

        return convertView;
    }

    @Override
    public void setPositionSelectState(int position, boolean select, boolean notify) {
        getItem(position).setSelect(select);
        if (notify) {
            notifyDataSetChanged();
        }

    }

    @Override
    public String getTitleNameFromPosition(int position) {
        return getItem(position).getName();
    }

    @Override
    public BaseAdapter getAdapter() {
        return this;
    }

    @Override
    public Object getSelectModelFromPosition(int position) {
        return getItem(position);
    }

    @Override
    public int getTitleIndex() {
        return mDefaultIndex;
    }

    @Override
    public Object getRightListModelFromPosition_left(int position) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateRightListModel_right(Object rightListModel) {
        List<ModelClassifyList> listRight = (List<ModelClassifyList>) rightListModel;
        updateData(listRight);
    }

    @Override
    public void setPositionSelectState_left(int positionLeft, int positionRight, boolean select) {

    }

}

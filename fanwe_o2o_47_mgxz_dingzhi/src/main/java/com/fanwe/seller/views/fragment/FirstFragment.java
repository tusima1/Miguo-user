package com.fanwe.seller.views.fragment;

/**
 * Created by Administrator on 2017/1/11.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.DPGridViewAdapter;
import com.fanwe.seller.model.TypeModel;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by whiskeyfei on 15-7-21.
 */
public class FirstFragment extends Fragment {

    private GridView mGridView;
    DPGridViewAdapter mDPGridViewAdapter;
    private int lastSelectedPosition = 0;


    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> mDataList = new ArrayList<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>();

    private SecondTypeClickListener secondTypeClickListener;
    private int childPosition = -1;
    /**
     * 对应的上层父亲fragment
     */
    private SecondTypeFragment bigFragment;

    public FirstFragment() {
        super();


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mDPGridViewAdapter = new DPGridViewAdapter(getActivity(), mDataList, R.layout.gridview_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mDataList == null || mDPGridViewAdapter == null) {
                    return;
                }
                if (lastSelectedPosition == position) {
                    mDataList.get(position).setChecked(true);
                } else {
                    mDataList.get(position).setChecked(true);
                    mDataList.get(lastSelectedPosition).setChecked(false);
                    lastSelectedPosition = position;
                }
                secondTypeClickListener.onItemClickListner(bigFragment, childPosition, mDataList.get(position));

                notifyAdapterChange();

            }
        });
        mGridView.setNumColumns(4);

        mGridView.setAdapter(mDPGridViewAdapter);
        return view;
    }


    public void setmDataList(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> mDataList) {
        this.mDataList = mDataList;
        if (mDPGridViewAdapter != null) {
            mDPGridViewAdapter.notifyDataSetChanged();
        }
    }

    public List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> getDataList() {
        return this.mDataList;
    }

    //取消当前adapter里面被选中的状态
    public void removeSelectedState() {
        mDataList.get(lastSelectedPosition).setChecked(false);
        notifyAdapterChange();
    }

    public void notifyAdapterChange() {
        if (mDPGridViewAdapter != null) {
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDPGridViewAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public interface SecondTypeClickListener {
        public void onItemClickListner(SecondTypeFragment bigFrag, int childPosition, SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean typeBean);
    }

    public SecondTypeClickListener getSecondTypeClickListener() {
        return secondTypeClickListener;
    }

    public void setSecondTypeClickListener(SecondTypeClickListener secondTypeClickListener) {
        this.secondTypeClickListener = secondTypeClickListener;
    }

    public DPGridViewAdapter getmDPGridViewAdapter() {
        return mDPGridViewAdapter;
    }

    public void setmDPGridViewAdapter(DPGridViewAdapter mDPGridViewAdapter) {
        this.mDPGridViewAdapter = mDPGridViewAdapter;
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public SecondTypeFragment getBigFragment() {
        return bigFragment;
    }

    public void setBigFragment(SecondTypeFragment bigFragment) {
        this.bigFragment = bigFragment;
    }

    public void updateCheckType(int newPosition, boolean ifCheck) {
        mDataList.get(lastSelectedPosition).setChecked(false);
        if (ifCheck) {
            mDataList.get(newPosition).setChecked(true);
        }
        notifyAdapterChange();
    }
}


package com.fanwe.seller.views.fragment;

/**
 * Created by Administrator on 2017/1/11.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private int mImageResId;
    private String mIndex;
    private GridView mGridView;
    DPGridViewAdapter mDPGridViewAdapter;
    private int lastSelectedPosition=0;
    public SecondSmallTypeChangeListener smallTypeChangeListener;

    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> mDataList = new ArrayList<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>();

    public FirstFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mImageResId = arguments.getInt(MeiTuanActivity.KEY_IMAGE);
//            mIndex = arguments.getString(MeiTuanActivity.KEY_INDEX);
//        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview, container, false);
        mGridView = (GridView)view.findViewById(R.id.gridview);
        mDPGridViewAdapter = new DPGridViewAdapter(getActivity(), mDataList, R.layout.gridview_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"index"+position,Toast.LENGTH_SHORT).show();
                if(mDataList==null||mDPGridViewAdapter==null){
                    return;
                }
                if(lastSelectedPosition==position){
                    mDataList.get(position).setChecked(true);
                }else{
                    mDataList.get(position).setChecked(true);
                    mDataList.get(lastSelectedPosition).setChecked(false);
                    lastSelectedPosition = position;
                }
                notifyAdapterChange();
//                smallTypeChangeListener.smallTypeChange(mDataList.get(position));
            }
        });
        mGridView.setNumColumns(4);

        mGridView.setAdapter(mDPGridViewAdapter);
        return view;
    }


    public void setmDataList(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> mDataList) {
        this.mDataList = mDataList;
        if(mDPGridViewAdapter!=null){
            mDPGridViewAdapter.notifyDataSetChanged();
        }
    }
    public List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> getDataList(){
        return this.mDataList;
    }
    //取消当前adapter里面被选中的状态
    public void removeSelectedState(){
         mDataList.get(lastSelectedPosition).setChecked(false);
         notifyAdapterChange();
    }

    public void notifyAdapterChange(){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDPGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface  SecondSmallTypeChangeListener{
        public void smallTypeChange(TypeModel model);
    }

    public SecondSmallTypeChangeListener getSmallTypeChangeListener() {
        return smallTypeChangeListener;
    }

    public void setSmallTypeChangeListener(SecondSmallTypeChangeListener smallTypeChangeListener) {
        this.smallTypeChangeListener = smallTypeChangeListener;
    }
}


package com.fanwe.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Deal_attrModel;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TuanDetailComboFragment extends TuanDetailBaseFragment
{
	@ViewInject(R.id.ll_combo_container)
	private LinearLayout mLl_combo;
	
	@ViewInject(R.id.v_line_top)
	private View v_top;
	
	@ViewInject(R.id.v_line_bot)
	private View v_bot;
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_tuan_detail_combo);
	}
	@Override
	protected void init() {
		super.init();
		bindData();
	}
	private void bindData()
	{
		if (!toggleFragmentView(mDealModel))
		{
			return;
		}
		List<Deal_attrModel> listModel = mDealModel.getDeal_attr();
		if (!toggleFragmentView(listModel))
		{
			return;
		}
		SDViewUtil.show(v_top);
		SDViewUtil.show(v_bot);
		for (int i = 0; i < listModel.size(); i++) {
			
		}
	}
	
}

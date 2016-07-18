package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.adapter.TuanDetailCombinedPackagesAdapter.TuanDetailCombinedPackagesAdapterListener;
import com.fanwe.library.adapter.SDBasePagerAdapter;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.customview.SDGridLinearLayout.StrokeCreater;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.o2o.miguo.R;

public class TuanDetailCombinedPackagesPageAdapter extends SDBasePagerAdapter<List<Deal_indexActModel>>
{

	private TuanDetailCombinedPackagesAdapterListener mListener;

	public void setmListener(TuanDetailCombinedPackagesAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public TuanDetailCombinedPackagesPageAdapter(List<List<Deal_indexActModel>> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(View container, int position)
	{
		SDGridLinearLayout ll = new SDGridLinearLayout(mActivity);
		ll.setmColNumber(3);
		ll.setmCreaterStroke(new StrokeCreater()
		{

			@Override
			public View createVer()
			{
				View view = new View(mActivity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
				params.topMargin = SDViewUtil.dp2px(10);
				params.bottomMargin = SDViewUtil.dp2px(20);
				view.setLayoutParams(params);
				view.setBackgroundColor(SDResourcesUtil.getColor(R.color.stroke));
				return view;
			}

			@Override
			public View createHor()
			{
				return null;
			}
		});
		List<Deal_indexActModel> listModel = getItemModel(position);

		TuanDetailCombinedPackagesAdapter adapter = new TuanDetailCombinedPackagesAdapter(listModel, mActivity);
		adapter.setmListener(mListener);
		ll.setAdapter(adapter);
		return ll;
	}

}

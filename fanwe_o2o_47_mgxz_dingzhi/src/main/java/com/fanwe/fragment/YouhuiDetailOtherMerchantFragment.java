package com.fanwe.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.adapter.GoodsDetailMerchantAdapter;
import com.fanwe.library.customview.SDMoreLinearLayout;
import com.fanwe.library.customview.SDMoreLinearLayout.OnOpenCloseListener;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 优惠详情 （其他门店fragment）
 * 
 * @author js02
 * 
 */
public class YouhuiDetailOtherMerchantFragment extends YouhuiDetailBaseFragment
{

	@ViewInject(R.id.frag_youhui_detail_other_merchant_ll_more)
	private SDMoreLinearLayout mLl_more;

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_youhui_detail_other_merchant);
	}

	@Override
	protected void init()
	{
		bindData();
	}

	private void bindData()
	{
		if (!toggleFragmentView(mYouhuiModel))
		{
			return;
		}

		final List<StoreModel> listModel = mYouhuiModel.getOther_supplier_location();
		if (!toggleFragmentView(listModel))
		{
			return;
		}
		mLl_more.setmMaxShowCount(3);
		mLl_more.setmViewMoreLayoutId(R.layout.view_more_switch);
		mLl_more.setmListenerOnOpenClose(new OnOpenCloseListener()
		{

			@Override
			public void onOpen(List<View> listView, View viewMore)
			{
				TextView tvMore = (TextView) viewMore.findViewById(R.id.tv_more);
				tvMore.setText(R.string.click_close);
			}

			@Override
			public void onClose(List<View> listView, View viewMore)
			{
				TextView tvMore = (TextView) viewMore.findViewById(R.id.tv_more);
				tvMore.setText(R.string.click_expand);
			}
		});
		GoodsDetailMerchantAdapter adapter = new GoodsDetailMerchantAdapter(listModel, getActivity());
		mLl_more.setAdapter(adapter);
	}

}
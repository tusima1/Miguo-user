package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DistributionRecommendModel;
import com.fanwe.o2o.miguo.R;

public class DistributionRecommendAdapter extends SDBaseAdapter<DistributionRecommendModel>
{
	private boolean mShowCheckBtn;
	private DistributionRecommendAdapterListener mListener;

	public DistributionRecommendAdapter(List<DistributionRecommendModel> listModel, boolean showCheckBtn, Activity activity)
	{
		super(listModel, activity);
		this.mShowCheckBtn = showCheckBtn;
	}

	public void setmListener(DistributionRecommendAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final DistributionRecommendModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_distribution_recommend, null);
		}

		TextView tv_username_label = ViewHolder.get(convertView, R.id.tv_username_label);
		TextView tv_username = ViewHolder.get(convertView, R.id.tv_username);
		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		View ll_check_recommend = ViewHolder.get(convertView, R.id.ll_check_recommend);

		SDViewBinder.setTextView(tv_username, model.getUser_name());
		SDViewBinder.setTextView(tv_money, model.getMoney());

		if (mShowCheckBtn)
		{
			tv_username_label.setText("我推荐的人");
			SDViewUtil.show(ll_check_recommend);
			ll_check_recommend.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO 跳到查看该会员推荐的人页面
					if (mListener != null)
					{
						mListener.onCheckRecommend(model);
					}
				}
			});
		} else
		{
			tv_username_label.setText("他推荐的人");
			SDViewUtil.hide(ll_check_recommend);
		}

		return convertView;
	}

	public interface DistributionRecommendAdapterListener
	{
		void onCheckRecommend(DistributionRecommendModel model);
	}

}

package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.fanwe.gif.SDImageTextView;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.ExpressionModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

public class ExpressionAdapter extends SDBaseAdapter<ExpressionModel>
{

	private ExpressionAdapterListener mListener;

	public void setmListener(ExpressionAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public ExpressionAdapter(List<ExpressionModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final ExpressionModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_expression, null);
		}

		SDImageTextView itv_expression = ViewHolder.get(convertView, R.id.itv_expression);
		itv_expression.setImage(model.getFilename());
		itv_expression.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.onItemClick(model, position);
				}
			}
		});
		itv_expression.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				MGToast.showToast(model.getTitle());
				return true;
			}
		});
		return convertView;
	}

	public interface ExpressionAdapterListener
	{
		void onItemClick(ExpressionModel model, int position);
	}

}

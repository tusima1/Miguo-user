package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.o2o.miguo.R;

public class TuanDetailCombinedPackagesAdapter extends SDBaseAdapter<Deal_indexActModel>
{

	private TuanDetailCombinedPackagesAdapterListener mListener;

	public void setmListener(TuanDetailCombinedPackagesAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public TuanDetailCombinedPackagesAdapter(List<Deal_indexActModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final Deal_indexActModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_tuan_detail_combined_packages, null);
		}

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		ImageView iv_selected = ViewHolder.get(convertView, R.id.iv_selected);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);

		if (model.isSelected())
		{
			iv_selected.setImageResource(R.drawable.ic_payment_selected);
		} else
		{
			iv_selected.setImageResource(R.drawable.ic_payment_normal);
		}
		SDViewBinder.setTextView(tv_name, model.getName());
		SDViewBinder.setImageView(model.getIcon(), iv_image, ImageLoaderManager.getOptionsNoResetViewBeforeLoading());
		iv_selected.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.onClickSelected(v, position, model, TuanDetailCombinedPackagesAdapter.this);
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.onClickItem(v, position, model, TuanDetailCombinedPackagesAdapter.this);
				}
			}
		});
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	@Override
	protected void onSelectedChange(int position, boolean selected, boolean notify)
	{
		getItem(position).setSelected(selected);
		updateItem(position);
		super.onSelectedChange(position, selected, notify);
	}

	public interface TuanDetailCombinedPackagesAdapterListener
	{
		public void onClickSelected(View view, int position, Deal_indexActModel model, TuanDetailCombinedPackagesAdapter adapter);

		public void onClickItem(View view, int position, Deal_indexActModel model, TuanDetailCombinedPackagesAdapter adapter);

	}

}

package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.SDImageCheckBox;
import com.fanwe.library.customview.SDImageCheckBox.SDCheckBoxListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.LocalImageModel;
import com.fanwe.o2o.miguo.R;

public class LocalImageAdapter extends SDBaseAdapter<LocalImageModel>
{

	private LocalImageAdapterListener mListener;

	public LocalImageAdapter(List<LocalImageModel> listModel, Activity activity, LocalImageAdapterListener listener)
	{
		super(listModel, activity);
		setmMode(EnumAdapterMode.MULTI);
		this.mListener = listener;
		updateSelectedModel();
	}

	@Override
	public void updateData(List<LocalImageModel> listModel)
	{
		super.updateData(listModel);
		updateSelectedModel();
	}

	public void updateSelectedModel()
	{
		if (mListModel != null)
		{
			for (LocalImageModel model : mListModel)
			{
				if (model.isSelected())
				{
					addSelectedModel(model);
				} else
				{
					removeSelectedModel(model);
				}
			}
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{

		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_local_image, null);
		}

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);

		final SDImageCheckBox cbSelected = ViewHolder.get(convertView, R.id.icb_selected);
		cbSelected.getmAttr().setmImageNormalResId(R.drawable.ic_image_unselected);
		cbSelected.getmAttr().setmImageSelectedResId(R.drawable.ic_image_selected);

		LocalImageModel model = getItem(position);

		if (model != null)
		{
			cbSelected.setCheckState(model.isSelected());
			cbSelected.setmListener(new SDCheckBoxListener()
			{
				@Override
				public void onChecked(boolean isChecked)
				{
					if (mListener != null)
					{
						if (mListener.onChecked(cbSelected, isChecked))
						{
							return;
						}
					}
					setmSelectedPosition(position, isChecked);
				}
			});

			SDViewBinder.setImageView(iv_image, "file:///" + model.getPath());
		}
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	@Override
	protected void onSelectedChange(int position, boolean selected, boolean notify)
	{
		getItem(position).setSelected(selected);
	}

	public interface LocalImageAdapterListener
	{
		public boolean onChecked(SDImageCheckBox checkBox, boolean isChecked);
	}

}

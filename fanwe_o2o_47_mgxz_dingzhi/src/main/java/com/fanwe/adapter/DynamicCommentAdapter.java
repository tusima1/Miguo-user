package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DynamicReplyModel;
import com.fanwe.o2o.miguo.R;

public class DynamicCommentAdapter extends SDBaseAdapter<DynamicReplyModel>
{

	private DynamicCommentAdapter_onClick mListener;

	public void setmListener(DynamicCommentAdapter_onClick mListener)
	{
		this.mListener = mListener;
	}

	public DynamicCommentAdapter(List<DynamicReplyModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final DynamicReplyModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_dynamic_comment, null);
		}

		TextView tv_comment_user = ViewHolder.get(convertView, R.id.tv_comment_user);
		TextView tv_comment_content = ViewHolder.get(convertView, R.id.tv_comment_content);

		SDViewBinder.setTextView(tv_comment_user, model.getUser_name());
		SDViewBinder.setTextView(tv_comment_content, ":" + model.getContent());

		tv_comment_user.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.onClickCommentUsername(model, v);
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
					mListener.onClickComment(model, v);
				}
			}
		});

		return convertView;
	}

	public interface DynamicCommentAdapter_onClick
	{
		public void onClickComment(DynamicReplyModel model, View v);

		public void onClickCommentUsername(DynamicReplyModel model, View v);
	}

}

package com.fanwe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.RedEnvelopeModel;
import com.fanwe.o2o.miguo.R;

public class MyRedPaymentAdapter extends SDSimpleBaseAdapter<RedEnvelopeModel>{

	private int mId;
	
	protected int selectId;
	private ArrayList<Integer> mList = new ArrayList<Integer>();
	public MyRedPaymentAdapter(List<RedEnvelopeModel> listModel,
			Activity activity,int mId) {
		super(listModel, activity);
		this.mId = mId;
		mList.clear();
	}
	
	public ArrayList<Integer> getPosition()
	{	
		return mList;
	}
	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent) {
		
		return R.layout.item_my_red_envelope;
	}
	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
			final RedEnvelopeModel model) {
		LinearLayout mLl_alpha = get(R.id.ll_background,convertView);
		TextView tv_money = get(R.id.tv_momey, convertView);
		TextView tv_tv_momey_dian = get(R.id.tv_momey_dian, convertView);
		TextView tv_sort = get(R.id.tv_sort, convertView);
		TextView tv_title = get(R.id.tv_title, convertView);
		TextView tv_type = get(R.id.tv_type, convertView);
		TextView tv_timebegin = get(R.id.tv_timebegin, convertView);
		TextView tv_timeclose = get(R.id.tv_timeclose, convertView);
		final ImageView iv_select = get(R.id.iv_select, convertView);
		
		SDViewBinder.setTextView(tv_timebegin, "• "+model.getTime_begin());
		SDViewBinder.setTextView(tv_timeclose, model.getTime_close());
		String money = model.getMoney()+"";
		String []arr=money.split("\\.");
		SDViewBinder.setTextView(tv_money, arr[0]);
		SDViewBinder.setTextView(tv_tv_momey_dian, "."+arr[1]);
		
		
		SDViewBinder.setTextView(tv_sort, "普通红包");
		SDViewBinder.setTextView(tv_type, "• 可叠加使用");
		SDViewBinder.setTextView(tv_title,"• 带红包标记的商品可使用");
		if(model.getValid() == 2 && model.getStatus() == 1)
		{
			if(mId == model.getId())
			{
				SDViewUtil.show(iv_select);
				selectId = mId;
				mList.add(Integer.valueOf(selectId));
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
						if(iv_select.getVisibility() == View.GONE)
						{
							SDViewUtil.show(iv_select);
							selectId = model.getId();
							mList.add(Integer.valueOf(selectId));
						}else
						{
							SDViewUtil.hide(iv_select);
							Integer in = new Integer(model.getId());
							mList.remove(in);
						}
				}
			});
		}else if(model.getValid() == 1 && model.getStatus() == 1)
		{
			mLl_alpha.setAlpha(0.6F);
		}
		
	}
}

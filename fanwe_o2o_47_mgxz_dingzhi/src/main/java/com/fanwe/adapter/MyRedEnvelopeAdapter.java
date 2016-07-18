package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.MyRedEnvelopDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.RedEnvelopeModel;
import com.fanwe.o2o.miguo.R;

public class MyRedEnvelopeAdapter extends SDBaseAdapter<RedEnvelopeModel>
{

	public MyRedEnvelopeAdapter(List<RedEnvelopeModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final RedEnvelopeModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_my_red_envelope, null);
		}
		
		TextView tv_money = ViewHolder.get(R.id.tv_momey, convertView);
		TextView tv_tv_momey_dian = ViewHolder.get(R.id.tv_momey_dian, convertView);
		TextView tv_sort = ViewHolder.get(R.id.tv_sort, convertView);
		TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
		TextView tv_type = ViewHolder.get(R.id.tv_type, convertView);
		TextView tv_timebegin = ViewHolder.get(R.id.tv_timebegin, convertView);
		TextView tv_timeclose = ViewHolder.get(R.id.tv_timeclose, convertView);
		ImageView iv_disabled = ViewHolder.get(R.id.iv_disabled, convertView);
		
		
		SDViewBinder.setTextView(tv_timebegin, "• "+model.getTime_begin());
		SDViewBinder.setTextView(tv_timeclose, model.getTime_close());
		String money = model.getMoney()+"";
		String []arr=money.split("\\.");
		SDViewBinder.setTextView(tv_money, arr[0]);
		SDViewBinder.setTextView(tv_tv_momey_dian, "."+arr[1]);
		if(model.getStatus()==1){
			SDViewUtil.hide(iv_disabled);
			if(model.getType()==0)
			{
				SDViewBinder.setTextView(tv_sort, "普通红包");
				SDViewBinder.setTextView(tv_type, "• 可叠加使用");
				SDViewBinder.setTextView(tv_title,"• 带红包标记的商品可使用");
				
			}else if(model.getType() == 1)
			{
				SDViewBinder.setTextView(tv_sort, "商户专属");
				SDViewBinder.setTextView(tv_type, "• 不可叠加使用");
				SDViewBinder.setTextView(tv_title, model.getDeal_name());
			}else if(model.getType() == 2)
			{
				SDViewBinder.setTextView(tv_sort, "商品专属");
				SDViewBinder.setTextView(tv_type, "• 不可叠加使用");
				SDViewBinder.setTextView(tv_title, model.getDeal_name());
			}
			/*convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mActivity,MyRedEnvelopDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("timebegin", "•　"+model.getTime_begin());
					bundle.putString("timeclose", model.getTime_close());
					intent.putExtras(bundle);
					mActivity.startActivity(intent);
				}
			});*/
			
		}else
		{			
			SDViewUtil.show(iv_disabled);
			if(model.getType()==0)
			{
				SDViewBinder.setTextView(tv_sort, "普通红包");
				SDViewBinder.setTextView(tv_type, "• 可叠加使用");
				SDViewBinder.setTextView(tv_title, "• 带红包标记的商品可使用");
				
			}else if(model.getType() == 1)
			{
				SDViewBinder.setTextView(tv_sort, "商户专属");
				SDViewBinder.setTextView(tv_type, "• 不可叠加使用");
				SDViewBinder.setTextView(tv_title, model.getDeal_name());
			}else if(model.getType() == 2)
			{
				SDViewBinder.setTextView(tv_sort, "商品专属");
				SDViewBinder.setTextView(tv_type, "• 不可叠加使用");
				SDViewBinder.setTextView(tv_title, model.getDeal_name());
			}
		}
		
		return convertView;
		
		
	}

}

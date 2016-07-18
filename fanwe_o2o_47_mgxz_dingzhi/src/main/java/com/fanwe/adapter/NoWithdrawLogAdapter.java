package com.fanwe.adapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.ZijinLogModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.R.color;
import com.umeng.socialize.utils.Log;

public class NoWithdrawLogAdapter extends SDBaseAdapter<ZijinLogModel>
{

	public NoWithdrawLogAdapter(List<ZijinLogModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}
	
//    "id": "5303",
//    "user_id": "35673",
//    "type": "31",
//    "desc": "一级代理费 (不可提现部分)",
//    "ref": "18668172681",
//    "money": "5.00",
//    "ctime": "1462266732",
//    "dist_id": "23428972",
//    "dist_id_from": "23428969"

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent,
			ZijinLogModel model) {
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.item_withdraw_log, null);
		}
		ImageView iv_image= ViewHolder.get(R.id.iv_image,convertView);
		TextView tv_bank = ViewHolder.get(R.id.tv_bank, convertView);
		TextView tv_type = ViewHolder.get(R.id.tv_type, convertView);
		TextView tv_pname = ViewHolder.get(R.id.tv_pname, convertView);
		TextView tv_time = ViewHolder.get(R.id.tv_time, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		if(model != null)
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 if(model.getType() == 31)
		{
			SDViewBinder.setTextView(tv_type, "我的推广佣金(不可提现部分)");
			iv_image.setImageResource(R.drawable.bg_int);
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			SDViewBinder.setTextView(tv_time, model.getDesc());
			SDViewUtil.hide(tv_pname);
			tv_money.setTextColor(Color.parseColor("#FB6F08"));
			SDViewBinder.setTextView(tv_money, model.getMoney()+"");
		}else if(model.getType() == 32)
		{
			SDViewBinder.setTextView(tv_type, "战队推广佣金 (不可提现部分)");
			iv_image.setImageResource(R.drawable.bg_int);
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			SDViewBinder.setTextView(tv_time, model.getDesc());
			SDViewUtil.hide(tv_pname);
			tv_money.setTextColor(Color.parseColor("#FB6F08"));
			SDViewBinder.setTextView(tv_money, model.getMoney()+"");
		}else if(model.getType() == 33)
		{
			SDViewBinder.setTextView(tv_type, "三级代理费 (不可提现部分)");
			iv_image.setImageResource(R.drawable.bg_int);
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			SDViewBinder.setTextView(tv_time, model.getDesc());
			SDViewUtil.hide(tv_pname);
			tv_money.setTextColor(Color.parseColor("#FB6F08"));
			SDViewBinder.setTextView(tv_money, model.getMoney()+"");
		}
		else if (model.getType() == 40) {//发红包
			SDViewBinder.setTextView(tv_type, "发红包");
			iv_image.setImageResource(R.drawable.bg_out);
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			SDViewBinder.setTextView(tv_time, model.getDesc());
			SDViewUtil.hide(tv_pname);
			tv_money.setTextColor(Color.parseColor("#FB6F08"));
			SDViewBinder.setTextView(tv_money, model.getMoney()+"");
		}else if (model.getType() == 50) {//发红包撤销的
			SDViewBinder.setTextView(tv_type, "发红包撤销的");
			iv_image.setImageResource(R.drawable.bg_int);
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			SDViewBinder.setTextView(tv_time, model.getDesc());
			SDViewUtil.hide(tv_pname);
			tv_money.setTextColor(Color.parseColor("#FB6F08"));
			SDViewBinder.setTextView(tv_money, model.getMoney()+"");
		}
	}
		return convertView;
	}
	@Override
	public void updateData(List<ZijinLogModel> listModel) 
	{
		super.updateData(listModel);
	}
}

package com.fanwe.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.ZijinLogModel;
import com.fanwe.o2o.miguo.R;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WithdrawLogAdapter extends SDBaseAdapter<ZijinLogModel>
{

	public WithdrawLogAdapter(List<ZijinLogModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	
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
			String typeTitle="";
			switch (model.getType()) {
			case 0:
				//其他
				typeTitle="其他";
				break;
			case 1:
				typeTitle="我的佣金";		
				break;
			case 2:
				typeTitle="战队佣金";
				break;
			case 3:
				typeTitle="三级佣金";
				break;
			case 10:
				typeTitle="提现";
				break;
			case 20:
				typeTitle="提现";
				break;
			case 11:
				typeTitle="推广服务费";
				break;
			case 12:
				typeTitle="战队推广服务费";
				break;
			case 13:
				typeTitle="三级服务费";
				break;
			case 30:
				typeTitle="用户升级消费";
				break;
			case 31:
				typeTitle="我的推广佣金中不可提现部分";
				break;
			case 32:
				typeTitle="战队推广佣金中不可提现部分";
				break;
			case 33:
				typeTitle="三级代理佣金中不可提现部分";
				break;
			case 50:
				typeTitle="未用完佣金";
				break;
			case 53:
				typeTitle="消费";
				break;
			case 54:
				typeTitle="退款返回";
				break;
			case 55:
				typeTitle="用户升级费用";
				break;
			default:
				typeTitle="其他";
				break;
			}
			
			//设置不同类型显示的标题
			SDViewBinder.setTextView(tv_type, typeTitle);
			
			//设置金额与显示文字颜色,图片
			if (model.getMoney()!=null && model.getMoney().floatValue()>0) {
				SDViewBinder.setTextView(tv_money,"+"+TextMoney.textFarmat(model.getMoney()));
				iv_image.setImageResource(R.drawable.bg_int);
				tv_money.setTextColor(Color.parseColor("#2ee17c"));
			}else if (model.getMoney()!=null && model.getMoney().floatValue()<0) {
				SDViewBinder.setTextView(tv_money, TextMoney.textFarmat(model.getMoney()));
				tv_money.setTextColor(Color.parseColor("#FB6F08"));
				iv_image.setImageResource(R.drawable.bg_out);
			}else {
				SDViewBinder.setTextView(tv_money, "0.00");
				iv_image.setImageResource(R.drawable.bg_int);
				tv_money.setTextColor(Color.parseColor("#2ee17c"));
			}
			
			//设置描述
			SDViewBinder.setTextView(tv_time, model.getDesc());
			
			//设置时间
			SDViewBinder.setTextView(tv_bank, format.format(new Date(model.getCtime()*1000)));
			
			//设置来自推荐人字段,为空时不设置
			if (TextUtils.isEmpty(model.getRef())) {
				SDViewBinder.setTextView(tv_pname, "");
			}else {
				SDViewBinder.setTextView(tv_pname, "来自推荐人"+model.getRef().substring(0, 3)+"****"+model.getRef().substring(model.getRef().length()-4, model.getRef().length()));
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

package com.fanwe.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.o2o.miguo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WithdrawLogAdapter extends SDBaseAdapter<ModelCommissionLog>
{

	public WithdrawLogAdapter(List<ModelCommissionLog> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent,
						ModelCommissionLog model) {
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.item_withdraw_log, null);
		}
		ImageView iv_image= ViewHolder.get(R.id.iv_image,convertView);
		TextView tv_time = ViewHolder.get(R.id.tv_time, convertView);
		TextView tv_type = ViewHolder.get(R.id.tv_type, convertView);
		TextView tv_pname = ViewHolder.get(R.id.tv_pname, convertView);
		TextView tv_desc = ViewHolder.get(R.id.tv_desc, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		if(model != null)
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String typeTitle="";
			//TODO 说是后台给描述type类型
//			switch (MGStringFormatter.getInt(model.getMoney_type())) {
//			//0：获得佣金, 我的佣金
//			//1：获得会员升级费用,1 //推广服务费
//			//2：用户余额充值，0//
//			//3:佣金消费，1 消费
//			//4:佣金提现，1 提现
//			//5:会员升级费用消费， 消费
//			//6:会员升级费用提现， 提现
//			//7:余额消费，消费
//			//8：余额提现， 提现
//			//9:在线支付记录, 消费
//
//			//10:用户余额退款,
//				// 11:用户佣金退款,  退款返回
//			//12:会员升级费用退款，
//			//13：在线支付退款,
//			case 0:
//				typeTitle="我的佣金";
//				break;
//			case 2:
//				typeTitle="战队佣金";
//				break;
//			case 3:
//				typeTitle="三级佣金";
//				break;
//			case 4:
//				typeTitle="提现";
//				break;
//			case 11:
//				typeTitle="推广服务费";
//				break;
//			case 12:
//				typeTitle="战队推广服务费";
//				break;
//			case 13:
//				typeTitle="三级服务费";
//				break;
//			case 1:
//				typeTitle="用户升级消费";
//				break;
//			case 31:
//				typeTitle="我的推广佣金中不可提现部分";
//				break;
//			case 32:
//				typeTitle="战队推广佣金中不可提现部分";
//				break;
//			case 33:
//				typeTitle="三级代理佣金中不可提现部分";
//				break;
//			case 50:
//				typeTitle="未用完佣金";
//				break;
//			case 3:
//				typeTitle="消费";
//				break;
//			case 13:
//				typeTitle="退款返回";
//				break;
//			case 1:
//				typeTitle="用户升级费用";
//				break;
//			default:
//				typeTitle="其他";
//				break;
//			}
			
			//设置不同类型显示的标题
			SDViewBinder.setTextView(tv_type, typeTitle);
			
			//设置金额与显示文字颜色,图片
			String money = model.getMoney();
			Float moneyShow = Float.valueOf(money);
			if (moneyShow>0) {
				SDViewBinder.setTextView(tv_money,"+"+TextMoney.textFarmat(model.getMoney())+"元");
				iv_image.setImageResource(R.drawable.bg_int);
				tv_money.setTextColor(Color.parseColor("#2ee17c"));
			}else if (moneyShow<0) {
				SDViewBinder.setTextView(tv_money, TextMoney.textFarmat(model.getMoney())+"元");
				tv_money.setTextColor(Color.parseColor("#FB6F08"));
				iv_image.setImageResource(R.drawable.bg_out);
			}else {
				SDViewBinder.setTextView(tv_money, "0.00元");
				iv_image.setImageResource(R.drawable.bg_int);
				tv_money.setTextColor(Color.parseColor("#2ee17c"));
			}
			
			//设置描述
			SDViewBinder.setTextView(tv_desc, model.getDescription());
			
			//设置时间
			SDViewBinder.setTextView(tv_time, format.format(new Date(Long.valueOf(model.getInsert_time()))));//1472127680687
			
			//设置来自推荐人字段,为空时不设置
			String mobile = model.getMobile();
			if (TextUtils.isEmpty(mobile)) {
				SDViewBinder.setTextView(tv_pname, "");
			}else {
				SDViewBinder.setTextView(tv_pname, "来自推荐人"+mobile.substring(0, 3)+"****"+mobile.substring(mobile.length()-4, mobile.length()));
			}
	}
		return convertView;
	}
	@Override
	public void updateData(List<ModelCommissionLog> listModel)
	{
		super.updateData(listModel);
	}
}

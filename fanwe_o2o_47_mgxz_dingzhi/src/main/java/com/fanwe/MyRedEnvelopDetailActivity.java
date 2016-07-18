package com.fanwe;



import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
	/**
	 * 我的红包详情
	 * @author cxk
	 *
	 */
public class MyRedEnvelopDetailActivity extends BaseActivity{
	
	@ViewInject(R.id.tv_begin)
	private TextView tv_begin;
	
	@ViewInject(R.id.tv_close)
	private TextView tv_close;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myred_detail);
		init();
	}

	private void init() {
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String timebegin = bundle.getString("timebegin");
		String timeclose = bundle.getString("timeclose");
		tv_begin.setText(timebegin);
		tv_close.setText(timeclose);
		
		   WindowManager m = getWindowManager();    
		   //为获取屏幕宽、高    
	       Display d = m.getDefaultDisplay();  
	       //获取对话框当前的参数值    
	       android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
	       //高度设置为屏幕的1.0   
	       p.height = (int) (d.getHeight() * 0.67); 
	       //宽度设置为屏幕的0.8   
	       p.width = (int) (d.getWidth() * 0.7);    
	       //设置本身透明度  
	       p.alpha = 1.0f;     
	       //设置黑暗度  
	       p.dimAmount = 0.0f;    
	       getWindow().setAttributes(p);
	}

	public void clickButton(View view)
	{
		switch(view.getId())
		{
		case R.id.bt_know:
			finish();
			break;
			
		default:
				break;
		}
	}
	
}

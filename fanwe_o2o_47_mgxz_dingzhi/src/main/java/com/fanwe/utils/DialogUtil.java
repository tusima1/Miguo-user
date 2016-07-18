package com.fanwe.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

/**
 * Dialog 显示的工具类
 * 
 * @author didikee
 *
 */
public class DialogUtil {
	
	/**
	 * 展示一个无交互的原始dialog
	 * 
	 * @param context Activity
	 * @param resLayout 布局
	 */
	public static void showDialog(Context context,int resLayout) {
		AlertDialog dialog = getDialog(context, resLayout);
		dialog.show();
	}
	
	/**
	 * 展示一个无交互,更多设置选项
	 * 
	 * @param context Activity
	 * @param resLayout 布局
	 * @param alpha dialog外围背景的透明度([0,1.0]:1.0表示全不透明(全黑))
	 * @param cancleOnTouchOutside 是否点击其他区域dismiss dialog
	 */
	public static void showDialog(Context context,int resLayout,float alpha,boolean cancleOnTouchOutside) {
		if (alpha<0 || alpha > 1.0f ) {
			return ;
		}
		AlertDialog dialog = getDialog(context, resLayout);
		dialog.getWindow().setDimAmount(alpha);
		dialog.setCanceledOnTouchOutside(cancleOnTouchOutside);
		dialog.show();
	}
	
	/**
	 * 
	 * @param context
	 * @param resLayout
	 * @return 返回一个dialog
	 */
	private static AlertDialog getDialog(Context context,int resLayout){
		View view=View.inflate(context, resLayout, null);
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		return builder.setView(view).create();
	}

	/**
	 * 
	 * @param context
	 * @param view
	 * @return 返回一个dialog的原始样式,自己充分利用布局里的控件
	 */
	public static AlertDialog getDialog(Context context,View view) {
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		return builder.setView(view).create();
	}
}

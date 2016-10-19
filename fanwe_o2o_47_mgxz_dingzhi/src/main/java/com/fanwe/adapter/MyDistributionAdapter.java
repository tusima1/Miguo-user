package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDImageUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.DistributionGoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class MyDistributionAdapter extends SDBaseAdapter<DistributionGoodsModel>
{

	private MyDistributionAdapterListener mListener;

	public void setmListener(MyDistributionAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public MyDistributionAdapter(List<DistributionGoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final DistributionGoodsModel model)
	{
		if (convertView == null)
		{
			convertView = View.inflate(mActivity, R.layout.item_my_distribution, null);
		}

		final ImageView siv_image = ViewHolder.get(convertView, R.id.siv_image);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_commission = ViewHolder.get(convertView, R.id.tv_commission);
		TextView tv_sale_count = ViewHolder.get(convertView, R.id.tv_sale_count);
		TextView tv_total_commission = ViewHolder.get(convertView, R.id.tv_total_commission);
		TextView tv_cancel = ViewHolder.get(convertView, R.id.tv_cancel);
		TextView tv_share = ViewHolder.get(convertView, R.id.tv_share);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);

		if (model != null)
		{
			SDViewBinder.setImageView(model.getIcon(), siv_image, new ImageLoadingListener()
			{

				@Override
				public void onLoadingStarted(String imageUri, View view)
				{
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
				{
					
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
				{
					if (model.getEnd_status() == 0)
					{
						Bitmap bmpGray = SDImageUtil.getGrayBitmap(loadedImage);
						if (bmpGray != null)
						{
							siv_image.setImageBitmap(bmpGray);
						}
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view)
				{
				}
			});
			SDViewBinder.setTextView(tv_name, model.getName());
			SDViewBinder.setTextView(tv_commission, model.getSale_balanceFormat());
			SDViewBinder.setTextView(tv_sale_count, String.valueOf(model.getSale_count()));
			SDViewBinder.setTextView(tv_total_commission, model.getSale_balance());
			SDViewBinder.setTextView(tv_time, model.getEnd_statusFormat());

			SDViewBinder.setTextView(tv_cancel, model.getCancelText());
			SDViewUtil.setTextViewColorResId(tv_cancel, model.getCancelTextColor());
			tv_cancel.setBackgroundResource(model.getCancelBackground());

			SDViewBinder.setTextView(tv_share, model.getShareText());
			SDViewUtil.setTextViewColorResId(tv_share, model.getShareTextColor());
			tv_share.setBackgroundResource(model.getShareBackground());

			switch (model.getEnd_status())
			{
			case 0: // 已过期
				SDViewUtil.show(tv_time);
				break;
			case 1:
				SDViewUtil.hide(tv_time);
				break;
			case 2: // 预告中
				SDViewUtil.show(tv_time);
				break;

			default:
				break;
			}

			tv_cancel.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					switch (model.getUd_is_effect())
					{
					case 0:
						// TODO 请求重新上架接口
						clickReShelves(model);
						break;
					case 1:
						// TODO 请求取消分销接口
						clickCancelDistribution(model);
						break;

					default:
						break;
					}
				}
			});

			tv_share.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					switch (model.getUd_is_effect())
					{
					case 0:
						// TODO 删除
						clickDeleteDistribution(model);
						break;
					case 1:
						// TODO 分享
//						UmengSocialManager.openShare(model.getSub_name(), model.getName() + model.getShare_url(), model.getIcon(),
//								model.getShare_url(), mActivity, null);
						UmengShareManager.share(mActivity, model.getSub_name(), model.getName() + model.getShare_url(), model.getShare_url(), UmengShareManager.getUMImage(mActivity, model.getIcon()), null);
						break;

					default:
						break;
					}

				}
			});

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					int id = model.getId();
					Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
					intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, id);
					mActivity.startActivity(intent);
				}
			});

		}
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	protected void clickDeleteDistribution(final DistributionGoodsModel model)
	{
		CommonInterface.requestDeleteDistribution(model.getId(), new SDRequestCallBack<BaseActModel>()
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					removeItem(indexOf(model));
					SDEventManager.post(EnumEventTag.DELETE_DISTRIBUTION_GOODS_SUCCESS.ordinal());
					if (mListener != null)
					{
						mListener.onStateChange();
					}
				}
			}

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("");
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
			}
		});
	}

	protected void clickCancelDistribution(final DistributionGoodsModel model)
	{
		if (model.getUd_type() == 1)
		{
			return;
		}

		CommonInterface.requestDistributionIsEffect(model.getId(), new SDRequestCallBack<BaseActModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					model.toggleIsEffect();
					updateItem(indexOf(model));
					if (mListener != null)
					{
						mListener.onStateChange();
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		});
	}

	protected void clickReShelves(DistributionGoodsModel model)
	{
		clickCancelDistribution(model);
	}

	public interface MyDistributionAdapterListener
	{
		public void onStateChange();
	}
}

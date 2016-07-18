package com.fanwe.common;

import com.alibaba.fastjson.JSONObject;
import com.fanwe.config.AppConfig;
import com.fanwe.dao.InitActModelDao;
import com.fanwe.dialog.InputImageCodeDialog;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.http.listener.proxy.SDRequestCallBackProxy;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Cart_indexActModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.Mobile_qrcode_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.Uc_fx_mallActModel;
import com.fanwe.model.Uc_fxinvite_indexActModel;
import com.fanwe.model.Uc_fxinvite_money_logActModel;
import com.fanwe.model.Uc_fxwithdraw_indexActModel;
import com.fanwe.model.Uc_home_do_replyActModel;
import com.fanwe.model.Uc_home_focusActModel;
import com.fanwe.model.Uc_home_showActModel;
import com.fanwe.model.User_infoModel;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.text.TextUtils;

public class CommonInterface
{

	public static void requestValidatePassword(String password, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_money");
		model.putAct("password_check");
		model.put("check_pwd", password);
		InterfaceServer.getInstance().requestInterface(model, listener);
	}

	public static void requestDynamicDetail(int id, SDRequestCallBack<Uc_home_showActModel> listener)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_home");
		model.putAct("show");
		model.put("id", id);
		InterfaceServer.getInstance().requestInterface(model, listener);
	}

	/**
	 * 请求赞接口
	 * 
	 * @param id
	 *            回复评论id
	 * @param listener
	 */
	public static void requestDeleteReply(int id, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_home");
		requestModel.putAct("del_reply");
		requestModel.put("id", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 请求赞接口
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestFocus(int id, SDRequestCallBack<Uc_home_focusActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_home");
		requestModel.putAct("focus");
		requestModel.put("uid", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 请求赞接口
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestDoFavTopic(int id, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_home");
		requestModel.putAct("do_fav_topic");
		requestModel.put("id", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 请求回复接口
	 * 
	 * @param id
	 *            分享主题id
	 * @param content
	 *            回复内容
	 * @param replyId
	 *            被回复的评论id
	 * @param listener
	 */
	public static void requestDoReply(int id, String content, int replyId, SDRequestCallBack<Uc_home_do_replyActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_home");
		requestModel.putAct("do_reply");
		requestModel.put("id", id);
		requestModel.put("content", content);
		requestModel.put("reply_id", replyId);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 分销商品上架和下架接口
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestDistributionIsEffect(int id, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fx");
		requestModel.putAct("do_is_effect");
		requestModel.put("deal_id", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 删除我的分销
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestDeleteDistribution(int id, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fx");
		requestModel.putAct("del_user_deal");
		requestModel.put("deal_id", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 我要分销
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestAddDistribution(int id, SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fx");
		requestModel.putAct("add_user_fx_deal");
		requestModel.put("deal_id", id);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 分销，我的小店
	 * 
	 * @param type
	 * @param page
	 * @param listener
	 */
	public static void requestDistributionStore(int type, int page, SDRequestCallBack<Uc_fx_mallActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fx");
		requestModel.putAct("mall");
		requestModel.put("type", type);
		requestModel.putPage(page);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 查看分销推荐
	 * 
	 * @param page
	 * @param listener
	 */
	public static void requestDistributionRecommend(int page, int userId, SDRequestCallBack<Uc_fxinvite_indexActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fxinvite");
		if (userId >= 0)
		{
			requestModel.put("user_id", userId);
		}
		requestModel.putPage(page);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 查看分销资金日志
	 * 
	 * @param page
	 * @param listener
	 */
	public static void requestDistributionMoneyLog(int page, SDRequestCallBack<Uc_fxinvite_money_logActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_fxinvite");
		requestModel.putAct("money_log");
		requestModel.putPage(page);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 查看分销提现日志
	 * 
	 * @param page
	 * @param listener
	 */
	public static void requestDistributionWithdrawLog(int page, SDRequestCallBack<Uc_fxwithdraw_indexActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("fxwithdraw");
		requestModel.putAct("applylog");
		requestModel.put("type", 1);
		requestModel.putPage(page);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 绑定手机号
	 * 
	 * @param mobile
	 *            手机号
	 * @param code
	 *            验证码
	 * @param listener
	 */
	public static void requestBindMobile(String mobile, String code, SDRequestCallBack<User_infoModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("user");
		requestModel.putAct("dophbind");
		requestModel.put("mobile", mobile);
		requestModel.put("sms_verify", code);
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBackProxy<User_infoModel>(listener)
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					LocalUserModel.dealLoginSuccess(actModel, false);
				}
				super.onSuccess(responseInfo);
			}
		});
	}

	public static void updateCartNumber()
	{
		// requestCart(null);
	}

	/**
	 * 请求购物车数据
	 * 
	 * @param listener
	 */
	public static void requestCart(SDRequestCallBack<Cart_indexActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("cart");
		requestModel.setmIsNeedCheckLoginState(false);
		requestModel.putUser();
		InterfaceServer.getInstance().requestInterface(HttpMethod.POST,requestModel, null,false, new SDRequestCallBackProxy<Cart_indexActModel>(listener)
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					int cartNumber = actModel.getCartNumber();
				}
				super.onSuccess(responseInfo);
			}
		});
	}

	/**
	 * 删除订单
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestDeleteOrder(int id, SDRequestCallBack<BaseActModel> listener)
	{
		if (id <= 0)
		{
			return;
		}
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("cancel");
		requestModel.put("id", id);
		requestModel.putUser();
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}
	/**
	 * 取消订单
	 * 
	 * @param id
	 * @param listener
	 */
	public static void requestCanCelOrder(int id, SDRequestCallBack<BaseActModel> listener)
	{
		if (id <= 0)
		{
			return;
		}
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("cancel_new");
		requestModel.put("id", id);
		requestModel.putUser();
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 请求绑定默认地址
	 * 
	 * @param id
	 *            地址id
	 * @param listener
	 */
	public static void requestBindDefaultAddress(int id, SDRequestCallBack<JSONObject> listener)
	{
		if (id <= 0)
		{
			return;
		}
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_address");
		requestModel.putAct("set_default");
		requestModel.put("id", id);
		requestModel.putUser();
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 请求验证码
	 * 
	 * @param mobile
	 *            手机号
	 * @param unique
	 *            0:不检测 ，1:要检测是否被抢占(绑定手机)， 2:要检测是否存在(修改密码)
	 */
	public static void requestValidateCode(String mobile, int unique, SDRequestCallBack<Sms_send_sms_codeActModel> listener)
	{
		String imageCode = AppConfig.getImageCode();
		requestValidateCode(mobile, unique, imageCode, listener);
	}

	private static void requestValidateCode(String mobile, int unique, final String imageCode, SDRequestCallBack<Sms_send_sms_codeActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("sms");
		requestModel.putAct("send_sms_code");
		requestModel.put("mobile", mobile);
		requestModel.put("unique", unique);
		requestModel.put("verify_code", imageCode);
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBackProxy<Sms_send_sms_codeActModel>(listener)
		{
			@Override
			public void onStart()
			{
				if (getOriginalCallBack() != null)
				{
					getOriginalCallBack().showToast = false;
				}
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (getOriginalCallBack() != null)
				{
					getOriginalCallBack().showToast = true;
				}
				switch (actModel.getStatus())
				{
				case -1:
					InputImageCodeDialog dialog = new InputImageCodeDialog();
					dialog.setImage(actModel.getVerify_image());
					dialog.show();
					if (!TextUtils.isEmpty(imageCode))
					{
						if (getOriginalCallBack() != null)
						{
							getOriginalCallBack().showToast();
						}
					}
					break;
				case 1:
					AppConfig.setImageCode("");
					if (getOriginalCallBack() != null)
					{
						getOriginalCallBack().showToast();
					}
					break;

				default:
					if (getOriginalCallBack() != null)
					{
						getOriginalCallBack().showToast();
					}
					break;
				}
				super.onSuccess(responseInfo);
			}
		});
	}

	/**
	 * 初始化
	 * 
	 * @param listener
	 */
	public static void requestInit(SDRequestCallBack<Init_indexActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("init");
		requestModel.putUser();
		requestModel.put("device_type", "android");
		requestModel.setmIsNeedShowErrorTip(false);
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBackProxy<Init_indexActModel>(listener)
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					// TODO:对初始化返回结果进行处理
					InitActModelDao.insertOrUpdateModel(actModel);
					LocalUserModel.dealLoginSuccess(actModel.getUser(), false);
				}
				super.onSuccess(responseInfo);
			}

			@Override
			public void onFinish()
			{
				super.onFinish();
			}
		});
	}

	/**
	 * 请求解释二维码数据
	 * 
	 * @param scanResult
	 *            二维码字符串
	 * @param listener
	 */
	public static void requestScanResult(String scanResult, final SDRequestCallBack<Mobile_qrcode_indexActModel> listener)
	{
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("mobile_qrcode");
		requestModel.put("pc_url", scanResult);
		InterfaceServer.getInstance().requestInterface(requestModel, listener);
	}

	/**
	 * 登出
	 */
	public static void requestLogout(SDRequestCallBack<BaseActModel> listener)
	{
		RequestModel model = new RequestModel();
		model.putCtl("user");
		model.putAct("loginout");
		InterfaceServer.getInstance().requestInterface(model, listener);
	}
}

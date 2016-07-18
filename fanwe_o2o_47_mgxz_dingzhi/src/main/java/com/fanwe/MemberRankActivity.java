package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_DistModel;
import com.fanwe.model.Uc_HomeModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MemberRankActivity extends BaseActivity {
	@ViewInject(R.id.iv_user_avatar)
	private CircularImageView mIv_avatar;

	@ViewInject(R.id.tv_name)
	private TextView mTv_name;

	@ViewInject(R.id.tv_address)
	private TextView mTv_address;

	@ViewInject(R.id.tv_rank)
	private TextView mTv_rank;

	@ViewInject(R.id.ll_rank_quan)
	private LinearLayout mLl_quan;

	@ViewInject(R.id.ll_rank_number)
	private LinearLayout mLl_number;

	@ViewInject(R.id.tv_number)
	private TextView mTv_number;

	@ViewInject(R.id.ll_rank_tixian)
	private LinearLayout mLl_ramk_tixian;

	@ViewInject(R.id.ll_rank_shop)
	private LinearLayout mLl_rank_shop;

	@ViewInject(R.id.iv_image)
	private ImageView iv_image;

	@ViewInject(R.id.bt_addMember)
	private Button mBt_addMember;

	protected Uc_HomeModel mActModel;

	@ViewInject(R.id.webView_dis)
	private WebView mWebView;

	@ViewInject(R.id.act_my_refresh)
	private PullToRefreshScrollView mPull_toRefresh;

	private String user_avatar;//用户头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_rank);
		init();
	}

	private void init() {
		initTitle();
		initWeb();
		// requestDatat();
		// requestWeb();
		initClick();
		initPullToRefreshListView();
	}

	private void initPullToRefreshListView() {
		mPull_toRefresh.setMode(Mode.PULL_FROM_START);
		mPull_toRefresh
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						requestDatat();
						requestWeb();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {

					}
				});
		mPull_toRefresh.setRefreshing();
	}

	private void requestWeb() {
		RequestModel model = new RequestModel();
		model.putCtl("page");
		model.putAct("fx_member");

		InterfaceServer.getInstance().requestInterface(model,
				new SDRequestCallBack<Uc_DistModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							bindResult(actModel);
						}
					}

					@Override
					public void onFinish() {
						mPull_toRefresh.onRefreshComplete();
					}
				});
	}

	protected void bindResult(Uc_DistModel actModel) {
		mWebView.loadDataWithBaseURL(null, actModel.getBody(), "text/html",
				"utf-8", null);
	}

	private void initWeb() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		mWebView.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	private void requestDatat() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_home");
		model.putAct("homepage");
		InterfaceServer.getInstance().requestInterface(model,
				new SDRequestCallBack<Uc_HomeModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							mActModel = actModel;
							bindData(actModel);
						}
					}

					@Override
					public void onFinish() {
						mPull_toRefresh.onRefreshComplete();
					}
				});
	}

	protected void bindData(Uc_HomeModel actModel) {
		if (actModel == null) {
			return;
		}
		SDViewBinder.setTextView(mTv_name, actModel.getUser_name(), "未找到");
		if (actModel.getDist().getRank() == 1) {
			SDViewBinder.setTextView(mTv_rank, "青铜", "未找到");
		} else if (actModel.getDist().getRank() == 2) {
			SDViewBinder.setTextView(mTv_rank, "白金", "未找到");
		} else if (actModel.getDist().getRank() == 3) {
			SDViewBinder.setTextView(mTv_rank, "钻石", "未找到");
		}

		SDViewBinder.setTextView(mTv_address, AppRuntimeWorker.getCity_name(),
				"未找到");
		user_avatar = actModel.getUser_avatar();
		SDViewBinder.setImageView(mIv_avatar, user_avatar);
		SDViewBinder.setTextView(mTv_number, actModel.getDist().getAllow()
				+ "个", "0个");
		if (actModel.getDist().getWithdraw() == 1) {
			iv_image.setImageResource(R.drawable.bg_rank_oktian);
		} else {
			iv_image.setImageResource(R.drawable.bg_rank_tixian);
		}

		if (actModel.getDist().getRank() < 3) {
			SDViewUtil.show(mBt_addMember);
		} else {
			SDViewUtil.hide(mBt_addMember);
		}
	}

	private void initClick() {
		mLl_quan.setOnClickListener(this);
		mBt_addMember.setOnClickListener(this);
		mTv_name.setOnClickListener(this);
		mIv_avatar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_rank_quan:
			clickMore();
			break;
		case R.id.bt_addMember:
			clickBt();
			break;
		case R.id.iv_user_avatar:
			gotoUserSet();
			break;
		case R.id.tv_name:
			gotoUserSet();
			break;
		default:
			break;
		}
	}

	/**
	 * 跳转到设置页面。
	 */
	public void gotoUserSet() {
		Intent intent = new Intent(this, MyAccountActivity.class);
		Bundle userFace=new Bundle();
		userFace.putString("user_face", user_avatar);
		intent.putExtras(userFace);
		startActivity(intent);
	}

	private void clickBt() {
		if (mActModel.getDist().getRank() >= 2) {
			SDToast.showToast("您还没有达到升级要求!");
			return;
		}
		Intent intent = new Intent(this, ConfirmTopUpActivity.class);
		startActivity(intent);
	}

	private void clickMore() {
		Intent intent = new Intent(this, MemberRankDetailActivity.class);
		intent.putExtra("int", mActModel.getDist().getRank());
		startActivity(intent);
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("我的会员");
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextBot("升级");
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
		super.onCLickRight_SDTitleSimple(v, index);

		if (mActModel.getDist().getRank() >= 2) {
			SDToast.showToast("您还没有达到升级要求!");
			return;
		}
		clickBt();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mPull_toRefresh.setRefreshing();
	}

}

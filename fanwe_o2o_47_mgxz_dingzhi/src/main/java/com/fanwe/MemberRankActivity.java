package com.fanwe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_DistModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getDistrInfo.ModelDistrInfo;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class MemberRankActivity extends BaseActivity implements CallbackView2 {
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

    @ViewInject(R.id.webView_dis)
    private WebView mWebView;

    @ViewInject(R.id.act_my_refresh)
    private PullToRefreshScrollView mPull_toRefresh;

    private String user_avatar;//用户头像
    private UserHttpHelper userHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_rank);
        init();
    }

    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(this, this);
        }
        userHttpHelper.getDistrInfo();
    }

    private void init() {
        initTitle();
        initWeb();
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
                        getData();
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
        Bundle userFace = new Bundle();
        userFace.putString("user_face", user_avatar);
        intent.putExtras(userFace);
        startActivity(intent);
    }

    private void clickBt() {
        if (DataFormat.toInt(modelDistrInfo.getFx_level()) >= 2) {
            SDToast.showToast("您还没有达到升级要求!");
            return;
        }
        Intent intent = new Intent(this, ConfirmTopUpActivity.class);
        startActivity(intent);
    }

    private void clickMore() {
        Intent intent = new Intent(this, MemberRankDetailActivity.class);
        intent.putExtra("int", DataFormat.toInt(modelDistrInfo.getFx_level()));
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

        if (DataFormat.toInt(modelDistrInfo.getFx_level()) >= 2) {
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

    @Override
    public void onSuccess(String responseBody) {

    }

    private ModelDistrInfo modelDistrInfo = new ModelDistrInfo();

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.DISTR_INFO.equals(method)) {
            msg.what = 0;
            if (!SDCollectionUtil.isEmpty(datas)) {
                modelDistrInfo = (ModelDistrInfo) datas.get(0);
            }
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (modelDistrInfo != null) {
                        SDViewBinder.setTextView(mTv_name, modelDistrInfo.getNick(), modelDistrInfo.getPhone());
                        if ("1".equals(modelDistrInfo.getFx_level())) {
                            SDViewBinder.setTextView(mTv_rank, "青铜", "未找到");
                        } else if ("2".equals(modelDistrInfo.getFx_level())) {
                            SDViewBinder.setTextView(mTv_rank, "白金", "未找到");
                        } else if ("3".equals(modelDistrInfo.getFx_level())) {
                            SDViewBinder.setTextView(mTv_rank, "钻石", "未找到");
                        }
                        SDViewBinder.setTextView(mTv_address, AppRuntimeWorker.getCity_name(),
                                "未找到");
                        user_avatar = modelDistrInfo.getIcon();
                        SDViewBinder.setImageView(mIv_avatar, user_avatar);
                        SDViewBinder.setTextView(mTv_number, modelDistrInfo.getFx_total_num()
                                + "个", "0个");
                        if (DataFormat.toInt(modelDistrInfo.getFx_level()) > 1) {
                            iv_image.setImageResource(R.drawable.bg_rank_oktian);
                        } else {
                            iv_image.setImageResource(R.drawable.bg_rank_tixian);
                        }

                        if (DataFormat.toInt(modelDistrInfo.getFx_level()) < 3) {
                            SDViewUtil.show(mBt_addMember);
                        } else {
                            SDViewUtil.hide(mBt_addMember);
                        }
                    }
                    break;
            }
        }
    };
}

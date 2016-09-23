package com.fanwe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.app.AppConfig;
import com.fanwe.app.AppHelper;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.LoadImageType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.SettingModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Discover_indexActModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.module.user.UserFaceModule;
import com.fanwe.o2o.miguo.R;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.io.File;
import java.util.List;

/**
 * 我的账户
 *
 * @author Administrator
 */
public class MyAccountActivity extends BaseActivity implements CallbackView2 {

    @ViewInject(R.id.et_username)
    private TextView mEt_username; // 用户名

    @ViewInject(R.id.et_email)
    private EditText mEt_email; // 邮箱

    @ViewInject(R.id.ll_withdraw)
    private LinearLayout mLl_withdraw; // 提现

    @ViewInject(R.id.ll_bind_mobile)
    private LinearLayout mLl_bind_mobile; // 绑定手机

    @ViewInject(R.id.tv_bind_mobile)
    private TextView mTv_bind_mobile;

    @ViewInject(R.id.tv_mobile)
    private TextView mTv_mobile;

    @ViewInject(R.id.rl_about_us)
    private RelativeLayout mRl_about_us;

    @ViewInject(R.id.rl_clear_cache)
    private RelativeLayout mRl_clear_cache;

    @ViewInject(R.id.rl_upgrade)
    private RelativeLayout mRlUpgrade;

    @ViewInject(R.id.tv_mobile_tip)
    private TextView mTv_mobile_tip;

    @ViewInject(R.id.cb_load_image_in_mobile_net)
    private CheckBox mCb_load_image_in_mobile_net;

    @ViewInject(R.id.ll_modify_password)
    private LinearLayout mLl_modify_password; // 修改密码

    @ViewInject(R.id.ll_delivery_address)
    private LinearLayout mLl_delivery_address; // 配送地址

    @ViewInject(R.id.ll_delivery_address_dc)
    private LinearLayout mLl_delivery_address_dc; // 外卖收货地址

    @ViewInject(R.id.ll_third_bind)
    private LinearLayout mLl_third_bind; // 第三方绑定

    @ViewInject(R.id.ll_bind_qq)
    private LinearLayout mLl_bind_qq; // 绑定qq

    @ViewInject(R.id.ll_bind_sina)
    private LinearLayout mLl_bind_sina; // 绑定新浪微博

    @ViewInject(R.id.btn_logout)
    private Button mBtn_logout; // 退出当前帐号

    private String mStrUsername;
    private String mStrEmail;

    @ViewInject(R.id.tv_kf_phone)
    private TextView mTv_kf_phone;

    @ViewInject(R.id.rl_kf_phone)
    private RelativeLayout mRl_kf_phone;

    @ViewInject(R.id.tv_version)
    private TextView mTv_version;

    @ViewInject(R.id.tv_cache_size)
    private TextView mTv_cache_size;

    @ViewInject(R.id.iv_user_face)
    private CircularImageView mUserFace;// 头像

    private LocalUserModel mUser;

    private UserFaceModule mUserFaceModule;
    private UserHttpHelper userHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_my_account);
        init();
    }

    private void init() {
        mUserFaceModule = new UserFaceModule(this);
        mUserFaceModule.initPhotoHandler();
        initViewState();
        initTitle();
        initBundle();
        bindData();
        showCacheSize();
        registerClick();
        setView();
    }

    private void setView() {
        mEt_username.setText(App.getInstance().getUserNickName());
        userHttpHelper = new UserHttpHelper(this, this);
    }

    private void initBundle() {
    }

    private void bindData() {
        int loadImageInMobileNet = SettingModelDao.getLoadImageType();
        if (loadImageInMobileNet == LoadImageType.ALL) {
            mCb_load_image_in_mobile_net.setChecked(true);
            mCb_load_image_in_mobile_net.setText("是");
        } else {
            mCb_load_image_in_mobile_net.setChecked(false);
            mCb_load_image_in_mobile_net.setText("否");
        }
        mCb_load_image_in_mobile_net
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            SettingModelDao
                                    .updateLoadImageType(LoadImageType.ALL);
                            mCb_load_image_in_mobile_net.setText("是");
                        } else {
                            SettingModelDao
                                    .updateLoadImageType(LoadImageType.ONLY_WIFI);
                            mCb_load_image_in_mobile_net.setText("否");
                        }
                    }
                });

        PackageInfo pi = SDPackageUtil.getCurrentPackageInfo();
        mTv_version.setText(String.valueOf(pi.versionName));

        String kfPhone = AppRuntimeWorker.getKf_phone();
        SDViewBinder.setTextView(mTv_kf_phone, kfPhone);
        ImageLoader.getInstance().displayImage(App.getInstance().getUserIcon(), mUserFace);

    }

    private void showCacheSize() {
        File cacheDir = ImageLoader.getInstance().getDiskCache().getDirectory();
        if (cacheDir != null) {
            long cacheSize = SDFileUtil.getFileSize(cacheDir);
            mTv_cache_size.setText(SDFileUtil.formatFileSize(cacheSize));
        }
    }

    private void initViewState() {

        mUser = AppHelper.getLocalUser();
        if (mUser == null) {
            return;
        }

        // 使用appconfig里的数据
//		mEt_username.setText(mUser.getUser_name());
        mEt_email.setText(mUser.getUser_email());

        int isTemp = mUser.getIs_tmp();
        if (isTemp == 1) {
            mEt_email.setEnabled(true);
            // mEt_username.setEnabled(true);
        } else {
            mEt_email.setEnabled(false);
            // mEt_username.setEnabled(false);
        }

        String mobile = mUser.getUser_mobile();
        if (isEmpty(mobile)) {
            SDViewUtil.hide(mTv_mobile_tip);
            mTv_bind_mobile.setText("未绑定手机");
        } else {
            SDViewUtil.hide(mTv_mobile_tip);
            mTv_bind_mobile.setText("已绑定手机");
            mTv_mobile.setText(SDOtherUtil.hideMobile(mobile));
        }

        if (AppRuntimeWorker.getIs_plugin_dc() == 1) {
            SDViewUtil.hide(mLl_delivery_address_dc);
        } else {
            SDViewUtil.hide(mLl_delivery_address_dc);
        }

        Init_indexActModel model = AppRuntimeWorker.getInitActModel();
        if (model == null) {
            return;
        }

        if (model.getMenu_user_withdraw() == 1) {
            SDViewUtil.hide(mLl_withdraw);
        } else {
            SDViewUtil.hide(mLl_withdraw);
        }

        // SDViewUtil.hide(mLl_third_bind);
        /*
         * String sinaAppKey = model.getSina_app_key(); if
		 * (TextUtils.isEmpty(sinaAppKey)) { SDViewUtil.hide(mLl_bind_sina); }
		 * else { SDViewUtil.show(mLl_bind_sina);
		 * SDViewUtil.show(mLl_third_bind); }
		 * 
		 * String qqAppKey = model.getQq_app_key(); if
		 * (TextUtils.isEmpty(qqAppKey)) { SDViewUtil.hide(mLl_bind_qq); } else
		 * { SDViewUtil.show(mLl_bind_qq); SDViewUtil.show(mLl_third_bind); }
		 */
    }

    private void registerClick() {
//        mLl_bind_mobile.setOnClickListener(this);
        mLl_modify_password.setOnClickListener(this);
        mLl_delivery_address.setOnClickListener(this);
        mLl_delivery_address_dc.setOnClickListener(this);
        mLl_bind_qq.setOnClickListener(this);
        mLl_bind_sina.setOnClickListener(this);
        mBtn_logout.setOnClickListener(this);
        mLl_withdraw.setOnClickListener(this);
        mEt_username.setOnClickListener(this);
        mRl_clear_cache.setOnClickListener(this);
        mRl_kf_phone.setOnClickListener(this);
        mRl_about_us.setOnClickListener(this);
        mRlUpgrade.setOnClickListener(this);
        mUserFace.setOnClickListener(this);
    }

    private void clickTestUpgrade() {
        Intent intent = new Intent(App.getApplication(),
                AppUpgradeService.class);
        intent.putExtra(AppUpgradeService.EXTRA_SERVICE_START_TYPE, 1);
        startService(intent);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("设置");
        /*
         * mTitle.initRightItem(0); if (mUser != null) { if (mUser.getIs_tmp()
		 * == 1) { mTitle.initRightItem(1);
		 * mTitle.getItemRight(0).setTextBot("保存"); } }
		 */
    }

	/*
     * @Override public void onCLickRight_SDTitleSimple(SDTitleItem v, int
	 * index) { clickSubmit(); }
	 */

    @Override
    public void onClick(View v) {
        if (v == mLl_bind_mobile) {
            clickBindMobile(v);
        } else if (v == mLl_modify_password) {
            clickModifyPassword(v);
        } else if (v == mLl_delivery_address) {
            // clickDeliveryAddress(v);
            MGToast.showToast("此功能已下线,暂不可用.");
        } else if (v == mLl_delivery_address_dc) {
            clickDeliveryAddressDc(v);
        } else if (v == mLl_bind_qq) {
            // clickBindQQ(v);
        } else if (v == mLl_bind_sina) {
            // clickBindSina(v);
        } else if (v == mBtn_logout) {
            clickLogout(v);
        } else if (v == mLl_withdraw) {
            clickWithdraw(v);
        } else if (v == mEt_username) {
            clickUsername();
        } else if (v == mRl_clear_cache) {
            clickClearCache();
        } else if (v == mRl_kf_phone) {
            clickKfPhone();
        } else if (v == mRl_about_us) {
            clickAbout();
        } else if (v == mRlUpgrade) {
            clickTestUpgrade();
        } else if (v == mUserFace) {
            clickUserFace();
        }
    }

    /**
     * 用户头像
     */
    private void clickUserFace() {
        mUserFaceModule.setupUserFace(this);
    }

    /**
     * 关于
     */
    private void clickAbout() {
        int noticeId = AppRuntimeWorker.getAbout_info();
        if (noticeId > 0) {
            Intent intent = new Intent(this, NoticeDetailActivity.class);
            intent.putExtra(NoticeDetailActivity.EXTRA_NOTICE_ID, noticeId);
            startActivity(intent);
        } else {
            MGToast.showToast("未找到关于我们ID");
        }
    }

    /**
     * 客服电话
     */
    private void clickKfPhone() {
        String kfPhone = AppRuntimeWorker.getKf_phone();
        if (!TextUtils.isEmpty(kfPhone)) {
            Intent intent = SDIntentUtil.getIntentCallPhone(kfPhone);
            SDActivityUtil.startActivity(this, intent);
        } else {
            MGToast.showToast("未找到客服电话");
        }
    }

    private String nickName;

    private void clickUsername() {
        // TODO 魅族上为啥这么丑
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.myaccount_dialog, null);
        TextView mTv_name = (TextView) view.findViewById(R.id.tv_name);
        final EditText mInputName = (EditText) view
                .findViewById(R.id.inputName);
        mTv_name.setText("当前的用户名: " + mEt_username.getText());
        final AlertDialog alertdialog = new AlertDialog.Builder(this).create();
        // 在此使用setview方法可以设置布局文件和alertdialog四周边框的距离，可以消除黑边框
        alertdialog.setView(view, 0, 0, 0, 0);
        alertdialog.setCanceledOnTouchOutside(false);
        alertdialog.show();

        Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
        Button bt_ensure = (Button) view.findViewById(R.id.bt_ensure);
        bt_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });
        bt_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("".equals(mInputName.getText().toString())) {
                    MGToast.showToast("名字不能为空!");
                    return;
                }
                if (mInputName.getText().toString().contains("米果")) {
                    MGToast.showToast("用户已被占用!");
                    return;
                }
                nickName = mInputName.getText().toString();
                updateNickname(nickName);
                alertdialog.dismiss();
            }

        });

    }

    private void updateNickname(String name) {
        userHttpHelper.updateUserInfo("nick", name);
    }

    // 修改名字
    private void updateName(final String name) {
        RequestModel model = new RequestModel();
        model.putCtl("user");
        model.putAct("update");
        model.put("user_name", name);

        InterfaceServer.getInstance().requestInterface(model,
                new SDRequestCallBack<Discover_indexActModel>() {
                    @Override
                    public void onStart() {
                        SDDialogManager.showProgressDialog("");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (actModel.getStatus() == 1) {
                            mEt_username.setText(name);
                            mUser.setUser_name(name);
                            AppHelper.updateLocalUser(mUser);

                            AppConfig.setUserName(name);
                        }

                    }

                    @Override
                    public void onFinish() {
                        SDDialogManager.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MGToast.showToast("修改失败,请检查网络...");
                    }
                });
    }

    private void clickWithdraw(View v) {
        Intent intent = new Intent(this, AccountMoneyActivity.class);
        startActivity(intent);
    }

    private void clickDeliveryAddressDc(View v) {
        // TODO 跳转到外卖收货地址列表界面
        // Intent intent = new Intent(this, MyAddressActivity_dc.class);
        // startActivity(intent);
    }

    private void clickLogout(View v) {
        App.getInstance().getmUserCurrentInfo().setUserInfoNew(null);
        App.getInstance().setImLoginSuccess(false);
        LocalUserModel userModel = new LocalUserModel();
        App.getInstance().setmLocalUser(userModel);
        AppConfig.setRefId("");
        App.getInstance().clearAllData();
        SDEventManager.post(EnumEventTag.LOGOUT.ordinal());
    }

    /**
     * 清除缓存
     */
    private void clickClearCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageLoader.getInstance().clearDiskCache();
                SDHandlerUtil.runOnUiThread(new Runnable() {
                    public void run() {
                        mTv_cache_size.setText("0.00B");
                        MGToast.showToast("清除完毕");
                    }
                });
            }
        }).start();
    }

    /**
     * 绑定新浪微博
     *
     * @param v
     */
    /**
     * private void clickBindSina(View v) {
     * UmengSocialManager.doOauthVerify(this, SHARE_MEDIA.SINA, new
     * UMAuthListener() {
     *
     * @Override public void onStart(SHARE_MEDIA arg0) { }
     * @Override public void onError(SocializeException arg0, SHARE_MEDIA arg1)
     * { }
     * @Override public void onComplete(Bundle bundle, SHARE_MEDIA arg1) {
     * String uid = bundle.getString("uid"); String access_token =
     * bundle.getString("access_secret"); requestBindSina(uid,
     * access_token); }
     * @Override public void onCancel(SHARE_MEDIA arg0) { } }); }
     **/

    protected void requestBindSina(String uid, String access_token) {
        RequestModel model = new RequestModel();
        model.putCtl("syncbind");
        model.put("login_type", "Sina");
        model.putUser();
        model.put("sina_id", uid);
        model.put("access_token", access_token);
        SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    /**
     * 绑定qq
     *
     * @param v
     */
    /**
     * private void clickBindQQ(View v) { UmengSocialManager.doOauthVerify(this,
     * SHARE_MEDIA.QQ, new UMAuthListener() {
     *
     * @Override public void onStart(SHARE_MEDIA arg0) { }
     * @Override public void onError(SocializeException arg0, SHARE_MEDIA arg1)
     * { }
     * @Override public void onComplete(Bundle bundle, SHARE_MEDIA arg1) {
     * String openId = bundle.getString("openid"); String access_token
     * = bundle.getString("access_token"); requestBindQQ(openId,
     * access_token); }
     * @Override public void onCancel(SHARE_MEDIA arg0) { } }); }
     **/

    protected void requestBindQQ(String openid, String access_token) {
        RequestModel model = new RequestModel();
        model.putCtl("syncbind");
        model.put("login_type", "Qq");
        model.putUser();
        model.put("qqv2_id", openid);
        model.put("access_token", access_token);
        SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    /**
     * 配送地址
     *
     * @param v
     */
    private void clickDeliveryAddress(View v) {
        Intent intent = new Intent(this, DeliveryAddressManageActivty.class);
        startActivity(intent);
    }

    /**
     * 修改密码
     *
     * @param v
     */
    private void clickModifyPassword(View v) {
        String mobile = AppHelper.getLocalUser().getUser_mobile();
        if (isEmpty(mobile)) {
            startActivity(new Intent(this, BindMobileActivity.class));
        } else {
            startActivity(new Intent(this, ModifyPasswordActivity.class));
        }
    }

    /**
     * 绑定手机
     *
     * @param v
     */
    private void clickBindMobile(View v) {
        Intent intent = new Intent(getApplicationContext(),
                BindMobileActivity.class);
        startActivity(intent);
    }

    private void clickSubmit() {
        if (!validateParams()) {
            return;
        }

        RequestModel model = new RequestModel();
        model.putUser();
        model.putCtl("uc_account");
        model.putAct("save");
        model.put("user_name", mStrUsername);
        model.put("user_email", mStrEmail);

        InterfaceServer.getInstance().requestInterface(model,
                new SDRequestCallBack<User_infoModel>() {

                    @Override
                    public void onStart() {
                        SDDialogManager.showProgressDialog("请稍候");
                    }

                    @Override
                    public void onFinish() {
                        SDDialogManager.dismissProgressDialog();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (actModel.getStatus() == 1) {
                            AppConfig.setUserName(actModel.getUser_name());
                            LocalUserModel.dealLoginSuccess(actModel, false);
                            initViewState();
                            initTitle();
                        }
                    }
                });
    }

    private boolean validateParams() {
        mStrUsername = mEt_username.getText().toString();
        if (isEmpty(mStrUsername)) {
            MGToast.showToast("用户名不能为空");
            return false;
        }
        mStrEmail = mEt_email.getText().toString();
        if (isEmpty(mStrEmail)) {
            MGToast.showToast("邮箱不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case BIND_MOBILE_SUCCESS:
                initViewState();
                break;
            case UPLOAD_USER_INFO_SUCCESS:
                mEt_username.setText(App.getInstance().getUserNickName());
                break;
            case UPLOAD_USER_HEAD_SUCCESS:
                ImageLoader.getInstance().displayImage(App.getInstance().getUserIcon(), mUserFace);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserFaceModule.onDestory();
        mUserFaceModule = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUserFaceModule.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.USER_INFO_METHOD.equals(method)) {
            MGToast.showToast("修改成功");
            App.getInstance().setUserNickName(nickName);
            SDEventManager.post(EnumEventTag.UPLOAD_USER_INFO_SUCCESS.ordinal());
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

}

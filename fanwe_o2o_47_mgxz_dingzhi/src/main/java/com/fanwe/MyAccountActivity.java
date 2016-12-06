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
import com.fanwe.base.CallbackView;
import com.fanwe.common.MGDict;
import com.fanwe.common.model.getMGDict.DictModel;
import com.fanwe.constant.Constant.LoadImageType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.dao.SettingModelDao;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.module.user.UserFaceModule;
import com.fanwe.o2o.miguo.R;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.LoginHelper;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.user.view.SexActivity;
import com.fanwe.user.view.SignActivity;
import com.fanwe.utils.StringTool;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的账户
 *
 * @author Administrator
 */
public class MyAccountActivity extends BaseActivity implements CallbackView {
    // 用户名
    @ViewInject(R.id.et_username)
    private TextView mEt_username;
    // 个人简介
    @ViewInject(R.id.tv_sign)
    private TextView tvSign;
    @ViewInject(R.id.layout_sign)
    private LinearLayout layoutSign;
    // 性别
    @ViewInject(R.id.tv_sex)
    private TextView tvSex;
    @ViewInject(R.id.layout_sex)
    private LinearLayout layoutSex;
    // 邮箱
    @ViewInject(R.id.et_email)
    private EditText mEt_email;
    // 提现
    @ViewInject(R.id.ll_withdraw)
    private LinearLayout mLl_withdraw;
    // 绑定手机
    @ViewInject(R.id.ll_bind_mobile)
    private LinearLayout mLl_bind_mobile;

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

    @ViewInject(R.id.btn_logout)
    private Button mBtn_logout; // 退出当前帐号

    @ViewInject(R.id.tv_kf_phone)
    private TextView mTv_kf_phone;

    @ViewInject(R.id.rl_kf_phone)
    private RelativeLayout mRl_kf_phone;

    @ViewInject(R.id.tv_version)
    private TextView mTv_version;

    @ViewInject(R.id.tv_cache_size)
    private TextView mTv_cache_size;

    @ViewInject(R.id.iv_user_face)
    private CircleImageView mUserFace;// 头像

    private LocalUserModel mUser;

    private UserFaceModule mUserFaceModule;
    private UserHttpHelper userHttpHelper;
    private String mKefuNum = "";
    private LoginHelper mLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_my_account);
        init();
    }

    private void init() {
        mLoginHelper = new LoginHelper(this);
        mUserFaceModule = new UserFaceModule(this);
        mUserFaceModule.initPhotoHandler();
        getServiceNum();
        initViewState();
        initTitle();
        initBundle();
        bindData();
        showCacheSize();
        registerClick();
        setView();
    }

    /**
     * 取字典中的客服电话
     */
    private void getServiceNum() {
        List<DictModel> dict = MGDict.getDict();
        if (dict == null || dict.size() < 1) {
            return;
        }
        for (DictModel data : dict) {
            String dic_value = data.getDic_value();
            if ("support_phone".equals(dic_value)) {
                mKefuNum = data.getDic_mean();
                break;
            }
        }
    }

    private void setView() {
        mEt_username.setText(App.getInstance().getUserNickName());
        userHttpHelper = new UserHttpHelper(this, this);
    }

    private void initBundle() {
    }

    private void bindData() {
        String remark = "";
        String sex = "性别";
        if (App.getInstance().getCurrentUser() != null) {
            remark = App.getInstance().getCurrentUser().getRemark();
            sex = App.getInstance().getCurrentUser().getSex();
        }
        SDViewBinder.setTextView(tvSign, remark, "个人简介");
        if ("1".equals(sex)) {
            //女
            tvSex.setText("女");
        } else if ("2".equals(sex)) {
            //男
            tvSex.setText("男");
        } else {
            tvSex.setText("性别");
        }
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

        SDViewBinder.setTextView(mTv_kf_phone, mKefuNum);
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
        mEt_email.setText(mUser.getUser_email());
        int isTemp = mUser.getIs_tmp();
        if (isTemp == 1) {
            mEt_email.setEnabled(true);
        } else {
            mEt_email.setEnabled(false);
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
        SDViewUtil.hide(mLl_withdraw);
        SDViewUtil.hide(mLl_delivery_address_dc);
    }

    private void registerClick() {
//        mLl_bind_mobile.setOnClickListener(this);
        mLl_modify_password.setOnClickListener(this);
        mLl_delivery_address.setOnClickListener(this);
        mLl_delivery_address_dc.setOnClickListener(this);
        mBtn_logout.setOnClickListener(this);
        mLl_withdraw.setOnClickListener(this);
        mEt_username.setOnClickListener(this);
        mRl_clear_cache.setOnClickListener(this);
        mRl_kf_phone.setOnClickListener(this);
        mRl_about_us.setOnClickListener(this);
        mRlUpgrade.setOnClickListener(this);
        mUserFace.setOnClickListener(this);
        layoutSign.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
    }

    private void clickTestUpgrade() {
        Intent intent = new Intent(App.getApplication(),
                AppUpgradeService.class);
        intent.putExtra(AppUpgradeService.EXTRA_SERVICE_START_TYPE, 1);
        startService(intent);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("设置");
    }

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
        } else if (v == layoutSign) {
            clickSign();
        } else if (v == layoutSex) {
            clickSex();
        }
    }

    /**
     * 性别
     */
    private void clickSex() {
        Intent intent = new Intent(MyAccountActivity.this, SexActivity.class);
        intent.putExtra("sex", App.getInstance().getCurrentUser().getSex());
        startActivity(intent);
    }

    /**
     * 个人简介
     */
    private void clickSign() {
        Intent intent = new Intent(MyAccountActivity.this, SignActivity.class);
        intent.putExtra("sign", App.getInstance().getCurrentUser().getRemark());
        startActivity(intent);
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
    }


    /**
     * 客服电话
     */
    private void clickKfPhone() {
        if (TextUtils.isEmpty(mKefuNum)) {
            MGToast.showToast("获取客服电话失败");
        } else {
            callKeFu(mKefuNum);
        }
    }

    private void callKeFu(String tel) {
        Intent intent = SDIntentUtil.getIntentCallPhone(tel);
        SDActivityUtil.startActivity(this, intent);
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
                String name = mInputName.getText().toString().trim();
                if ("".equals(name)) {
                    MGToast.showToast("名字不能为空!");
                    return;
                }
                if (name.contains("米果")) {
                    MGToast.showToast("用户已被占用!");
                    return;
                }
                if (StringTool.getLengthChinese(name) > 7) {
                    MGToast.showToast("昵称不能大于7个中文字符");
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
//        JpushHelper.unRegisterJpush();
        mLoginHelper.imLogout();
        App.getInstance().setImLoginSuccess(false);
        LocalUserModel userModel = new LocalUserModel();
        App.getInstance().setmLocalUser(userModel);
        AppConfig.setRefId("");
        App.getInstance().clearAllData();
        SharedPreferencesUtils.getInstance().clearUserNameAndUserPassword();
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

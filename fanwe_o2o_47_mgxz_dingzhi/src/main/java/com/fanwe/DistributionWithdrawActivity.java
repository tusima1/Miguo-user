package com.fanwe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.app.AppHelper;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Root;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getUserAccount.ModelUserAccount;
import com.fanwe.commission.model.getUserAccount.ResultUserAccount;
import com.fanwe.commission.presenter.MoneyHttpHelper;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.listener.NumLimitWatcher;
import com.fanwe.model.LocalUserModel;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.DisplayUtil;
import com.sunday.eventbus.SDBaseEvent;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 提现
 *
 * @author Administrator
 *
 */
public class DistributionWithdrawActivity extends BaseActivity implements CallbackView2,
        CallbackView {
    @ViewInject(R.id.ll_withdraw_type)
    private LinearLayout mLl_withdraw_type;

    @ViewInject(R.id.tv_withdraw_type)
    private TextView mTv_withdraw_type;

    @ViewInject(R.id.et_money)
    private EditText mEt_money;

    @ViewInject(R.id.ll_bank_info)
    private LinearLayout mLl_bank_info;

    @ViewInject(R.id.et_bank_name)
    private EditText mEt_bank_name;

    @ViewInject(R.id.et_bank_number)
    private EditText mEt_bank_number;

    @ViewInject(R.id.et_real_name)
    private EditText mEt_real_name;

    @ViewInject(R.id.et_code)
    private EditText mEt_code;

    @ViewInject(R.id.btn_send_code)
    private SDSendValidateButton mBtn_send_code;

    @ViewInject(R.id.btn_submit)
    private Button mBtn_submit;

    @ViewInject(R.id.tv_circle)
    private TextView mTv_circle;

    @ViewInject(R.id.iv_img)
    private ImageView iv_select;

    @ViewInject(R.id.ssv_scroll)
    private ScrollView rootView;

    @ViewInject(R.id.tv_card_num)
    private TextView tv_bank_card_info;

    /** 0表示提现至余额 1表示提至银行卡 */
    private int mWithdrawType = 1;
    private String mStrMoney;
    private String mStrBankName;//银行名
    private String mStrBankNumber;//银行卡号
    private String mStrBank_UserName;//开户名
    private String mStrCode;//验证码
    private String mStrMobile;//发送验证码的手机号码
    private Float money;//可提现的金额(佣金和余额两种)(一个来表示)
    private PopupWindow pop;
    private MoneyHttpHelper moneyHttpHelper;//用户获取用户信息
    private int money_type = 0;/*1:余额,2:佣金*/
    private CommonHelper commonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_distribution_withdraw);
        init();
    }

    private void init() {
        getIntentData();
        moneyHttpHelper = new MoneyHttpHelper(this);
        moneyHttpHelper.getUserAccount();
        commonHelper = new CommonHelper(null, this);
        registerClick();
        hideKeyboradOnTouchOutside(DistributionWithdrawActivity.this, rootView);
    }

    private void getIntentData() {
        String moneyFrom = getIntent().getStringExtra("money");
        if (TextUtils.isEmpty(moneyFrom)) {
            money = 0f;
        } else {
            money = Float.parseFloat(moneyFrom);
        }
        money_type = getIntent().getIntExtra("money_type", 0);
        //获取金额
        SDViewBinder.setTextView(mTv_circle, "可提现额（元）" + "\n" + money);
        initTitle();
    }

    private void binData(ModelUserAccount modelUserAccount) {
        mStrMobile = modelUserAccount.getPhone();
        //获取银行卡信息
        String bank_card = modelUserAccount.getBank_card();//银行卡
        String bank_name = modelUserAccount.getBank_name();//银行名
        String bank_type = modelUserAccount.getBank_type();//银行卡类型
        String level_id = modelUserAccount.getLevel_id();//用户等级
        String bank_user = modelUserAccount.getBank_user();//开户名(姓名)
        if (TextUtils.isEmpty(bank_card) || TextUtils.isEmpty(bank_name) || TextUtils.isEmpty
                (bank_user)) {
            tv_bank_card_info.setText("");
            mLl_bank_info.setVisibility(View.VISIBLE);
        } else {
            mStrBank_UserName = bank_user;
            mStrBankName = bank_name;
            mStrBankNumber = bank_card;
            int length = bank_card.length();
            String bank_card_start = bank_card.substring(0, 4);
            String bank_card_end = bank_card.substring(length - 4, length);
            StringBuilder sb = new StringBuilder(bank_card_start);
            for (int i = 0; i < length - 8; i++) {
                sb.append("*");
            }
            sb.append(bank_card_end);
            tv_bank_card_info.setText(sb.toString());
        }
    }

    private void initMobile() {
        LocalUserModel user = AppHelper.getLocalUser();
        if (user != null) {
            mStrMobile = user.getUser_mobile();
        }
    }

    private void initSDSendValidateButton() {
        mBtn_send_code.setmListener(new SDSendValidateButtonListener() {
            @Override
            public void onTick() {

            }

            @Override
            public void onClickSendValidateButton() {
                requestSendCode();
            }
        });
    }

    /**
     * 绑定手机号
     */
    private void showBindMobileDialog() {
        Intent intent = new Intent(this, BindMobileActivity.class);
        startActivity(intent);
    }

    /**
     * 请求验证码
     */
    protected void requestSendCode() {
        if (TextUtils.isEmpty(mStrMobile)) {
            showBindMobileDialog();
            return;
        }
        commonHelper.doGetCaptcha(mStrMobile, 3, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson = new Gson();
                Root root = gson.fromJson(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                if ("200".equals(statusCode)) {
                    mBtn_send_code.setmDisableTime(5 * 60);
                    mBtn_send_code.startTickWork();
                }
            }
        });
    }

    private void registerClick() {
        mLl_withdraw_type.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mEt_money.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        mEt_money.addTextChangedListener(new NumLimitWatcher() {

            @Override
            protected void afterTextChanged2(Editable s) {
                Float et_money = 0.0f;
                String number = s.toString();
                if (!number.equals("")) {
                    et_money = Float.parseFloat(number);
                    BigDecimal bd1 = new BigDecimal(money);
                    bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
                    Float total = Float.parseFloat(bd1 + "");
                    if (et_money > total || et_money == 0 || et_money < 0) {
                        mEt_money.setTextColor(Color.RED);
                        mBtn_send_code
                                .setBackgroundResource(R.drawable.my_tixian_sendcode);
                        mBtn_send_code.setmTextEnable("");
                        mBtn_send_code.setClickable(false);
                        mBtn_send_code.stopTickWork();
                    } else {
                        mEt_money.setTextColor(Color.parseColor("#4C4C4C"));
                        mBtn_send_code
                                .setBackgroundResource(R.drawable.selector_main_color_corner);
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setStroke(SDViewUtil.dp2px(1),
                                mBtn_send_code.getmTextColorEnable());
                        gradientDrawable.setCornerRadius(SDViewUtil.dp2px(5));
                        SDViewUtil.setBackgroundDrawable(mBtn_send_code,
                                gradientDrawable);
                        mBtn_send_code.setmTextEnable("获取验证码");
                        mBtn_send_code.setClickable(true);
                        mBtn_send_code.setmBackgroundEnableResId(0);
                        mBtn_send_code.updateViewState(true);
                        initSDSendValidateButton();
                    }
                }
            }
        });
        iv_select.setOnClickListener(this);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("提现");
        mTitle.initRightItem(1);
        if (money_type == 2) {
            mTitle.getItemRight(0).setTextBot("提现日志");
        }
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        clickWithdrawLog();
    }

    /**
     * 提现日志
     */
    private void clickWithdrawLog() {
        // TODO 跳到提现日志界面
        Intent intent = new Intent(this, DistributionWithdrawLogActivity.class);
        money_type = 2;
        intent.putExtra("money_type", money_type);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mLl_withdraw_type) {
            clickWithDrawType();
        } else if (v == mBtn_submit) {
            clickSubmit();
        } else if (v == iv_select) {
            clickCardSelect();
        }
    }

    /**
     * 选择:
     * 目前只有一个选项,不知道什么鬼
     */
    private void clickCardSelect() {
        if (mLl_bank_info.getVisibility() == View.VISIBLE) {
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_pop_bankcard, null);
        int width = mEt_money.getWidth();
        int offset = DisplayUtil.dp2px(this, 12);
        int ox = DisplayUtil.dp2px(this, 14);
        FrameLayout tv_bank = (FrameLayout) contentView.findViewById(R.id.tv_bank);
        tv_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLl_bank_info.setVisibility(View.VISIBLE);
                pop.dismiss();
            }
        });
        pop = new PopupWindow(contentView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setFocusable(true);
        pop.showAsDropDown(mLl_withdraw_type, width / 2 - ox, -offset);
    }

    private void clickSubmit() {
        // TODO 请求提现接口
        if (!validateParams()) {
            return;
        }
        if (money_type == 2) {
            //佣金提现
            moneyHttpHelper.getUserCommissionWithdraw(true, mStrMobile, mStrCode, "0", mStrMoney,
                    mStrBankName, mStrBankNumber, mStrBank_UserName);
        } else if (money_type == 1) {
            //余额提现
            moneyHttpHelper.getUserCommissionWithdraw(false, mStrMobile, mStrCode, "0",
                    mStrMoney, mStrBankName, mStrBankNumber, mStrBank_UserName);
        }
    }

    private boolean validateParams() {
        mStrMoney = mEt_money.getText().toString().trim();
        if (isEmpty(mStrMoney)) {
            SDToast.showToast("请输入提现金额");
            return false;
        }

        switch (mWithdrawType) {
            case 0:
                mStrBankName = null;
                mStrBankNumber = null;
                mStrBank_UserName = null;
                break;
            case 1:
                mStrBankName = mEt_bank_name.getText().toString().trim();
                if (isEmpty(mStrBankName)) {
                    SDToast.showToast("请输入开户行名称");
                    return false;
                }

                mStrBankNumber = mEt_bank_number.getText().toString().trim();
                if (isEmpty(mStrBankNumber)) {
                    SDToast.showToast("请输入银行卡号");
                    return false;
                }

                mStrBank_UserName = mEt_real_name.getText().toString().trim();
                if (isEmpty(mStrBank_UserName)) {
                    SDToast.showToast("请输入姓名");
                    return false;
                }
                break;

            default:
                break;
        }

        mStrCode = mEt_code.getText().toString().trim();
        if (isEmpty(mStrCode)) {
            SDToast.showToast("请输入验证码");
            return false;
        }
        return true;
    }

    /**
     * 点击提现类型
     */
    private void clickWithDrawType() {
        // TODO 弹出dialog选择提现类型
        SDDialogMenu dialog = new SDDialogMenu(this);
        String[] arrType = new String[]{"银行卡"};
        final SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(
                Arrays.asList(arrType), this);
        dialog.setmListener(new SDDialogMenuListener() {

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0:
                        mWithdrawType = 1;
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onDismiss(SDDialogMenu dialog) {

            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog) {
            }
        });
        dialog.setAdapter(adapter);
        dialog.showBottom();
    }

    @Override
    public void onDestroy() {
        mBtn_send_code.stopTickWork();
        super.onDestroy();
        commonHelper.onDestory();
        moneyHttpHelper.onDestroy();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case BIND_MOBILE_SUCCESS:
                initMobile();
                break;
            case CONFIRM_IMAGE_CODE:
                if (SDActivityManager.getInstance().isLastActivity(this)) {
                    requestSendCode();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (CommissionConstance.USER_ACCOUNT.equals(method)) {
            ResultUserAccount resultUserAccount = (ResultUserAccount) datas.get(0);
            List<ModelUserAccount> body = resultUserAccount.getBody();
            if (body != null && body.size() > 0) {
                ModelUserAccount modelUserAccount = body.get(0);
                binData(modelUserAccount);
            } else {
                MGToast.showToast("数据获取异常!");
            }
        }
        if (CommissionConstance.USER_WITHDRAW.equals(method) || CommissionConstance
                .USER_WITHDRAW_FX.equals(method)) {
            mEt_code.setText("");
            Intent intent = new Intent(DistributionWithdrawActivity.this,
                    DistributionWithdrawLogActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailue(String responseBody) {
        if (CommissionConstance.USER_WITHDRAW.equals(responseBody) || CommissionConstance
                .USER_WITHDRAW_FX.equals(responseBody)) {
            MGToast.showToast("提现失败!");
        }
    }

    @Override
    public void onFinish(String method) {

    }
}

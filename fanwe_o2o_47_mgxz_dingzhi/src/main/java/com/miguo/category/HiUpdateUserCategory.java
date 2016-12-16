package com.miguo.category;

import android.content.Intent;
import android.widget.TextView;

import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiUpdateUserActivity;
import com.miguo.definition.ClassPath;
import com.miguo.definition.ResultCode;
import com.miguo.definition.WechatPayStatus;
import com.miguo.factory.ClassNameFactory;
import com.miguo.listener.HiUpdateUserListener;
import com.miguo.presenters.UserUpgradePresenter;
import com.miguo.presenters.impl.UserUpgradePresenterImpl;
import com.miguo.utils.BaseUtils;
import com.miguo.view.UserUpgradePresenterView;

/**
 * Created by zlh on 2016/12/14.
 * 升级代言人
 */
public class HiUpdateUserCategory extends Category implements UserUpgradePresenterView{

    /**
     * 您的余额可以抵扣x元
     */
    @ViewInject(R.id.user_account)
    TextView userAccount;

    /**
     * 预扣0元，后续收益慢慢还
     */
    @ViewInject(R.id.update_by_withholding)
    TextView updateByWithholding;

    /**
     * 微信支付0元
     */
    @ViewInject(R.id.update_by_wechat)
    TextView updateByWechat;
    /**
     * 支付宝支付0元
     */
    @ViewInject(R.id.update_by_alipay)
    TextView updateByAlipay;


    /**
     * 用户升级presenter
     */
    UserUpgradePresenter userUpgradePresenter;

    /**
     * 访问网络时候的Dialog
     */
    MGProgressDialog dialog;

    public HiUpdateUserCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {
        userUpgradePresenter = new UserUpgradePresenterImpl(this);
    }

    @Override
    protected void initThisListener() {
        listener = new HiUpdateUserListener(this);
    }

    @Override
    protected void setThisListener() {
        updateByWithholding.setOnClickListener(listener);
        updateByWechat.setOnClickListener(listener);
        updateByAlipay.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        WechatPayStatus.reset();
    }

    @Override
    protected void initViews() {
        userAccount.setText(getUserAccount());
        updateByWithholding.setText(getUpdateByWithholding());
        updateByWechat.setText(getUpdateByWechat());
        updateByAlipay.setText(getUpdateByAlipay());
    }

    /**
     * 预扣费支付
     * 这里不会涉及到全额支付，全额支付是一键升级
     */
    public void clickUpdateByWithholding(){
        showDialog();
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByWithholding();
    }

    /**
     * 微信支付
     */
    public void clickUpdateByWechat(){
        showDialog();
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByWechat();
    }

    /**
     * 支付宝支付
     */
    public void clickByUpdateByAlipay(){
        showDialog();
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByAlipay();
    }

    @Override
    public void userUpgradeError(String message) {
        showToast(message);
        userUpgradeError();
    }

    public void userUpgradeError(){
        hideDialog();
    }

    @Override
    public void userUpgradeSuccess(String message) {
        hideDialog();
        showToast(message);
        userUpgradeSuccess();
    }

    public void userUpgradeSuccess(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.WITHDRAWAL_CONDITIONS_ACTIVITY));
        getActivity().setResult(ResultCode.RESUTN_OK, intent);
        BaseUtils.finishActivity(getActivity());
    }

    private void showDialog(){
        dialog = new MGProgressDialog(getActivity(),R.style.MGProgressDialog);
        dialog.show();
    }

    private void hideDialog(){
        if(dialog == null){
            return;
        }
        dialog.dismiss();
    }

    @Override
    public HiUpdateUserActivity getActivity() {
        return (HiUpdateUserActivity) super.getActivity();
    }

    private String getUserAccount(){
        return "您的余额可以抵扣" + ( getActivity().getUserAccount() < 0 ? "0" : DataFormat.toDoubleTwo(getActivity().getUserAccount()) ) + "元";
    }

    private String getUpdateByWithholding(){
        return "预扣" + ( DataFormat.toDoubleTwo(getActivity().getUserAccount() < 0 ? getActivity().getUpdateAccount() : getActivity().getUpdateAccount() - getActivity().getUserAccount()) ) + "元，后续收益慢慢还";
    }

    private String getUpdateByWechat(){
        return "微信支付 " + DataFormat.toDoubleTwo(getActivity().getUpdateAccount()) + "元";
    }

    private String getUpdateByAlipay(){
        return "支付宝支付 " + DataFormat.toDoubleTwo(getActivity().getUpdateAccount()) + "元";
    }

}

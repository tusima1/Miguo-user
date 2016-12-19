package com.miguo.category;

import android.content.Intent;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiUpdateUserWithEnoughMoneyActivity;
import com.miguo.definition.ClassPath;
import com.miguo.definition.ResultCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.listener.HiUpdateUserWithEnoughMoneyListener;
import com.miguo.presenters.UserUpgradePresenter;
import com.miguo.presenters.impl.UserUpgradePresenterImpl;
import com.miguo.utils.BaseUtils;
import com.miguo.view.UserUpgradePresenterView;

/**
 * Created by zlh on 2016/12/15.
 */
public class HiUpdateUserWithEnoughMoneyCategory extends Category implements UserUpgradePresenterView{

    @ViewInject(R.id.update_account)
    TextView updateAccount;

    @ViewInject(R.id.update)
    TextView update;

    /**
     * 用户升级
     */
    UserUpgradePresenter userUpgradePresenter;

    /**
     * 访问网络时候启用的Dialog
     */
    MGProgressDialog dialog;

    public HiUpdateUserWithEnoughMoneyCategory(HiBaseActivity activity) {
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
        listener = new HiUpdateUserWithEnoughMoneyListener(this);
    }

    @Override
    protected void setThisListener() {
        update.setOnClickListener(listener);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        updateAccount.setText(getUpdateAccount());
    }

    public void clickUpdate(){
        showDialog();
        /**
         * {@link UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByAccount();
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
    public void userUpgradeError(String message) {
        hideDialog();
        showToast(message);
    }

    @Override
    public void userUpgradeSuccess(String message) {
        hideDialog();
        showToast(message);
        userUpgradeSuccess();
        handleUpdateUserFixLevel();
    }

    private void handleUpdateUserFixLevel(){
        int userLevel = DataFormat.toInt(App.getInstance().getCurrentUser().getFx_level());
        App.getInstance().getCurrentUser().setFx_level((userLevel + 1)  + "");
    }

    public void userUpgradeSuccess(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.WITHDRAWAL_CONDITIONS_ACTIVITY));
        getActivity().setResult(ResultCode.RESUTN_OK, intent);
        BaseUtils.finishActivity(getActivity());
    }

    private String getUpdateAccount(){
        return "恭喜，您的余额足够支付" + getActivity().getUpdateAccount() + "元年费";
    }

    @Override
    public HiUpdateUserWithEnoughMoneyActivity getActivity() {
        return (HiUpdateUserWithEnoughMoneyActivity) super.getActivity();
    }
}

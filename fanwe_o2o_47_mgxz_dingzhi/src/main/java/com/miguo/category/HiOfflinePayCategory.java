package com.miguo.category;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiOfflinePayActivity;
import com.miguo.dao.BeforeOnlinePayDao;
import com.miguo.dao.impl.BeforeOnlinePayDaoImpl;
import com.miguo.entity.BeforeOnlinePayBean;
import com.miguo.entity.HiShopDetailBean;
import com.miguo.listener.HiOfflinePayListener;
import com.miguo.ui.view.RecyclerBounceNestedScrollView;
import com.miguo.view.BeforeOnlinePayView;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/14.
 */
public class HiOfflinePayCategory extends Category {

    @ViewInject(R.id.recycler_scrollview)
    RecyclerBounceNestedScrollView recyclerBounceNestedScrollView;

    /**
     * 消费金额
     */
    @ViewInject(R.id.amount_of_consumption)
    EditText amountOfConsumption;

    /**
     * 输入不参与优惠金额的复选框
     */
    @ViewInject(R.id.do_not_participate_in_the_amount_of_concessions)
    CheckBox withoutDiscountAmount;

    /**
     * 输入不参与优惠金额提示
     */
    @ViewInject(R.id.do_not_participate_in_the_amount_of_concessions_text)
    TextView withoutDiscountAmountText;

    /**
     * 不参与优惠金额
     */
    @ViewInject(R.id.do_not_participate_in_the_amount_of_consumption)
    EditText doNotParticipateInTheamountOfConsumption;

    /**
     * 满减文本
     */
    @ViewInject(R.id.decrease)
    TextView decrease;
    /**
     * 优惠时间段
     */
    @ViewInject(R.id.pay_time)
    TextView payTime;

    /**
     * 实付金额
     */
    @ViewInject(R.id.order_amount)
    TextView orderAmount;

    @ViewInject(R.id.commit_order)
    TextView commitOrder;
    /**
     * 记录上一次确认订单图标的背景，防止重复设置
     */
    int preConfirmOrderBackground = R.drawable.offline_pay_confirm_unselect;

    /**
     * 不参与优惠金额
     */
    @ViewInject(R.id.do_not_participate_in_the_amount_layout)
    RelativeLayout doNotParticipateInTheAmountLayout;

    @ViewInject(R.id.group2_layout)
    LinearLayout gruop2;


    BeforeOnlinePayDao beforeOnlinePayDao;
    BeforeOnlinePayBean.Result.Body offlinePayInfo;

    public HiOfflinePayCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void initThisListener() {
        listener = new HiOfflinePayListener(this);
    }

    @Override
    protected void setThisListener() {
        amountOfConsumption.addTextChangedListener(listener);
        doNotParticipateInTheamountOfConsumption.addTextChangedListener(listener);
        withoutDiscountAmount.setOnCheckedChangeListener(listener);
        withoutDiscountAmountText.setOnClickListener(listener);
        commitOrder.setOnClickListener(listener);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        initBeforeOnlinePayDao();
        initAmountOfConsumptionHint();
        recyclerBounceNestedScrollView.hideLoadingLayout();
    }

    private void initBeforeOnlinePayDao(){
        beforeOnlinePayDao = new BeforeOnlinePayDaoImpl(new BeforeOnlinePayView() {
            @Override
            public void getOfflinePayInfoSuccess(BeforeOnlinePayBean.Result.Body offlinePayInfo) {
                HiOfflinePayCategory.this.offlinePayInfo = offlinePayInfo;
                handleGetOfflineInfoSuccess();
            }

            @Override
            public void getOfflinePayInfoError(String message) {
                showToast(message);
            }
        });
        beforeOnlinePayDao.getOfflinePayInfo(getActivity().getShopId());
    }

    private void handleGetOfflineInfoSuccess(){
        handleCanPayFromOffline();
        handleOfflinePayType();
    }

    /**
     * 是否支持线下买单
     */
    private void handleCanPayFromOffline(){
//        decrease.setVisibility(offlinePayInfo.canPayFromOffline() ? View.VISIBLE : View.GONE);
//        payTime.setVisibility(offlinePayInfo.canPayFromOffline() ? View.VISIBLE : View.GONE);
    }

    /**
     * 处理不同买单类型的UI变动
     */
    private void handleOfflinePayType(){
        switch (offlinePayInfo.getOnline_pay_type()){
            case HiShopDetailBean.Result.Offline.ORIGINAL:
                updatePayTimeVisibility(View.GONE);
                updateDecreaseVisibility(View.GONE);
                break;
            case HiShopDetailBean.Result.Offline.DISCOUNT:
                updateDecrease(offlinePayInfo.getDiscountText());
                updatePayTimeVisibility(View.VISIBLE);
                updateDecreaseVisibility(View.VISIBLE);
                updatePayTime();
                break;
            case HiShopDetailBean.Result.Offline.DECREASE:
                updateDecrease(offlinePayInfo.getDecreaseText());
                updatePayTimeVisibility(View.VISIBLE);
                updateDecreaseVisibility(View.VISIBLE);
                updatePayTime();
                break;
        }
    }

    /**
     * 买单优惠时间
     */
    private void updatePayTime(){
        this.payTime.setText(offlinePayInfo.getAvailableWeek() + "  " + offlinePayInfo.getAvailableTime());
    }

    /**
     * 更新满减文字
     * @param decrease 内容
     */
    private void updateDecrease(String decrease){
        this.decrease.setText(decrease);
    }

    /**
     * 更新时间文本是否可见（原价不可见）
     * @param visibility 是否可见码
     */
    private void updatePayTimeVisibility(int visibility){
        this.payTime.setVisibility(visibility);
    }

    private void updateDecreaseVisibility(int visibility){
        this.decrease.setVisibility(visibility);
    }

    /**
     * 设置输入金额的Hint字体大小
     */
    private void initAmountOfConsumptionHint(){
        SpannableString spannableString = new SpannableString("询问服务员后输入");
        /**
         * 设置字体大小，true表示单位是sp
         */
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(12, true);
        spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amountOfConsumption.setHint(new SpannableString(spannableString));
        doNotParticipateInTheamountOfConsumption.setHint(new SpannableString(spannableString));
    }

    public void clickWithoutDiscountAmount(){
        withoutDiscountAmount.setChecked(!withoutDiscountAmount.isChecked());
    }

    /**
     * 编辑框内容监听
     * 不管哪个编辑框内容变动了，都要重新计算并且检查状态
     */
    public void onTextChanged(){
        handleChangeCommitOrderBackground();
        handleCalculateAmount();
    }

    public void onCheckedChanged(boolean isChecked){
        if(isChecked){
            showDoNotParticipateAmountTextView();
            return;
        }
        hideDoNotParticipateAmountTextView();
    }

    private void showDoNotParticipateAmountTextView(){
        doNotParticipateInTheAmountLayout.setVisibility(View.VISIBLE);
        final TranslateAnimation animation1 = new TranslateAnimation(getScreenWidth(), 0, 0 , 0);
        animation1.setDuration(300);
        animation1.setStartOffset(300);
        doNotParticipateInTheAmountLayout.startAnimation(animation1);

        TranslateAnimation animation = new TranslateAnimation(0, 0, -dip2px(50), 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gruop2.startAnimation(animation);
    }

    private void hideDoNotParticipateAmountTextView(){
        final TranslateAnimation animation3 = new TranslateAnimation(0, 0, dip2px(50), 0);
        animation3.setDuration(300);

        TranslateAnimation animation1 = new TranslateAnimation(0, -getScreenWidth(), 0 , 0);
        animation1.setDuration(300);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                doNotParticipateInTheAmountLayout.setVisibility(View.GONE);
                gruop2.startAnimation(animation3);
                doNotParticipateInTheamountOfConsumption.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        doNotParticipateInTheAmountLayout.startAnimation(animation1);
    }

    private void handleCalculateAmount(){
        if(inputAmountNotEmpty()){

        }
    }

    /**
     * 如果编辑框输入的金额不为空就要设置确认订单的背景
     */
    private void handleChangeCommitOrderBackground(){
        if(!inputAmountNotEmpty()){
            commitOrder.setBackground(getDrawable(R.drawable.offline_pay_confirm_unselect));
            this.preConfirmOrderBackground = R.drawable.offline_pay_confirm_unselect;
            return;
        }
        if(isCommitOrderSelect()){
            return;
        }
        commitOrder.setBackground(getDrawable(R.drawable.offline_pay_confirm_select));
        this.preConfirmOrderBackground = R.drawable.offline_pay_confirm_select;
    }

    /**
     * 已经是黄色背景确认订单
     * @return
     */
    private boolean isCommitOrderSelect(){
        return preConfirmOrderBackground == R.drawable.offline_pay_confirm_select;
    }

    /**
     * 输入了金额
     */
    private boolean inputAmountNotEmpty(){
        return !isEmpty(amountOfConsumption.getText().toString()) || !isEmpty(doNotParticipateInTheamountOfConsumption.getText().toString());
    }

    @Override
    public HiOfflinePayActivity getActivity() {
        return (HiOfflinePayActivity)super.getActivity();
    }
}

package com.miguo.live.views;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.fanwe.BaseActivity;
import com.fanwe.MainActivity;
import com.fanwe.constant.Constant;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.fanwe.reward.RewardConstants;
import com.fanwe.reward.adapters.DiamondGridAdapter;
import com.fanwe.reward.model.DiamondTypeEntity;
import com.fanwe.reward.model.DiamondUserOwnEntity;
import com.fanwe.reward.presenters.DiamondHelper;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.shoppingcart.presents.CommonShoppingHelper;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * 充值买钻页。
 * Created by Administrator on 2016/9/11.
 */
public class RechargeDiamondActivity extends BaseActivity implements RefreshCalbackView {

    @ViewInject(R.id.gridview)
    private GridView gridView;

    @ViewInject(R.id.diamond_line)
    private LinearLayout diamond_line;
    /**
     * 大笔对应的钻
     */
    @ViewInject(R.id.diamond_value)
    private TextView diamond_value;


    /**
     * 购买钻对应的金额。
     */
    @ViewInject(R.id.money_value)
    private TextView money_value;

    @ViewInject(R.id.self_diamond)
    private TextView self_diamond;
    /**
     * 支付按钮。
     */
    @ViewInject(R.id.pay_btn)
    private Button pay_btn;
    //支付方式详情
    protected OrderDetailPaymentsFragment mFragPayments;

    private CommonShoppingHelper commonShoppingHelper;
    private DiamondHelper diamondHelper;

    private DiamondGridAdapter diamondGridAdapter;


    private DiamondTypeEntity currentDiamondType;
    private DiamondTypeEntity  bigDiamondType;
    private PaymentTypeInfo currentPayType;
    private List<DiamondTypeEntity> diamondTypeEntityList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.recharge_diamond_activity);
        init();
    }

    private void init() {
        initTitle();

        diamondGridAdapter = new DiamondGridAdapter(this);

        gridView.setAdapter(diamondGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(diamondTypeEntityList==null||diamondTypeEntityList.size()<1){
                    return;
                }
                for(int i = 0 ; i < diamondTypeEntityList.size() ;i++){
                    DiamondTypeEntity entity0 = diamondTypeEntityList.get(i);
                    if(i==position){
                        currentDiamondType = entity0;
                        entity0.setChecked(true);
                    }else{
                        entity0.setChecked(false);
                    }
                }
                diamondGridAdapter.setDatas(diamondTypeEntityList);
                diamondGridAdapter.notifyDataSetChanged();
                setColor(false);
            }
        });

        diamond_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDiamondType = bigDiamondType;
                setColor(true);
            }
        });
        initFragment();
        getBaseData();
    }


    public void setColor(boolean checked){
        if(checked) {
            diamond_value.setTextColor(getResources().getColor(R.color.white));
            money_value.setTextColor(getResources().getColor(R.color.white));
            diamond_line.setBackground(getResources().getDrawable(R.drawable.bg_orange_small));
        }else{
            diamond_value.setTextColor(getResources().getColor(R.color.main_color));
            money_value.setTextColor(getResources().getColor(R.color.main_color_press));
            diamond_line.setBackground(getResources().getDrawable(R.drawable.bg_orange_smallline));
        }
    }
    private void initFragment() {

        // 支付方式列表
        mFragPayments = new OrderDetailPaymentsFragment();

        mFragPayments.setmListener(new OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener() {

            @Override
            public void onPaymentChange(PaymentTypeInfo model) {
                currentPayType = model;
            }
        });


        getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments, mFragPayments);
        commonShoppingHelper = new CommonShoppingHelper(this);
        diamondHelper = new DiamondHelper(this);
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidate()) {
                    String content = "您确认以 ￥" + currentDiamondType.getPrice() + " 的价格购买 " + currentDiamondType.getDiamond() + " 米果钻吗?";
                    showTitleDialog(content);
                }
            }
        });
    }

    private void showTitleDialog(final String context) {
        new SDDialogConfirm()
                .setTextContent(
                        context)
                .setmListener(new SDDialogCustom.SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        // 支付操作。
                        String payment_id = currentPayType.getId();
                        String diamond_id = currentDiamondType.getId();
                        diamondHelper.createDiamondOrder(payment_id,diamond_id);
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }


    /**
     * 确认是否允许进入下一步。
     *
     * @return boolean
     */
    public boolean checkValidate() {
        if (currentDiamondType == null) {
            SDToast.showToast("请选择您想购买的米钻。");
            return false;
        }
        if (currentPayType == null) {
            SDToast.showToast("请选择一种支付方式。");
            return false;
        }
        return true;
    }

    /*
   取支付方式 。
    */
    public void getBaseData() {
        diamondHelper.GetDiamondList();
        diamondHelper.getUserDiamond();
        commonShoppingHelper.getPayment();
    }

    /**
     * 绑定 支付方式
     *
     * @param datas
     */
    private void bindPayment(List<PaymentTypeInfo> datas) {
        if (datas != null && datas.size() > 0) {
            mFragPayments.setListPayment(datas);
        }
    }

    /**
     * 绑定 当前用户已经有的果钻数。
     * @param diamondUserOwnEntityList
     */
    private void bindUserDiamond(List<DiamondUserOwnEntity> diamondUserOwnEntityList){
        if(diamondUserOwnEntityList!=null){
            DiamondUserOwnEntity entityOwn = diamondUserOwnEntityList.get(0);
            float value = SDFormatUtil.stringToFloat(entityOwn.getDiamond_android())+SDFormatUtil.stringToFloat(entityOwn.getCommon_diamond());

            self_diamond.setText(value+"");

        }
    }

    /**
     * 设置支付结果。
     */
    private void bindPayDiamondResult(){
        Intent intent = new Intent(RechargeDiamondActivity.this,PayHistoryActivity.class);
        startActivity(intent);
        finish();

    }

    private void bindDiamondType(List<DiamondTypeEntity> diamondTypeEntityList) {

        if (diamondTypeEntityList != null) {
            int size = diamondTypeEntityList.size();
            if(size <7) {
                diamondGridAdapter.setDatas(diamondTypeEntityList);
            }else{
                bigDiamondType = diamondTypeEntityList.get(size-1);
                money_value.setText( "￥ "+bigDiamondType.getPrice() +"元");
                diamond_value.setText(bigDiamondType.getDiamond() +"钻石");
                diamondTypeEntityList.remove(size-1);
                diamondGridAdapter.setDatas(diamondTypeEntityList);
            }
            this.diamondTypeEntityList = diamondTypeEntityList;
            diamondGridAdapter.notifyDataSetChanged();
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("充值页面");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextTop("充值记录");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        //进入充值记录页。
    }

    @Override
    public void onFailue(String method, String responseBody) {
        SDToast.showToast(responseBody);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case ShoppingCartconstants.GET_PAYMENT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayment(datas);
                    }
                });

                break;
            case RewardConstants.BUY_DIAMOND:
              MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bindDiamondType(datas);
                }
            });
            break;

            case RewardConstants.USER_DIAMOND:
                 MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bindUserDiamond(datas);
                }
            });
                break;
            case RewardConstants.DIAMOND_ORDER:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayDiamondResult();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentDiamondType = null;
        currentPayType = null;
    }
}

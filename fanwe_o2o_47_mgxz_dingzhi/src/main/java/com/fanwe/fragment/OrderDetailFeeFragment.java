package com.fanwe.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 订单详情页(费用详情fragment)
 *
 * @author js02
 */
public class OrderDetailFeeFragment extends OrderDetailBaseFragment {

    /**
     * 商品总价。
     */
    @ViewInject(R.id.total_fee_value)
    TextView total_fee;
    /**
     * 总计。
     */
    @ViewInject(R.id.total)
    TextView total;
    /**
     * 支付方式
     */
    @ViewInject(R.id.pay_type)
    TextView pay_type;
    /**
     * 余额支付
     */
    @ViewInject(R.id.yue_fee)
    TextView yue_fee;
    /**
     * 应付金额
     */
    @ViewInject(R.id.need_pay_fee)
    TextView need_pay_fee;

    @ViewInject(R.id.need_pay_line)
    LinearLayout need_pay_line;
    /**
     * 支付方式行、
     */
    @ViewInject(R.id.pay_type_line)
    LinearLayout pay_type_line;
    /**
     * 余额支付行。
     */
    @ViewInject(R.id.yue_line)
    LinearLayout yue_line;
    /**
     * 优惠行。
     */
    @ViewInject(R.id.youhui_line)
    LinearLayout youhui_line;
    /**
     * 优惠金额。
     */
    @ViewInject(R.id.youhui_account)
    TextView youhui_account;

    /**
     * 首单 优惠。
     */
    @ViewInject(R.id.firstPriceTotalline)
      LinearLayout  firstPriceTotalline;
    @ViewInject(R.id.firstPriceTotal)
    TextView  firstPriceTotalText;


    /**
     * 当前的第三方支付方式。
     */
    PaymentTypeInfo currentFeeInfoModel;

    boolean ifYueChecked = false;

    //总金额。
    float totalFloat = 0.00f;
    //用户余额。
    float yueFloat = 0.00f;
    /**
     * 优惠金额。
     */
    float youhuiFloat = 0.00f;
    /**
     * 需要支付金额。
     */
    float needFloat = 0.00f;
    /**
     * 首单 支付。
     */
    float  firstPriceTotal = 0.00f;
    /**
     * 订单编号 。
     */
    String  orderId ="";

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_order_detail_fee);
    }

    @Override
    protected void init() {
        super.init();
        bindData();
    }


    private void bindData() {
        if (!toggleFragmentView(mCheckActModel)) {
            return;
        }
        //总金额。
        totalFloat = SDFormatUtil.stringToFloat(mCheckActModel.getPayPrice());
        //用户余额。
        yueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney());

        firstPriceTotal = SDFormatUtil.stringToFloat(mCheckActModel.getFirstPriceTotal());

        youhuiFloat = SDFormatUtil.stringToFloat(mCheckActModel.getYouhuiPrice());
        needFloat = totalFloat - yueFloat - youhuiFloat;
        total_fee.setText(mCheckActModel.getGoodsTotal());
        total.setText(mCheckActModel.getTotal());

        if(TextUtils.isEmpty(mCheckActModel.getYouhuiPrice())||"0".equals(mCheckActModel.getYouhuiPrice())){
            youhui_account.setText(0+"");
        }else {
            youhui_account.setText(mCheckActModel.getYouhuiPrice());
        }
        yue_fee.setText(mCheckActModel.getUserAccountMoney());
        if(need_pay_line!=null) {
            need_pay_line.setVisibility(View.VISIBLE);
        }
        if(need_pay_fee!=null) {
            need_pay_fee.setText(needFloat + "");
        }
        if(firstPriceTotal>0){
            firstPriceTotalline.setVisibility(View.VISIBLE);
            firstPriceTotalText.setText(firstPriceTotal+"");
        }else{
            firstPriceTotalline.setVisibility(View.GONE);
        }
        calculateFee();

    }

    public void showYueLine(){
        boolean result = false;

        if(ifYueChecked){
            result = true;
        }else{
            if(!TextUtils.isEmpty(orderId)){
                float oldYueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getAccountmoney());
                if(oldYueFloat >0){
                    result =  true;
                }else{
                    result = false;
                }
            }
        }
        if(result){
            yue_line.setVisibility(View.VISIBLE);
        }else{
            yue_line.setVisibility(View.GONE);

        }
    }

    /**
     * "firstPriceTotal": "0.00",  首单优惠金额
     "accountmoney": "30.99",  上次使用的余额金额
     "userAccountMoney": "0", 用户当前账户余额
     "goodsTotal": "48.00",  总金额
     "payPrice": "4.71",  应付金额
     "Total": "48.00",   总金额
     "youhuiPrice": "12.30",  优惠金额
     */
    private void calculateFee() {
        //总金额。
        float totalFloat = SDFormatUtil.stringToFloat(mCheckActModel.getPayPrice());
        //用户余额。
        float yueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney())+SDFormatUtil.stringToFloat(mCheckActModel.getAccountmoney());
        //首单优惠 TODO 该怎么显示呢

        float youhuiFloat = SDFormatUtil.stringToFloat(mCheckActModel.getYouhuiPrice());
        float needFloat = totalFloat - yueFloat - youhuiFloat;
        float needFloat2= totalFloat-youhuiFloat;
        //余额 》= 总金额。
        if (yueFloat >= totalFloat) {
            if (ifYueChecked) {
                //使用余额支付
                yue_fee.setText(needFloat2 + "");
                yue_line.setVisibility(View.VISIBLE);
                pay_type_line.setVisibility(View.GONE);
                need_pay_line.setVisibility(View.GONE);
            } else {
                //不使用余额支付
                need_pay_fee.setText(needFloat2 + "");
                yue_line.setVisibility(View.GONE);
                if (currentFeeInfoModel != null) {
                    pay_type.setText(currentFeeInfoModel.getName());
                    pay_type_line.setVisibility(View.VISIBLE);
                    need_pay_line.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (ifYueChecked) {
                //使用余额支付
                yue_fee.setText(yueFloat + "");
                yue_line.setVisibility(View.VISIBLE);

                need_pay_fee.setText(needFloat + "");
                if (currentFeeInfoModel != null) {
                    pay_type.setText(currentFeeInfoModel.getName());

                    pay_type_line.setVisibility(View.VISIBLE);
                } else {
                    pay_type_line.setVisibility(View.GONE);
                }
                need_pay_line.setVisibility(View.VISIBLE);
            } else {
                //不使用余额支付
                need_pay_fee.setText(needFloat2 + "");
                if (currentFeeInfoModel != null) {
                    yue_line.setVisibility(View.GONE);
                    pay_type.setText(currentFeeInfoModel.getName());
                    pay_type_line.setVisibility(View.VISIBLE);
                } else {
                    yue_line.setVisibility(View.GONE);
                    pay_type_line.setVisibility(View.GONE);
                }
                need_pay_line.setVisibility(View.VISIBLE);
            }
        }

        if(!TextUtils.isEmpty(orderId)){
            //应付金额
            float payPrice = SDFormatUtil.stringToFloat(mCheckActModel.getPayPrice());
            //上次用余额支付的金额
            float accountMoney = SDFormatUtil.stringToFloat(mCheckActModel.getAccountmoney());
            //当前账户余额。
            float userAccountMoney = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney());

            //上次余额支付的金额 大于零 或者  本次选择了余额支付 显示  余额支付。
            if((accountMoney>0) || ifYueChecked){
                yue_line.setVisibility(View.VISIBLE);
            }else{
                yue_line.setVisibility(View.GONE);
            }
           //显示 金额 问题
        //用户本次选择了余额支付 。
         if(ifYueChecked){
             if(payPrice>userAccountMoney){
                 yueFloat = userAccountMoney + accountMoney;
                 needFloat =payPrice-userAccountMoney;
             }else{
                 yueFloat = payPrice+accountMoney;
                 needFloat =0.00f;
             }
         }else{
             if(accountMoney>0){
                 yueFloat = accountMoney;
             }
             needFloat= payPrice;
         }
            yue_fee.setText(SDFormatUtil.formatNumberString(String.valueOf(yueFloat),2));
            need_pay_fee.setText(SDFormatUtil.formatNumberString(String.valueOf(needFloat),2));
            need_pay_line.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onRefreshData() {
        bindData();
        super.onRefreshData();
    }



    public void setCurrentFeeInfoModel(PaymentTypeInfo currentFeeInfoModel) {
        this.currentFeeInfoModel = currentFeeInfoModel;
    }

    public boolean isIfYueChecked() {
        return ifYueChecked;
    }

    public void setIfYueChecked(boolean ifYueChecked) {
        this.ifYueChecked = ifYueChecked;

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
package com.fanwe.commission.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Root;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getUserAccount.ResultUserAccount;
import com.fanwe.commission.model.getUserAccount.RootUserAccount;
import com.fanwe.commission.model.getUserBankCardList.ModelUserBankCard;
import com.fanwe.commission.model.getUserBankCardList.ResultUserBankCard;
import com.fanwe.commission.model.getUserBankCardList.RootUserBankCard;
import com.fanwe.commission.model.getWithdrawLog.ResultWithdrawLog;
import com.fanwe.commission.model.getWithdrawLog.RootWithdrawLog;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/8/28.
 * 提现(关于钱的部分)
 */
public class MoneyHttpHelper implements IHelper{

    private Gson gson;
    private CallbackView2 mView2;

    public MoneyHttpHelper(CallbackView2 mView2) {
        this.mView2=mView2;
        this.gson=new Gson();
    }


    /**
     * 资金日志
     * @param page 默认1
     * @param page_size 默认10
     */
    public void getGetBalance(String page,String page_size,String select_type){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.COMMISSION_LOG);
        params.put("page", page);
        params.put("page_size", page_size);
        params.put("select_type", select_type);
        //TODO 资金日志
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("test",responseBody);
            }
        });

    }

    /**
     * 用户信息
     * 包括 手机号
     * 银行卡信息 等等
     */
    public void getUserAccount(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.USER_ACCOUNT);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                final List<ResultUserAccount> result = gson.fromJson(responseBody, RootUserAccount
                        .class).getResult();
                if (result!=null && result.size()>0){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(CommissionConstance.USER_ACCOUNT,result);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(CommissionConstance.USER_ACCOUNT);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(CommissionConstance.USER_ACCOUNT);
                    }
                });
            }
        });
    }

    /**
     * 获取银行卡列表
     */
    public void getUserBankCardList(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.USER_BANK_CARD_LIST);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                List<ResultUserBankCard> result = gson.fromJson(responseBody, RootUserBankCard
                        .class).getResult();
                if (result!=null && result.size()>0){
                    ResultUserBankCard resultUserBankCard = result.get(0);
                    if (resultUserBankCard!=null){
                        final List<ModelUserBankCard> body = resultUserBankCard.getBody();
                        if (body!=null && body.size()>0){
                            MGUIUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView2.onSuccess(CommissionConstance.USER_BANK_CARD_LIST,body);
                                }
                            });
                            return;
                        }else {
                            MGUIUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView2.onSuccess(CommissionConstance.USER_BANK_CARD_LIST,null);
                                }
                            });
                            return;
                        }
                    }
                }
                mView2.onFailue(CommissionConstance.USER_BANK_CARD_LIST);
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(CommissionConstance.USER_BANK_CARD_LIST);
                    }
                });
            }
        });
    }

    /**
     * 提现日志 Error 这是商家版
     * @param money_type 1:余额提现，2:佣金提现
     */
    public void getUserWithdrawLog(String money_type){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.USER_WITHDRAW_LOG);
        params.put("wd_type", money_type);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                final List<ResultWithdrawLog> result = gson.fromJson(responseBody, RootWithdrawLog
                        .class).getResult();
                if (result!=null && result.size()>0){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(CommissionConstance.USER_WITHDRAW_LOG,result);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(CommissionConstance.USER_WITHDRAW_LOG);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(CommissionConstance.USER_WITHDRAW_LOG);
                    }
                });
            }
        });
    }

    /**
     * 佣金提现
     * @param isFx 必选 余额提现为 false
     *                  佣金提现为 true
     * @param mobile 必须     手机号
     * @param captcha 必须			验证码
     * @param wd_to_where 必须		0表示 提现至银行卡，1 表示 到余额	提现到哪里
     * @param dw_money 必须			提现的金额
     * @param bank_name 可选		提现到银行卡时必传该参数	开户行名称
     * @param bank_card 可选		提现到银行卡时必传该参数	银行卡号
     * @param bank_user 可选		提现到银行卡时必传该参数	银行卡户主姓名
     */
    public void getUserCommissionWithdraw(final boolean isFx, String mobile, String captcha, String wd_to_where, String dw_money, String bank_name, String bank_card, String bank_user){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", isFx?CommissionConstance.USER_WITHDRAW_FX:CommissionConstance.USER_WITHDRAW);
        params.put("mobile",mobile);
        params.put("captcha",captcha);
        params.put("wd_to_where",wd_to_where);
        if (!TextUtils.isEmpty(bank_name)){
            params.put("bank_name",bank_name);
        }
        params.put("dw_money",dw_money);
        if (!TextUtils.isEmpty(bank_card)){
            params.put("bank_card",bank_card);
        }
        if (!TextUtils.isEmpty(bank_user)){
            params.put("bank_user",bank_user);
        }
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = gson.fromJson(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                if ("200".equals(statusCode)){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(isFx?CommissionConstance.USER_WITHDRAW_FX:CommissionConstance.USER_WITHDRAW,null);
                        }
                    });
                    return;
                }
                if ("301".equals(statusCode)){
                    MGToast.showToast("验证码错误");
                    doOnFail(isFx);
                }else if ("303".equals(statusCode)){
                    MGToast.showToast("验证码次数过多");
                    doOnFail(isFx);
                }else if ("305".equals(statusCode)){
                    MGToast.showToast("验证码过期");
                    doOnFail(isFx);
                }else if ("302".equals(statusCode)){
                    MGToast.showToast("验证码过期");
                    doOnFail(isFx);
                }else {
                    MGToast.showToast("系统异常");
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(isFx?CommissionConstance.USER_WITHDRAW_FX:CommissionConstance.USER_WITHDRAW);
                    }
                });
            }
        });


    }

    private void doOnFail(final boolean isFx){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView2.onFailue(isFx?CommissionConstance.USER_WITHDRAW_FX:CommissionConstance.USER_WITHDRAW);
            }
        });
    }

    @Override
    public void onDestroy() {
        mView2=null;
        gson=null;
    }
}

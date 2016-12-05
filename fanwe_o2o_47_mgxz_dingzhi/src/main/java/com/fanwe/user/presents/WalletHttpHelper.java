package com.fanwe.user.presents;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.OldCallbackHelper;
import com.fanwe.base.Result;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.wallet.ExchangeDiamondHistoryModel;
import com.fanwe.user.model.wallet.ExchangeListModel;
import com.fanwe.user.model.wallet.InviteModel;
import com.fanwe.user.model.wallet.RefundModel;
import com.fanwe.user.model.wallet.WalletBalance;
import com.fanwe.user.model.wallet.WalletResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.PageModel;
import com.miguo.live.views.customviews.MGToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 用户钱包相关的接口。
 * Created by Administrator on 2016/11/16.
 */

public class WalletHttpHelper extends OldCallbackHelper implements IHelper {

    private static final String TAG = UserHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;


    public WalletHttpHelper(CallbackView mView) {

        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    /**
     * 2.0.1新的钱包接口
     */
    public void getWalletNew() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.WALLET);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<WalletResult>>() {
                }.getType();
                Gson gson = new Gson();
                Root<WalletResult> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                WalletResult walletResult = (WalletResult) validateBody(root);
                if ("200".equals(status)) {
                    if (walletResult != null) {
                        List<WalletResult> data = new ArrayList<WalletResult>();
                        data.add(walletResult);
                        onSuccess(mView, UserConstants.WALLET, data);
                    } else {
                        onFailure2(mView, message);
                    }
                } else {
                    onFailure2(mView, message);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.WALLET);
            }


        });

    }


    /**
     * 取余额、佣金接口
     */
    public void getWalletBalance() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.WALLET_BALANCE);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<WalletBalance>>() {
                }.getType();
                Gson gson = new Gson();
                Root<WalletBalance> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                WalletBalance walletBalance = (WalletBalance) validateBody(root);
                if ("200".equals(status)) {
                    if (walletBalance != null) {
                        List<WalletBalance> data = new ArrayList<WalletBalance>();
                        data.add(walletBalance);
                        onSuccess(mView, UserConstants.GET_WALLET_BALANCE, data);
                    } else {
                        onFailure2(mView, message);
                    }
                } else {
                    onFailure2(mView, message);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.GET_WALLET_BALANCE);
            }


        });

    }

    /**
     * 从钱包到我的收益页面
     */
    public void getWalletIncomeTotal() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.WALLET_INCOME);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                HashMap<String, String> hashMap = (HashMap<String, String>) validateBody(root);
                if ("200".equals(status)) {
                    if (hashMap != null) {
                        List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                        data.add(hashMap);
                        onSuccess(mView, UserConstants.WALLET_INCOME_GET, data);
                    } else {
                        onFailure2(mView, message);
                    }
                } else {
                    onFailure2(mView, message);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.WALLET_INCOME_GET);
            }


        });
    }

    /**
     * 从钱包到我的收益页面
     */
    public void getDiamondExchangeList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.WALLET_INCOME);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<ExchangeListModel>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ExchangeListModel> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                ExchangeListModel model = (ExchangeListModel) validateBody(root);
                if ("200".equals(status)) {
                    if (model != null) {
                        List<ExchangeListModel> data = new ArrayList<ExchangeListModel>();
                        data.add(model);
                        onSuccess(mView, UserConstants.WALLET_INCOME_POST, data);
                    } else {
                        onFailure2(mView, message);
                    }
                } else {
                    onFailure2(mView, message);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.WALLET_INCOME_POST);
            }


        });
    }

    /**
     * 兑换米果钻。
     *
     * @param regex_id 兑换的ID。
     */

    public void doExchangeDiamond(String regex_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("regex_id", regex_id);
        params.put("method", UserConstants.WALLET_INCOME);
        OkHttpUtils.getInstance().put(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                HashMap<String, String> model = (HashMap<String, String>) validateBody(root);
                if ("200".equals(status)) {
                    if (model != null) {
                        List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                        data.add(model);
                        onSuccess(mView, UserConstants.WALLET_INCOME_PUT, data);
                    } else {
                        onFailure2(mView, UserConstants.WALLET_INCOME_PUT);
                    }
                } else {
                    onFailure2(mView, UserConstants.WALLET_INCOME_PUT);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.WALLET_INCOME_POST);
            }


        });
    }

    /**
     * 兑换记录。
     * @param page page
     * @param page_size pagesize
     */
    public void WalletIncomeConvertHistory(String page, String page_size) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("page", page);
        params.put("page_size", page_size);
        params.put("method", UserConstants.WALLET_INCOME_CONVERTHISTORY);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<ExchangeDiamondHistoryModel>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ExchangeDiamondHistoryModel> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                if ("200".equals(status)) {
                    if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null) {
                        Result result = root.getResult().get(0);
                        PageModel pageModel = new PageModel();
                        pageModel.setPage(result.getPage());
                        pageModel.setPage_total(result.getPage_total());
                        pageModel.setPage_count(result.getPage_count());
                        //分页信息
                        List datas = new ArrayList();
                        datas.add(pageModel);
                        //数据信息。
                        List<ExchangeDiamondHistoryModel> data = validateBodyList(root);
                        if(data!=null) {
                            datas.addAll(data);
                        }
                        onSuccess(mView, UserConstants.WALLET_INCOME_CONVERTHISTORY, datas);
                    } else {
                        onFailure2(mView, UserConstants.WALLET_INCOME_CONVERTHISTORY);
                    }
                } else {
                    onFailure2(mView, UserConstants.WALLET_INCOME_CONVERTHISTORY);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.WALLET_INCOME_POST);
            }


        });
    }


    /**
     * 获取邀请佣金 或者退款记录
     * @param page_size
     * @param page
     */
    public void  getRefundList(String page,String page_size){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        //1：邀请佣金，2：退款记录
        params.put("log_type", "2");
        params.put("page_size", page_size);
        params.put("page", page);
        params.put("method", UserConstants.WALLET_BALANCE);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<RefundModel>>() {
                }.getType();
                Gson gson = new Gson();
                Root<RefundModel> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();

                if ("200".equals(status)) {
                    if (root.getResult()!=null&&root.getResult().size()>0&&root.getResult().get(0) != null) {
                        Result result = root.getResult().get(0);
                        PageModel pageModel = new PageModel();
                        pageModel.setPage(result.getPage());
                        pageModel.setPage_count(result.getPage_count());
                        pageModel.setPage_total(result.getPage_total());
                        pageModel.setPage_size(result.getPage_size());
                        List<Object> data = new ArrayList<Object>();
                        List<RefundModel> list = validateBodyList(root);
                        data.add(pageModel);
                        if(list!=null&&list.size()>0) {
                            data.addAll(list);
                        }
                        onSuccess(mView, UserConstants.POST_WALLET_BALANCE, data);
                    } else {
                        onFailure2(mView, message);
                    }
                } else {
                    onFailure2(mView, message);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView, UserConstants.POST_WALLET_BALANCE);
            }


        });
    }
    /**
     *
     *邀请佣金。
     */
    public void getInviteList(String page,String page_size){
            TreeMap<String, String> params = new TreeMap<String, String>();
            params.put("token", App.getInstance().getToken());
            //1：邀请佣金，2：退款记录
            params.put("log_type", "1");
            params.put("page_size", page_size);
            params.put("page", page);
            params.put("method", UserConstants.WALLET_BALANCE);
            OkHttpUtils.getInstance().post(null, params, new MgCallback() {
                @Override
                public void onErrorResponse(String message, String errorCode) {
                    MGToast.showToast(message);
                }

                @Override
                public void onSuccessResponse(String responseBody) {
                    Type type = new TypeToken<Root<InviteModel>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Root<InviteModel> root = gson.fromJson(responseBody, type);
                    String status = root.getStatusCode();
                    String message = root.getMessage();

                    if ("200".equals(status)) {
                        if (root.getResult()!=null&&root.getResult().size()>0&&root.getResult().get(0) != null) {
                            Result result = root.getResult().get(0);
                            PageModel pageModel = new PageModel();
                            pageModel.setPage(result.getPage());
                            pageModel.setPage_count(result.getPage_count());
                            pageModel.setPage_total(result.getPage_total());
                            pageModel.setPage_size(result.getPage_size());
                            List<Object> data = new ArrayList<Object>();
                            data.add(pageModel);
                            List<RefundModel> list = validateBodyList(root);
                            if(list!=null) {
                                data.addAll(list);
                            }
                            onSuccess(mView, UserConstants.POST_WALLET_BALANCE, data);
                        } else {
                            onFailure2(mView, message);
                        }
                    } else {
                        onFailure2(mView, message);
                    }

                }

                @Override
                public void onFinish() {
                    onFinish2(mView, UserConstants.POST_WALLET_BALANCE);
                }


            });

    }
    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }
}

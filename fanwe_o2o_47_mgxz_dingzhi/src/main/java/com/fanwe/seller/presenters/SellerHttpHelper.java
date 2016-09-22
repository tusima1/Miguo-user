package com.fanwe.seller.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Root;
import com.fanwe.constant.Constant;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.ModelComment;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.model.checkShopCollect.ResultCheckShopCollect;
import com.fanwe.seller.model.checkShopCollect.RootCheckShopCollect;
import com.fanwe.seller.model.getBusinessCircleList.ModelBusinessCircleList;
import com.fanwe.seller.model.getBusinessCircleList.ResultBusinessCircleList;
import com.fanwe.seller.model.getBusinessCircleList.RootBusinessCircleList;
import com.fanwe.seller.model.getBusinessDistributionList.ResultBusinessDistributionList;
import com.fanwe.seller.model.getBusinessDistributionList.RootBusinessDistributionList;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.seller.model.getCityList.ResultCityList;
import com.fanwe.seller.model.getCityList.RootCityList;
import com.fanwe.seller.model.getClassifyList.ModelClassifyList;
import com.fanwe.seller.model.getClassifyList.ResultClassifyList;
import com.fanwe.seller.model.getClassifyList.RootClassifyList;
import com.fanwe.seller.model.getCommentList.ResultCommentList;
import com.fanwe.seller.model.getCommentList.RootCommentList;
import com.fanwe.seller.model.getCroupBuyByMerchant.ModelCroupBuyByMerchant;
import com.fanwe.seller.model.getCroupBuyByMerchant.ResultCroupBuyByMerchant;
import com.fanwe.seller.model.getCroupBuyByMerchant.RootCroupBuyByMerchant;
import com.fanwe.seller.model.getGroupBuyCollect.ModelGroupBuyCollect;
import com.fanwe.seller.model.getGroupBuyCollect.ResultGroupBuyCollect;
import com.fanwe.seller.model.getGroupBuyCollect.RootGroupBuyCollect;
import com.fanwe.seller.model.getGroupBuyDetail.ModelGroupBuyDetail;
import com.fanwe.seller.model.getGroupBuyDetail.ResultGroupBuyDetail;
import com.fanwe.seller.model.getGroupBuyDetail.RootGroupBuyDetail;
import com.fanwe.seller.model.getGroupList.ModelGroupList;
import com.fanwe.seller.model.getGroupList.ResultGroupList;
import com.fanwe.seller.model.getGroupList.RootGroupList;
import com.fanwe.seller.model.getMarketList.ModelMarketListItem;
import com.fanwe.seller.model.getMarketList.ResultMarketList;
import com.fanwe.seller.model.getMarketList.RootMarketList;
import com.fanwe.seller.model.getOrderByList.ResultOrderByList;
import com.fanwe.seller.model.getOrderByList.RootOrderByList;
import com.fanwe.seller.model.getRepresentMerchant.RootRepresentMerchant;
import com.fanwe.seller.model.getShopInfo.ResultShopInfo;
import com.fanwe.seller.model.getShopInfo.RootShopInfo;
import com.fanwe.seller.model.getShopList.ModelShopListNavs;
import com.fanwe.seller.model.getShopList.ResultShopList;
import com.fanwe.seller.model.getShopList.RootShopList;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.seller.model.getStoreList.ResultStoreList;
import com.fanwe.seller.model.getStoreList.RootStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.interf.IHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/4.
 */
public class SellerHttpHelper implements IHelper {

    private static final String TAG = SellerHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private CallbackView2 mView2;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public SellerHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public SellerHttpHelper(Context mContext, CallbackView2 mView2, String type) {
        this.mContext = mContext;
        this.mView2 = mView2;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        return userCurrentInfo.getToken();
    }

    /**
     * 请求门店列表
     */
    public void getStoreList(int pageNum, int pageSize, String type, String cityId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("condition_type", type);
        params.put("city_id", cityId);
        params.put("method", SellerConstants.STORE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootStoreList rootShopList = gson.fromJson(responseBody, RootStoreList.class);
                List<ResultStoreList> resultShopList = rootShopList.getResult();
                if (SDCollectionUtil.isEmpty(resultShopList)) {
                    mView.onSuccess(SellerConstants.STORE_LIST, null);
                    return;
                }
                List<ModelStoreList> items = resultShopList.get(0).getBody();
                mView.onSuccess(SellerConstants.STORE_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取门店详情信息。
     *
     * @param sellerId
     */
    public void getSellerDetail(String sellerId) {
        if (TextUtils.isEmpty(sellerId)) {
            return;
        }
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("biz_id", sellerId);
        params.put("method", SellerConstants.LIVE_BIZ_SHOP);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<SellerDetailInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<SellerDetailInfo> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                //200为正常的返回 。
                if (Constant.RESULT_SUCCESS.equals(status)) {
                    SellerDetailInfo sellerDetailInfo = (SellerDetailInfo) validateBody(root);
                    if (sellerDetailInfo != null) {
                        List<SellerDetailInfo> datas = new ArrayList<SellerDetailInfo>();
                        datas.add(sellerDetailInfo);
                        mView.onSuccess(SellerConstants.LIVE_BIZ_SHOP, datas);
                    }
                } else {
                    onErrorResponse(message, status);
                }


            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 收藏门店
     */
    public void postShopCollect(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.SHOP_COLLECT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.SHOP_COLLECT_POST, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取收藏门店列表
     */
    public void getShopCollect() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.SHOP_COLLECT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootStoreList root = gson.fromJson(responseBody, RootStoreList.class);
                List<ResultStoreList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.SHOP_COLLECT_GET, null);
                    return;
                }
                List<ModelStoreList> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.SHOP_COLLECT_GET, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 取消门店收藏
     */
    public void deleteShopCollect(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.SHOP_COLLECT);

        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.SHOP_COLLECT_DELETE, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }


    /**
     * 收藏团购
     */
    public void postGroupBuyCollect(String tuan_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("tuan_id", tuan_id);
        params.put("method", SellerConstants.GROUP_BUY_COLLECT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.GROUP_BUY_COLLECT_POST, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取收藏团购列表
     */
    public void getGroupBuyCollect() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.GROUP_BUY_COLLECT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGroupBuyCollect root = gson.fromJson(responseBody, RootGroupBuyCollect.class);
                List<ResultGroupBuyCollect> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.GROUP_BUY_COLLECT_GET, null);
                    return;
                }
                List<ModelGroupBuyCollect> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.GROUP_BUY_COLLECT_GET, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 取消团购收藏
     */
    public void deleteGroupBuyCollect(String tuan_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("tuan_id", tuan_id);
        params.put("method", SellerConstants.GROUP_BUY_COLLECT);

        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.GROUP_BUY_COLLECT_DELETE, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取商圈列表
     */
    public void getBusinessCircleList(String city_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("city_id", city_id);
        params.put("method", SellerConstants.BUSINESS_CIRCLE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootBusinessCircleList root = gson.fromJson(responseBody, RootBusinessCircleList.class);
                List<ResultBusinessCircleList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.BUSINESS_CIRCLE_LIST, null);
                    return;
                }
                List<ModelBusinessCircleList> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.BUSINESS_CIRCLE_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 检测门店是否收藏过
     *
     * @param shop_id
     */
    public void checkShopCollect(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.CHECK_SHOP_COLLECT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCheckShopCollect root = gson.fromJson(responseBody, RootCheckShopCollect.class);
                List<ResultCheckShopCollect> result = root.getResult();
                if (!SDCollectionUtil.isEmpty(result) && result.get(0) != null && result.get(0).getBody() != null) {
                    List<ModelCheckShopCollect> items = result.get(0).getBody();
                    mView.onSuccess(SellerConstants.CHECK_SHOP_COLLECT, items);
                } else {
                    mView.onSuccess(SellerConstants.CHECK_SHOP_COLLECT, null);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 检测团购是否收藏过
     *
     * @param tuan_id
     */
    public void checkGroupCollect(String tuan_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("tuan_id", tuan_id);
        params.put("method", SellerConstants.CHECK_GROUP_BUY_COLLECT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCheckShopCollect root = gson.fromJson(responseBody, RootCheckShopCollect.class);
                List<ResultCheckShopCollect> result = root.getResult();
                if (!SDCollectionUtil.isEmpty(result) && result.get(0) != null && result.get(0).getBody() != null) {
                    List<ModelCheckShopCollect> items = result.get(0).getBody();
                    mView.onSuccess(SellerConstants.CHECK_GROUP_BUY_COLLECT, items);
                } else {
                    mView.onSuccess(SellerConstants.CHECK_GROUP_BUY_COLLECT, null);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 取得排序列表
     */
    public void getOrderByList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.ORDER_BY_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootOrderByList root = gson.fromJson(responseBody, RootOrderByList.class);
                List<ResultOrderByList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.ORDER_BY_LIST, null);
                    return;
                }
                List<ModelShopListNavs> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.ORDER_BY_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取分类列表
     */
    public void getClassifyList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.CLASSIFY_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootClassifyList root = gson.fromJson(responseBody, RootClassifyList.class);
                List<ResultClassifyList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.CLASSIFY_LIST, null);
                    return;
                }
                List<ModelClassifyList> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.CLASSIFY_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获取商家列表
     *
     * @param tid        二级分类ID
     * @param cate_id    大分类ID
     * @param city_id    城市
     * @param order_type 排序
     * @param pid        大区id
     * @param store_type 1优惠商家2全部商家
     * @param quan_id    商圈id
     * @param keyword    关键字
     * @param pageNum
     * @param pageSize
     */
    public void getShopList(String tid, String cate_id, String city_id, String order_type, String pid, String store_type, String quan_id, String keyword, int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("tid", tid);
        params.put("cate_id", cate_id);
        params.put("city_id", city_id);
        params.put("order_type", order_type);
        params.put("pid", pid);
        params.put("store_type", store_type);
        params.put("quan_id", quan_id);
        params.put("keyword", keyword);
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("method", SellerConstants.SHOP_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootShopList rootShopList = gson.fromJson(responseBody, RootShopList.class);
                List<ResultShopList> result = rootShopList.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.SHOP_LIST, null);
                    return;
                }
                mView.onSuccess(SellerConstants.SHOP_LIST, result);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 城市列表
     */
    public void getCityList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.CITY_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCityList root = gson.fromJson(responseBody, RootCityList.class);
                List<ResultCityList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.CITY_LIST, null);
                    return;
                }
                List<ModelCityList> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.CITY_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 市场商家代言列表
     */
    public void getMarketList(int pageNum, int pageSize, String buss_type, String keyword, String city_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("buss_type", buss_type);
        params.put("keyword", keyword);
        params.put("city_id", city_id);
        params.put("method", SellerConstants.MARKET_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootMarketList root = gson.fromJson(responseBody, RootMarketList.class);
                List<ResultMarketList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.MARKET_LIST, null);
                    return;
                }
                List<ModelMarketListItem> items = result.get(0).getList();
                mView.onSuccess(SellerConstants.MARKET_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 门店信息
     */
    public void getShopInfo(String shop_id, String city_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("shop_id", shop_id);
        params.put("city_id", city_id);
        params.put("method", SellerConstants.SHOP_INFO);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootShopInfo root = gson.fromJson(responseBody, RootShopInfo.class);
                List<ResultShopInfo> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView2.onSuccess(SellerConstants.SHOP_INFO, null);
                    return;
                }
                mView2.onSuccess(SellerConstants.SHOP_INFO, result);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                mView2.onFinish(SellerConstants.SHOP_INFO);
            }
        });
    }

    /**
     * 商家分销商品列表
     */
    public void getBusinessDistributionList(int pageNum, int pageSize, String ent_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        if (userCurrentInfo.getUserInfoNew() != null) {
            params.put("user_id", userCurrentInfo.getUserInfoNew().getUser_id());
        } else {
            params.put("user_id", "");
        }
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("ent_id", ent_id);
        params.put("method", SellerConstants.BUSINESS_DISTRIBUTION_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootBusinessDistributionList root = gson.fromJson(responseBody, RootBusinessDistributionList.class);
                List<ResultBusinessDistributionList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.BUSINESS_DISTRIBUTION_LIST, null);
                    return;
                }
                mView.onSuccess(SellerConstants.BUSINESS_DISTRIBUTION_LIST, result);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 通过商家id获取团购列表
     */
    public void getCroupBuyByMerchant(int pageNum, int pageSize, String ent_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("ent_id", ent_id);
        params.put("method", SellerConstants.GROUP_BUY_BY_MERCHANT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCroupBuyByMerchant root = gson.fromJson(responseBody, RootCroupBuyByMerchant.class);
                List<ResultCroupBuyByMerchant> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.GROUP_BUY_BY_MERCHANT, null);
                    return;
                }
                List<ModelCroupBuyByMerchant> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.GROUP_BUY_BY_MERCHANT, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 评论列表
     */
    public void getCommentList(int pageNum, int pageSize, String tuan_id, String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("tuan_id", tuan_id);
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.COMMENT_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCommentList root = gson.fromJson(responseBody, RootCommentList.class);
                List<ResultCommentList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.COMMENT_LIST, null);
                    return;
                }
                List<ModelComment> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.COMMENT_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 团购详情
     */
    public void getGroupBuyDetail(String tuan_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("id", tuan_id);
        params.put("method", SellerConstants.GROUP_BUY_DETAIL);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGroupBuyDetail root = gson.fromJson(responseBody, RootGroupBuyDetail.class);
                List<ResultGroupBuyDetail> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.GROUP_BUY_DETAIL, null);
                    return;
                }
                ModelGroupBuyDetail item = result.get(0).getDetail();
                List<ModelGroupBuyDetail> items = new ArrayList<>();
                items.add(item);
                mView.onSuccess(SellerConstants.GROUP_BUY_DETAIL, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 我要代言，get代言delete取消代言
     */
    public void getRepresentMerchant(String ent_id, String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("ent_id", ent_id);
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.REPRESENT_MERCHANT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootRepresentMerchant root = gson.fromJson(responseBody, RootRepresentMerchant.class);
                if ("307".equals(root.getStatusCode())) {
                    List<RootRepresentMerchant> roots = new ArrayList<RootRepresentMerchant>();
                    roots.add(root);
                    mView.onSuccess(SellerConstants.REPRESENT_MERCHANT, roots);
                } else
                    mView.onSuccess(SellerConstants.REPRESENT_MERCHANT, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }


    /**
     * 获取团购列表
     *
     * @param tid        二级分类ID
     * @param cate_id    大分类ID
     * @param city_id    城市
     * @param order_type 排序
     * @param quan_id    商圈id
     * @param keyword    关键字
     * @param pageNum
     * @param pageSize
     */
    public void getGroupList(String tid, String cate_id, String city_id, String order_type, String quan_id, String keyword, int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("tid", tid);
        if (TextUtils.isEmpty(cate_id)) {
            cate_id = "0";
        }
        params.put("cate_id", cate_id);
        params.put("city_id", city_id);
        params.put("order_type", order_type);
        params.put("qid", quan_id);
        params.put("keyword", keyword);
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("method", SellerConstants.GROUP_BUY);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGroupList rootGroupList = gson.fromJson(responseBody, RootGroupList.class);
                List<ResultGroupList> result = rootGroupList.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(SellerConstants.GROUP_BUY, null);
                    return;
                }
                List<ModelGroupList> items = result.get(0).getBody();
                mView.onSuccess(SellerConstants.GROUP_BUY, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 点评门店
     *
     * @param shop_id
     * @param content
     * @param point
     * @param image
     */
    public void postShopComment(String shop_id, String content, String point, String image) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("shop_id", shop_id);
        params.put("content", content);
        params.put("point", point);
        params.put("image", image);
        params.put("method", SellerConstants.SHOP_COMMENT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.SHOP_COMMENT, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 点评订单
     *
     * @param order_item_id
     * @param content
     * @param point
     * @param image
     */
    public void postGroupBuyComment(String order_item_id, String content, String point, String image) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("order_item_id", order_item_id);
        params.put("content", content);
        params.put("point", point);
        params.put("image", image);
        params.put("method", SellerConstants.GROUP_BUY_COMMENT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.GROUP_BUY_COMMENT, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    public void getMyDistributionShop() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", SellerConstants.MY_DISTRIBUTION_SHOP);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerConstants.MY_DISTRIBUTION_SHOP, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mView = null;
        gson = null;
        userCurrentInfo = null;
    }
}

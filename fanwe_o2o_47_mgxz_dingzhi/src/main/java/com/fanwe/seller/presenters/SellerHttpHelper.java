package com.fanwe.seller.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Root;
import com.fanwe.constant.Constant;
import com.fanwe.home.model.ResultLive;
import com.fanwe.home.model.Room;
import com.fanwe.home.model.RootLive;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.model.getShopList.ModelShopList;
import com.fanwe.seller.model.getShopList.ResultShopList;
import com.fanwe.seller.model.getShopList.RootShopList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;

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
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public SellerHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        if (!TextUtils.isEmpty(token)) {
            return token;
        } else {
            token = userCurrentInfo.getToken();
            return token;
        }
    }

    /**
     * 请求门店列表
     */
    public void getShopList(int pageNum, int pageSize, String type, String keyword, String city) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token",  getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("keyword", keyword);
        params.put("city", city);
        params.put("method", SellerConstants.SHOP_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {

                RootShopList rootShopList = gson.fromJson(responseBody, RootShopList.class);
                List<ResultShopList> resultShopList = rootShopList.getResult();
                if (SDCollectionUtil.isEmpty(resultShopList)) {
                    mView.onSuccess(SellerConstants.SHOP_LIST, null);
                    return;
                }
                List<ModelShopList> items = resultShopList.get(0).getItem();
                mView.onSuccess(SellerConstants.SHOP_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 获取门店详情信息。
      * @param sellerId
     */
    public void getSellerDetail(String sellerId){
        if(TextUtils.isEmpty(sellerId)){
            return;
        }
        TreeMap<String, String> params = new TreeMap<String, String>();
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
                if(Constant.RESULT_SUCCESS.equals(status)){
                    SellerDetailInfo sellerDetailInfo = (SellerDetailInfo) validateBody(root);
                    if(sellerDetailInfo!=null){
                        List<SellerDetailInfo> datas = new ArrayList<SellerDetailInfo>();
                        datas.add(sellerDetailInfo);
                        mView.onSuccess(SellerConstants.LIVE_BIZ_SHOP,datas);
                    }
                }else{
                    onErrorResponse(message,status);
                }


            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
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

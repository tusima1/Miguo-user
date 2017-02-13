package com.fanwe.reward.presenters;

import com.fanwe.app.App;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.reward.RewardConstants;
import com.fanwe.reward.model.DiamondTypeEntity;
import com.fanwe.reward.model.DiamondUserOwnEntity;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.OrderDetailInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imcore.Context;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/9/11.
 */
public class DiamondHelper  extends Presenter {
    private RefreshCalbackView mCallbackView;
    private Context mContext;

    public DiamondHelper(Context mContext,RefreshCalbackView mCallbackView){
        this.mContext = mContext;
        this.mCallbackView = mCallbackView;

    }
    public DiamondHelper(RefreshCalbackView mCallbackView){
        this.mCallbackView = mCallbackView;
    }

    /**
     * 获取果钻列表。
     */

    public void GetDiamondList(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", RewardConstants.BUY_DIAMOND);

        OkHttpUtil.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<DiamondTypeEntity>>() {
                }.getType();
                Gson gson = new Gson();
                Root<DiamondTypeEntity> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<DiamondTypeEntity> datas = validateBodyList(root);
                    mCallbackView.onSuccess(RewardConstants.BUY_DIAMOND,datas);
                }else{
                    mCallbackView.onFailue(RewardConstants.BUY_DIAMOND,message);
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                mCallbackView.onFailue(RewardConstants.BUY_DIAMOND,message);
            }
        });
    }

    /**
     * 获取当前用户已有的果钻。
     */
    public void getUserDiamond(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", RewardConstants.USER_DIAMOND);

        OkHttpUtil.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<DiamondUserOwnEntity>>() {
                }.getType();
                Gson gson = new Gson();
                Root<DiamondUserOwnEntity> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<DiamondUserOwnEntity> datas = validateBodyList(root);
                    mCallbackView.onSuccess(RewardConstants.USER_DIAMOND,datas);
                }else{
                    mCallbackView.onFailue(RewardConstants.USER_DIAMOND,message);
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                mCallbackView.onFailue(RewardConstants.USER_DIAMOND,message);
            }
        });
    }

    /**
     * 创建购买果钻订单。
     *302：  参数错误
     200：  成功
     300：  操作失败
     * @param payment_id 支付方式id
     * @param diamond_id 钻id
     */

    public void createDiamondOrder(String  payment_id,String diamond_id){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("payment_id", payment_id);
        params.put("diamond_id", diamond_id);
        params.put("token", App.getInstance().getToken());
        params.put("method", RewardConstants.DIAMOND_ORDER);

        OkHttpUtil.getInstance().put(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {


                Type type = new TypeToken<Root<OrderDetailInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<OrderDetailInfo> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<OrderDetailInfo> datas = validateBodyList(root);
                    if(mCallbackView!=null) {
                        mCallbackView.onSuccess(RewardConstants.DIAMOND_ORDER, datas);
                    }
                }else{
                    if(mCallbackView!=null) {
                        mCallbackView.onFailue(RewardConstants.DIAMOND_ORDER, message);
                    }
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView!=null) {
                    mCallbackView.onFailue(RewardConstants.DIAMOND_ORDER, message);
                }
            }
        });
    }
    @Override
    public void onDestory() {
        mCallbackView = null;
        mContext = null;
    }
}

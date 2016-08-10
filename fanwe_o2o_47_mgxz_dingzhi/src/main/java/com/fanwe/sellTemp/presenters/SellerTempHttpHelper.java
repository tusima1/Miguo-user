package com.fanwe.sellTemp.presenters;

import android.content.Context;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.sellTemp.model.SellerTempConstants;
import com.fanwe.sellTemp.model.getRedPacket.ModelGetRedPacket;
import com.fanwe.sellTemp.model.getRedPacket.ResultGetRedPacket;
import com.fanwe.sellTemp.model.getRedPacket.RootGetRedPacket;
import com.fanwe.sellTemp.model.postRedPacket.ModelPostRedPacket;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/4.
 */
public class SellerTempHttpHelper implements IHelper {

    private static final String TAG = SellerTempHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public SellerTempHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        return userCurrentInfo.getToken();
    }

    /**
     * 商家按批次取得红包
     *
     * @param shopId
     */
    public void getRedPacket(String shopId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("id", shopId);
        params.put("method", SellerTempConstants.CREATE_RED_PACKET);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGetRedPacket rootGetRedPacket = gson.fromJson(responseBody, RootGetRedPacket.class);
                List<ResultGetRedPacket> resultGetRedPackets = rootGetRedPacket.getResult();
                if (SDCollectionUtil.isEmpty(resultGetRedPackets) || resultGetRedPackets.size() < 1) {
                    mView.onSuccess(SellerTempConstants.GET_CREATE_RED_PACKET, null);
                    return;
                }
                ResultGetRedPacket resultGetRedPacket = resultGetRedPackets.get(0);
                List<ModelGetRedPacket> modelGetRedPackets = resultGetRedPacket.getBody();
                mView.onSuccess(SellerTempConstants.GET_CREATE_RED_PACKET, modelGetRedPackets);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 商家生成红包
     *
     * @param modelPostRedPacket
     */
    public void postRedPacket(ModelPostRedPacket modelPostRedPacket) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("id", modelPostRedPacket.getId());
        params.put("special_note", modelPostRedPacket.getSpecial_note());
        params.put("amount_limit", modelPostRedPacket.getAmount_limit());
        params.put("is_preferential_overlay", modelPostRedPacket.getIs_preferential_overlay());
        params.put("available_time_start", modelPostRedPacket.getAvailable_time_start());
        params.put("available_time_end", modelPostRedPacket.getAvailable_time_end());
        params.put("use_area_restrictions", modelPostRedPacket.getUse_area_restrictions());
        params.put("event_end", modelPostRedPacket.getEvent_end());
        params.put("available_period_data", modelPostRedPacket.getAvailable_period_data());
        params.put("available_period", modelPostRedPacket.getAvailable_period());
        params.put("use_store_restrictions", modelPostRedPacket.getUse_store_restrictions());
        params.put("only_one", modelPostRedPacket.getOnly_one());
        params.put("red_packet_name", modelPostRedPacket.getRed_packet_name());
        params.put("event_start", modelPostRedPacket.getEvent_start());
        params.put("red_packets", modelPostRedPacket.getRed_packets());
        params.put("deduction_limit", modelPostRedPacket.getDeduction_limit());
        params.put("method", SellerTempConstants.CREATE_RED_PACKET);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerTempConstants.POST_CREATE_RED_PACKET, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 商家删除某一批次的红包
     *
     * @param shopId
     * @param redKey
     */
    public void deleteRedPacket(String shopId, String redKey) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("id", shopId);
        params.put("red_packet_batch_number", redKey);
        params.put("method", SellerTempConstants.CREATE_RED_PACKET);

        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerTempConstants.DELETE_CREATE_RED_PACKET, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 商家分配指定批次的红包给旗下的代言人（先平均后随机）
     *
     * @param shopId
     * @param redKey
     */
    public void distributionRedPacket(String shopId, String redKey) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("id", shopId);
        params.put("red_packet_batch_number", redKey);
        params.put("method", SellerTempConstants.DISTRIBUTION_RED_PACKET);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(SellerTempConstants.DISTRIBUTION_RED_PACKET, null);
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

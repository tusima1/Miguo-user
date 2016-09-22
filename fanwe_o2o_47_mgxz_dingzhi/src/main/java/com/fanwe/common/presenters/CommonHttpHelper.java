package com.fanwe.common.presenters;

import android.content.Context;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.model.getHomeClassifyList.ResultHomeClassifyList;
import com.fanwe.common.model.getHomeClassifyList.RootHomeClassifyList;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CommonHttpHelper implements IHelper {

    private static final String TAG = CommonHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public CommonHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        return userCurrentInfo.getToken();
    }

    /**
     * 请求首页分类图标
     */
    public void getHomeClassifyList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", CommonConstants.HOME_CLASSIFY_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHomeClassifyList root = gson.fromJson(responseBody, RootHomeClassifyList.class);
                List<ResultHomeClassifyList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(CommonConstants.HOME_CLASSIFY_LIST, null);
                    return;
                }
                List<ModelHomeClassifyList> items = result.get(0).getBody();
                mView.onSuccess(CommonConstants.HOME_CLASSIFY_LIST, items);
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

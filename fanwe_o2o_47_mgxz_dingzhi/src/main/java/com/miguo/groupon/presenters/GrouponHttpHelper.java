package com.miguo.groupon.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.google.gson.Gson;
import com.miguo.definition.GrouponConstants;
import com.miguo.entity.ResultFeaturedGroupBuy;
import com.miguo.entity.RootFeaturedGroupBuy;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/4.
 */
public class GrouponHttpHelper implements IHelper {

    private static final String TAG = GrouponHttpHelper.class.getSimpleName();
    private Gson gson;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public GrouponHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
    }

    public String getToken() {
        return App.getInstance().getCurrentUser().getToken();
    }

    /**
     * 请求今日精选
     */
    public void getFeaturedGroupBuy(String cityId, String pageNum, String pageSize, String keyword, String m_longitude, String m_latitude) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("city_id", cityId);
        params.put("page", pageNum);
        params.put("page_size", pageSize);
        params.put("keyword", keyword);
        params.put("m_longitude", m_longitude);
        params.put("m_latitude", m_latitude);
        params.put("method", GrouponConstants.FEATURED_GROUP_BUG);

        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                //200
                RootFeaturedGroupBuy root = gson.fromJson(responseBody, RootFeaturedGroupBuy.class);
                //过滤错误请求
                if (!"200".equals(root.getStatusCode())) {
                    if (!TextUtils.isEmpty(root.getMessage())) {
                        mView.onFailue(root.getMessage());
                    } else {
                        mView.onFailue("getFeaturedGroupBuy error");
                    }
                    return;
                }
                List<ResultFeaturedGroupBuy> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(GrouponConstants.FEATURED_GROUP_BUG, null);
                    return;
                }
                mView.onSuccess(GrouponConstants.FEATURED_GROUP_BUG, result);
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
    }
}

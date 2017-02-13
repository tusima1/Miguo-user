package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.InterestingLiveVideoDao;
import com.miguo.definition.PageSize;
import com.miguo.entity.HiFunnyLiveVideoBean;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.entity.InterestingLiveVideoBean;
import com.miguo.view.BaseView;
import com.miguo.view.InterestingLiveVideoView;

import java.util.TreeMap;

/**
 * Created by 狗蛋哥 on 2016/11/23.
 * 获取有趣页对应tab的直播列表
 */
public class InterestingLiveVideoDaoImpl extends BaseDaoImpl implements InterestingLiveVideoDao{

    public InterestingLiveVideoDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getInterestingLiveVideo(final int page, int page_size, String tab_id, String city_id) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "InterestingLiveVideo");
        map.put("page", page + "");
        map.put("page_size", page_size + "");
        map.put("tab_id", tab_id);
        map.put("city_id", city_id);
        OkHttpUtil.getInstance().get("", map, new MgCallback(HiFunnyLiveVideoBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                HiFunnyLiveVideoBean bean = (HiFunnyLiveVideoBean)responseBody;
                if(null == bean){
                    getListener().getInterestingLiveVideoError();
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getInterestingLiveVideoError();
                    return;
                }
                if(SDCollectionUtil.isEmpty(bean.getResult())){
                    getListener().getInterestingLiveVideoError();
                    return;
                }
                if(SDCollectionUtil.isEmpty(bean.getResult().get(0).getBody())){
                    getListener().getInterestingLiveVideoError();
                    return;
                }
                if(page == PageSize.BASE_NUMBER_ONE){
                    getListener().getInterestingLiveVideoSuccess(bean.getResult().get(0).getBody());
                    return;
                }
                getListener().getInterestingLiveVideoLoadmoreSuccess(bean.getResult().get(0).getBody());
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getInterestingLiveVideoError();
            }

        });
    }

    @Override
    public InterestingLiveVideoView getListener() {
        return (InterestingLiveVideoView)baseView;
    }
}

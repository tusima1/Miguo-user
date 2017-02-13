package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.HiFunnyTabDao;
import com.miguo.definition.TabSet;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.entity.MenuBean;
import com.miguo.view.BaseView;
import com.miguo.view.HiFunnyTabView;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/23.
 */

public class HiFunnyTabDaoImpl extends BaseDaoImpl implements HiFunnyTabDao{

    public HiFunnyTabDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getFunnyTab(final String tab_set) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "InterestingTab");
        map.put("tab_set", tab_set);
        OkHttpUtil.getInstance().get("", map, new MgCallback(HiFunnyTabBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                HiFunnyTabBean bean = (HiFunnyTabBean)responseBody;
                if(bean != null){
                    if(bean.getStatusCode() == 200){

                        if(null == bean || null == bean.getResult() || null == bean.getResult().get(0) || null == bean.getResult().get(0).getBody()){
                            getFunnyTabError(tab_set);
                            return;
                        }

                        getFunnyTabSuccess(tab_set, bean);

                    }else {
                        getFunnyTabError(tab_set);
                    }
                }else {
                    getFunnyTabError(tab_set);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
            }

        });
    }

    private void getFunnyTabError(String tab_set){
        if(tab_set.equals(TabSet.LIVE)){
            getListener().getLiveTabListError();
            return;
        }
        getListener().getGuideTabListError();
    }

    private void getFunnyTabSuccess(String tab_set, HiFunnyTabBean bean){
        if(tab_set.equals(TabSet.LIVE)){
            getListener().getLiveTabListSuccess(bean.getResult().get(0).getBody());
            return;
        }
        getListener().getGuideTabListSuccess(bean.getResult().get(0).getBody());

    }

    @Override
    public HiFunnyTabView getListener() {
        return (HiFunnyTabView)baseView;
    }
}

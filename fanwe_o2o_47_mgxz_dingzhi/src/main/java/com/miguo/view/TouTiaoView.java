package com.miguo.view;

import com.miguo.entity.ToutiaoBean;

import java.util.List;

/**
 * Created by Barry on 2017/4/13.
 */
public interface TouTiaoView extends BaseView {

    void getToutiaoListSuccess(List<ToutiaoBean.Result.Body> toutiao);

    void getToutiaoListError(String message);

}

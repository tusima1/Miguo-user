package com.miguo.view;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface CollectShopView extends BaseView{

    void collectSuccess(HashMap<String,String> map);
    void collectError();

    void unCollectSuccess();
    void unCollectError();

}

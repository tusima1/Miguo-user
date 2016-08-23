package com.fanwe.shoppingcart;

import com.fanwe.base.CallbackView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public interface RefreshCalbackView extends CallbackView{


    /**
     * 失败返回 。
     * @param method
     * @param responseBody
     */
    void onFailue(String method,String responseBody);
}

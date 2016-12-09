package com.fanwe.network.okhttp3;

import android.support.v7.widget.MenuPopupWindow;

/**
 * Created by didik 
 * Created time 2016/12/2
 * Description: 
 */

public class Test {
    public void mainoo(){
        new OkCallback<MenuPopupWindow.MenuDropDownListView>() {
            @Override
            protected void onSuccess(String method, MenuPopupWindow.MenuDropDownListView bean) {
            }

        };

        new XXCallbackView() {
            @Override
            public void onSuccess(String method, Object bean) {

            }
        };
    }
}

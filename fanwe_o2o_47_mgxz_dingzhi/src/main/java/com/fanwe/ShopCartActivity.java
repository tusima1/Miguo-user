package com.fanwe;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.fragment.ShopCartFragmentNew;
import com.fanwe.o2o.miguo.R;

/**
 * 购物车
 *
 * @author js02
 *
 */
public class ShopCartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_container);
        init();
    }

    private void init() {
        getSDFragmentManager().replace(R.id.view_container_fl_content, new ShopCartFragmentNew());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init();
    }


}
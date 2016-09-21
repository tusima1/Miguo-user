package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fanwe.app.AppHelper;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;


/**
 * 分销小店
 *
 * @author Administrator
 */
public class DistributionStoreWapActivity extends BaseActivity {
    private String user_id = "";
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_distribution_store_wap);
        getIntentData();
    }

    private void init() {
        LocalUserModel userModel = AppHelper.getLocalUser();
        if (userModel == null) {
            SDToast.showToast("请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        String name = userModel.getUser_mobile();
        String pwd = userModel.getUser_pwd();

        AppWebViewFragment frag = new AppWebViewFragment();

        frag.setShowTitle(true);
        String url;
        if (ServerUrl.DEBUG) {
        } else {
        }
        url = ServerUrl.SERVER_H5 + "user/applogin?name=" + name + "&pwd=" + pwd;
        frag.setUrl(url);
        frag.setmProgressMode(EnumProgressMode.NONE);
        getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("user_id") && intent.hasExtra("url")) {
            //进别人的小店。
            user_id = intent.getStringExtra("user_id");
            String url = intent.getStringExtra("url");

            AppWebViewFragment frag = new AppWebViewFragment();
            frag.setShowTitle(true);
            frag.setUrl(url);
            frag.setUserId(user_id);
            frag.setmProgressMode(EnumProgressMode.NONE);
            getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
        } else {
            //进我的小店。
            init();
        }
    }

}

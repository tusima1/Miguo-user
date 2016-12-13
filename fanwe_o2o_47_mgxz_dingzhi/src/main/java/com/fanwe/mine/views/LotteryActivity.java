package com.fanwe.mine.views;

import android.os.Bundle;

import com.fanwe.BaseActivity;
import com.fanwe.app.AppHelper;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;

/**
 * 抽奖模块
 * Created by Administrator on 2016/12/12.
 */

public class LotteryActivity extends BaseActivity {
    private String url, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_distribution_store_wap);
        getIntentData();
    }

    private void getIntentData() {
        LocalUserModel userModel = AppHelper.getLocalUser();
        String name = "";
        String pwd = "";
        if (userModel != null) {
            name = userModel.getUser_mobile();
            pwd = userModel.getUser_pwd();
        }
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            id = getIntent().getStringExtra("id");
            url = url + "?name=" + name + "&pwd=" + pwd + "&from=app";
            AppWebViewFragment frag = new AppWebViewFragment();
            frag.setUserId(id);
            frag.setShowTitle(true);
            frag.setUrl(url);
            frag.setmProgressMode(WebViewFragment.EnumProgressMode.NONE);
            getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
        } else {
            finish();
        }
    }

}

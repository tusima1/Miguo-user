package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fanwe.app.AppHelper;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;


/**
 * 分销小店
 *
 * @author Administrator
 */
public class DistributionStoreWapActivity extends BaseActivity {
    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_distribution_store_wap);
        getIntentData();
    }

    private void init() {
        LocalUserModel userModel = AppHelper.getLocalUser();
        if (userModel == null) {
            MGToast.showToast("请先登录");
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
        //9月23日添加  &from=app
        url = ServerUrl.SERVER_H5 + "user/applogin?name=" + name + "&pwd=" + pwd + "&from=app";
        frag.setUrl(url);
        frag.setmProgressMode(EnumProgressMode.NONE);
        getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
        Log.e("Test",url);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("user_id") && intent.hasExtra("url")) {
            //进别人的小店。
            user_id = intent.getStringExtra("user_id");
            String url = intent.getStringExtra("url");
            url = url + "/from/app";

            LocalUserModel userModel = AppHelper.getLocalUser();
            if (userModel != null) {
                String name = userModel.getUser_mobile();
                String pwd = userModel.getUser_pwd();
                url = url + "/name/" + name + "/pwd/" + pwd;
            }

            AppWebViewFragment frag = new AppWebViewFragment();
            frag.setShowTitle(true);
            frag.setUrl(url);
            frag.setUserId(user_id);
            frag.setmProgressMode(EnumProgressMode.NONE);
            getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
            Log.e("Test",url);
        } else if (intent.hasExtra("id")) {
            //进别人的小店。
            String id = intent.getStringExtra("id");
            AppWebViewFragment frag = new AppWebViewFragment();
            frag.setShowTitle(true);
            String url;

            LocalUserModel userModel = AppHelper.getLocalUser();
            if (userModel != null) {
                String name = userModel.getUser_mobile();
                String pwd = userModel.getUser_pwd();
                url = ServerUrl.SERVER_H5 + "user/applogin?name=" + name + "&pwd=" + pwd + "&from=app&uid="+id ;
            } else {
                url = ServerUrl.SERVER_H5 + "user/shop/from/app/uid/" + id+"/";
            }

            frag.setUrl(url);
            frag.setmProgressMode(EnumProgressMode.NONE);
            getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
            Log.e("Test",url);
        } else {
            //进我的小店。
            init();
        }
    }

}

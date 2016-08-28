package com.fanwe;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.app.AppConfig;
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
    private int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_distribution_store_wap);
        init();
    }

    private void init() {
        LocalUserModel userModel = AppHelper.getLocalUser();
        if (userModel == null) {
            SDToast.showToast("请先登录");
            return;
        }

        String name = userModel.getUser_mobile();
        String pwd = userModel.getUser_pwd();

        Intent intent = getIntent();
        user_id = intent.getIntExtra("id", 0);
        AppWebViewFragment frag = new AppWebViewFragment();
        String session = AppConfig.getSessionId();
        frag.setShowTitle(true);
        String url;
        if (ServerUrl.DEBUG) {
//			url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_TEST_URL + ServerUrl.URL_PART_MOB+"&sess_id="
//					+ session + "#!/shop/1/"+ user_id;
//            url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_TEST_URL + "/mob/index.php?from=app" + "&sess_id="
//                    + session + "#!/shop/1/" + user_id;
            url = ServerUrl.SERVER_API_URL_PRE + "m.w2.mgxz.com" + "/user/applogin?name=" + name + "&pwd=" + pwd;
        } else {
//            url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_URL_MID + ServerUrl.URL_PART_MOB + "&sess_id=" + session + "#!/shop/1/" + user_id;
            url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_URL_MID + ServerUrl.URL_PART_MOB + "/user/applogin?name=" + name + "&pwd=" + pwd;
        }
        frag.setUrl(url);
        frag.setUserId(user_id);
        frag.setmProgressMode(EnumProgressMode.NONE);
        getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
    }

}

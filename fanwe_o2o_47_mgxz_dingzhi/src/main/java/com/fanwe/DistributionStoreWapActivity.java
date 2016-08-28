package com.fanwe;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.app.AppConfig;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.o2o.miguo.R;


/**
 * 分销小店
 *
 * @author Administrator
 *
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
        Intent intent = getIntent();
        user_id = intent.getIntExtra("id", 0);
        AppWebViewFragment frag = new AppWebViewFragment();
        String session = AppConfig.getSessionId();
        frag.setShowTitle(true);
        String url;
        if (ServerUrl.DEBUG) {
            url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_TEST_URL + "/mob/index" +
                    ".php?from=app" + "&sess_id="
                    + session + "#!/shop/1/" + user_id;
        } else {
            url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_URL_MID + ServerUrl
					.URL_PART_MOB + "&sess_id="
                    + session + "#!/shop/1/" + user_id;
        }
        frag.setUrl(url);
        frag.setUserId(user_id);
        frag.setmProgressMode(EnumProgressMode.NONE);
        getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
    }

}

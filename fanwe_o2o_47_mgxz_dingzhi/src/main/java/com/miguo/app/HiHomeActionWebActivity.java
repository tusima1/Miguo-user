package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiHomeActionWebCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by Barry on 2017/4/17.
 */
public class HiHomeActionWebActivity extends HiBaseActivity {

    String url;
    String webTitle;

    @Override
    protected Category initCategory() {
        getIntentData();
        return new HiHomeActionWebCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hihome_action_web);
    }

    private void getIntentData(){
        setUrl(getIntent().getStringExtra(IntentKey.WEB_URL));
        setWebTitle(getIntent().getStringExtra(IntentKey.WEB_TITLE));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }
}

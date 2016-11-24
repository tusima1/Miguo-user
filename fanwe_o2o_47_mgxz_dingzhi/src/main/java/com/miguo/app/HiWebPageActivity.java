package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiWebPageCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/3.
 */
public class HiWebPageActivity extends HiBaseActivity{

    String url;
    String webTitle;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hiwebpage);
    }

    @Override
    protected Category initCategory() {
        getIntentData();
        return new HiWebPageCategory(this);
    }

    private void getIntentData(){
        setUrl(getIntent().getStringExtra(IntentKey.HOME_BANNER_WEB_PAGE));
        setWebTitle(getIntent().getStringExtra(IntentKey.HOME_BANNER_WEB_TITLE));
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

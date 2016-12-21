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

    /**

     * 未签约城市的标题
     */
    String unSignCityTitle;

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
        setUnSignCityTitle(getIntent().getStringExtra(IntentKey.HOME_WEB_PAGE_TITLE));
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

    @Override

    protected void doOnPause() {
        getCategory().onPause();
    }

    @Override
    protected void doOnDestory() {
        getCategory().onDestory();
    }

    public String getUnSignCityTitle() {
        return unSignCityTitle;
    }

    public void setUnSignCityTitle(String unSignCityTitle) {
        this.unSignCityTitle = unSignCityTitle;
    }

    @Override
    public HiWebPageCategory getCategory() {
        return (HiWebPageCategory)super.getCategory();
    }
}

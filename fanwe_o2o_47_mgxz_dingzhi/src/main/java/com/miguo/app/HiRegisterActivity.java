package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.miguo.category.Category;
import com.miguo.category.HiRegisterCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by zlh on 2016/12/3.
 * 注册界面
 */
public class HiRegisterActivity extends HiBaseActivity {

    /**
     * 如果是从领钻码对话框进来的，结束Activity的时候要设置requestcode用于HiHomeActivity接收
     */
    boolean isFromDiamond;
    /**
     * 第三方OPENID ，用于第三方注册 。
     */
    String openid;
    String platform;
    String icon;
    String nick;
    String shareId;

    @Override
    protected Category initCategory() {
        return new HiRegisterCategory(this);
    }

    @Override
    protected void setContentView() {
        getIntentData();
        setContentView(R.layout.activity_hiregister);
    }

    private void getIntentData() {
        if(getIntent() != null){
            setFromDiamond(getIntent().getBooleanExtra(IntentKey.FROM_DIAMOND_TO_LOGIN, false));
            if (getIntent().hasExtra(UserConstants.THIRD_OPENID)) {
                setOpenid(getIntent().getStringExtra(UserConstants.THIRD_OPENID));
                setPlatform(getIntent().getStringExtra(UserConstants.THIRD_PLATFORM));
                setIcon(getIntent().getStringExtra(UserConstants.THIRD_ICON));
                setNick(getIntent().getStringExtra(UserConstants.THIRD_NICK));
            }
            if(getIntent().hasExtra(UserConstants.SHARE_ID)) {
                shareId = getIntent().getStringExtra(UserConstants.SHARE_ID);
            }

        }

    }

    public boolean isFromDiamond() {
        return isFromDiamond;
    }

    public void setFromDiamond(boolean fromDiamond) {
        isFromDiamond = fromDiamond;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
}

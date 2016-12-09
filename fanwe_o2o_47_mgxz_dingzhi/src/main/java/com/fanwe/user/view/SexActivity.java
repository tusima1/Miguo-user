package com.fanwe.user.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * 设置性别
 * Created by qiang.chen on 2016/10/27.
 */
public class SexActivity extends BaseActivity implements CallbackView {
    private TextView tvSex1, tvSex2;
    private String strSex;
    private UserHttpHelper userHttpHelper;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_sex);
        mTitle.setMiddleTextTop("设置性别");
        getIntentData();
        preWidget();
        setListener();
        setView();
        userHttpHelper = new UserHttpHelper(this, this);
    }

    private void setView() {
        if ("1".equals(strSex)) {
            tvSex1.setCompoundDrawables(null, null, drawable, null);
            tvSex2.setCompoundDrawables(null, null, null, null);
        } else if ("2".equals(strSex)) {
            tvSex1.setCompoundDrawables(null, null, null, null);
            tvSex2.setCompoundDrawables(null, null, drawable, null);
        } else {
            tvSex1.setCompoundDrawables(null, null, null, null);
            tvSex2.setCompoundDrawables(null, null, null, null);
        }
    }

    private void getIntentData() {
        if (getIntent() != null) {
            strSex = getIntent().getStringExtra("sex");
        }
        drawable = getResources().getDrawable(R.drawable.ic_gou_setting);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    private void setListener() {
        //女
        tvSex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSex = "1";
                userHttpHelper.updateUserInfo("sex", strSex);
                setView();
            }
        });
        //男
        tvSex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSex = "2";
                userHttpHelper.updateUserInfo("sex", strSex);
                setView();
            }
        });
    }

    private void preWidget() {
        tvSex1 = (TextView) findViewById(R.id.tv_sex1_act_sex);
        tvSex2 = (TextView) findViewById(R.id.tv_sex2_act_sex);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.USER_INFO_METHOD.equals(method)) {
            App.getInstance().getCurrentUser().setSex(strSex);
            MGToast.showToast("性别设置成功");
            finish();
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}

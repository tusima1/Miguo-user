package com.fanwe.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fanwe.BaseActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.StringTool;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * 个人简介
 * Created by qiang.chen on 2016/10/27.
 */
public class SignActivity extends BaseActivity implements CallbackView {
    private EditText etAdvice;
    private Button btnSubmit;
    private String strSign;
    private UserHttpHelper userHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_advice);
        mTitle.setMiddleTextTop("个人简介");
        getIntentData();
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            strSign = getIntent().getStringExtra("sign");
        }
    }

    private void setListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSign = etAdvice.getText().toString().trim();
                if (TextUtils.isEmpty(strSign)) {
                    MGToast.showToast("请输入个人简介");
                } else {
                    if (StringTool.getLengthChinese(strSign) > 50) {
                        MGToast.showToast("不能超过50个字");
                    } else {
                        btnSubmit.setClickable(false);
                        userHttpHelper.updateUserInfo("personality", strSign);
                    }
                }
            }
        });
    }

    private void preWidget() {
        etAdvice = (EditText) findViewById(R.id.et_advice);
        btnSubmit = (Button) findViewById(R.id.btn_advice);
        if (!TextUtils.isEmpty(strSign)) {
            etAdvice.setText(strSign);
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.USER_INFO_METHOD.equals(method)) {
            App.getInstance().getmUserCurrentInfo().getUserInfoNew().setRemark(strSign);
            MGToast.showToast("个人简介更新成功");
            finish();
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        if (UserConstants.USER_INFO_METHOD.equals(method)) {
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSubmit.setClickable(true);
                }
            });
        }
    }
}

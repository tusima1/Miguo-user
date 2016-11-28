package com.fanwe.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fanwe.BaseActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class AdviceActivity extends BaseActivity implements CallbackView2 {
    private EditText etAdvice;
    private Button btnSubmit;
    private String strAdvice;
    private UserHttpHelper userHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_advice);
        mTitle.setMiddleTextTop("我有建议");
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
    }

    private void setListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setClickable(false);
                strAdvice = etAdvice.getText().toString().trim();
                if (TextUtils.isEmpty(strAdvice)) {
                    MGToast.showToast("请输入建议");
                } else {
                    btnSubmit.setClickable(false);
                    userHttpHelper.advice(strAdvice);
                }
            }
        });
    }

    private void preWidget() {
        etAdvice = (EditText) findViewById(R.id.et_advice);
        btnSubmit = (Button) findViewById(R.id.btn_advice);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.ADVICE.equals(method)) {
            MGToast.showToast("成功提交，谢谢！");
           finish();
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnSubmit.setClickable(true);
            }
        });
    }
}

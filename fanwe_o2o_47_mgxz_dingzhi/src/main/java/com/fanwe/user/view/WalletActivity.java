package com.fanwe.user.view;

import android.app.Activity;
import android.os.Bundle;

import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.presents.UserHttpHelper;

import java.util.List;

public class WalletActivity extends Activity implements CallbackView2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        UserHttpHelper httpHelper=new UserHttpHelper(null,this);
        httpHelper.getMyWallet();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}

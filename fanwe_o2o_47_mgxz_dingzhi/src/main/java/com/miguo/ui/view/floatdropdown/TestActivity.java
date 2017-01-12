package com.miguo.ui.view.floatdropdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.ui.view.dropdown.DropDownMenu;
import com.miguo.ui.view.floatdropdown.helper.DropDownHelper;

import java.util.TreeMap;

public class TestActivity extends AppCompatActivity {

    private DropDownMenu ddm;
    private DropDownHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ddm = ((DropDownMenu) findViewById(R.id.ddm));

        helper = new DropDownHelper(this,ddm);
//        helper.setTwoModes(twoModes);

        getHttpData();
    }

    private void getHttpData(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "GetSearchCateCondition");
        params.put("city_id", "69e0405b-de8c-4247-8a0a-91ca45c4b30c");
        OkHttpUtil.getInstance().get(params, new HttpCallback<SearchCateConditionBean>() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccessWithBean(SearchCateConditionBean shaiXuanModel) {
                super.onSuccessWithBean(shaiXuanModel);
                SearchCateConditionBean.ResultBean.BodyBean body=null;
                try {
                    body = shaiXuanModel.getResult().get(0)
                            .getBody().get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("test","body is null!!!");
                }
                if (body!=null){
                    helper.setData(body);
                }
            }
        });
    }


}

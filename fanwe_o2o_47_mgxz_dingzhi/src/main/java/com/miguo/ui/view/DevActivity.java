package com.miguo.ui.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.didikee.uilibs.views.MaxHeightListView;
import com.fanwe.constant.ServerUrl;
import com.fanwe.o2o.miguo.R;
import com.miguo.utils.DevCode;
import com.miguo.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class DevActivity extends AppCompatActivity {

    private MaxHeightListView listView_api;
    private MaxHeightListView listView_h5;

    String[] h5Array = {
            DevCode.SERVER_H5_DEV,
            DevCode.SERVER_H5_TEST,
            DevCode.SERVER_H5_ONLINE
    };
    String[] apiArray = {
            DevCode.SERVER_API_JAVA_DEV_URL,
            DevCode.SERVER_API_JAVA_TEST_URL,
            DevCode.SERVER_API_ONLINE_URL,
            DevCode.JAVA_API_36,
            DevCode.JAVA_API_37,
            DevCode.JAVA_API_58
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        listView_api = (MaxHeightListView) findViewById(R.id.listView_api);
        listView_h5 = (MaxHeightListView) findViewById(R.id.listView_h5);
        initApi();
        initH5();

        initStatus();
        setListener();
    }

    private void setListener() {

        listView_h5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String h5;
                switch (position) {
                    case 0:
                        h5=DevCode.SERVER_H5_DEV;
                        break;
                    case 1:
                        h5=DevCode.SERVER_H5_TEST;
                        break;
                    case 2:
                        h5=DevCode.SERVER_H5_ONLINE;
                        break;
                    default:
                        h5=DevCode.SERVER_H5_DEV;
                        break;
                }
                SPUtil.putData(DevActivity.this, DevCode.API_H5, h5);
                ServerUrl.setServerH5Using(h5);
            }
        });

        listView_api.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String api_java;
                switch (position) {
                    case 0:
                        api_java=DevCode.SERVER_API_JAVA_DEV_URL;
                        break;
                    case 1:
                        api_java=DevCode.SERVER_API_JAVA_TEST_URL;
                        break;
                    case 2:
                        api_java=DevCode.SERVER_API_ONLINE_URL;
                        break;
                    case 3:
                        api_java=DevCode.JAVA_API_36;
                        break;
                    case 4:
                        api_java=DevCode.JAVA_API_37;
                        break;
                    case 5:
                        api_java=DevCode.JAVA_API_58;
                        break;
                    default:
                        api_java=DevCode.SERVER_API_JAVA_DEV_URL;
                        break;
                }
                SPUtil.putData(DevActivity.this, DevCode.API_JAVA,api_java);
                ServerUrl.setServerApi(api_java);
            }
        });

    }

    private void initStatus() {
        String javaApi = (String) SPUtil.getData(DevActivity.this, DevCode.API_JAVA, DevCode
                .SERVER_API_JAVA_DEV_URL);
        String h5Api = (String) SPUtil.getData(DevActivity.this, DevCode.API_H5, DevCode
                .SERVER_H5_DEV);
        int javaIndex = -1;
        for (int i = 0; i < apiArray.length; i++) {
            if (javaApi.equals(apiArray[i])) {
                javaIndex = i;
                break;
            }
        }
        int h5Index = -1;
        for (int i = 0; i < h5Array.length; i++) {
            if (h5Api.equals(h5Array[i])) {
                h5Index = i;
                break;
            }
        }
        if (javaIndex != -1) {
            listView_api.performItemClick(listView_api.getChildAt(javaIndex),javaIndex,listView_api.getAdapter().getItemId(javaIndex));
        }

        if (h5Index != -1) {
            listView_h5.performItemClick(listView_h5.getChildAt(h5Index),h5Index,listView_h5.getAdapter().getItemId(h5Index));
        }

    }


    private void initH5() {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, h5Array);

        listView_h5.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView_h5.setAdapter(arrayAdapter);

    }

    private void initApi() {
        List<String> showApiArray = new ArrayList<>();
        String dust = "mgxz.BussRPC/";
        for (String s : apiArray) {
            if (s.contains(dust)) {
                String replace = s.replace(dust, "");
                showApiArray.add(replace);
                continue;
            }
            showApiArray.add(s);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, showApiArray);

        listView_api.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView_api.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

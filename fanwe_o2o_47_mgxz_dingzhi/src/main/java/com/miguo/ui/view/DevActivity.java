package com.miguo.ui.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.didikee.uilibs.views.MaxHeightListView;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.DevCode;
import com.miguo.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class DevActivity extends AppCompatActivity {

    private MaxHeightListView listView_api;
    private MaxHeightListView listView_h5;
    private CheckedTextView checkedTextView;
    //            "h5开发: http://m.dev.mgxz.com/",
//            "h5测试: http://m.test.mgxz.com/",
//            "h5线上: http://m.mgxz.com/"
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

    //            "java开发: http://mapi.dev.mgxz.com/",
//            "java测试: http://mapi.test.mgxz.com/",
//            "java线上: http://mapi.mgxz.com/",
//            "java个人: http://192.168.90.36:8080/",
//            "java个人: http://192.168.90.37:8080/",
//            "java个人: http://192.168.90.58:8080/"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        listView_api = (MaxHeightListView) findViewById(R.id.listView_api);
        listView_h5 = (MaxHeightListView) findViewById(R.id.listView_h5);
        checkedTextView = ((CheckedTextView) findViewById(R.id.ctv_debug));

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("haha");
            }
        });
        initApi();
        initH5();

        initStatus();
        setListener();
    }

    private void setListener() {

        listView_h5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        SPUtil.putData(DevActivity.this, DevCode.API_H5, DevCode.SERVER_H5_DEV);
                        break;
                    case 1:
                        SPUtil.putData(DevActivity.this, DevCode.API_H5, DevCode.SERVER_H5_TEST);
                        break;
                    case 2:
                        SPUtil.putData(DevActivity.this, DevCode.API_H5, DevCode.SERVER_H5_ONLINE);
                        break;
                    default:
                        SPUtil.putData(DevActivity.this, DevCode.API_H5, DevCode.SERVER_H5_DEV);
                        break;
                }
            }
        });

        listView_api.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("test", "item: " + listView_api.getCheckedItemPosition());
                switch (position) {
                    case 0:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode
                                .SERVER_API_JAVA_DEV_URL);
                        break;
                    case 1:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode
                                .SERVER_API_JAVA_TEST_URL);
                        break;
                    case 2:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode
                                .SERVER_API_ONLINE_URL);
                        break;
                    case 3:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode.JAVA_API_36);
                        break;
                    case 4:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode.JAVA_API_37);
                        break;
                    case 5:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode.JAVA_API_58);
                        break;
                    default:
                        SPUtil.putData(DevActivity.this, DevCode.API_JAVA, DevCode
                                .SERVER_API_JAVA_DEV_URL);
                        break;
                }
            }
        });

    }

    private void initStatus() {
        String javaApi = (String) SPUtil.getData(DevActivity.this, DevCode.API_JAVA, DevCode
                .SERVER_API_JAVA_DEV_URL);
        String h5Api = (String) SPUtil.getData(DevActivity.this, DevCode.API_H5, DevCode
                .SERVER_API_JAVA_DEV_URL);
        int javaIndex = -1;
        for (int i = 0; i < h5Array.length; i++) {
            if (h5Api.equals(h5Array[i])) {
                javaIndex = i;
                break;
            }
        }
        int h5Index = -1;
        for (int i = 0; i < apiArray.length; i++) {
            if (javaApi.equals(apiArray[i])) {
                h5Index = i;
                break;
            }
        }
        if (javaIndex != -1) {

        }

        if (h5Index != -1) {

        }

//        String javaApi = (String) SPUtil.getData(DevActivity.this, DevCode.API_DEBUG, DevCode
//                .SERVER_API_JAVA_DEV_URL);

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

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

import java.util.ArrayList;
import java.util.List;

public class DevActivity extends AppCompatActivity {

    private MaxHeightListView listView_api;
    private MaxHeightListView listView_h5;
    private CheckedTextView checkedTextView;

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
    }

    private void initStatus() {
//        SharedPreferencesUtils.getInstance(this).
    }


    private void initH5() {
        String[] h5Array = {
                "h5开发: http://m.dev.mgxz.com/",
                "h5测试: http://m.test.mgxz.com/",
                "h5线上: http://m.mgxz.com/"
                };
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, h5Array);

        listView_h5.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView_h5.setAdapter(arrayAdapter);

        listView_h5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("test","item: "+listView_h5.getCheckedItemPosition());
            }
        });
    }

    private void initApi() {
        String[] apiArray = {
                "java开发: http://mapi.dev.mgxz.com/",
                "java测试: http://mapi.test.mgxz.com/",
                "java线上: http://mapi.mgxz.com/",
                "java个人: http://192.168.90.36:8080/",
                "java个人: http://192.168.90.37:8080/",
                "java个人: http://192.168.90.58:8080/"
                };

        List<String> showApiArray=new ArrayList<>();
        String dust="mgxz.BussRPC/";
        for (String s : apiArray) {
            if (s.contains(dust)){
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

        listView_api.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("test","item: "+listView_api.getCheckedItemPosition());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

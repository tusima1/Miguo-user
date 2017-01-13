package com.miguo.ui.view.floatdropdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.entity.SingleMode;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.floatdropdown.helper.DropDownPopHelper;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

import java.util.List;

public class TestDropDownPopActivity extends AppCompatActivity {


    private DropDownPopHelper popHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drop_down_pop);

        FakeDropDownMenu anchor = (FakeDropDownMenu) findViewById(R.id.anchor);
        popHelper = new DropDownPopHelper(this,anchor);

        popHelper.setOnDropDownListener(new OnDropDownListener() {
            @Override
            public void onItemSelected(int index, Pair<SingleMode, SingleMode> pair, List
                                <SingleMode> items) {
                if (index == 4){
                    if (items!=null){
                        MGToast.showToast("数量: "+items.size());
                    }

                }else {
                    if (pair!=null){
                        MGToast.showToast("一级id: " + pair.first.getSingleId() +"   二级id: "+pair.second.getSingleId());
                    }
                }
                popHelper.dismiss();
            }
        });


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popHelper.show();
            }
        });
    }
}

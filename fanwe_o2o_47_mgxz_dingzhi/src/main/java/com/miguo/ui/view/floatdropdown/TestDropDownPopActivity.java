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

import java.util.ArrayList;
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
            public void onItemSelected(int index, Pair<SingleMode,SingleMode> pair, List
                                <SingleMode> items) {
                if (index == 4){
                    if (items!=null){
                        MGToast.showToast("数量: "+items.size());
                    }

                }else {
                    SingleMode first = pair.first;
                    SingleMode second = pair.second;
                    String singleId1 = "无";
                    String singleId2 = "无";
                    if (first!=null ){
                        singleId1 = first.getSingleId();
                    }
                    if (second!=null ){
                        singleId2 = second.getSingleId();
                    }
                    MGToast.showToast("一级id: " + singleId1 +"   二级id: "+singleId2);

                    String titleName="";
                    if (pair.second!=null){
                        titleName = pair.second.getName();
                    }else {
                        titleName = pair.first.getName();
                    }
                    popHelper.setTitleText(index,titleName);
                }

                popHelper.dismiss();
            }
        });


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popHelper.show();
                //36531bd0-51c0-4f88-bb41-f2534b986118
                //popularity
                List<String> ids=new ArrayList<String>();
                ids.add("first_order");
                ids.add("fifty_to_hundred");
                ids.add("over_three_hundred");
                ids.add("popularity");
                ids.add("36531bd0-51c0-4f88-bb41-f2534b986118");
                ids.add("6b430429-081f-48a6-a092-a3bdd1b4ad63");
//                popHelper.performMarkIds("popularity");
                popHelper.performMarkIds(ids);
            }
        });
    }
}

package com.miguo.ui.view.floatdropdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.dropdown.DropDownPopup;

public class TestDropDownPopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drop_down_pop);

        View anchor = findViewById(R.id.anchor);
        final DropDownPopup popup=new DropDownPopup(this,anchor);


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
    }
}

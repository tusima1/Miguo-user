package com.miguo.live.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/8/2.
 */
public class TestRedActivity extends Activity {
    SendRedPacketDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_red);
        Button btn = (Button)findViewById(R.id.goRed);
        createDialog();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });

    }
    public void createDialog(){
        if(dialog==null){
            SendRedPacketDialog.Builder builder = new SendRedPacketDialog.Builder(this);
            builder.setSendListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SDToast.showToast("红包已经发出去了。");
                }
            });
            builder.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });
            dialog = builder.create();
        }
    }
}

package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * 看大图
 * Created by qiang.chen on 2016/6/20.
 */
public class ImgViewActivity extends Activity {
    private Context mContext = ImgViewActivity.this;
    private ImageView ivImg;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_img);
        preWidget();
        preData();
        setListener();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_delete_img_view:
                LiveAuthActivity.datas.remove(path);
                finish();
                break;
        }
    }

    private void preData() {
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
            File file = new File(path);
            if (file.exists()) {
                ImageLoader.getInstance().displayImage("file://" + path, ivImg);
            }
        }
    }

    private void setListener() {
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void preWidget() {
        ivImg = (ImageView) findViewById(R.id.iv_visit_img);
    }
}

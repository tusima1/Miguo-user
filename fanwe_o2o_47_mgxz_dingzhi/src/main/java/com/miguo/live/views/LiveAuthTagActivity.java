package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.AuthTagAdapter;
import com.miguo.live.interf.MyItemClickListener;
import com.miguo.live.model.getAuthTag.ModelAuthTag;
import com.miguo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class LiveAuthTagActivity extends Activity implements MyItemClickListener {
    private Context mContext = LiveAuthTagActivity.this;
    private AuthTagAdapter mAuthTagAdapter;
    private List<ModelAuthTag> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText et_tags;
    private Button btnSubmit;
    private String strTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_auth_tag);
        setWidget();
        preParam();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_arrow_left_bar_tag:
                finish();
                break;
            case R.id.btn_submit_live_auth_tag:
                Intent intent = new Intent();
                intent.putExtra("tags", et_tags.getText().toString().trim());
                setResult(8888, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void preParam() {
        datas.clear();
        for (int i = 0; i < 14; i++) {
            ModelAuthTag modelAuthTag = new ModelAuthTag();
            modelAuthTag.setId("" + i);
            modelAuthTag.setName("Tag:" + i);
            datas.add(modelAuthTag);
        }
        //换一批
        ModelAuthTag modelAuthTag = new ModelAuthTag();
        modelAuthTag.setName("换一批");
        datas.add(modelAuthTag);

        mAuthTagAdapter = new AuthTagAdapter(mContext, datas);
        mAuthTagAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAuthTagAdapter);
    }

    private void setWidget() {
        btnSubmit = (Button) findViewById(R.id.btn_submit_live_auth_tag);
        et_tags = (EditText) findViewById(R.id.et_tags);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView_auth_tag);
        //GridLayout 3列
        GridLayoutManager mgr = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(mgr);
        final int dp10 = DisplayUtil.dp2px(mContext, 10);
        final int dp15 = DisplayUtil.dp2px(mContext, 15);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                int childLayoutPosition = parent.getChildLayoutPosition(view);
                if (childLayoutPosition % 3 != 0) {
                    outRect.left = dp10;
                }
                if (childLayoutPosition > 2) {
                    outRect.top = dp15;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (postion == (datas.size() - 1)) {
            //换一批
            preParam();
        } else {
            strTags = et_tags.getText().toString().trim();
            ModelAuthTag modelAuthTag = datas.get(postion);
            String name = modelAuthTag.getName();
            if (TextUtils.isEmpty(strTags)) {
                et_tags.setText(name);
                btnSubmit.setBackgroundResource(R.drawable.bg_orange);
                btnSubmit.setClickable(true);
            } else {
                if (!strTags.contains(name)) {
                    strTags = strTags + "," + name;
                    et_tags.setText(strTags);
                }
            }
            //普通标签
            datas.remove(postion);
            mAuthTagAdapter.notifyDataSetChanged();
        }
    }
}

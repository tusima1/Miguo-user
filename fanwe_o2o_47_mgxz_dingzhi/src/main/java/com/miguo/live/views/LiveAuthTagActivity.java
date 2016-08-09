package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fanwe.base.CallbackView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.AuthTagAdapter;
import com.miguo.live.interf.MyItemClickListener;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getHostTags.ModelHostTags;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class LiveAuthTagActivity extends Activity implements MyItemClickListener, CallbackView {
    private Context mContext = LiveAuthTagActivity.this;
    private AuthTagAdapter mAuthTagAdapter;
    private List<ModelHostTags> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText et_tags;
    private Button btnSubmit;
    private String strTags;
    private LiveHttpHelper liveHttpHelper;


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
        liveHttpHelper = new LiveHttpHelper(this, this);
        liveHttpHelper.getHostTags("14");

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
            liveHttpHelper.getHostTags("14");
        } else {
            strTags = et_tags.getText().toString().trim();
            ModelHostTags ModelHostTags = datas.get(postion);
            String name = ModelHostTags.getDic_mean();
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

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List res) {
        if (LiveConstants.HOST_TAGS.equals(method)) {
            datas.clear();
            datas.addAll(res);
            //换一批
            ModelHostTags ModelHostTags = new ModelHostTags();
            ModelHostTags.setDic_mean("换一批");
            datas.add(ModelHostTags);
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAuthTagAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

}

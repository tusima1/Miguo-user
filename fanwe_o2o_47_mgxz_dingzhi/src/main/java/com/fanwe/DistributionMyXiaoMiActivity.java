package com.fanwe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.MyDistFragment;
import com.fanwe.fragment.MyDistFragment.OnDialogData;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.presents.UserHttpHelper;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 我的战队
 *
 * @author cxk
 */
public class DistributionMyXiaoMiActivity extends BaseActivity implements View.OnClickListener, CallbackView2 {

    @ViewInject(R.id.tv_textOne)
    private TextView mTv_textOne;

    @ViewInject(R.id.tv_number)
    private TextView mTv_number;

    @ViewInject(R.id.tv_textTwo)
    private TextView mTv_textTwo;

    @ViewInject(R.id.tv_stationName)
    private TextView mTv_stationName;

    private int type = 1;

    private boolean isShow;

    private MyDistFragment mDist;

    protected String money;

    private AlertDialog builder;
    private UserHttpHelper userHttpHelper;

    private int up_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_my_xiaomi);
        init();
    }

    private void init() {
        userHttpHelper = new UserHttpHelper(this, this);
        initgetIntent();
        initTitle();
        initClick();
        clickOne();
    }

    private void initShowDialog(int mVip1, int mNum1, int mNum2, String money) {
        View view = SDViewUtil.inflate(R.layout.bg_dialog_dist, null);
        builder = new AlertDialog.Builder(this).create();
        builder.setCanceledOnTouchOutside(false);
        builder.setView(view);
        builder.show();
        Window window = builder.getWindow();
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        Button bt_confirm = (Button) window.findViewById(R.id.bt_confirm);
        SDViewBinder.setTextView(tv_content, "当前您的青铜成员人数为" + mVip1 + "人，其中我的大掌柜  " + mNum1 + "人，我的店小二" + mNum2 + "人，全部升级后预计可提现金额为"
                + money + "元，点击成员手机号提醒该成员升级。");
        bt_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
                isShow = false;
            }
        });
    }

    private void initgetIntent() {
        isShow = getIntent().getBooleanExtra("yes", false);
        money = getIntent().getStringExtra("money");
//		up_name = getIntent().getStringExtra("up_name");
        up_id = getIntent().getIntExtra("up_id", -1);
//		if(up_id == -1){
//			up_name= "无";
//		}
    }

    private void initClick() {
        mTv_textOne.setOnClickListener(this);
        mTv_textTwo.setOnClickListener(this);
        mTv_stationName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_textOne) {
            mTv_textOne.setEnabled(false);
            mTv_textOne.setTextColor(getResources().getColor(R.color.main_color));
            mTv_textTwo.setEnabled(true);
            mTv_textTwo.setTextColor(getResources().getColor(R.color.text_fenxiao));
            type = 1;
            clickOne();
        } else if (v == mTv_textTwo) {
            mTv_textOne.setEnabled(true);
            mTv_textOne.setTextColor(getResources().getColor(R.color.text_fenxiao));
            mTv_textTwo.setEnabled(false);
            mTv_textTwo.setTextColor(getResources().getColor(R.color.main_color));
            type = 2;
            clickOne();
        } else if (v == mTv_stationName && up_id != -1) {
            cilckWeb();
        }
    }

    private void cilckWeb() {
        Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
        //跳转到"上线"的小店,所以需要"上线小店的id"
        intent.putExtra("id", up_id);
        startActivity(intent);
    }

    private void clickOne() {
        mTv_number.setText("");

        mDist = new MyDistFragment();
        mDist.setmListener(new OnDialogData() {
            @Override
            public void setData(int vip1, int num1, int num2, int total, String up_name, int up_id) {
                SDViewBinder.setTextView(mTv_number, String.valueOf(total), "0");
                SDViewBinder.setTextView(mTv_stationName, up_name);
                DistributionMyXiaoMiActivity.this.up_id = up_id;
                String money = DistributionMyXiaoMiActivity.this.money;
                if (isShow) {
                    initShowDialog(vip1, num1, num2, money);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        mDist.setArguments(bundle);
        getSDFragmentManager().toggle(R.id.act_my_xiaomi_fl, null, mDist);

        userHttpHelper.getMyDistributionCorps(type + "", "", 1, 10, "");
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("我的战队");
        mTitle.initRightItem(0);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ResultMyDistributionCorps> results;
    ResultMyDistributionCorps currResult;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.MY_DISTRIBUTION_CROPS.equals(method)) {
            results = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(results)) {
                        currResult = results.get(0);
                        mTv_number.setText(currResult.getTotal());
                        mTv_stationName.setText(currResult.getUp_name());
                        mDist.setResultMyDistributionCorps(currResult);
                    }
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}

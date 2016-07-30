package com.miguo.live.views.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/7/30.
 *
 * 主播的美化功能
 */
public class HostMeiToolView extends RelativeLayout implements IViewGroup, View.OnClickListener {

    private Context mContext;
    private CheckBox mCb_lighting;
    private ImageView mIv_meiyan;
    private ImageView mIv_meibai;
    private ImageView mIv_mei;
    private boolean isShow=true;

    public HostMeiToolView(Context context) {
        super(context);
        init(context);
    }

    public HostMeiToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HostMeiToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_mei_tool_view,this);
        mCb_lighting = ((CheckBox) this.findViewById(R.id.cb_lighting));
        mIv_meiyan = ((ImageView) this.findViewById(R.id.iv_meiyan));
        mIv_meibai = ((ImageView) this.findViewById(R.id.iv_meibai));
        mIv_mei = ((ImageView) this.findViewById(R.id.iv_mei));

        mCb_lighting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //默认为false
                    MGToast.showToast("关闭");

                }else {
                    //
                    MGToast.showToast("打开");
                }
            }
        });
        mCb_lighting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("哈哈哈");
            }
        });

        mIv_mei.setOnClickListener(this);
        mIv_meiyan.setOnClickListener(this);
        mIv_meibai.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {

    }

    /**
     * 隐藏按钮
     */
    public void hide(){
        if (mCb_lighting!=null && mIv_meibai!=null && mIv_meiyan!=null){
            mCb_lighting.setVisibility(View.INVISIBLE);
            mIv_meibai.setVisibility(View.INVISIBLE);
            mIv_meiyan.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示按钮
     */
    public void show(){
        if (mCb_lighting!=null && mIv_meibai!=null && mIv_meiyan!=null){
            mCb_lighting.setVisibility(View.VISIBLE);
            mIv_meibai.setVisibility(View.VISIBLE);
            mIv_meiyan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==mIv_meibai){
            clickMeiBai();
        }else if(v==mIv_meiyan){
            clickMeiYan();
        }else if (v==mIv_mei){
            clickMei();
        }
    }

    private void clickMei() {
        if (isShow){
            hide();
        }else {
            show();
        }
        isShow =!isShow;
    }

    /**
     * 美颜
     */
    private void clickMeiYan() {
        MGToast.showToast("美颜");
    }

    /**
     * 美白
     */
    private void clickMeiBai() {
        MGToast.showToast("美白");
    }
}

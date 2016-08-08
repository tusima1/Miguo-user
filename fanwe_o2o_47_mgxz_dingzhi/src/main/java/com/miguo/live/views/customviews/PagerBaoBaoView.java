package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.PagerBaoBaoAdapter;


/**
 * Created by didik on 2016/7/28.
 */
public class PagerBaoBaoView extends RelativeLayout implements View.OnClickListener {

    private ImageView mIv_img;//商品图片
    private TextView mTv_title;//标题
    private TextView mIv_title_append;//标题说明
    private TextView mGet;//获取按钮
    private RecyclerView mRecycler;
    private Context mContext;

    public PagerBaoBaoView(Context context) {
        super(context);
        init(context);
    }

    public PagerBaoBaoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagerBaoBaoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 入口
     */
    private void init(Context context) {
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.item_pager_baobao,this);

        mIv_img = ((ImageView) this.findViewById(R.id.iv_baobao));
        mTv_title = ((TextView) this.findViewById(R.id.tv_title));
        mIv_title_append = ((TextView) this.findViewById(R.id.tv_title_append));
        mGet = ((TextView) this.findViewById(R.id.get));
        mRecycler = ((RecyclerView) findViewById(R.id.bao_recyler));

        mGet.setOnClickListener(this);

        PagerBaoBaoAdapter mAdapter=new PagerBaoBaoAdapter(mContext);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
//        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v==mGet){
            clickGet();
        }
    }

    /**
     * 点击了获取按钮
     */
    private void clickGet() {
        Toast.makeText(mContext, "点击了获取", Toast.LENGTH_SHORT).show();
    }
}
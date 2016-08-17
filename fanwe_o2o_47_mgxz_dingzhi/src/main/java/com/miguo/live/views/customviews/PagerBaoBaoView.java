package com.miguo.live.views.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.base.CallbackView;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.pagermodel.BaoBaoEntity;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.miguo.utils.DisplayUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by didik on 2016/7/28.
 */
public class PagerBaoBaoView extends RelativeLayout implements View.OnClickListener,CallbackView {

//    private ImageView mIv_img;//商品图片
//    private TextView mTv_title;//标题
//    private TextView mIv_title_append;//标题说明
//    private TextView mGet;//获取按钮
    private RecyclerView mRecycler;
    private Context mContext;
    private  PagerBaoBaoAdapter mAdapter;
    private  List<BaoBaoEntity> baoBaoEntityList;
    private ShoppingCartHelper mShoppingCartHelper;


    public PagerBaoBaoView(Context context) {
        super(context);
        mShoppingCartHelper= new ShoppingCartHelper(context,this);
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

//        mIv_img = ((ImageView) this.findViewById(R.id.iv_baobao));
//        mTv_title = ((TextView) this.findViewById(R.id.tv_title));
//        mIv_title_append = ((TextView) this.findViewById(R.id.tv_title_append));
//        mGet = ((TextView) this.findViewById(R.id.get));
        mRecycler = ((RecyclerView) findViewById(R.id.bao_recyler));

      //  mGet.setOnClickListener(this);

        mAdapter=new PagerBaoBaoAdapter(mContext,mShoppingCartHelper);
        if(baoBaoEntityList!=null&&baoBaoEntityList.size()>0){
            mAdapter.setData(baoBaoEntityList);
            mAdapter.notifyDataSetChanged();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        final int offset = DisplayUtil.dp2px(mContext, 2);
        mRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView
                    .State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top=offset;
                outRect.bottom=offset;
            }
        });
//        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
//        if (v==mGet){
//            clickGet();
//        }
    }

    /**
     * 点击了获取按钮
     */
    private void clickGet() {

        Toast.makeText(mContext, "点击了获取", Toast.LENGTH_SHORT).show();
    }


    /**
     * 更新商品列表数据。
     */
    public void onRefreshData(){
        if(baoBaoEntityList!=null&&baoBaoEntityList.size()>0){
            mAdapter.setData(baoBaoEntityList);
        }
    }


    public void setBaoBaoEntityList(List<BaoBaoEntity> baoBaoEntityList) {
        this.baoBaoEntityList = baoBaoEntityList;
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }
}

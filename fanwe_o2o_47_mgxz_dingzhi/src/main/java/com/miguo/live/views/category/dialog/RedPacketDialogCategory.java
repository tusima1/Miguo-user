package com.miguo.live.views.category.dialog;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.RedNumGridAdapter;
import com.miguo.live.adapters.RedTypeAdapter;
import com.miguo.live.model.getHandOutRedPacket.ModelHandOutRedPacket;
import com.miguo.live.model.getHandOutRedPacket.ModelRedNum;
import com.miguo.live.views.dialog.BaseDialog;
import com.miguo.live.views.dialog.RedPacketDialog;
import com.miguo.live.views.listener.dialog.DialogListener;
import com.miguo.live.views.listener.dialog.RedPacketDialogListener;
import com.miguo.utils.DisplayUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class RedPacketDialogCategory extends DialogCategory{

    /**
     * 发送按钮。
     */
    private Button sendBtn;
    /**
     * gridView.
     */
    private RecyclerView mTypeGridView;
    private RecyclerView mNumGridView;
    /**
     * 手工输入红包数量
     */
    private EditText countEdit;
    /**
     * 全部红包。
     */
    private Button allBtn;
    /**
     * 删除按钮。
     */
    private Button cancelBtn;
    /**
     * 商铺名称
     */
    private TextView tvShopName;

    /**
     * 发送
     */
    Button btnSure;


    List<ModelHandOutRedPacket> redList;

    RedTypeAdapter redTypeAdapter;

    RedNumGridAdapter redNumGridAdapter;
    List<ModelRedNum> redNumDatas;



    public RedPacketDialogCategory(BaseDialog dialog) {
        super(dialog);
    }

    @Override
    protected void findViews() {
        btnSure = findViewsById(R.id.btn_submit_dialog_red);
        tvShopName = findViewsById(R.id.shop_name_dialog_live_red_packet);
        cancelBtn = findViewsById(R.id.cancel_btn);
        mTypeGridView = findViewsById(R.id.type_grid);
        mNumGridView = findViewsById(R.id.num_grid);
    }

    @Override
    protected DialogListener initListener() {
        return new RedPacketDialogListener(this);
    }

    @Override
    protected void setListener() {
        cancelBtn.setOnClickListener(listener);
        btnSure.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initRecyclers();
        initViewsData();
    }

    private void initRecyclers(){
        //GridLayout 3列
        GridLayoutManager mgr = new GridLayoutManager(getContext(), 2);
        mTypeGridView.setLayoutManager(mgr);
        final int dp12 = DisplayUtil.dp2px(getContext(), 12);
        final int dp5 = DisplayUtil.dp2px(getContext(), 5);
        final int dp7 = DisplayUtil.dp2px(getContext(), 7);
        mTypeGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                int childLayoutPosition = parent.getChildLayoutPosition(view);
                if (childLayoutPosition % 2 == 0) {
                    outRect.right = dp5;
                } else {
                    outRect.left = dp5;
                }
                outRect.top = dp12;
            }
        });

        //---设置数量gridview--
        mNumGridView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mNumGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                int childLayoutPosition = parent.getChildLayoutPosition(view);
                if (childLayoutPosition % 3 == 0) {
                    outRect.right = dp7;
                } else if (childLayoutPosition % 3 == 1) {
                    outRect.left = dp7;
                    outRect.right = dp7;
                } else {
                    //%3==2
                    outRect.left = dp7;
                }
                outRect.top = dp12;
            }
        });
        redNumDatas = new ArrayList<ModelRedNum>();
    }

    private void initViewsData(){
        tvShopName.setText(CurLiveInfo.modelShop.getShop_name());

        redList = new ArrayList<ModelHandOutRedPacket>();
        redTypeAdapter = new RedTypeAdapter(redList, getContext());
        //设置适配器
        mTypeGridView.setAdapter(redTypeAdapter);
        redNumGridAdapter = new RedNumGridAdapter(getContext(), redNumDatas);
        mNumGridView.setAdapter(redNumGridAdapter);
    }

    public void clickSure(){
        if(getDialog().getOnRedPacketClickListener() != null){
            getDialog().getOnRedPacketClickListener().clickSend();
        }
    }

    public void clickCancel(){
        if(getDialog().getOnRedPacketClickListener() != null){
            getDialog().getOnRedPacketClickListener().clickCancel();
        }
    }

    public interface OnRedPacketClickListener{
        void clickSend();
        void clickCancel();
    }

    @Override
    public RedPacketDialog getDialog() {
        return (RedPacketDialog) super.getDialog();
    }
}

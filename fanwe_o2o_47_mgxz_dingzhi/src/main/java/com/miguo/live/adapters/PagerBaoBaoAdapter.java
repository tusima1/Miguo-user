package com.miguo.live.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.pagermodel.BaoBaoEntity;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.List;


/**
 * Created by didik on 2016/7/28.
 */
public class PagerBaoBaoAdapter extends RecyclerView.Adapter<PagerBaoBaoAdapter.ViewHolder> {

    private Context mContext;
    private List<BaoBaoEntity> mData;
    private ShoppingCartHelper mShoppingCartHelper;

    /**
     * 构造函数确定填充数据
     */
    public PagerBaoBaoAdapter(Context mContext, ShoppingCartHelper mShoppingCartHelper)
    {
        this.mContext=mContext;
        this.mShoppingCartHelper = mShoppingCartHelper;

    }
    public PagerBaoBaoAdapter(Context mContext)
    {
        this.mContext=mContext;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout
                .item_pager_baobao_recycler, parent, false);
        ViewHolder holder=new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.ll_two_bt.setVisibility(View.VISIBLE);
        final BaoBaoEntity baoBaoEntity = mData.get(position);
        if(baoBaoEntity!=null){
            if(!TextUtils.isEmpty(baoBaoEntity.getImg())){
                ImageLoader.getInstance().displayImage(baoBaoEntity.getImg(),holder.iv_img);
            }else {
                holder.iv_img.setImageResource(R.drawable.list_empty);
            }
            holder.tv_title .setText(baoBaoEntity.getName());
            holder.tv_price.setText(baoBaoEntity.getTuan_price());


        }
        final boolean clicked = baoBaoEntity.isClicked();
        if (clicked){
            holder.add.setImageResource(R.drawable.ic_close_transparent);
            holder.ll_two_bt.setVisibility(View.VISIBLE);
        }else {
            holder.add.setImageResource(R.drawable.ic_add_transparent);
            holder.ll_two_bt.setVisibility(View.GONE);
        }
        //显示按钮
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(position).setClicked(!clicked);
                notifyItemChanged(position);
            }
        });


        holder.add2cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoodsToShoppingPacket(baoBaoEntity);

            }
        });
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickBuyGoods(baoBaoEntity);
            }
        });

    }

    /**
     * 设置数据,并刷新
     * @param data
     */
    public void setData(List<BaoBaoEntity> data){
        this.mData = data;
        notifyItemRangeChanged(0,getItemCount());

    }

    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }

    /**
     *  添加到购物车。
     * @param entity
     */
    public void addGoodsToShoppingPacket(final BaoBaoEntity entity){
        if(entity!=null){
            String roomId = CurLiveInfo.getRoomNum()+"";
            String fx_user_id = CurLiveInfo.getHostID();
            String lgn_user_id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
            String goods_id = entity.getId();
            String cart_type = "1";
            String add_goods_num = "1";
            if(mShoppingCartHelper!=null) {
                mShoppingCartHelper.addToShoppingCart(roomId, fx_user_id, lgn_user_id, goods_id, cart_type, add_goods_num);
            }
        }
    }

    /**
     * 立即购买。
     * @param entity
     */
    public void quickBuyGoods(final BaoBaoEntity entity){
        addGoodsToShoppingPacket(entity);
        Intent bintent = new Intent(mContext, ShopCartActivity.class);
        mContext.startActivity(bintent);

    }

    public ShoppingCartHelper getmShoppingCartHelper() {
        return mShoppingCartHelper;
    }

    public void setmShoppingCartHelper(ShoppingCartHelper mShoppingCartHelper) {
        this.mShoppingCartHelper = mShoppingCartHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView add2cart;
        public TextView buy;
        public ImageView iv_img;
        public TextView tv_title;
        public TextView tv_price;
        public TextView tv_tag;
        public ImageView add;

        public LinearLayout ll_two_bt;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = ((ImageView) itemView.findViewById(R.id.iv_img));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_price = ((TextView) itemView.findViewById(R.id.tv_price));
            tv_tag = ((TextView) itemView.findViewById(R.id.tv_tag));
            add = ((ImageView) itemView.findViewById(R.id.add));

            ll_two_bt=(LinearLayout)itemView.findViewById(R.id.ll_two_bt);
            add2cart = ((TextView) itemView.findViewById(R.id.add2cart));
            buy = ((TextView) itemView.findViewById(R.id.buy));
        }
    }
}

package com.miguo.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;


/**
 * Created by didik on 2016/7/28.
 */
public class PagerBaoBaoAdapter extends RecyclerView.Adapter<PagerBaoBaoAdapter.ViewHolder> {

    private Context mContext;

    /**
     * 构造函数确定填充数据
     */
    public PagerBaoBaoAdapter(Context mContext){
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ll_two_bt.setVisibility(View.VISIBLE);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ll_two_bt.getVisibility()==View.GONE){
                    holder.ll_two_bt.setVisibility(View.VISIBLE);
                }else {
                    holder.ll_two_bt.setVisibility(View.GONE);
                }

            }
        });
        holder.add2cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "立即购买", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 100;
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
            buy = ((TextView) itemView.findViewById(R.id.add2cart));
        }
    }
}

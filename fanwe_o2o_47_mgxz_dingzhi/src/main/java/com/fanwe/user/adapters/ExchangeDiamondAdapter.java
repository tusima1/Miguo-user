package com.fanwe.user.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.wallet.Convert_list;
import com.fanwe.user.model.wallet.ExchangeListModel;

import java.util.List;

/**
 * 兑换果钻adapter.
 * Created by zhouhy on 2016/11/23.
 */

public class ExchangeDiamondAdapter extends RecyclerView.Adapter<ExchangeDiamondAdapter.MyViewHolder> {

    private List<Convert_list> mDatas;

    private Context context ;

    private ExchangeListener exchangeListener;
    public ExchangeDiamondAdapter(Context context){
        this.context = context;

    }
    @Override
    public ExchangeDiamondAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exchangediamond, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ExchangeDiamondAdapter.MyViewHolder holder, int position) {
        final Convert_list model = mDatas.get(position);
        holder.diamond.setText(model.getDiamond());
        holder.bean.setText(model.getBean());
        Resources resources=context.getResources();
        Drawable drawable =null;
        if(model.isExchangeAble()){
            drawable =resources.getDrawable(R.drawable.black_30shape);
            holder.bean_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exchangeListener.doExchange(model);
                }
            });
        }else{
            drawable =resources.getDrawable(R.drawable.gray_30shape);
        }
        holder.bean_lay.setBackground(drawable);


    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public List<Convert_list> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<Convert_list> mDatas) {
        this.mDatas = mDatas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView diamond;
        TextView bean;
        RelativeLayout bean_lay;

        public MyViewHolder(View view)
        {
            super(view);
            diamond = (TextView) view.findViewById(R.id.diamond);
            bean = (TextView) view.findViewById(R.id.bean);
            bean_lay = (RelativeLayout) view.findViewById(R.id.bean_lay);
        }
    }

    public interface ExchangeListener{
        public void doExchange(Convert_list data);
    }

    public ExchangeListener getExchangeListener() {
        return exchangeListener;
    }

    public void setExchangeListener(ExchangeListener exchangeListener) {
        this.exchangeListener = exchangeListener;
    }
}

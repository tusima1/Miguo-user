package com.fanwe.user.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.wallet.ModelWalletNew;

import java.util.List;

/**
 * 钱包页面adapter
 * Created by zhouhy on 2016/11/14.
 */
public class WalletAdapter extends BaseAdapter {
    private Activity mContext;

    private List<ModelWalletNew> walletList;
    /**
     * 负责处理各行的点击响应事件 。
     */
    private  WalletLineOnClickListener onClickListener;



    public WalletAdapter(Activity mContext,WalletLineOnClickListener onClickListener,List<ModelWalletNew> walletList) {
        this.mContext =mContext;
        this.onClickListener = onClickListener;
        this.walletList = walletList;
    }
    @Override
    public int getCount() {
        return walletList==null?0:walletList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ModelWalletNew modelWalletNew = walletList.get(position);
        if(modelWalletNew==null){
            return null;
        }
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_line,null);
            holder.wallet_line = (RelativeLayout)convertView.findViewById(R.id.wallet_line);
            holder.icon= (ImageView) convertView.findViewById(R.id.icon);
            holder.title= (TextView) convertView.findViewById(R.id.title);
            holder.value= (TextView) convertView.findViewById(R.id.money_value);
            holder.unit=(TextView) convertView.findViewById(R.id.unit);

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.icon.setImageResource(modelWalletNew.getIcon());
        holder.title.setText(modelWalletNew.getTitle());
        holder.value.setText(modelWalletNew.getValue());
        holder.unit.setText(modelWalletNew.getUnit());
        holder.wallet_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v,position);
            }
        });

        return convertView;

    }


    private class ViewHolder{
        public RelativeLayout wallet_line;
        public ImageView icon;
        public TextView title;
        public TextView value;
        public TextView unit;

    }

    public interface WalletLineOnClickListener{
        void onClick(View v,int position);
    }

    public List<ModelWalletNew> getWalletList() {
        return walletList;
    }

    public void setWalletList(List<ModelWalletNew> walletList) {
        this.walletList = walletList;
    }
}

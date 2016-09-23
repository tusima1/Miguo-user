package com.fanwe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;

import java.util.List;

/**
 * 支付方式adapter.
 * Created by Administrator on 2016/9/23.
 */
public class PaymentAdapter extends BaseAdapter {


    private boolean singleCheck;//为true可选

    List<PaymentTypeInfo> mData;

    private String currentPaymentId="";
    private PaymentTypeChangeListener paymentTypeChangeListener;

    public PaymentAdapter(List<PaymentTypeInfo> mData, boolean singleCheck) {
        currentPaymentId = "";
        this.mData = mData;
        this.singleCheck=singleCheck;
    }

    /**
     * 更新数据
     * @param mData PaymentTypeInfo
     */
    public void update(List<PaymentTypeInfo> mData){
        currentPaymentId = "";
        this.mData=mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData==null?0:mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_list,null);
            holder.iv_image = ((ImageView) convertView.findViewById(R.id.iv_iamge));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
            holder.iv_selected = ((ImageView) convertView.findViewById(R.id.iv_selected));

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //bind data
        final PaymentTypeInfo paymentTypeInfo = mData.get(position);
         boolean check = paymentTypeInfo.isChecked();
        SDViewBinder.setImageView(holder.iv_image, paymentTypeInfo.getLogo());
        holder.tv_name.setText(paymentTypeInfo.getName());
        //选中。
        if (check){
            currentPaymentId = paymentTypeInfo.getId();
            holder.iv_selected.setImageResource(R.drawable.ic_payment_selected);
          }else{
            holder.iv_selected.setImageResource(R.drawable.ic_payment_normal);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCheckModel(paymentTypeInfo);
                paymentTypeChangeListener.onPaymentChange(paymentTypeInfo);

            }
        });
        return convertView;
    }

    private void changeCheckModel(PaymentTypeInfo paymentTypeInfo ){
        for(int i = 0 ; i < mData.size();i++){
            PaymentTypeInfo entity = mData.get(i);
            if(entity.getId().equals(paymentTypeInfo.getId())){
                entity.setChecked(!entity.isChecked());
            }else{
                entity.setChecked(false);
            }
        }
        if(paymentTypeInfo.isChecked()){
            currentPaymentId = paymentTypeInfo.getId();
        }else{
            currentPaymentId="";
        }
        notifyDataSetChanged();

    }

    public interface PaymentTypeChangeListener
    {
        public void onPaymentChange(PaymentTypeInfo model);
    }


    private class ViewHolder{
        public ImageView iv_image;//有效期
        public TextView tv_name;//类型(专属优惠)
        public ImageView iv_selected;//标题

    }

    public PaymentTypeChangeListener getPaymentTypeChangeListener() {
        return paymentTypeChangeListener;
    }

    public void setPaymentTypeChangeListener(PaymentTypeChangeListener paymentTypeChangeListener) {
        this.paymentTypeChangeListener = paymentTypeChangeListener;
    }

    public String getCurrentPaymentId() {
        return currentPaymentId;
    }

    public void setCurrentPaymentId(String currentPaymentId) {
        this.currentPaymentId = currentPaymentId;
    }
}

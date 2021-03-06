package com.miguo.ui.view.floatdropdown.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.entity.SingleMode;
import com.miguo.ui.view.floatdropdown.interf.OnSingleModeRVItemClickListener;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class RightListAdapter extends RecyclerView.Adapter<RightListAdapter.ViewHolder> {

    private List<SingleMode> singleModes;
    private OnSingleModeRVItemClickListener itemClickListener;
    private final int UnKnowPosition = -2;
    private int preClickPosition = UnKnowPosition;
    public final int checkedTextColor;
    public final int uncheckedTextColor;
    public final int uncheckedDividerColor;

    public RightListAdapter(List<SingleMode> singleModes) {
        this.singleModes=singleModes;
        checkedTextColor = Color.parseColor("#F5B830");
        uncheckedTextColor=Color.parseColor("#999999");
        uncheckedDividerColor=Color.parseColor("#A1A2A5");
    }

    public void updateData(List<SingleMode> newSingleModes){
        boolean changed=false;
        for (int i = 0; i < newSingleModes.size(); i++) {
            if (newSingleModes.get(i).isChecked()){
                preClickPosition=i;
                changed=true;
            }
        }
        if (!changed){
            preClickPosition = UnKnowPosition;
        }

        singleModes = newSingleModes;
        notifyDataSetChanged();
    }

    public void setData(List<SingleMode> singleModes){
        this.singleModes=singleModes;
    }

    /**
     * 代替点击某个item,代替 {@link View#performClick()}
     * @param handlePosition 位置
     */
    public void performPosition(int handlePosition){
        if (singleModes ==null ){
            Log.e("test","请先调用setData()方法,目前数据为null!");
            return;
        }
        int size = singleModes.size();
        if (size-1 <handlePosition ){
            Log.e("test","performPosition(int handlePosition)方法,position out of bound!--> except position: "+handlePosition);
            return;
        }
        SingleMode singleMode = singleModes.get(handlePosition);
        boolean checked = singleMode.isChecked();
        if (checked){
            Log.e("test","performPosition(int handlePosition)方法,The Item has already checked");
            return;
        }
        singleMode.setChecked(true);
        notifyItemChanged(handlePosition);
        if (preClickPosition == handlePosition){
            //Note: already marked,Do Not Mark Again.
            return;
        }
        if (preClickPosition !=UnKnowPosition && preClickPosition < size){
            singleModes.get(preClickPosition).setChecked(false);
            notifyItemChanged(preClickPosition);
        }
        preClickPosition = handlePosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_right,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SingleMode singleMode = singleModes.get(position);
        final boolean checked = singleMode.isChecked();
        toggle(checked,holder.tv_name,holder.divider);
        holder.tv_name.setText(singleMode.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked){
                    return;
                }
                singleMode.setChecked(true);
                int adapterCurPosition = holder.getAdapterPosition();
                notifyItemChanged(adapterCurPosition);
                if (preClickPosition !=UnKnowPosition){
                    singleModes.get(preClickPosition).setChecked(false);
                    notifyItemChanged(preClickPosition);
                }
                preClickPosition = adapterCurPosition;
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(v,holder.getAdapterPosition(),singleMode);
                }
            }
        });
    }
    private void toggle(boolean isCheck,TextView tv_name,View divider){
        if (isCheck){
            tv_name.setTextColor(checkedTextColor);
            divider.setBackgroundColor(checkedTextColor);
        }else {
            tv_name.setTextColor(uncheckedTextColor);
            divider.setBackgroundColor(uncheckedDividerColor);
        }
    }

    @Override
    public int getItemCount() {
        return singleModes ==null ? 0 :singleModes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public View divider;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    public void setOnItemClickListener(OnSingleModeRVItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}

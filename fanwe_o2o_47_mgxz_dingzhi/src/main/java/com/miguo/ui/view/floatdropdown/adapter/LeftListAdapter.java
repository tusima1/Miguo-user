package com.miguo.ui.view.floatdropdown.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.entity.TwoMode;
import com.miguo.ui.view.floatdropdown.interf.OnLeftRVItemClickListener;

import java.util.List;


/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class LeftListAdapter extends RecyclerView.Adapter<LeftListAdapter.ViewHolder> {
    private List<TwoMode> twoModes;
    private OnLeftRVItemClickListener itemClickListener;
    private int preClickPosition = -2;
    public final int checkedBgColor= Color.WHITE;
    public final int uncheckedBgColor;

    public LeftListAdapter(List<TwoMode> twoModes) {
        this.twoModes = twoModes;
        uncheckedBgColor = Color.parseColor("#EEEEEE");
    }

    private void toggle(boolean isCheck,View layout){
        if (isCheck){
            layout.setBackgroundColor(checkedBgColor);
        }else {
            layout.setBackgroundColor(uncheckedBgColor);
        }
    }

    public int getClickPosition() {
        return preClickPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_left,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TwoMode twoMode = twoModes.get(position);
        final boolean checked = twoMode.isChecked();
        toggle(checked,holder.fl_bg);
        holder.tv_name.setText(twoMode.getName());
        holder.fl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked){
                    return;
                }
                twoMode.setChecked(true);
                int adapterCurPosition = holder.getAdapterPosition();
                notifyItemChanged(adapterCurPosition);
                if (preClickPosition !=-2){
                    twoModes.get(preClickPosition).setChecked(false);
                    notifyItemChanged(preClickPosition);
                }
                preClickPosition = adapterCurPosition;
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(v,holder.getAdapterPosition(),twoMode.getSingleModeList());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return twoModes == null ? 0 : twoModes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public View fl_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            fl_bg = itemView.findViewById(R.id.fl);
        }
    }

    public void setOnItemClickListener(OnLeftRVItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}

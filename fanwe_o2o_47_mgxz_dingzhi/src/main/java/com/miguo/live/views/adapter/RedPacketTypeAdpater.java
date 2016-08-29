package com.miguo.live.views.adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.getHandOutRedPacket.ModelHandOutRedPacket;
import com.miguo.live.views.listener.Listener;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class RedPacketTypeAdpater extends HijasonBaseRecyclerAdapter{

    public RedPacketTypeAdpater(AppCompatActivity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return null;
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.red_packet_item, null);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected Listener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return null;
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, Listener listener) {

    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        ModelHandOutRedPacket modelHandOutRedPacket = getItem(position);
        String countTextValue = "";
        String count = modelHandOutRedPacket.getRed_packets();
        String type = modelHandOutRedPacket.getRed_packet_type();
        String amount = modelHandOutRedPacket.getRed_packet_amount();
        if ("1".equals(type)) {
            getHolder(holder).typeText.setText(amount + "折");
        } else if ("2".equals(type)) {
            getHolder(holder).typeText.setText(amount + "元");
        } else {
            return;
        }
        if (Integer.valueOf(count) > 999) {
            countTextValue = "余(999+)";
        } else {
            countTextValue = "余(" + count + "+)";
        }
        getHolder(holder).countText.setText(countTextValue);
        if (modelHandOutRedPacket.isChecked()) {
            //橙色
            getHolder(holder).itemView.setBackgroundResource(R.drawable.bg_orange_small);
            getHolder(holder).typeText.setTextColor(getColor(R.color.white));
            getHolder(holder).countText.setTextColor(getColor(R.color.white));
        } else {
            //白色
            getHolder(holder).itemView.setBackgroundResource(R.drawable.shape_cricle_bg_white_gray);
            getHolder(holder).typeText.setTextColor(getColor(R.color.text_line));
            getHolder(holder).countText.setTextColor(getColor(R.color.text_line));
        }
        if (Integer.valueOf(count) <= 0) {
            getHolder(holder).itemView.setBackgroundColor(Color.GRAY);
        }
    }

    public ModelHandOutRedPacket getItem(int position){
        return (ModelHandOutRedPacket)super.getItem(position);
    }

    public ViewHolder getHolder(RecyclerView.ViewHolder holder){
        return (ViewHolder)holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        @ViewInject(R.id.red_line)
        public FrameLayout red_line;
        @ViewInject(R.id.type_text)
        public TextView typeText;
        @ViewInject(R.id.count_text)
        public TextView countText;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

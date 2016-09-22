package com.miguo.live.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListenerRedNum;

public class ViewHolderRedNum extends ViewHolder implements OnClickListener {

    public TextView tv_num;
    private MyItemClickListenerRedNum mListener;

    public ViewHolderRedNum(View view, MyItemClickListenerRedNum listener) {
        super(view);

        tv_num = (TextView) itemView.findViewById(R.id.tv_num_red_num);
        this.mListener = listener;
        view.setOnClickListener(this);
    }

    /**
     * �������
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClickNum(v, getAdapterPosition());
        }
    }

}

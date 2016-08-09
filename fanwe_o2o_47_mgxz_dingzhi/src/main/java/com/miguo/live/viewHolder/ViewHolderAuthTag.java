package com.miguo.live.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListener;

public class ViewHolderAuthTag extends ViewHolder implements OnClickListener {

    public TextView tvTag, tvChange;
    private MyItemClickListener mListener;

    public ViewHolderAuthTag(View view, MyItemClickListener listener) {
        super(view);
        tvTag = (TextView) view.findViewById(R.id.tv_tag_item_auth_tag);
        tvChange = (TextView) view.findViewById(R.id.tv_change_tag_item_auth_tag);
        this.mListener = listener;
        view.setOnClickListener(this);
    }

    /**
     * �������
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getPosition());
        }
    }

}

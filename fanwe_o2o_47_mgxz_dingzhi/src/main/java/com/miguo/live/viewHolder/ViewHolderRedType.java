package com.miguo.live.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListenerRedType;

public class ViewHolderRedType extends ViewHolder implements OnClickListener {

    public RelativeLayout red_line;
    public TextView typeText;
    public TextView countText;
    private MyItemClickListenerRedType mListener;

    public ViewHolderRedType(View view, MyItemClickListenerRedType listener) {
        super(view);

        red_line = (RelativeLayout) view.findViewById(R.id.red_line);
        typeText = (TextView) view.findViewById(R.id.type_text);
        countText = (TextView) view.findViewById(R.id.count_text);

        this.mListener = listener;
        view.setOnClickListener(this);
    }

    /**
     * �������
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClickType(v, getPosition());
        }
    }

}

package com.fanwe.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;

/**
 * Created by didik 
 * Created time 2016/12/15
 * Description: 
 */

public class PopTipShare extends PopupWindow {

    private final Context context;
    private final View anchor;
    private int rightMargin;
    private int topMargin;

    public PopTipShare(Context context,View anchor) {
        super(context);
        this.context = context;
        this.anchor=anchor;
        initPop();
    }

    private void initPop() {
        if (!checkView()){
            Log.e("test","view is null!");
            return;
        }
        View popLayout = LayoutInflater.from(context).inflate(R.layout.layout_pop_share_show, null,
                false);
        rightMargin = DisplayUtil.dp2px(context, 24);
        topMargin = DisplayUtil.dp2px(context, 10);
        this.setContentView(popLayout);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(false);
    }

    private boolean checkView() {
        return (context!=null && anchor!=null);
    }

    public void show(){
        this.showAsDropDown(anchor,-rightMargin, -topMargin);
    }
}

package com.miguo.ui.view.floatdropdown.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by didik 
 * Created time 2017/1/9
 * Description: 
 */

public class SearchItemDecoration extends RecyclerView.ItemDecoration {
    private int hOffset;//6.5dp
    private int vOffset;//15dp

    public SearchItemDecoration(int h,int v) {
        this.hOffset = h;
        this.vOffset = v;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childPosition = parent.getChildAdapterPosition(view);
        int order = childPosition % 3;
        outRect.top=0;
        outRect.bottom=vOffset;
        if (order == 0){
            outRect.left=0;
            outRect.right=hOffset;
            return;
        }
        if (order == 1){
            outRect.left=hOffset;
            outRect.right=hOffset;
            return;
        }
        if (order == 2){
            outRect.left=hOffset;
            outRect.right=0;
            return;
        }

    }
}

package com.miguo.live.views.Manager;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanwe.app.App;
import com.miguo.utils.DisplayUtil;

/**
 * Created by didik on 2016/9/11.
 */
public class GiftGridItemDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        outRect.top=offset;
    }

    public void setOffset(float dp){
        int offset = DisplayUtil.dp2px(App.getApplication().getApplicationContext(), dp);
        this.offset=offset;
    }
}

package com.miguo.live.views.customviews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanwe.app.App;
import com.miguo.utils.DisplayUtil;

/**
 * Created by didik on 2016/8/5.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int dp) {
        this.space = DisplayUtil.dp2px(App.getApplication().getApplicationContext(), dp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        outRect.left = space;
        outRect.right = space;
    }
}

package com.fanwe.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 *
 * 判断滑动置顶加载更多的RecyclerView
 */
public class LoadMoreRecyclerView extends RecyclerView {

    int lastVisibleItem = 1;
    int firstVisibleItem = 0;
    int top = 0;

    private boolean isLoading;
    private OnRefreshEndListener mEndListener;
    private OnRecyclerViewScrollListener onRecyclerViewScrollListener;
    String tag = "LoadMoreRecyclerView";

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /**
         * 新版本的recycleview不支持此方法
         */
//        setScrollbarFadingEnabled(false);
        isLoading = false;
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }


    /**
     * dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
     * @param dx
     * @param dy
     */
    @Override
    public void onScrolled(int dx, int dy) {
        if(onRecyclerViewScrollListener != null){
            onRecyclerViewScrollListener.onScrolled(dx, dy);
        }
        if (!isLoading) {
//        if (isLoading) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                linearLayoutScrolled(dy);
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutScrolled(dy);
            }
        }
    }

    private void linearLayoutScrolled(int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager != null && layoutManager != null) {
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
//            LogUtil.d(tag, "dy: " + dy + " ,last visibleItem: " + lastVisibleItem + " total item count: " + totalItemCount);
            this.lastVisibleItem = lastVisibleItem;
            this.firstVisibleItem = firstVisibleItem;
//                Log.d(tag, " ,top: " + getChildAt(0).getTop() + " firstVisibleItem : " + firstVisibleItem);
            if(lastVisibleItem == 1){
                this.top = getChildAt(0).getTop();
                if(top == 0 && mEndListener != null){
                    mEndListener.onMoveTop();
                }
//                LogUtil.d(tag, " ,top: " + getChildAt(0).getTop());
            }
            /**
             * 加载更多
             */
            if (lastVisibleItem + 1 >= totalItemCount && dy > 0) {
                isLoading = true;
                if (mEndListener != null) {
                    mEndListener.onLoadmore();
                }
            }
        }
    }

    private void StaggeredGridLayoutScrolled(int dy) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
        if (layoutManager != null && layoutManager != null) {

            int[] lastItemPositions = layoutManager.findLastCompletelyVisibleItemPositions(null);

            int lastVisibleItem = lastItemPositions[lastItemPositions.length - 1];
            int totalItemCount = layoutManager.getItemCount();

            if (lastVisibleItem + 1 >= totalItemCount && dy > 0) {
                isLoading = false;
                if (mEndListener != null) {
                    mEndListener.onLoadmore();
                }
            }
        }
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean boo) {
        this.isLoading = boo;
    }

    public void setOnRefreshEndListener(OnRefreshEndListener endListener) {
        mEndListener = endListener;
    }

    public interface OnRefreshEndListener {
        void onLoadmore();
        void onMoveTop();
    }

    public interface OnRecyclerViewScrollListener{
        /**
         * dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
         *
         * @param dx
         * @param dy
         */
        void onScrolled(int dx, int dy);
    }

    public boolean isRefreshAble(){
        return firstVisibleItem == 0 && top == 0 && isLoading();
    }

    public void loadComplete(){
        setLoading(true);
    }


    public void addOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        this.onRecyclerViewScrollListener = onRecyclerViewScrollListener;
    }
}

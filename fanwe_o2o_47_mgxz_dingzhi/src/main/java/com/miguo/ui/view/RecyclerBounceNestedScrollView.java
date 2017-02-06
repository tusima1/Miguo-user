package com.miguo.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.BaseUtils;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 狗蛋哥/zlh on 16/4/13.
 */
public class RecyclerBounceNestedScrollView extends NestedScrollView{

    ImageView refreshView;
    TextView notice;

    private LinearLayout child;
    private LinearLayout endLayout;

    private float y;// 点击时y坐标

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算
    private boolean isMoveBegin = false; //是否开始计算移动
    int childTop;
    int childBottom;

    RecyclerScrollViewOnTouchListener recyclerScrollViewOnTouchListener;

    OnRecyclerScrollViewListener onHomeScrollViewListener;
    boolean isLoading = false;

    float startDownY;
//    float distance;

    int currentTop = 0;
    int downX = 0;
    int downY = 0;
    int touchSlop = 0;
    int moveY = 0;
    int halfScreenHeight = 0;
    int screenHeight = 0;
    int downOffsetY = 0;
    String tag = this.getClass().getSimpleName();
    /**
     * 记录按下的时间（防止到底部判断是否要移动的时候按下就移动了）
     */
    long downTime;

    static final int LOAD_MORE = 0x0010;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_MORE:
                    handleMessageScrollToEnd();
                    break;
            }
            return false;
        }
    });

    public RecyclerBounceNestedScrollView(Context context) {
        super(context);
    }

    public RecyclerBounceNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.halfScreenHeight = BaseUtils.getHeight(getContext()) / 4;
//        this.halfScreenHeight = 0;
        this.screenHeight = BaseUtils.getHeight(getContext());
        setVerticalScrollBarEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        if(null != getChildAt(0)){
            child = (LinearLayout) getChildAt(0);
            child.addView(loadEndView());
        }
        super.onFinishInflate();
    }

    private View loadEndView(){
        endLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams endLayoutParams = getLineaLayoutParams(martchParent(), dip2px(40));
        endLayout.setLayoutParams(endLayoutParams);
        endLayout.setGravity(Gravity.CENTER);
        endLayout.setOrientation(LinearLayout.HORIZONTAL);

        refreshView = new ImageView(getContext());
        LinearLayout.LayoutParams refreshViewParams = getLineaLayoutParams(dip2px(15), dip2px(15));
        refreshView.setLayoutParams(refreshViewParams);
        refreshView.setImageResource(R.drawable.refresh);
        endLayout.addView(refreshView);

        notice = new TextView(getContext());
        LinearLayout.LayoutParams noticeParams = getLineaLayoutParams(wrapContent(), wrapContent());
        noticeParams.setMargins(dip2px(5), 0, 0, 0);
        notice.setLayoutParams(noticeParams);
        notice.setTextSize(14);
        notice.setText("正在加载...");
        notice.setTextColor(getColor(R.color.text_base_color));
        endLayout.addView(notice);
        return endLayout;
    }

    public void showLoadingLayout(){
        endLayout.setVisibility(View.VISIBLE);
    }

    public void hideLoadingLayout(){
        endLayout.setVisibility(View.GONE);
    }

    private void startRefreshAnimation(){
        int count = 1000;
        RotateAnimation animation = new RotateAnimation(0, 360f * count, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(800 * count);
        animation.setInterpolator(new LinearInterpolator());
        animation.setStartOffset(50);
        setRefreshText("正在加载...");
        refreshView.startAnimation(animation);
    }

    private void setRefreshText(final String text){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notice.setText(text);
                LinearLayout.LayoutParams params = getLineaLayoutParams(wrapContent(), wrapContent());
                params.setMargins(dip2px(5), 0, 0, 0);
                notice.setLayoutParams(params);
                endLayout.invalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.d(tag, "recycler scroll view action down..");
                break;
            case MotionEvent.ACTION_MOVE:
                if(getRecyclerScrollViewOnTouchListener() != null){
                    getParent().requestDisallowInterceptTouchEvent(false);
                    getRecyclerScrollViewOnTouchListener().onActionMove(ev);
                }
//                Log.d(tag, "recycler scroll view action move..");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(getRecyclerScrollViewOnTouchListener() != null){
                    getParent().requestDisallowInterceptTouchEvent(false);
                    getRecyclerScrollViewOnTouchListener().onActionCancel(ev);
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        if (child != null) {
            commOnTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.downOffsetY = getScrollY();
                this.downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                // 手指松开
                if (isNeedAnimation()) {
                    animation();
                    isCount = false;
                    handleBeginMoveOut();
                }
                break;
            /***
             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!isCount) {
                    startDownY = nowY;
                    deltaY = 0; // 在这里要归0.
                    Log.d(tag, "deltaY: " + deltaY + " child top: " + child.getTop() + " ,cut: " + (child.getTop() - deltaY) + " child bottom: " + child.getBottom());
                }

//                distance = nowY - startDownY;
//                LogUtil.d(tag, "deltaY: " + deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if(!isMoveBegin){
                        childTop = child.getTop();
                        childBottom = child.getMeasuredHeight();
                    }
                    // 初始化头部矩形
//                    if (normal.isEmpty()) {
//                        // 保存正常的布局位置
//                        normal.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
//                    }
//                    // 移动布局
//                    if(Math.abs(deltaY) < 30){
//                        Log.d(tag, "deltaY: " + deltaY + " child top 1: " + child.getTop() + " child top 2 : " + childTop  + " child bottom: " + child.getBottom() + " child bottom 2: " + childBottom + " messure height: " + child.getMeasuredHeight());
////                        deltaY = Math.abs(deltaY);
//                        childTop = childTop - deltaY / 2;
//                        childBottom = childBottom - deltaY / 2;
//                        child.layout(child.getLeft(), childTop,child.getRight(), child.getBottom() - deltaY / 2);
//                    }
                }
                isCount = true;
                isMoveBegin = true;
                break;

            default:
                break;
        }
    }

    private void handleBeginMoveOut(){
        isMoveBegin = false;
//        childTop = 0;
//        childBottom = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)ev.getRawX();
                downY = (int)ev.getRawY();
                moveY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int)ev.getRawY();
                if(Math.abs(moveY - downY) > touchSlop){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(Math.abs(moveY - downY) > touchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /***
     * 回缩动画
     */
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, childTop,
                normal.top);
        ta.setDuration(200);
        child.startAnimation(ta);
        // 设置回到正常的布局位置
        child.layout(normal.left, normal.top, normal.right, normal.bottom);

        normal.setEmpty();
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onScrollToEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的总高度
     *
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    public boolean isNeedMove() {
        int offset = child.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY() + endLayout.getMeasuredHeight();
        // 0是顶部，后面那个是底部
//        return scrollY == 0 || scrollY == offset;
        Log.d(tag, "scrollY: " + scrollY + " offset: " + offset);
        return scrollY > offset && startDownY   != getScrollY();
    }

    public interface RecyclerScrollViewOnTouchListener{
        void onActionMove(MotionEvent ev);
        void onActionCancel(MotionEvent ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(onHomeScrollViewListener != null){
            onHomeScrollViewListener.onScrollChanged(l, t , oldl, oldt);
        }
        if(!isLoading()){
            /**
             * 判断滑动距离是否需要加载更多
             */
            Log.d(tag, "verticalOffset: " + t + " ,get messure height: " + getMeasuredHeight() + " child 0 messure height: " + getChildAt(0).getMeasuredHeight());
            if((t + 0 + getMeasuredHeight()) +  endLayout.getMeasuredHeight() > getChildAt(0).getMeasuredHeight()){
                onScrollToEnd();
            }
        }
        setCurrentTop(t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void onScrollToEnd(){
        if(isLoading()){
            return;
        }
        if(onHomeScrollViewListener != null){
            startRefreshAnimation();
            setIsLoading(true);
            handleScrollToEnd();
        }
    }

    private void handleScrollToEnd(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(LOAD_MORE);
            }
        };
        timer.schedule(task, 500);
    }

    private void handleMessageScrollToEnd(){
        endLayout.setVisibility(View.VISIBLE);
        onHomeScrollViewListener.onScrollToEnd();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public interface OnRecyclerScrollViewListener {
        void onScrollToEnd();
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void loadComplite(){
        setIsLoading(false);
        stopRefreshAnimation();
    }

    public void loadCompliteWithNoData(){
        loadComplite();
        setRefreshText("加载完成，无更多数据");
    }

    public void loadCompliteWithError(){
        loadComplite();
        setRefreshText("加载失败，请重试！");
    }

    private void stopRefreshAnimation(){
        if(null != refreshView && null != refreshView.getAnimation()){
            refreshView.getAnimation().cancel();
        }
    }

    public void setOnRecyclerScrollViewListener(OnRecyclerScrollViewListener onHomeScrollViewListener) {
        this.onHomeScrollViewListener = onHomeScrollViewListener;
    }


    private LinearLayout.LayoutParams getLineaLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    private int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    private int martchParent(){
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    private int wrapContent(){
        return LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    private int dip2px(int dp){
        return BaseUtils.dip2px(getContext(), dp);
    }


    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public int getCurrentTop() {
        return currentTop;
    }

    public void setCurrentTop(int currentTop) {
        this.currentTop = currentTop;
    }

    public boolean canRefresh(){
        return getCurrentTop() == 0;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public RecyclerScrollViewOnTouchListener getRecyclerScrollViewOnTouchListener() {
        return recyclerScrollViewOnTouchListener;
    }

    public void setRecyclerScrollViewOnTouchListener(RecyclerScrollViewOnTouchListener recyclerScrollViewOnTouchListener) {
        this.recyclerScrollViewOnTouchListener = recyclerScrollViewOnTouchListener;
    }

    public Activity getActivity(){
        return (Activity)getContext();
    }
}

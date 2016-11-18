package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.adapter.base.BaseRVViewHolder;
import com.miguo.adapter.base.SimpleRVAdapter;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.interf.ExpandListener;
import com.miguo.ui.view.interf.ExpandStatus;
import com.miguo.ui.view.interf.Expandable;
import com.miguo.ui.view.interf.IActGuideLayout;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.MGLog;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/11/16.
 */

public class ActGuideLayout extends LinearLayout implements Expandable,IActGuideLayout, View
        .OnClickListener {
    private int minHeight=0;
    private int maxHeight=0;
    private final Context context;
    private View ll_tools;
    private ImageView iv_star;
    private ImageView iv_share;
    private LinearLayout ll_root;//root
    private LinearLayout ll_text_container;//文字
    private LinearLayout ll_dot_container;//小点点
    private FrameLayout fl_rv_container;//rv
    private ExpandListener expandListener;
    private boolean expandable=false;
    private ActLayoutReceiveDataListener sendDataListener;
    private RecyclerView parentRV;
//    private RecyclerView mRVInner;

    public ActGuideLayout(Context context) {
        this(context,null);
    }

    public ActGuideLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ActGuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        startFlow();
    }

    private void startFlow() {
        LayoutInflater.from(context).inflate(R.layout.custom_act_guide_layout, this);
        initView();
        registerClickListener();
    }

    private void initView() {
        //分享与
        ll_tools = findViewById(R.id.ll_tools);
        iv_star = (ImageView) findViewById(R.id.iv_star);//点赞
        iv_share = (ImageView) findViewById(R.id.iv_share);//分享

//        mRVInner = (RecyclerView) findViewById(R.id.recyclerview);

        ll_text_container = (LinearLayout) findViewById(R.id.ll_txt_container);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        ll_dot_container = (LinearLayout) findViewById(R.id.ll_dot_container);
        fl_rv_container = (FrameLayout) findViewById(R.id.fl_recyclerview_container);
    }

    private void registerClickListener() {
        iv_share.setOnClickListener(this);
        iv_star.setOnClickListener(this);
        ll_text_container.setOnClickListener(this);
    }

    public void bindData(List data,int statement) {
        //TODO 默认是收缩时的状态
        if (statement== ExpandStatus.NORMAL){
            ll_text_container.setClickable(true);
            ll_tools.setVisibility(GONE);
            addDot2LinearLayout(3,ll_dot_container);
            addTextView(createTestData(),ll_text_container,2);
            hideHorizontalRecyclerView(fl_rv_container);
        }else if (statement == ExpandStatus.EXPANDED){
            ll_text_container.setClickable(false);
            ll_tools.setVisibility(VISIBLE);
            hideDotLayout(ll_dot_container);
            addTextView(createTestData(),ll_text_container,-1);
            addHorizontalRecyclerView(null,fl_rv_container);
        }

    }

    private List<String> createTestData(){
        List<String> tags=new ArrayList<>();
        tags.add("葡萄美酒夜光杯,背背背饿瘪贝贝");
        tags.add("Smart PNG and JPEG compression");
        tags.add("葡萄美酒夜光杯,背背背饿瘪贝贝");
        tags.add("Smart PNG and JPEG compression");
        tags.add("葡萄美酒夜光杯,背背背饿瘪贝贝");
        tags.add("Smart PNG and JPEG compression");
        tags.add("葡萄美酒夜光杯,背背背饿瘪贝贝");
        tags.add("Smart PNG and JPEG compression");
        return tags;
    }


    @Override
    public void expand() {
        if (expandListener!=null)expandListener.expandStart();
        minHeight=getHeight();
        expandable=true;
        MGLog.e("test","prepare=="+"height: "+getHeight() +"measuredHeight: "+getMeasuredHeight());
        bindData(null,ExpandStatus.EXPANDED);
        MGLog.e("test","prepare=="+"height: "+getHeight() +"measuredHeight: "+getMeasuredHeight());
    }

    @Override
    public void shrink() {
        if (expandListener!=null)expandListener.shrinkStart();
    }

    @Override
    public void addDot2LinearLayout(int num, LinearLayout target) {
        if (target==null || num <=0)return;
        if (target.getVisibility()==GONE){
            target.setVisibility(VISIBLE);
        }
        target.removeAllViews();
        int size = DisplayUtil.dp2px(context, 2);
        int margin = DisplayUtil.dp2px(context, 5);
        LinearLayout.LayoutParams params=new LayoutParams(size,size);
        params.setMargins(margin,0,margin,0);
        for (int i = 0; i < num; i++) {
            View dotView = createDotView();
            target.addView(dotView,params);
        }

    }

    @Override
    public void hideDotLayout(ViewGroup dotContainer) {
        if (dotContainer==null)return;
        dotContainer.removeAllViews();
        dotContainer.setVisibility(GONE);
    }

    @Override
    public void removeTextView(int left) {
        int childCount = ll_text_container.getChildCount();
        for (int index = childCount; index > left; index--) {
            ll_text_container.removeViewAt(index);
        }
    }

    @Override
    public TextView createTextView() {
        TextView textView=new TextView(context);
        textView.setTextColor(Color.parseColor("#999999"));
        textView.setTextSize(12);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public View createDotView() {
        View dot=new View(context);
        dot.setBackgroundColor(Color.DKGRAY);
        return dot;
    }

    @Override
    public void addHorizontalRecyclerView(List data,ViewGroup rvContainer) {
        if (rvContainer==null)return;
        rvContainer.removeAllViews();
        rvContainer.setVisibility(VISIBLE);
        ArrayList<String> data2=new ArrayList();
        data2.add("i");
        data2.add("like");
        data2.add("you");
        RecyclerView rvInner=new RecyclerView(context);
        rvInner.setHasFixedSize(true);
        rvInner.setLayoutManager(new LinearLayoutManager(context,HORIZONTAL,false));
        SimpleRVAdapter<String> adapter=new SimpleRVAdapter<String>(context,R.layout.item_custom_guide_inner,data2) {
            @Override
            protected void bindView(BaseRVViewHolder helper, String item) {

            }
        };
        rvInner.setAdapter(adapter);
        rvInner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                Log.e("test-inner","x: "+x+"   y:"+y+"   rawX:"+rawX + "   rawY: "+rawY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return false;
            }
        });
        rvContainer.addView(rvInner,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void hideHorizontalRecyclerView(ViewGroup rvContainer) {
        if (rvContainer == null)return;
        rvContainer.removeAllViews();
        rvContainer.setVisibility(GONE);
    }

    @Override
    public void sendData2Parent(List childData) {

    }

    @Override
    public void addTextView(List tags, LinearLayout target,int num) {
        if (target == null ||tags==null || tags.size()<=0)return;
        target.removeAllViews();
        int size = num ==-1 ? tags.size() : num;
        for (int i = 0; i < size; i++) {
            TextView textView = createTextView();
            textView.setText(tags.get(i).toString());
            target.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_star:
                doStar();
                break;
            case R.id.iv_share:
                doShare();
                break;
            case R.id.ll_txt_container:
                expand();
                break;
            default:
                break;
        }
    }

    private void doStar(){
        MGToast.showToast("star");
    }
    private void doShare(){
        MGToast.showToast("share");
    }

    public void setExpandListener(ExpandListener listener){
        this.expandListener=listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (expandable){
//            expandable=false;
//            maxHeight=getHeight();
//            SimpleExpandAnimation simpleExpandAnimation=new SimpleExpandAnimation(minHeight,maxHeight);
//            Animator[] animators = simpleExpandAnimation.getAnimators(ll_root);
//            Animator animator = animators[0];
//            animator.setDuration(1500);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.start();
//        }
        Log.e("test","height-layout: "+getHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void requestGuideGoods(String guide_id){
        String city_id = AppRuntimeWorker.getCity_id();
        if (TextUtils.isEmpty(city_id) || TextUtils.isEmpty(guide_id))return;
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("city_id", city_id);
        params.put("guide_id", guide_id);
        params.put("method", "InterestingGuideGoods");
        OkHttpUtil.getInstance().get(params, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                Log.e("test",response);
                if (sendDataListener !=null){
                    sendDataListener.onChildReceiveData(null);
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void setOnChildReceiveDataListener(ActLayoutReceiveDataListener listener){
        this.sendDataListener=listener;
    }

    public void setRecycerView(RecyclerView mRecyclerView) {
        this.parentRV=mRecyclerView;
    }

    public interface ActLayoutReceiveDataListener{
        void onChildReceiveData(List childData);
    }
}

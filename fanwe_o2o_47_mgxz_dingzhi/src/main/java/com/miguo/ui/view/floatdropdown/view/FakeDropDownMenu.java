package com.miguo.ui.view.floatdropdown.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.floatdropdown.interf.OnCallDismissPopListener;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/10
 * Description: 
 */

public class FakeDropDownMenu extends LinearLayout implements View.OnClickListener{
    private List<String> defaultNames;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private TextView tv_name4;
    private ImageView iv_name1;
    private ImageView iv_name2;
    private ImageView iv_name3;
    private ImageView iv_name4;
    private View ll_1;
    private View ll_2;
    private View ll_3;
    private View ll_4;
    private boolean fake;//是不是假的
    private OnClickListener listener;


    private ObjectAnimator expandAnimator;
    private ObjectAnimator reverseAnimator;
    private int lastPosition = -1;

    public FakeDropDownMenu(Context context) {
        this(context,null);
    }

    public FakeDropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FakeDropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_fake_dropdownmenu,this);

        tv_name1 = ((TextView) findViewById(R.id.tv_name1));
        tv_name2 = ((TextView) findViewById(R.id.tv_name2));
        tv_name3 = ((TextView) findViewById(R.id.tv_name3));
        tv_name4 = ((TextView) findViewById(R.id.tv_name4));

        iv_name1 = ((ImageView) findViewById(R.id.iv_name1));
        iv_name2 = ((ImageView) findViewById(R.id.iv_name2));
        iv_name3 = ((ImageView) findViewById(R.id.iv_name3));
        iv_name4 = ((ImageView) findViewById(R.id.iv_name4));

        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        ll_4 = findViewById(R.id.ll_4);

        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
        if (v == ll_1){
            handleClick(v,1);
            return;
        }

        if (v == ll_2){
            handleClick(v,2);
            return;
        }

        if (v == ll_3){
            handleClick(v,3);
            return;
        }

        if (v == ll_4){
            handleClick(v,4);
            return;
        }
    }

    private void handleClick(View v,int index){
        if (fakeTitleTabClickListener!=null){
            fakeTitleTabClickListener.onFakeClick(v,index);
        }
        //anim
        handleArrowImageAnim(index);
    }

    public void resetLastPosition(){
        lastPosition = -1;
    }

    public void handleArrowImageAnim(int index){//when fakeView click,make index == 0 to dismiss pop and reverse arrow imageView.
        if (fake)return;
        if (index == 0){
            reverseAnimator = ObjectAnimator.ofFloat(getTargetAnimationView(lastPosition),"rotation",180f,360f);
            reverseAnimator.start();
            lastPosition = -1;
            callPopDismissImmediately();
        }
        if (index == lastPosition){
            //点击了同一个item 第二次
            reverseAnimator = ObjectAnimator.ofFloat(getTargetAnimationView(index),"rotation",180,360f);
            reverseAnimator.start();
            lastPosition = -1;
            callPopDismissImmediately();
        }else {
            expandAnimator = ObjectAnimator.ofFloat(getTargetAnimationView(index),"rotation",0,180f);
            expandAnimator.start();
            if (lastPosition!=-1){
                reverseAnimator = ObjectAnimator.ofFloat(getTargetAnimationView(lastPosition),"rotation",180f,360f);
                reverseAnimator.start();
            }
            lastPosition = index;
        }
    }

    private void callPopDismissImmediately() {//Immediate
        if (callDismissPopListener!=null){
            callDismissPopListener.callDismissPop(true);
        }
    }

    private ImageView getTargetAnimationView(int index){
        ImageView result;
        switch (index){
            case 1:
                result = iv_name1;
                break;
            case 2:
                result = iv_name2;
                break;
            case 3:
                result = iv_name3;
                break;
            case 4:
                result = iv_name4;
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    public void performIndexClick(int index){
        switch (index){
            case 1:
                ll_1.performClick();
                break;
            case 2:
                ll_2.performClick();
                break;
            case 3:
                ll_3.performClick();
                break;
            case 4:
                ll_4.performClick();
                break;
        }
    }

    public void setIsFake(boolean isFake) {
        this.fake= isFake;
    }

    public interface OnFakeTitleTabClickListener{
        void onFakeClick(View v,int index);
    }
    private OnFakeTitleTabClickListener fakeTitleTabClickListener;

    public void setFakeTitleTabClickListener(OnFakeTitleTabClickListener
                                                     fakeTitleTabClickListener) {
        this.fakeTitleTabClickListener = fakeTitleTabClickListener;
    }

    private OnCallDismissPopListener callDismissPopListener;
    public void setOnFakeCallDismissPopListener(OnCallDismissPopListener callDismissPopListener) {
        this.callDismissPopListener = callDismissPopListener;
    }

    public void setTitleText(int index, String text){
        text = TextUtils.isEmpty(text) ? "" :text;
        switch (index){
            case 1:
                tv_name1.setText(text);
                break;
            case 2:
                tv_name2.setText(text);
                break;
            case 3:
                tv_name3.setText(text);
                break;
            case 4:
                tv_name4.setText(text);
                break;
        }
    }

    public void setOnFakeClickListener(OnClickListener listener){
        this.listener = listener;
    }

}

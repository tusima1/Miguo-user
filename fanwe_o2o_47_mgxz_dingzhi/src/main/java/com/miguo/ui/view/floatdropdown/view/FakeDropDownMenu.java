package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.dropdown.interf.ExpandReverse;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/10
 * Description: 
 */

public class FakeDropDownMenu extends LinearLayout implements View.OnClickListener,ExpandReverse{
    private List<String> defaultNames;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private TextView tv_name4;
    private View ll_1;
    private View ll_2;
    private View ll_3;
    private View ll_4;
    private boolean fake;//是不是假的
    private OnClickListener listener;

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

    @Override
    public void expand() {
        if (fake)return;
    }

    @Override
    public void reverse() {
        if (fake)return;
    }

    public interface OnFakeTitleTabClickListener{
        void onFakeClick(View v,int index);
    }
    private OnFakeTitleTabClickListener fakeTitleTabClickListener;

    public void setFakeTitleTabClickListener(OnFakeTitleTabClickListener
                                                     fakeTitleTabClickListener) {
        this.fakeTitleTabClickListener = fakeTitleTabClickListener;
    }

    public void setTitleText(int index,String text){
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

    public void onFakeClickListener(OnClickListener listener){
        this.listener = listener;
    }
}

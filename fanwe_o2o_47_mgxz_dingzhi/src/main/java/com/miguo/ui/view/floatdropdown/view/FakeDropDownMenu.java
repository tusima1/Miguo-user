package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.fanwe.o2o.miguo.R;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/10
 * Description: 
 */

public class FakeDropDownMenu extends LinearLayout {
    private List<String> defaultNames;

    public FakeDropDownMenu(Context context) {
        this(context,null);
    }

    public FakeDropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FakeDropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_fake_dropdownmenu,this);
    }
}

package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.ui.view.floatdropdown.interf.OnCheckChangeListener;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class FilterView extends LinearLayout implements OnCheckChangeListener, View
        .OnClickListener {

    private LinearLayout ll_like;
    private LinearLayout ll_youHui;
    private LinearLayout ll_price;

    private final int like = 0;
    private final int youHui = 1;
    private final int price = 2;

    private final int itemWidth;
    private final int itemHeight;
    private final int itemMarginRight;
    private final int containerMarginBottom;

    private int checkedNum = 0;//缓存数据
    private OnDropDownSelectedListener2<SingleMode> onDropDownSelectedListener;
    private View clear;
    private View done;
    private List<TwoMode> data;
    private TextView tv_like;
    private TextView tv_youHui;
    private TextView tv_price;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_filter_four, this);

        ll_like = ((LinearLayout) findViewById(R.id.ll_1));
        ll_youHui = ((LinearLayout) findViewById(R.id.ll_2));
        ll_price = ((LinearLayout) findViewById(R.id.ll_3));

        tv_like = ((TextView) findViewById(R.id.tv_name_1));
        tv_youHui = ((TextView) findViewById(R.id.tv_name_2));
        tv_price = ((TextView) findViewById(R.id.tv_name_3));

        clear = findViewById(R.id.clear);//重置
        done = findViewById(R.id.done);//完成

        clear.setOnClickListener(this);
        done.setOnClickListener(this);

        itemWidth = DisplayUtil.dp2px(context, 55);
        itemHeight = DisplayUtil.dp2px(context, 25);
        itemMarginRight = DisplayUtil.dp2px(context, 15);
        containerMarginBottom = DisplayUtil.dp2px(context, 16);
    }


    private void addLine(TwoMode data, int index) {
        List<SingleMode> singleModeList = data.getSingleModeList();
        if (singleModeList == null || singleModeList.size() <= 0) {
            return;
        }
        int num = singleModeList.size();
        LayoutParams itemParams = getItemParams();
        LayoutParams containerParams = getContainerParams();
        ArrayList<LinearLayout> itemContainer = getItemContainer(num);
        for (int i = 0; i < num; i++) {
            int containerIndex = i / 4;
            itemContainer.get(containerIndex).addView(getItemView(singleModeList.get(i)),
                    itemParams);
        }
        switch (index) {
            case like:
                for (LinearLayout linearLayout : itemContainer) {
                    ll_like.addView(linearLayout, containerParams);
                }
                tv_like.setText(data.getName());
                break;
            case youHui:
                for (LinearLayout linearLayout : itemContainer) {
                    ll_youHui.addView(linearLayout, containerParams);
                }
                tv_youHui.setText(data.getName());
                break;
            case price:
                for (LinearLayout linearLayout : itemContainer) {
                    ll_price.addView(linearLayout, containerParams);
                }
                tv_price.setText(data.getName());
                break;
        }

    }

    private LayoutParams getItemParams() {
        LayoutParams itemParams = new LayoutParams(itemWidth, itemHeight);
        itemParams.setMargins(0, 0, itemMarginRight, 0);
        return itemParams;
    }

    private LayoutParams getContainerParams() {
        LayoutParams containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        containerParams.setMargins(0, 0, 0, containerMarginBottom);
        return containerParams;
    }

    private CheckTextView getItemView(SingleMode singleMode) {
        CheckTextView checkTextView = new CheckTextView(getContext());
        String name = singleMode.getName();
        name = TextUtils.isEmpty(name) ? "" : name;
        checkTextView.setText(name);
        checkTextView.setHold(singleMode);
        checkTextView.setOnCheckChangeListener(this);
        return checkTextView;
    }

    private ArrayList<LinearLayout> getItemContainer(int num) {
        ArrayList<LinearLayout> result = new ArrayList<>();
        if (num <= 0) {
            return result;
        }
        int intNum = num / 4;
        int remainder = num % 4;
        if (remainder != 0) {
            intNum++;
        }
        if (intNum <= 0) {
            return result;
        }
        for (int i = 0; i < intNum; i++) {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            result.add(ll);
        }
        return result;
    }

    @Override
    public void changed(View view, boolean isChecked) {
        if (view instanceof CheckTextView) {
            boolean checked = ((CheckTextView) view).isChecked();
            if (checked) {
                checkedNum++;
            } else {
                checkedNum--;
            }
        }
    }

    public void setOnDropDownSelectedListener2(OnDropDownSelectedListener2<SingleMode>
                                                       onDropDownSelectedListener) {
        this.onDropDownSelectedListener = onDropDownSelectedListener;
    }

    public interface OnClearAllListener {
        void clear();
    }

    private OnClearAllListener clearAllListener;

    public void setClearAllListener(OnClearAllListener clearAllListener) {
        this.clearAllListener = clearAllListener;
    }

    public void setData(List<TwoMode> data) {
        this.data = data;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                addLine(data.get(i), i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == clear) {
            clear();
            return;
        }
        if (v == done) {
            done();
        }
    }

    private void done() {
        if (onDropDownSelectedListener != null) {
            onDropDownSelectedListener.onDropDownSelected(getSelectedModel(ll_price, ll_like,
                    ll_youHui));
        }
    }

    private ArrayList<SingleMode> getSelectedModel(ViewGroup... viewGroup) {
        ArrayList<SingleMode> strings = new ArrayList<>();
        if (viewGroup == null || viewGroup.length <= 0) {
            return strings;
        }
        for (int i = 0; i < viewGroup.length; i++) {
            ViewGroup group = viewGroup[i];
            int childCount = group.getChildCount();
            for (int i1 = 0; i1 < childCount; i1++) {
                View childAt = group.getChildAt(i1);
                if (childAt instanceof ViewGroup){
                    ArrayList<SingleMode> fromViewGroup = getFromViewGroup((ViewGroup) childAt);
                    if (fromViewGroup!=null && fromViewGroup.size()>0){
                        strings.addAll(fromViewGroup);
                    }
                }else {
                    Log.e("test","这里不存在单独存在的元素");
                }
            }
        }
        return strings;
    }

    private ArrayList<SingleMode> getFromViewGroup(ViewGroup viewGroup){
        ArrayList<SingleMode> modes =new ArrayList<>();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            SingleMode fromView = getFromView(childAt);
            if (fromView!=null){
                modes.add(fromView);
            }
        }
        return modes;
    }

    private SingleMode getFromView(View view){
        SingleMode mode =null;
        if (view instanceof  CheckTextView){
            boolean checked = ((CheckTextView) view).isChecked();
            Log.e("test","isChecked: "+checked);
            if (checked) {
                mode = (SingleMode) ((CheckTextView) view).getHold();
            }
        }
        return mode;
    }

    private void clear() {
        if (clearAllListener != null) {
            clearAllListener.clear();
        }
    }
}

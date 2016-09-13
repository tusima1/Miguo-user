package com.fanwe.user.view.customviews;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;


/**
 * Created by didik on 2016/8/11.
 * 带小红点
 * 带文字
 * 带图标
 * 的组合控件
 */
public class RedDotView extends LinearLayout {
    private Context mContext;
    private ImageView mIcon;//图标
    private TextView mTitle;//标题(下行文字)
    private TextView mRedNum;//红点的文字
    private OnRedDotViewClickListener mClickListener;

    public RedDotView(Context context) {
        super(context);
        init(context);
    }

    public RedDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RedDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext=context;
        LayoutInflater.from(mContext).inflate(R.layout.reddot_layout,this);

        //init view
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        mTitle = ((TextView) findViewById(R.id.tv_title));
        mRedNum = ((TextView) findViewById(R.id.tv_red_num));

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener!=null){
                    mClickListener.onRedDotViewClick(v);
                }
            }
        });
        setRotDotBackgroundDrawable(0);
    }

    /**
     * 设置小红点的数量
     * @param num >0即可
     */
    public void setRedNum(int num){
        if (num<=0){
            mRedNum.setVisibility(View.GONE);
        }else if(num<=99){
            if (mRedNum.getVisibility()==View.GONE){
                mRedNum.setVisibility(View.VISIBLE);
            }
            mRedNum.setText(""+num);
        }else {
            if (mRedNum.getVisibility()==View.GONE){
                mRedNum.setVisibility(View.VISIBLE);
            }
            mRedNum.setText("99+");
        }
    }

    /**
     * 设置图标
     * @param resId 图标资源文件id
     */
    public void setIcon(int resId){
        mIcon.setImageResource(resId);
    }

    /**
     * 设置标题
     * @param title 文字
     */
    public void setTitle(String title){
        if (!TextUtils.isEmpty(title)){
            mTitle.setText(title);
        }else {
            mTitle.setText("米果");
        }
    }

    /**
     * 设置点击事件
     * @param listener
     */
    public void setOnRedDotViewClickListener(OnRedDotViewClickListener listener){
        this.mClickListener=listener;
    }
    public interface OnRedDotViewClickListener{
        void onRedDotViewClick(View v);
    }

    /**
     * 设置文字颜色
     * @param color
     */
    public void setTextColor(int color){
        mTitle.setTextColor(color);
    }

    public void setAllParams(String title,int iconResId,int redNum,int textColor){
        setTitle(title);
        setIcon(iconResId);
        setRedNum(redNum);
        setTextColor(textColor);
    }

    /**
     *
     * @param drawableId 资源id
     */
    public void setRotDotBackgroundDrawable(int drawableId){
        if (drawableId==0){
            drawableId=R.drawable.md_ripple_white;
        }
        this.setBackground(getResources().getDrawable(drawableId,null));
    }
}

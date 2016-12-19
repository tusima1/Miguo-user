package com.miguo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;

/**
 * BaseNewActivity. 默认不显示右边的imageview 和textview
 * 所有的布局文件 自己加入
 * <include layout="@layout/item_layout_sample_title"/>
 * 功能：基本的 返回  +title +  textView/imageView
 * Created by zhy on 2016/11/24.
 */

public abstract class BaseNewActivity extends Activity {
    /**
     * 左边返回键。
     */
    ImageView iv_left;
    /**
     * 右边按钮。
     */
    ImageView iv_right;
    /**
     * 标题title
     */
    TextView tv_middle;

    /**
     * 右边 文字。
     */
    TextView tv_right;

    /**
     * 初始化title .
     */
    public void initTitleView(String title) {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_middle = (TextView) findViewById(R.id.tv_middle);
        tv_right = (TextView) findViewById(R.id.tv_right);

        if (!TextUtils.isEmpty(title)) {
            tv_middle.setText(title);
        } else {
            tv_middle.setText(R.string.app_name);
        }
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonPressed();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightTextViewPressed();
            }
        });
    }

    /**
     * 右边按钮事件 。
     */

    public  void onRightButtonPressed(){

    };

    /**
     * 右边文字 事件。
     */

    public  void onRightTextViewPressed(){

    };


    /**
     * 设置 左返回 按钮的图标。
     *
     * @param drawable
     */
    public void setLeftDrawable(int drawable) {
        iv_left.setImageResource(drawable);
    }

    /**
     * 显示左边返回按钮是否可见
     *
     * @param visible
     */

    public void setRightImageViewVisible(boolean visible) {
        if (visible) {
            iv_right.setVisibility(View.VISIBLE);
        } else {
            iv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边图标。
     *
     * @param drawable
     */
    public void setRightImageViewDrawable(int drawable) {
        Resources resources = getResources();
        Drawable drawable1 = resources.getDrawable(drawable);
        iv_right.setBackground(drawable1);
    }

    /**
     * 设置右边图标是否可见。
     *
     * @param visible
     */
    public void setRightTextViewVisible(boolean visible) {
        if (visible) {
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边文字 。
     *
     * @param text
     */
    public void setRightTextViewText(String text) {

        tv_right.setText(text);

    }

    public void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
    public String yearStr(String value) {
        if (TextUtils.isEmpty(value) || value.length() < 4) {
            return "";
        }
        String  valueYear = value.substring(0,4);
        String sysYear = DateFormat.format("yyyy", App.getInstance().getSysTime()).toString();
        if (sysYear.equals(valueYear)&&value.length()>5) {
            return value.substring(5);
        }else{
            return value;
        }
    }

}

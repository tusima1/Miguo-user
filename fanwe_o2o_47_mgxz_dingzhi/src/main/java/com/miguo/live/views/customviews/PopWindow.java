package com.miguo.live.views.customviews;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.fanwe.o2o.miguo.R;


/**
 * Created by didik on 2016/7/25.
 */
public class PopWindow {

    /**
     *
     * @param activity
     * @param ResID
     * @param width -1:match_parent -2:wrap_content 其他数字为精确值
     * @param height
     * @param gravity
     */
    public void show(Activity activity, int ResID, int width, int height, int gravity,boolean isOutsideTouchable,View parent){
//        LayoutInflater inflater = (LayoutInflater) activity
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.contentView = inflater.inflate(R.layout.popupwindow_custom, null);
//        this.setContentView(contentView);
//        // 设置弹出窗体的宽
//        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置弹出窗体的高
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置弹出窗体可点击
//        this.setTouchable(true);
//        this.setFocusable(true);
//        // 设置点击是否消失
//        this.setOutsideTouchable(true);
//        //设置弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnimation);
//        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable background = new ColorDrawable(0x4f000000);
//        //设置弹出窗体的背景
//        this.setBackgroundDrawable(background);

        View contentView = LayoutInflater.from(activity).inflate(ResID, null);
        SeekBar sb_progress = (SeekBar) contentView.findViewById(R.id.seekbar_progress);
        View tv_commit = contentView.findViewById(R.id.tv_commit);

        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                MGToast.showToast(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PopupWindow pop=new PopupWindow(contentView,width,height);
        pop.setTouchable(true);

        //设置窗体动画效果
//        ColorDrawable background=new ColorDrawable(0x4f000000);
        BitmapDrawable background=new BitmapDrawable();
        pop.setBackgroundDrawable(background);
        pop.setFocusable(false);
        pop.setOutsideTouchable(isOutsideTouchable);
        pop.showAtLocation(parent, Gravity.BOTTOM,0,0);
    }
}

package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiHomeFragmentCategory;
import com.miguo.entity.MenuBean;
import com.miguo.live.views.base.BaseHorizantalScrollView;

import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/18.
 */
public class HomeTagsView extends BaseHorizantalScrollView{

    HomeTagLineaLayout content;
    OnHomeTagsClickListener onHomeTagsClickListener;
    List<MenuBean.Result.Body> list;
    HiHomeFragmentCategory hiHomeFragmentCategory;

    public HomeTagsView(Context context) {
        super(context);
        init();
    }

    public HomeTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeTagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initContent();
    }

    private void initContent(){
        setHorizontalScrollBarEnabled(false);
        content = new HomeTagLineaLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(matchParent(), wrapContent());
        content.setOrientation(LinearLayout.HORIZONTAL);
        content.setLayoutParams(params);
        addView(content);
    }

    public void init(List<MenuBean.Result.Body> tags){
        content.removeAllViews();
        if(tags == null || tags.size()==0){
            return ;
        }
        this.list = tags;
        int iconWidth = getScreenWidth() / 8;
        int iconHeight = iconWidth;
        for(int i = 0; i < tags.size(); i++){
            LinearLayout group = new LinearLayout(getContext());
            LinearLayout.LayoutParams grouParams = getLinearLayoutParams(wrapContent(), wrapContent());
            group.setLayoutParams(grouParams);
            group.setGravity(Gravity.CENTER);
            group.setPadding(dip2px(18), dip2px(18), dip2px(18), dip2px(18));
            group.setOrientation(LinearLayout.VERTICAL);
            group.setOnClickListener(new HomeTagsViewListener(i));

            HomeTagCircleImageView icon = new HomeTagCircleImageView(getContext());
            LinearLayout.LayoutParams iconParams = getLinearLayoutParams(iconWidth, iconHeight);
            icon.setLayoutParams(iconParams);
            icon.setHomeTagsView(this);


            SDViewBinder.setImageView(getItem(i).getIcon(), icon);
            group.addView(icon);

            TextView title = new TextView(getContext());
            LinearLayout.LayoutParams titleParams = getLinearLayoutParams(wrapContent(), wrapContent());
            titleParams.setMargins(0, dip2px(3), 0, 0);
            title.setLayoutParams(titleParams);
            title.setTextColor(getColor(R.color.text_base_color));
            title.setTextSize(12);
            title.setText(getItem(i).getTitle());
            group.addView(title);
            content.addView(group);
        }


        View line = new View(getContext());
        LinearLayout.LayoutParams lineParams = getLinearLayoutParams(matchParent(), 1);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(getColor(R.color.text_base_color_2));
        content.addView(line);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                handlerActionMove();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handlerActionCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void handlerActionDown(){
        Log.d(tag, "handler down..");
        if(getHiHomeFragmentCategory() != null){
            getHiHomeFragmentCategory().setTouchDisableMove(true);
        }
    }

    public void handlerActionMove(){
        Log.d(tag, "handler move..");
        if(getHiHomeFragmentCategory() != null) {
            getHiHomeFragmentCategory().setTouchDisableMove(true);
        }
    }

    public void handlerActionCancel(){
        Log.d(tag, "handler cancel..");
        if(getHiHomeFragmentCategory() != null) {
            getHiHomeFragmentCategory().setTouchDisableMove(false);
            Log.d(tag, "parent disable : " + getHiHomeFragmentCategory().isTouchDisableMove());

        }
    }

    public MenuBean.Result.Body getItem(int position){
        return list.get(position);
    }

    class HomeTagsViewListener implements View.OnClickListener{

        int position;

        public HomeTagsViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(onHomeTagsClickListener != null){
                onHomeTagsClickListener.onTagsClick(getItem(position));
            }
        }
    }

    public HiHomeFragmentCategory getHiHomeFragmentCategory() {
        return hiHomeFragmentCategory;
    }

    public void setHiHomeFragmentCategory(HiHomeFragmentCategory hiHomeFragmentCategory) {
        this.hiHomeFragmentCategory = hiHomeFragmentCategory;
    }

    public interface OnHomeTagsClickListener{
        void onTagsClick(MenuBean.Result.Body item);
    }

    public void setOnHomeTagsClickListener(OnHomeTagsClickListener onHomeTagsClickListener) {
        this.onHomeTagsClickListener = onHomeTagsClickListener;
    }
}

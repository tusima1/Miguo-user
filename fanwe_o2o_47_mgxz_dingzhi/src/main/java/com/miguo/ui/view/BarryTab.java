package com.miguo.ui.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiBaseActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.definition.TabId;
import com.miguo.live.views.base.BaseRelativeLayout;
import com.miguo.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 狗蛋哥 on 16/8/10.
 */
public class BarryTab extends BaseRelativeLayout implements ViewPager.OnPageChangeListener{

    /**
     * 正常的文字颜色
     */
    int normalColor = R.color.main_color;
    /**
     * 按下后的文字颜色
     */
    int pressColor = R.color.main_color;
    /**
     * tab文字大小
     */
    int textSize;
    /**
     * 所有的tab
     */
    List<Tab> tabs;

    /**
     * tab监听
     */
    OnTabClickListener onTabClickListener;
    /**
     * 当前tab
     */
    LinearLayout currentTab;
    /**
     * Tag集合
     */
    List<LinearLayout> allTabs;
    /**
     * 当前坐标
     */
    int currentIndex = 0;

    /**
     * TAB 类型
     */
    Type type = Type.NORMAL;

    ViewPager viewPager;

    /**
     * 图标宽度
     */
    int iconWidht;
    /**
     * 图标高度
     */
    int iconHeight;
    /**
     * 中间图标高度
     */
    int centerIconWidth;
    /**
     * 中间图标宽度
     */
    int centerIconHeight;

    int defaultNormalColor = 0;
    int defaultpressColor = 0;

    Tab center;

    public static final int LIVE_ID = 0x03217;

    public BarryTab(Context context) {
        super(context);
    }

    public BarryTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init() {
        setBackgroundColor(Color.WHITE);
        setDefaultNormalColor(Color.parseColor("#FF6600")); //橘黄色
        setDefaultpressColor(Color.parseColor("#8c8c8c")); //灰色
        setTextSize(10);
        setIconWidht(20);
        setIconHeight(20);
        tabs = new ArrayList<>();
        allTabs = new ArrayList<>();
    }

    public BarryTab addTab(String name, int icon, int pressIcon, int id){
        tabs.add(new Tab(name, icon, pressIcon, id));
        return this;
    }

    public BarryTab addTab(String name, int icon, int pressIcon, int id, boolean center){
        Tab tab = new Tab(name, icon, pressIcon, id);
        tab.setCenter(center);
        tabs.add(tab);
        setCenter(tab);
        return this;
    }

    public BarryTab setTabType(Type type){
        setType(type);
        return this;
    }

    public BarryTab setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        return this;
    }

    /**
     * 单个icon的width是屏幕宽的5 / 100
     */
    public void builder(){
        if(tabs.size() == 0){
            return;
        }

        /**
         * 分割线
         */
        View topLine = new View(getContext());
        RelativeLayout.LayoutParams topLineParams = getRelativeLayoutParams(matchParent(), dip2px(1));
        topLineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topLine.setLayoutParams(topLineParams);
        topLine.setBackgroundColor(Color.parseColor("#f0f0f0")); //浅灰色
        addView(topLine);

        int screenWidth = getScreenWidth();
        int groupWidth = screenWidth / (hasCenterIcon() ? tabs.size() + 0 : tabs.size());

        int iconWidth = dip2px(getType() == Type.NORMAL ? getIconWidht() : 35);
        int centerWidth = dip2px(getCenterIconWidth());

        for(int i=0; i<tabs.size(); i++){
            LinearLayout group = new LinearLayout(getContext());
            RelativeLayout.LayoutParams groupParams = getRelativeLayoutParams(groupWidth, matchParent());
//            int marginLeft = hasCenterIcon() ? i >= 2 ? (i + 1 ) * groupWidth : i * groupWidth : i * groupWidth;
            int marginLeft = i * groupWidth;
            groupParams.setMargins(marginLeft, 0, 0, 0);
            groupParams.addRule(BELOW, topLine.getId());
            group.setLayoutParams(groupParams);
            group.setOrientation(LinearLayout.VERTICAL);
            group.setGravity(Gravity.CENTER);
            group.setId(tabs.get(i).getId());
            group.setOnClickListener(new TabListener(i, tabs.get(i).getId()));

            ImageView icon = new ImageView(getContext());
            LinearLayout.LayoutParams iconParams = getLinearLayoutParams(tabs.get(i).isCenter() ? centerWidth : iconWidth, tabs.get(i).isCenter() ? centerWidth : iconWidth);
            icon.setLayoutParams(iconParams);
            icon.setImageResource(tabs.get(i).getIcon());
            group.addView(icon);

            if(getType() == Type.NORMAL && !tabs.get(i).isCenter()){
                TextView name = new TextView(getContext());
                name.setText(tabs.get(i).getName());
                name.setTextColor(getNormalColor());
                name.setTextSize(getTextSize());
                LinearLayout.LayoutParams nameParams = getLinearLayoutParams(wrapContent(), wrapContent());
                nameParams.setMargins(0, dip2px(1), 0, 0);
                name.setLayoutParams(nameParams);
                group.addView(name);
            }
            allTabs.add(group);
            addView(group);
        }

//        if(hasCenterIcon()){
//            ImageView icon = new ImageView(getContext());
//            RelativeLayout.LayoutParams iconParams = getRelativeLayoutParams(centerWidth, centerWidth);
//            iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            icon.setLayoutParams(iconParams);
//            icon.setImageResource(center.getIcon());
//            icon.setOnClickListener(new TabListener(4, TabId.TAB_C));
//            icon.setId(LIVE_ID);
//            addView(icon);
//        }

        if(viewPager != null){
            viewPager.setOnPageChangeListener(this);
        }

        setCurrentTab(0);
    }

    public void setCurrentTab(int position){
        ImageView currentIcon = (ImageView)allTabs.get(currentIndex).getChildAt(0);
        ImageView positionIcon = (ImageView)allTabs.get(position).getChildAt(0);
        currentIcon.setImageResource(tabs.get(currentIndex).getIcon());
        positionIcon.setImageResource(tabs.get(position).getPressIcon());

        if(type == Type.NORMAL){
            TextView currentTabName = (TextView)allTabs.get(currentIndex).getChildAt(1);
            TextView positionTabName = (TextView)allTabs.get(position).getChildAt(1);
            if(null != currentTabName){
                currentTabName.setTextColor(getNormalColor());
            }
            if(null != positionTabName){
                positionTabName.setTextColor(getPressColor());
            }
        }

        startTabAnim(allTabs.get(position));


        if(onTabClickListener != null){
            onTabClickListener.onTabClick(tabs.get(position).getId());
        }

        this.currentIndex = position;

    }

    /**
     * tab抖动动画
     * @param view
     */
    @SuppressWarnings("ResourceType")
    private void startTabAnim(View view){
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.anim.scaletab);
        animator.setTarget(view);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }

    class Tab{
        String name;
        int icon;
        int pressIcon;
        int id;
        boolean center;

        public Tab(String name, int icon,int pressIcon, int id){
            this.name = name;
            this.icon = icon;
            this.pressIcon = pressIcon;
            this.id = id;
        }

        public boolean isCenter() {
            return center;
        }

        public void setCenter(boolean center) {
            this.center = center;
        }

        public int getPressIcon() {
            return pressIcon;
        }

        public void setPressIcon(int pressIcon) {
            this.pressIcon = pressIcon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    public class TabListener implements View.OnClickListener {

        int id;
        int position;

        public TabListener(int position, int id){
            this.position = position;
            this.id = id;
        }


        @Override
        public void onClick(View v) {
            if(onTabClickListener != null){
                if(onTabClickListener.onInterceptScrollEvent(id)){
                    if(onTabClickListener != null){
                        onTabClickListener.onTabClick(id);
                    }
                    return;
                }
                clickLogin();
            }
//            if(viewPager != null){
//                viewPager.setCurrentItem(position);
//            }
        }
    }

    private void clickLogin(){
        Intent intent = new Intent(getContext(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
        BaseUtils.jumpToNewActivity((HiBaseActivity)getContext(), intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnTabClickListener{
        void onTabClick(int tabId);

        /**
         * 是否拦截
         * @param tabId
         * @return
         */
        boolean onInterceptScrollEvent(int tabId);
    }

    /**
     * NORMAL 图标 + 文字
     * NO_TAB_NAME 图标
     */
    public enum Type{
        NORMAL,
        NO_TAB_NAME
    }

    public boolean hasCenterIcon(){
        return center != null;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    public int getNormalColor() {
        return normalColor == 0 ? getDefaultNormalColor() : getColor(normalColor);
    }

    public BarryTab setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }


    public int getPressColor() {
        return pressColor == 0 ? getDefaultpressColor() : getColor(pressColor);
    }


    public int getDefaultNormalColor() {
        return defaultNormalColor;
    }

    public void setDefaultNormalColor(int defaultNormalColor) {
        this.defaultNormalColor = defaultNormalColor;
    }

    public int getDefaultpressColor() {
        return defaultpressColor;
    }

    public void setDefaultpressColor(int defaultpressColor) {
        this.defaultpressColor = defaultpressColor;
    }

    public BarryTab setPressColor(int pressColor) {
        this.pressColor = pressColor;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }

    public BarryTab setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public int getIconWidht() {
        return iconWidht;
    }

    public BarryTab setIconWidht(int iconWidht) {
        this.iconWidht = iconWidht;
        return this;
    }

    public Tab getCenter() {
        return center;
    }

    public void setCenter(Tab center) {
        this.center = center;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public BarryTab setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
        return this;
    }

    public int getCenterIconWidth() {
        return centerIconWidth;
    }

    public BarryTab setCenterIconWidth(int centerIconWidth) {
        this.centerIconWidth = centerIconWidth;
        return this;
    }

    public int getCenterIconHeight() {
        return centerIconHeight;
    }

    public BarryTab setCenterIconHeight(int centerIconHeight) {
        this.centerIconHeight = centerIconHeight;
        return this;
    }
}

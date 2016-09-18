package com.miguo.live.views.danmu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SimpleTextCacheStuffer;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by zlh on 2016/9/12.
 * 弹幕杀手
 */
public class Danmukiller {

    String TAG = "Danmukiller";
    Context context;
    DanmakuView danmakuView;
    DanmakuContext danmakuContext;
    /**
     * 头像宽度
     */
    int avatarWidth;
    /**
     * 头像高度
     */
    int avatarHeight;

    /**
     * 通过设置弹幕的padding值来扩充弹幕背景区域
     */
    int danmuPadding;
    /**
     * 弹幕文字大小
     */
    int textSize;

    /**
     * 设置弹幕背景的圆角弧度
     */
    int danmuBackRadius;

    /**
     *
     * 弹幕生命时长
     */
    int danmuLive;

    public Danmukiller(Context context) {
        this.context = context;
        initParams();
        initDanmuParams();
    }

    private void initParams(){
        setAvatarWidth(dip2px(40));
        setAvatarHeight(dip2px(40));
        setTextSize(dip2px(12));
        setDanmuBackRadius(dip2px(24));
        setDanmuLive(3000);
        /**
         * 为了使弹幕文字处于头像的下半部分，所以需要将头像画布原点向上移动头像高度一般的距离
         * 这个值刚好的弹幕padding的值，从而弧形背景就可以和头像融合在一起（背景填充的情况）
         * 也利于计算高度和坐标
         */
        setDanmuPadding(getAvatarHeight() / 2);
    }

    /**
     * 初始化弹幕参数
     */
    private void initDanmuParams(){
        /**
         * 设置最大显示行数
         * 3行
         */
        HashMap<Integer, Integer> maxLine = new HashMap<Integer, Integer>();
        maxLine.put(BaseDanmaku.TYPE_SCROLL_RL, 3);
        /**
         * 设置是否禁止重叠
         */
        HashMap<Integer, Boolean> overlapping = new HashMap<Integer, Boolean>();
        overlapping.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlapping.put(BaseDanmaku.TYPE_FIX_TOP, true);

        danmakuContext = DanmakuContext.create()
                /**
                 * 设置弹幕样式
                 */
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                /**
                 * 是否禁止重叠
                 */
                .preventOverlapping(overlapping)
                /**
                 * 设置最大显示行数
                 */
                .setMaximumLines(maxLine)
                /**
                 * 不合并重复弹幕
                 */
                .setDuplicateMergingEnabled(false)
                /**
                 * 文字缩放
                 */
                .setScaleTextSize(1.0f)
                /**
                 * 速度越大越慢
                 */
//                .setScrollSpeedFactor(4.2f)
                /**
                 * 设置缓存绘制填充器，默认使用{@link SimpleTextCacheStuffer}只支持纯文字显示, 如果需要图文混排请设置{@link SpannedCacheStuffer}
                 * 如果需要定制其他样式请扩展{@link SimpleTextCacheStuffer}|{@link SpannedCacheStuffer}
                 */
                .setCacheStuffer(new DanmuBgCacheStuffer(this), new DanmuCacheStufferProxy());
    }

    public void addDanmus(final List<DanmuBean> danmuBeanList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<danmuBeanList.size(); i++){
                    addDanmu(danmuBeanList.get(i), i);
                }
            }
        }).start();
    }

    public void addDanmu(DanmuBean danmuBean, int index){
        /**
         * 创建一个从右向左的弹幕
         */
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        /**
         * 将弹幕信息保存到弹幕额外信息中
         */
        Log.d(TAG, "danmuBean.getName: " + danmuBean.getName());

        danmaku.tag = danmuBean.getName();
        danmaku.text = getSpannable(danmuBean);
        danmaku.padding = getDanmuPadding();
        danmaku.priority = 0;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
        danmaku.isLive = false;
        danmaku.time = danmakuView.getCurrentTime() + (index * getDanmuLive());
        danmaku.textSize = getTextSize();/* * (mDanmakuContext.getDisplayer().getDensity() - 0.6f);*/
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.paintWidth = 100;
        danmakuView.addDanmaku(danmaku);
    }

    public void addDanmu(DanmuBean danmuBean){
        addDanmu(danmuBean, 0);
    }

    /**
     * 图文混牌
     * @param danmuBean
     * @return
     */
    private SpannableStringBuilder getSpannable(DanmuBean danmuBean){
        Bitmap bitmap = getAvatarBitMapFromUrl(danmuBean.getUrl());
        DanmuDrawable danmuDrawable = new DanmuDrawable(bitmap);
        /**
         * 设置头像绘制区域
         */
        danmuDrawable.setBounds(0, 0, getAvatarWidth(), getAvatarHeight());
        /**
         * 如果文字内容比头像少，要设置空格
         */
        danmuBean.setContent(danmuBean.getContent().length() < danmuBean.getName().length() ? danmuBean.getContent() + getSpaceText(danmuBean.getName().length() - danmuBean.getContent().length()) : danmuBean.getContent());
        return newSpannable(danmuDrawable, danmuBean.getContent());
    }

    private SpannableStringBuilder newSpannable(DanmuDrawable danmuDrawable, String content){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        DanmuSpan span = new DanmuSpan(danmuDrawable);
        spannableStringBuilder.setSpan(span, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(content)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(content);
        }
        return spannableStringBuilder;
    }

    private Bitmap getAvatarBitmapFromLocal(int id){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        if(bitmap == null){
            return null;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix =new Matrix();
        matrix.postScale((float)getAvatarWidth() / width, (float)getAvatarHeight() / height);
        Log.d(TAG, "width: " + width + " ,height: " + height + " ,avaWidth: " + getAvatarWidth() + " ,avaHeight: " + getAvatarHeight());
        bitmap = Bitmap.createBitmap(bitmap,
                /**
                 * x坐标
                 */
                0,
                /**
                 * y坐标
                 */
                (int)(getAvatarHeight() / height),
                /**
                 * 宽度
                 */
                (int)width,
                /**
                 * 高度
                 */
                (int)height,
                matrix,
                true);
        return bitmap;
    }

    private Bitmap getAvatarBitMapFromUrl(String url){
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
        Matrix matrix = new Matrix();
        bitmap = bitmap == null ? getAvatarBitmapFromLocal(R.drawable.userlogo) : bitmap;
        matrix.postScale(getAvatarWidth() / (float)bitmap.getWidth(), getAvatarHeight() / (float)bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void setDanmakuView(DanmakuView danmakuView) {
        this.danmakuView = danmakuView;
        startDanmu();
    }

    public void startDanmu(){
        if (danmakuView != null) {
            danmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
        }

        danmakuView.prepare(new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, danmakuContext);
        danmakuView.enableDanmakuDrawingCache(true);
    }

    public void start(){
        if (danmakuView != null) {
            danmakuView.start();
        }
    }

    public void pause() {
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    public void hide() {
        if (danmakuView != null) {
            danmakuView.hide();
        }
    }

    public void show() {
        if (danmakuView != null) {
            danmakuView.show();
        }
    }

    public void resume() {
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    public void destroy() {
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }

    private String getSpaceText(int count){
        String space = " ";
        for(int i=0; i<count; i++){
            space = " " + space;
        }
        return space;
    }

    public int getAvatarWidth() {
        return avatarWidth;
    }

    public void setAvatarWidth(int avatarWidth) {
        this.avatarWidth = avatarWidth;
    }

    public int getAvatarHeight() {
        return avatarHeight;
    }

    public void setAvatarHeight(int avatarHeight) {
        this.avatarHeight = avatarHeight;
    }

    public int getDanmuPadding() {
        return danmuPadding;
    }

    public void setDanmuPadding(int danmuPadding) {
        this.danmuPadding = danmuPadding;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getDanmuBackRadius() {
        return danmuBackRadius;
    }

    public void setDanmuBackRadius(int danmuBackRadius) {
        this.danmuBackRadius = danmuBackRadius;
    }

    public int getDanmuLive() {
        return danmuLive;
    }

    public void setDanmuLive(int danmuLive) {
        this.danmuLive = danmuLive;
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}

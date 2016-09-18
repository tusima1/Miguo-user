package com.miguo.live.views.danmu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 * Created by zlh on 2016/9/12.
 */
public class DanmuBgCacheStuffer extends SpannedCacheStuffer {

    Danmukiller danmukiller;

    /**
     * 弹幕背景的画笔
     */
    Paint bgPaint;
    /**
     * 弹幕昵称的画笔
     */
    Paint namePaint;


    public DanmuBgCacheStuffer(Danmukiller danmukiller) {
        this.danmukiller = danmukiller;
        bgPaint = new Paint();
        namePaint = new Paint();
        initBgPaint();
        initNamePaint();
    }

    private void initBgPaint(){
        /**
         * 防锯齿
         */
        bgPaint.setAntiAlias(true);
        /**
         * 设置一定透明度的背景色
         */
        bgPaint.setColor(Color.argb(80, 0, 0, 0));
    }

    private void initNamePaint(){
        /**
         * 防锯齿
         */
        namePaint.setAntiAlias(true);
        /**
         * 设置线宽
         */
        namePaint.setStrokeWidth(3);
        /**
         *  设置画笔文字大小
         */
        namePaint.setTextSize(getTextSize());
        /**
         * 设置画笔文字颜色
         */
        namePaint.setColor(Color.WHITE);
        /**
         * 设置靠左对齐
         */
        namePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
        super.drawBackground(danmaku, canvas, left, top);

        /**
         * 画圆弧背景
         */
        canvas.drawRoundRect(getDanmuBgBgRect(danmaku), getDanmuBackRadius(), getDanmuBackRadius(), bgPaint);
        /**
         * 将昵称画上去
         */
        String name = (String)danmaku.tag;
        namePaint.getTextBounds(name, 0, name.length(), getNameRect());
        canvas.drawText(
                /**
                 * 文字内容
                 */
                name,
                /**
                 * x坐标，在头像右边 padding + 头像width
                 */
                getDanmuPadding() + getAvatarWidth() + 12,
                /**
                 * y坐标：此时头像在背景顶部，需要将文字设置在头像中间上面一点
                 * 头像一半的高度即为padding高度，为了不太贴中间，向上移动12个像素
                 */
                getDanmuPadding() - 12,
                /**
                 * 名字画笔
                 */
                namePaint);
    }

    /**
     *
     * @return 带弧度的背景
     */
    private RectF getDanmuBgBgRect(BaseDanmaku danmaku){
        int extraLeft = getAvatarWidth() / 4;
        int extraBottom = 0;
        return new RectF(getDanmuPadding() + extraLeft, getAvatarHeight() / 2, danmaku.paintWidth, getAvatarHeight() + extraBottom);
    }

    private Rect getNameRect(){
        return new Rect();
    }

    /**
     * 获取头像宽度
     * @return
     */
    public int getAvatarWidth() {
        return danmukiller.getAvatarWidth();
    }

    /**
     * 获取头像高度
     * @return
     */
    public int getAvatarHeight() {
        return danmukiller.getAvatarHeight();
    }

    /**
     * 获取padding
     * 通过设置弹幕的padding值来扩充弹幕背景区域
     */
    public int getDanmuPadding() {
        return danmukiller.getDanmuPadding();
    }

    /**
     * 弹幕文字大小
     */
    public int getTextSize() {
        return danmukiller.getTextSize();
    }

    /**
     * 获取弹幕背景的弧度
     * @return
     */
    public int getDanmuBackRadius() {
        return danmukiller.getDanmuBackRadius();
    }

}

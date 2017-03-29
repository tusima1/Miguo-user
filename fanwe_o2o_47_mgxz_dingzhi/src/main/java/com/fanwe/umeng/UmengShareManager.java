package com.fanwe.umeng;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.miguo.live.views.customviews.MGToast;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * @author didikee
 * @date 2016年6月30日 下午2:26:40
 * <p/>
 * 友盟分享类
 */
public class UmengShareManager {

    public static final String wxAppKey = "wx6aafdae1bac40206";
    public static final String wxAppSecret = "5f29a228760302af0d85774d02390273";
    public static final String qqAppKey = "1101169715";   //1101169715
    public static final String qqAppSecret = "FtAZVvB6LZ85hjdE";  // FtAZVvB6LZ85hjdE

    public static final String sinaAppKey = "3061230415";
    public static final String sinaAppSecret = "b5fe7acf5dc0eecaf23344a0f84b26e6";
    //-------------------------------             b5fe7acf5dc0eecaf23344a0f84b26e6
    public static int mTag = 0;
    public static Activity context;
    public static String mOrder_id;

    public static void initConfig() {
        // 初始化qq与qq空间分享
        PlatformConfig.setQQZone(qqAppKey, qqAppSecret);
        // 初始化 微信 appid appsecretA
        PlatformConfig.setWeixin(wxAppKey, wxAppSecret);
        PlatformConfig.setSinaWeibo(sinaAppKey, sinaAppSecret);

        Config.REDIRECT_URL="http://sns.whalecloud.com";
    }

    /**
     * 分享
     *
     * @param mActivity     Activity
     * @param title         标题
     * @param content       内容
     * @param url           点击的分享内容的跳转url
     * @param umImage       分享的图片
     * @param shareListener 回调,传null会走默认的回调.
     */
    public static void share(Activity mActivity, String title, String content, String url, UMImage umImage, UMShareListener shareListener) {
        if (shareListener == null) {
            shareListener = getDefaultListener();
        }
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA};

        new ShareAction(mActivity)
                .setDisplayList(displaylist)
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(umImage)
                .setCallback(shareListener).open();
    }

    public static void share(SHARE_MEDIA platform, Activity mActivity, String title, String content, String url, UMImage umImage, UMShareListener shareListener) {
        if (shareListener == null) {
            shareListener = getDefaultListener();
        }

        new ShareAction(mActivity)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(url)
                .withMedia(umImage)
                .setPlatform(platform)
                .setCallback(shareListener)
                .share();
    }

    private static UMShareListener getDefaultListener() {
        return new UMShareListener() {

            @Override
            public void onResult(SHARE_MEDIA arg0) {
                if (mTag == 1) {
                }
            }

            @Override
            public void onError(SHARE_MEDIA arg0, Throwable arg1) {
                if (mTag == 1) {
                    if (onSharedListener != null) {
                        onSharedListener.doFail();
                    }
                } else {
                    MGToast.showToast(arg0 + "分享失败");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                if (mTag == 1) {
                    if (onSharedListener != null) {
                        onSharedListener.doFail();
                    }
                }
                MGToast.showToast(arg0 + "分享取消");

            }
        };
    }

    //-------------------------------------------
    public interface onSharedListener {
        void doSuccess();

        void doFail();
    }

    private static onSharedListener onSharedListener;

    public static void setonSharedListener(onSharedListener listener) {
        onSharedListener = listener;
    }

    //---------------------------------------------
    public static void setShareTag(int tag, Activity activity, String order_id) {
        mTag = tag;
        context = activity;
        mOrder_id = order_id;
    }
    //---------------------------------------------

    /**
     * 获取友盟分享参数 UMImage
     *
     * @param mContext
     * @param imageUrl 从服务器传过来的图片url
     * @return UMImage
     */
    public static UMImage getUMImage(Context mContext, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        return new UMImage(mContext, imageUrl);
    }

    /**
     * 获取友盟分享参数 UMImage
     *
     * @param mContext
     * @param filePath 本地文件的绝对路径
     * @return UMImage
     */
    public static UMImage getUMImage2(Context mContext, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return new UMImage(mContext, BitmapFactory.decodeFile(filePath));
    }

    /**
     * 获取友盟分享参数 UMImage
     *
     * @param mContext
     * @param resId    资源id
     * @return UMImage
     */
    public static UMImage getUMImage(Context mContext, int resId) {
        UMImage umImage = null;
        try {
            umImage = new UMImage(mContext, BitmapFactory.decodeResource(mContext.getResources(), resId));
        } catch (Exception e) {
            e.printStackTrace();
            MGToast.showToast("解析资源id错误");
        }
        return umImage;
    }


}

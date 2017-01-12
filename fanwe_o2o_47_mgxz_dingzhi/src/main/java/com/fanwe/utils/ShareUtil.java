package com.fanwe.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.SharePopHelper;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.miguo.live.views.customviews.MGToast;

/**
 * Created by qiang.chen on 2016/12/8.
 */

public class ShareUtil {
    public static void share(Activity mActivity, ShareInfoBean shareInfoBean, String shareRecordId, String type) {
        if (mActivity == null || shareInfoBean == null) {
            MGToast.showToast("无分享内容");
            return;
        }
        if (shareRecordId == null) {
            shareRecordId = "";
        }
        if (TextUtils.isEmpty(shareInfoBean.getSummary())) {
            shareInfoBean.setSummary("欢迎来到米果小站");
        }
        String imageUrl = shareInfoBean.getImageurl();
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
            if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                imageUrl = MGDictUtil.getShareIcon();
            }
        } else if (!imageUrl.startsWith("http")) {
            imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
            if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                imageUrl = MGDictUtil.getShareIcon();
            }
        }
        shareInfoBean.setImageurl(imageUrl);
        String clickUrl = shareInfoBean.getClickurl();
        if (TextUtils.isEmpty(clickUrl)) {
            clickUrl = ServerUrl.getAppH5Url();
        } else {
            if (!clickUrl.contains("/share_record_id/")) {
                clickUrl = clickUrl + "/share_record_id/" + shareRecordId;
            } else if (!TextUtils.isEmpty(shareRecordId) && !clickUrl.contains(shareRecordId)) {
                int i = clickUrl.indexOf("/share_record_id/");
                String temp = clickUrl.substring(0, i);
                clickUrl = temp + "/share_record_id/" + shareRecordId;
            }
        }
        shareInfoBean.setClickurl(clickUrl);
        if (TextUtils.isEmpty(shareInfoBean.getTitle())) {
            shareInfoBean.setTitle("米果小站");
        }

        SharePopHelper sharePopHelper = new SharePopHelper(mActivity, type);
        sharePopHelper.setShareInfoBean(shareInfoBean);
        sharePopHelper.show();
    }
}

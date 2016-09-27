package com.fanwe.utils;

import android.text.TextUtils;

import com.fanwe.common.MGDict;
import com.fanwe.common.model.MGDict.DictModel;
import com.fanwe.library.utils.SDCollectionUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class MGDictUtil {
    private static List<DictModel> dicts = MGDict.getDict();
    private static String uploadToken = "";
    private static String shareIcon = "";

    /**
     * 获取字典中的七牛uploadToken
     *
     * @return
     */
    public static String getUploadToken() {
        if (TextUtils.isEmpty(uploadToken)) {
            if (!SDCollectionUtil.isEmpty(dicts)) {
                for (DictModel bean : dicts) {
                    //Upload
                    if ("Upload".equals(bean.getDic_name())) {
                        uploadToken = bean.getDic_mean();
                    }
                }
            }
        }
        return uploadToken;
    }

    /**
     * 获取字典中分享的默认图片
     *
     * @return
     */
    public static String getShareIcon() {
        if (TextUtils.isEmpty(shareIcon)) {
            if (!SDCollectionUtil.isEmpty(dicts)) {
                for (DictModel bean : dicts) {
                    //User
                    if ("User".equals(bean.getDic_name())) {
                        shareIcon = bean.getDic_mean();
                    }
                }
            }
        }
        return shareIcon;
    }
}

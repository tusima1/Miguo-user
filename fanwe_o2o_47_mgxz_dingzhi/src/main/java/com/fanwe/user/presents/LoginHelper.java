package com.fanwe.user.presents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.ThirdLoginInfo;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.app.HiHomeActivity;
import com.miguo.definition.ClassPath;
import com.miguo.definition.ResultCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.MGUIUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.SxbLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by Administrator on 2016/7/22.
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();


    public LoginHelper(Context context) {
        mContext = context;

    }

    /**
     * 退出imsdk
     * <p>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                SxbLog.e(TAG, "IMLogout fail ：" + i + " msg " + s);
            }
            @Override
            public void onSuccess() {
                SxbLog.i(TAG, "IMLogout succ !");
                //反向初始化avsdk
                stopAVSDK();
            }
        });

    }

    /**
     * 反初始化AVADK
     */
    public void stopAVSDK() {
        QavsdkControl.getInstance().stopContext();

    }

    /**
     * 判断BODY对象是否存在。
     *
     * @param root
     * @return
     */

    public UserInfoNew validateInfoBody(Root<UserInfoNew> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;
    }

    @Override
    public void onDestory() {
        mContext = null;
    }
}

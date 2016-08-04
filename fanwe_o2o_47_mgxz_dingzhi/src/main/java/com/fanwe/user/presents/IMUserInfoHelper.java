package com.fanwe.user.presents;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.qcloud.suixinbo.utils.SxbLog;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IMUserInfoHelper {


    /**
     * 设置呢称。
     * @param nickName
     */
    public void setMyNickName(String nickName){
        if(TextUtils.isEmpty(nickName)){
            UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
            if(userCurrentInfo!=null){
                UserInfoNew user = userCurrentInfo.getUserInfoNew();
                if(user!=null){
                    String value = TextUtils.isEmpty(user.getNick())==true?user.getUser_name():user.getNick();
                    if(TextUtils.isEmpty(value)){
                        return;
                    }else{
                        nickName = value;
                    }
                }else{
                    return;
                }
            }else{
                return;
            }
        }
        TIMFriendshipManager.getInstance().setNickName(nickName, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 设置签名。
     * @param sign
     */
    public void setMySign(String sign){


        TIMFriendshipManager.getInstance().setSelfSignature(sign, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 设置头像URL.
     * @param url
     */
    public void setMyAvator(String url){

        if(TextUtils.isEmpty(url)){
            UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
            if(userCurrentInfo!=null){
                UserInfoNew user = userCurrentInfo.getUserInfoNew();
                if(user!=null){
                    String value =user.getIcon();
                    if(TextUtils.isEmpty(value)){
                        return;
                    }else{
                        url = value;
                    }
                }else{
                    return;
                }
            }else{
                return;
            }
        }

        TIMFriendshipManager.getInstance().setFaceUrl(url, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

}

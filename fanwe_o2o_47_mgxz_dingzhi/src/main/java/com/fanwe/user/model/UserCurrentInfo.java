package com.fanwe.user.model;

/**
 * 当前用户的信息，只存在于缓存中。
 * Created by Administrator on 2016/7/27.
 */
public class UserCurrentInfo  {
   public static   UserCurrentInfo currentInfo;
    /**
     *签名后的USER.
     */
    public  String userSign;
    /**
     * 当前用户登录信息。
     */
    public UserInfoNew userInfoNew;
    /**
     * 用户当前TOKEN.
     */
    public String token="";
    public static UserCurrentInfo getInstance(){
        if(currentInfo == null){
            currentInfo = new UserCurrentInfo();
        }
        return currentInfo;
    }

    public UserInfoNew getUserInfoNew() {
        return userInfoNew;
    }

    public void setUserInfoNew(UserInfoNew userInfoNew) {
        this.userInfoNew = userInfoNew;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public  String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }
}

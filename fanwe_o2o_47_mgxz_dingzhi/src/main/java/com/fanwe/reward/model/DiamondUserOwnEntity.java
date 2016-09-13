package com.fanwe.reward.model;

import java.io.Serializable;

/**
 * 用户可用钻宝。
 * Created by Administrator on 2016/9/11.
 */
public class DiamondUserOwnEntity implements Serializable {


    private String diamond_android;


    private String diamond_ios;


    private String common_diamond;


    public void setDiamond_android(String diamond_android) {

        this.diamond_android = diamond_android;

    }

    public String getDiamond_android() {

        return this.diamond_android;

    }

    public void setDiamond_ios(String diamond_ios) {

        this.diamond_ios = diamond_ios;

    }

    public String getDiamond_ios() {

        return this.diamond_ios;

    }

    public void setCommon_diamond(String common_diamond) {

        this.common_diamond = common_diamond;

    }

    public String getCommon_diamond() {

        return this.common_diamond;

    }

}
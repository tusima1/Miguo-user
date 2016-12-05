package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**
 * "diamond":"0.00",   // 钻石
 "balance":"0.00",   // 余额
 "redpacket":"0",    // 红包
 "bean":"0.00"       // 米果豆
 * Created by zhouhy on 2016/11/14.
 */
public class WalletResult implements Serializable {
    /**
     * 钻石.
     */
    public String diamond;
    /**
     * 余额
     */
    public String balance;
    /**
     * 红包
     */
    public String redpacket;
    /**
     * 米果豆.
     */
    public String bean;

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(String redpacket) {
        this.redpacket = redpacket;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }
}

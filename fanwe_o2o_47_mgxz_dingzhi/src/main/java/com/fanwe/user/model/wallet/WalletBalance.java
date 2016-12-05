package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**
 * 钱包余额。。。
 * Created by zhouhy on 2016/11/16.
 */

public class WalletBalance implements Serializable {
    /**
     * 余额。
     */
    private String balance;
    /**
     * 佣金
     */

    private String commission_total;

    /**
     * 分享提成。
     */
    private String share_total;


    public void setBalance(String balance){

        this.balance = balance;

    }

    public String getBalance(){

        return this.balance;

    }

    public void setCommission_total(String commission_total){

        this.commission_total = commission_total;

    }

    public String getCommission_total(){

        return this.commission_total;

    }

    public void setShare_total(String share_total){

        this.share_total = share_total;

    }

    public String getShare_total(){

        return this.share_total;

    }
}

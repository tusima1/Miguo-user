package com.fanwe.shoppingcart.model;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车确认页。
 * Created by Administrator on 2016/8/22.
 */
public class ShoppingBody implements Serializable {

//    "deals":
//            "userAccountMoney":"0",//该用户可用多少余额
//            "goodsTotal":"4752",//商品总价
//            "payPrice":"4752",//应付价格
//            "Total":"4752",
//            "firstPriceTotal":"100",
//            "id":"25aa3fed-f491-4789-97d1-9009906f7210",//id：下一个请求需求数据
//            "userAccountMoneyFlg":"0",//判断余额能否使用，0：不可用，1：可用
    /**
     * 商家 商品详情。
     */
    private List<Deals> deals ;
    /**
     * 用户留言
     */
    private String content;
    /**
     * 该用户可用多少余额
     */
    private String userAccountMoney;
    //首单优惠总计
    private String firstPriceTotal;
    //判断余额能否使用，0：不可用，1：可用
    private String userAccountMoneyFlg;
    /**
     * 该用户是否存在可使用的红包flg，0：无红包，1有红包
     */
    private String red_item_flg;
    /**
     * 商品总价.
     */
    private String goodsTotal;
    /**
     * 应付价格
     */
    private String payPrice;
    //总计
    private String Total;
    /**
     * 优惠了多少价格，这个优惠是指红包优惠了的
     */
    private String youhuiPrice;
    /**
     * 订单ID下一个请求需求数据
     */
    private String id;
    /**
     * 使用了多少余额
     */
    private String userUseingAccountMoney;
    /**
     * 原来用余额支付 了多少钱。
     */

    private String accountmoney;

    public String getFirstPriceTotal() {
        return firstPriceTotal;
    }

    public void setFirstPriceTotal(String firstPriceTotal) {
        this.firstPriceTotal = firstPriceTotal;
    }

    public String getUserAccountMoneyFlg() {
        return userAccountMoneyFlg;
    }

    public void setUserAccountMoneyFlg(String userAccountMoneyFlg) {
        this.userAccountMoneyFlg = userAccountMoneyFlg;
    }

    public void setDeals(List<Deals> deals){
        this.deals = deals;
    }
    public List<Deals> getDeals(){
        return this.deals;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setUserAccountMoney(String userAccountMoney){
        this.userAccountMoney = userAccountMoney;
    }
    public String getUserAccountMoney(){
        return this.userAccountMoney;
    }
    public void setRed_item_flg(String red_item_flg){
        this.red_item_flg = red_item_flg;
    }
    public String getRed_item_flg(){
        return this.red_item_flg;
    }
    public void setGoodsTotal(String goodsTotal){
        this.goodsTotal = goodsTotal;
    }
    public String getGoodsTotal(){
        return this.goodsTotal;
    }
    public void setPayPrice(String payPrice){
        this.payPrice = payPrice;
    }
    public String getPayPrice(){
        return this.payPrice;
    }
    public void setTotal(String Total){
        this.Total = Total;
    }
    public String getTotal(){
        return this.Total;
    }
    public void setYouhuiPrice(String youhuiPrice){
        this.youhuiPrice = youhuiPrice;
    }
    public String getYouhuiPrice(){
        return this.youhuiPrice;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setUserUseingAccountMoney(String userUseingAccountMoney){
        this.userUseingAccountMoney = userUseingAccountMoney;
    }
    public String getUserUseingAccountMoney(){
        return this.userUseingAccountMoney;
    }

    public String getAccountmoney() {
        return accountmoney;
    }

    public void setAccountmoney(String accountmoney) {
        this.accountmoney = accountmoney;
    }
}
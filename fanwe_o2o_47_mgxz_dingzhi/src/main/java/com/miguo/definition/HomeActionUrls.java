package com.miguo.definition;

import com.fanwe.constant.ServerUrl;

/**
 * Created by Barry on 2017/4/17.
 * 首页四个板块跳转url
 */
public class HomeActionUrls {

    /**
     * 米果达人榜
     */
    public static final String MIGUO_DAREN_BANG = ServerUrl.getAppH5Url() + "page/topuser";

    /**
     * 代言红店榜
     */
    public static final String DAIYAN_HONGDIAN_BANG = ServerUrl.getAppH5Url() + "page/topshop";

    /**
     * 买单佣金榜
     */
    public static final String MAIDAN_YONGJIN_BANG = ServerUrl.getAppH5Url() + "page/help/type/brokerage";

    /**
     * 红包优惠券
     */
    public static final String HONG_BAO_YOU_HUI_QUAN = ServerUrl.getAppH5Url() + "page/help/type/redpacket";


}

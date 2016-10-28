package com.fanwe.base;

import java.util.HashMap;

/**
 *错误码通用处理。
 * Created by Administrator on 2016/9/19.
 */
public class ErrorCodeParse {

    public static final HashMap<String, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put("010", "出错啦!");
        errorCodeMap.put("099", "出错啦!");
        errorCodeMap.put("100", "出错啦!");
        errorCodeMap.put("200", "您的操作已成功");
        errorCodeMap.put("210", "您已登录成功！");
        errorCodeMap.put("211", "您已注册成功！");
        errorCodeMap.put("212", "您的密码修改成功");
        errorCodeMap.put("220", "您已成功授权");
        errorCodeMap.put("230", "订单已生成！");
        errorCodeMap.put("240", "您的回答正确");
        errorCodeMap.put("300", "您的操作失败，请重试");
        errorCodeMap.put("301", "验证码输入错误，请重试");
        errorCodeMap.put("302", "出错啦");
        errorCodeMap.put("303", "操作频繁，请稍后再试");
        errorCodeMap.put("304", "出错啦");
        errorCodeMap.put("305", "验证码已失效，请刷新");
        errorCodeMap.put("307", "代言名额已满");
        errorCodeMap.put("308", "升级即可代言更多商家");
        errorCodeMap.put("309", "您的积分不足哦");
        errorCodeMap.put("310", "用户名或密码错误");
        errorCodeMap.put("311", "该用户已存在");
        errorCodeMap.put("312", "密码修改失败");
        errorCodeMap.put("313", "您的账号已锁定");
        errorCodeMap.put("314", "您的权限不足");
        errorCodeMap.put("315", "手机事情已被注册");
        errorCodeMap.put("320", "请登录账号");
        errorCodeMap.put("321", "请登录账号");
        errorCodeMap.put("322", "认证失败");
        errorCodeMap.put("329", "二维码已失效");
        errorCodeMap.put("330", "恭喜，您已支付成功");
        errorCodeMap.put("331", "未支付订单过多，请处理");
        errorCodeMap.put("332", "该订单无效");
        errorCodeMap.put("333", "对不起，支付未完成");
        errorCodeMap.put("334", "您已评价");
        errorCodeMap.put("335", "请消费后再评价");
        errorCodeMap.put("340", "请设置安全问题");
        errorCodeMap.put("341", "您的回答有误");
        errorCodeMap.put("351", "请在对应的商店使用");
        errorCodeMap.put("352", "此券已使用");
        errorCodeMap.put("353", "此券已过期");
        errorCodeMap.put("354", "团购还在路上~");
        errorCodeMap.put("355", "团购还在路上~");
        errorCodeMap.put("361", "很抱歉，只能抢一个哦");
        errorCodeMap.put("362", "抢完啦，你来迟一步");
        errorCodeMap.put("362", "抢完啦，你来迟一步");

        errorCodeMap.put("363", "很遗憾，你没有抢到红包");

        errorCodeMap.put("364", "直播房间已关闭");

        errorCodeMap.put("365", "该用户还不是主播");

        errorCodeMap.put("366", "该房间不存在");
        errorCodeMap.put("367", "该直播已结束");
        errorCodeMap.put("400", "出错啦");
        errorCodeMap.put("401", "出错啦");
        errorCodeMap.put("402", "来晚一步，商品下架啦");
        errorCodeMap.put("403", "请去手机客户端购买");
        errorCodeMap.put("404", "对不起，超出购买限制");
        errorCodeMap.put("405", "库存不足");
        errorCodeMap.put("406", "您的等级不足，无法购买");
        errorCodeMap.put("500", "出错啦");
        errorCodeMap.put("501", "出错啦");
        errorCodeMap.put("502", "请求已超时");
        errorCodeMap.put("503", "出错啦");
        errorCodeMap.put("504", "出错啦");
        errorCodeMap.put("505", "访问失败");
        errorCodeMap.put("510", "您的操作太频繁");
        errorCodeMap.put("511", "对不起，无法操作");





    }

    public static HashMap<String, String> getErrorCodeMap() {
        return errorCodeMap;
    }
}

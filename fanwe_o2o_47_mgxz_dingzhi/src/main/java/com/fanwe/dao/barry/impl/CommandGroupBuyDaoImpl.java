package com.fanwe.dao.barry.impl;

import com.fanwe.app.App;
import com.fanwe.dao.barry.CommandGroupBuyDao;
import com.fanwe.dao.barry.view.CommandGroupBuyView;
import com.fanwe.model.CommandGroupBuyBean;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;

import java.util.TreeMap;

/**
 * Created by 狗蛋哥/zlh on 2016/9/23.
 * 获取首页团购列表接口实现
 */
public class CommandGroupBuyDaoImpl implements CommandGroupBuyDao{

    CommandGroupBuyView commandGroupBuyView;

    public CommandGroupBuyDaoImpl(CommandGroupBuyView commandGroupBuyView) {
        this.commandGroupBuyView = commandGroupBuyView;
    }

    @Override
    public void getCommandGroupBuyDaoList(final int page, int page_size, String tag,String keyword, String city) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(page_size));
        params.put("tag", tag);
        params.put("keyword", keyword);
        params.put("city", city);
        params.put("tag", tag);
        params.put("method", LiveConstants.COMMAND_GROUP_BUY);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                CommandGroupBuyBean bean = new Gson().fromJson(responseBody, CommandGroupBuyBean.class);
                CommandGroupBuyBean.Result result = bean.getResult().get(0);
                /**
                 * 刷新
                 */
                if(page == 1){
                    commandGroupBuyView.getCommandGroupBuyDaoListSuccess(result);
                }else {
                    commandGroupBuyView.getCommandGroupBuyDaoListLoadMore(result);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                commandGroupBuyView.getCommandGroupBuyDaoListError(message);
            }

            @Override
            public void onFinish() {
                commandGroupBuyView.getCommandGroupBuyDaoListError("");
            }
        });
    }

}

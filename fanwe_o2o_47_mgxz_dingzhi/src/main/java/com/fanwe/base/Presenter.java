package com.fanwe.base;

/*
 * 页面展示逻辑基类
 */
public abstract class Presenter {
    /**
     * 判断BODY对象是否存在。
     * @param root
     * @return
     */
    public Object validateBody(Root<Object> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0)
        {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;

    }
    //销去持有外部的mContext;
    public abstract void onDestory();
}

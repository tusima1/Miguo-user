package com.miguo.view;



import com.miguo.entity.ModelFeaturedGroupBuy;

import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 */
public interface FeaturedGrouponView extends BaseView{

    void getFeaturedGrouponSuccess(List<ModelFeaturedGroupBuy> list);
    void getFeaturedGrouponLoadmoreSuccess(List<ModelFeaturedGroupBuy> list);
    void getFeaturedGrouponError(String message);

}

package com.miguo.factory;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.entity.SearchCateConditionBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zlh on 2017/1/11.
 */

public class SearchCateConditionFactory {

    static HashMap<Integer, SearchCateConditionBean.ResultBean.BodyBean>    conditions;
    static final int USER_LOGIN_CONDITION           =   0x00010;
    static final int WITHOUT_USER_LOGIN_CONDITION   =   0x00020;
    static final String COLLECT_ID                  = "collect";

    static {
        conditions = new HashMap<>();
    }

    public static SearchCateConditionBean.ResultBean.BodyBean get(){
        if(conditions.size() == 0){
            return null;
        }
        return conditions.get(isUserLogin() ? USER_LOGIN_CONDITION : WITHOUT_USER_LOGIN_CONDITION);
    }

    public static void update(SearchCateConditionBean.ResultBean.BodyBean condition){
        conditions.clear();
        SearchCateConditionBean.ResultBean.BodyBean withoutUserLoginCondition = condition.clone();
        conditions.put(USER_LOGIN_CONDITION, condition);
        conditions.put(WITHOUT_USER_LOGIN_CONDITION, getWithoutUserLoginCondition(withoutUserLoginCondition));
    }

    private static SearchCateConditionBean.ResultBean.BodyBean getWithoutUserLoginCondition(SearchCateConditionBean.ResultBean.BodyBean condition){
        if(null == condition){
            return condition;
        }
        if(!SDCollectionUtil.isEmpty(condition.getCategoryList())){
            int categoryCount = condition.getCategoryList().size();
            for(int i = 0; i < categoryCount; i++){
                if(isCollect(condition.getCategoryList().get(i))){
                    condition.getCategoryList().remove(i);
                    break;
                }
            }
        }
        if(!SDCollectionUtil.isEmpty(condition.getFilterList1())){
            int filterCount = condition.getFilterList1().size();
            for(int i = 0; i < filterCount; i++){
                if(isCollect(condition.getFilterList1().get(i).getFilterList2())){
                    break;
                }
            }
        }
        return condition;
    }

    private static boolean isCollect(SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean category){
        return category.getId().equals(COLLECT_ID);
    }

    private static boolean isCollect(List<SearchCateConditionBean.ResultBean.BodyBean.FilterList1Bean.FilterList2Bean> filters){
        for(SearchCateConditionBean.ResultBean.BodyBean.FilterList1Bean.FilterList2Bean filter:filters){
            if(isCollect(filter)){
                filters.remove(filter);
                return true;
            }
        }
        return false;
    }

    private static boolean isCollect(SearchCateConditionBean.ResultBean.BodyBean.FilterList1Bean.FilterList2Bean filter){
        return filter.getKey().equals(COLLECT_ID);
    }

    private static boolean isUserLogin(){
        return !TextUtils.isEmpty(App.getInstance().getToken());
    }
}

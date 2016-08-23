package com.fanwe.shoppingcart.model;

import com.fanwe.dao.JsonDbModelDaoX;
import com.fanwe.utils.SDFormatUtil;

import java.util.List;

/**
 * 本地购物车逻辑。
 * Created by Administrator on 2016/8/22.
 */
public class LocalShoppingcartDao {

    public static boolean insertModel(List<ShoppingCartInfo> model)
    {
        return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model, true);
    }

    /**
     * 增加单个商品。
     * @param model
     * @return
     */
    public static boolean insertModel(ShoppingCartInfo model)
    {
        List<ShoppingCartInfo> cartInfos = queryModel();
        if(cartInfos!=null){
            if(cartInfos.contains(model)) {
                int number = SDFormatUtil.stringToInteger(model.getNumber());
                number++;
                model.setNumber(number + "");
            }else{
                cartInfos.add(model);
            }
        }
        return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model, true);
    }
    public static List<ShoppingCartInfo> queryModel()
    {
        return JsonDbModelDaoX.getInstance().queryJsonDbModel(List.class, true);
    }

    public static void deleteAllModel()
    {
        JsonDbModelDaoX.getInstance().deleteJsonDbModel(List.class);
    }
}

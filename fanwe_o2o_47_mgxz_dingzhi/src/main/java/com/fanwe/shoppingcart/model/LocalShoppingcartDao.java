package com.fanwe.shoppingcart.model;

import android.text.TextUtils;

import com.fanwe.dao.JsonDbModelDaoX;
import com.fanwe.utils.SDFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地购物车逻辑。
 * Created by Administrator on 2016/8/22.
 */
public class LocalShoppingcartDao {

    public static boolean insertModel(List<ShoppingCartInfo> model)
    {
      return  JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbListModel(model,ShoppingCartInfo.class,true);
    }
    /**
     * 增加一定数量的商品。
     * @param model
     * @param  num  增加的商品数量。
     * @return
     */
    public static boolean insertModel(ShoppingCartInfo model,int num)
    {
        List<ShoppingCartInfo> cartInfos = queryModel();
        boolean exist =false;
        if(cartInfos!=null){
            //判断是否已经存在同一个商品，如果存在的话数量+1
            for(int i =0; i <cartInfos.size() ; i++){
                ShoppingCartInfo shoppingCartInfo = cartInfos.get(i);

                if(shoppingCartInfo.getId() != null && shoppingCartInfo.getId().equals(model.getId())){
                    //原来数据
                    int number = Integer.valueOf(shoppingCartInfo.getNumber());

                    number +=num;
                    cartInfos.remove(i);
                    model.setNumber(number+"");
                    cartInfos.add(i,model);
                    exist = true;
                    break;
                }
            }
            if(!exist){
                cartInfos.add(model);
            }

        }else{
            cartInfos = new ArrayList<ShoppingCartInfo>();
            cartInfos.add(model);
        }
        return insertModel(cartInfos);
    }
    public static boolean insertModel(ShoppingCartInfo model)
    {
        int num = 1;
        if(!TextUtils.isEmpty(model.getNumber())){
            num = Integer.valueOf(model.getNumber());
        }
       return  insertModel(model,num);
    }

    /**
     * 增加单个 商品。
     * @param model
     * @return
     */
    public  static boolean insertSingleNum(ShoppingCartInfo model){
      return   insertModel(model,1);
    }


    public static List<ShoppingCartInfo> queryModel()
    {
        return JsonDbModelDaoX.getInstance().queryJsonDbListModel(ShoppingCartInfo.class, true);
    }

    public static void deleteAllModel()
    {
        JsonDbModelDaoX.getInstance().deleteJsonDbListModel(ShoppingCartInfo.class);

    }

    public static void deleteModel(ShoppingCartInfo model)
    {
        if(model==null){
            return;
        }
        List<ShoppingCartInfo> cartInfos = queryModel();
        for(int i = 0 ; i <cartInfos.size() ; i++){
            ShoppingCartInfo shoppingCartInfo = cartInfos.get(i);
            if(shoppingCartInfo!=null) {
                if (shoppingCartInfo.getId().equals(model.getId())) {
                    cartInfos.remove(i);
                    System.out.println(cartInfos.size());
                    break;
                }
            }
        }
        insertModel(cartInfos);
    }


}

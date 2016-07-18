package com.fanwe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fanwe.library.utils.SDCollectionUtil;

public class Cart_indexActModel extends BaseActModel
{

	private int is_score; // 0:普通商品 1:积分商品
	private int has_mobile; // 是否有手机号 0无 1有
	private CartTotal_dataModel total_data;
	private Map<String, CartGoodsModel> cart_list;
	//我的 小米
	private List<Member> totalList;

	// 定制，猜你喜欢
	private List<GoodsModel>deal_list;
	
	

	
	
	
	public List<GoodsModel> getDeal_list() {
		return deal_list;
	}

	public void setDeal_list(List<GoodsModel> deal_list) {
		this.deal_list = deal_list;
	}

	// add
	private List<CartGoodsModel> listCartGoods;

	public int getCartNumber()
	{
		int number = 0;
		if (!SDCollectionUtil.isEmpty(listCartGoods))
		{
			for (CartGoodsModel model : listCartGoods)
			{
				number += model.getNumber();
			}
		}
		return number;
	}

	public List<CartGoodsModel> getListCartGoods()
	{
		return listCartGoods;
	}

	public void setListCartGoods(List<CartGoodsModel> listCartGoods)
	{
		this.listCartGoods = listCartGoods;
	}

	public Map<String, CartGoodsModel> getCart_list()
	{
		return cart_list;
	}
	public List<Member> getTotalList()
	{
		return totalList;
	}
	public void setTotalList(List<Member> list)
	{
		this.totalList=list;
	}

	public void setCart_list(Map<String, CartGoodsModel> cart_list)
	{
		this.cart_list = cart_list;
		if (cart_list != null)
		{
			List<CartGoodsModel> listModel = new ArrayList<CartGoodsModel>();
			for (Entry<String, CartGoodsModel> item : cart_list.entrySet())
			{
				listModel.add(item.getValue());
			}
			setListCartGoods(listModel);
		} else
		{
			setListCartGoods(null);
		}
	}

	

	public int getIs_score()
	{
		return is_score;
	}

	public void setIs_score(int is_score)
	{
		this.is_score = is_score;
	}

	public int getHas_mobile()
	{
		return has_mobile;
	}

	public void setHas_mobile(int has_mobile)
	{
		this.has_mobile = has_mobile;
	}

	public CartTotal_dataModel getTotal_data()
	{
		return total_data;
	}

	public void setTotal_data(CartTotal_dataModel total_data)
	{
		this.total_data = total_data;
	}

}

package com.fanwe.model;

import java.util.List;

import android.text.TextUtils;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class Uc_fx_deal_fxActModel extends BaseActModel
{

	private List<DistributionMarketCateModel> cate_list;
	private List<Supplier_fx>list;
	
	private PageModel page;

	
	public List<Supplier_fx> getList() {
		return list;
	}

	public void setList(List<Supplier_fx> list) {
		this.list = list;
	}

	public List<DistributionMarketCateModel> getCate_list()
	{
		return cate_list;
	}

	public void setCate_list(List<DistributionMarketCateModel> cate_list)
	{
		this.cate_list = cate_list;
		if (!SDCollectionUtil.isEmpty(cate_list))
		{
			DistributionMarketCateModel model = cate_list.get(0);
			String image = model.getIcon_img();
			if (TextUtils.isEmpty(image))
			{
				image = Scheme.DRAWABLE.wrap(String.valueOf(R.drawable.ic_distribution_market_cate_all));
			}
			model.setIcon_img(image);
		}
	}

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}


}

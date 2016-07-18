package com.fanwe.model;

import java.util.List;

public class Uc_fxwithdraw_indexActModel extends BaseActModel
{

	private String bank_info;
	private String bank_name;
	private String h_tel;
	private String balance;
	private List<DistributionWithdrawLogModel> result;
	private PageModel page;

	
	public String getBank_info() {
		return bank_info;
	}

	public void setBank_info(String bank_info) {
		this.bank_info = bank_info;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getH_tel() {
		return h_tel;
	}

	public void setH_tel(String h_tel) {
		this.h_tel = h_tel;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setResult(List<DistributionWithdrawLogModel> result) {
		this.result = result;
	}

	public List<DistributionWithdrawLogModel> getResult()
	{
		return result;
	}

	public void setList(List<DistributionWithdrawLogModel> result)
	{
		this.result = result;
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

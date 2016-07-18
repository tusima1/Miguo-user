package com.fanwe.model;

import java.util.List;

public class Uc_money_Zijin_LogActModel extends BaseActModel{
	private PageModel page;
	private List<ZijinLogModel> result;
	private String salary;
	private String blocksalary;
	
	public String getBlocksalary() {
		return blocksalary;
	}
	public void setBlocksalary(String blocksalary) {
		this.blocksalary = blocksalary;
	}
	public List<ZijinLogModel> getResult() {
		return result;
	}
	public void setResult(List<ZijinLogModel> result) {
		this.result = result;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public PageModel getPage() {
		return page;
	}
	public void setPage(PageModel page) {
		this.page = page;
	}
}

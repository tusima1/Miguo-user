package com.fanwe.model;

/**
 * 支付方式实体
 * 
 * @author js02
 * 
 */
public class Payment_listModel
{

	private int id;
	private String code;
	private String name;
	private String logo;
	
	// add
	private boolean isSelected;

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public String getLogo()
	{
		return logo;
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() {
		return "Payment_listModel [id=" + id + ", code=" + code + ", name="
				+ name + ", logo=" + logo + ", isSelected=" + isSelected + "]";
	}

}

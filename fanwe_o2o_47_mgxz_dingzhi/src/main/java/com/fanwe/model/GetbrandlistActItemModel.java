package com.fanwe.model;

import com.fanwe.library.utils.SDTypeParseUtil;

public class GetbrandlistActItemModel
{

	private String id = null;
	private String name = null;
	private String dy_count = null;
	private String logo = null;
	private String is_checked = null;

	private boolean is_checked_format_boolean = false;

	public boolean isIs_checked_format_boolean()
	{
		return is_checked_format_boolean;
	}

	public void setIs_checked_format_boolean(boolean is_checked_format_boolean)
	{
		this.is_checked_format_boolean = is_checked_format_boolean;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDy_count()
	{
		return dy_count;
	}

	public void setDy_count(String dy_count)
	{
		this.dy_count = dy_count;
	}

	public String getLogo()
	{
		return logo;
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	public String getIs_checked()
	{
		return is_checked;
	}

	public void setIs_checked(String is_checked)
	{
		this.is_checked = is_checked;
		int checked = SDTypeParseUtil.getInt(is_checked, 0);
		if (checked == 0)
		{
			setIs_checked_format_boolean(false);
		} else if (checked == 1)
		{
			setIs_checked_format_boolean(true);
		}
	}

}

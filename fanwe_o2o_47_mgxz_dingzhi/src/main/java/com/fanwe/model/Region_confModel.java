package com.fanwe.model;

import com.lidroid.xutils.db.annotation.Id;

public class Region_confModel
{
	@Id
	private int _id;
	private int id;
	private int pid;
	private String name;
	private String region_level;

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRegion_level()
	{
		return region_level;
	}

	public void setRegion_level(String region_level)
	{
		this.region_level = region_level;
	}

	@Override
	public String toString()
	{
		return getName();
	}

}

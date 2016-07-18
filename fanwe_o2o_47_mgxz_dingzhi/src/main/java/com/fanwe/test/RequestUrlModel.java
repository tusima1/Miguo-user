package com.fanwe.test;

import com.lidroid.xutils.db.annotation.Id;

public class RequestUrlModel
{

	@Id
	private int id;
	private String url;
	private String act;
	private String ctl;
	private String name;
	private long time = System.currentTimeMillis();

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getAct()
	{
		return act;
	}

	public void setAct(String act)
	{
		this.act = act;
	}

	public String getCtl()
	{
		return ctl;
	}

	public void setCtl(String ctl)
	{
		this.ctl = ctl;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		return ctl + "(" + act + ")" + ":" + url;
	}

}

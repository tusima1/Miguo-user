package com.fanwe.test;

import com.lidroid.xutils.db.annotation.Id;

public class AvailableUrlModel
{
	@Id
	private int id;
	private String url;
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
		return url;
	}

}

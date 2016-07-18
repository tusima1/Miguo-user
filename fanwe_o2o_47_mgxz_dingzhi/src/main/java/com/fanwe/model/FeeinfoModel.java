package com.fanwe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FeeinfoModel implements Parcelable
{

	private String name;
	private String value;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{	
		this.value = value;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(value);
		
	}
	
	
	 public static final Parcelable.Creator<FeeinfoModel> CREATOR 
     = new Parcelable.Creator<FeeinfoModel>()
     {
	 // From Parcelable.Creator
	 @Override
	 public FeeinfoModel createFromParcel(Parcel in)
	 {
		 FeeinfoModel brief = new FeeinfoModel();
	     
	     // 从包裹中读出数据
		 brief.name = in.readString();
	     brief.value = in.readString();
	     
	     
	     
	     
	     return brief;
	 }
	
	
	
	 
	 @Override
	 public FeeinfoModel[] newArray(int size)
	 {
	     return new FeeinfoModel[size];
	 }
	};

}

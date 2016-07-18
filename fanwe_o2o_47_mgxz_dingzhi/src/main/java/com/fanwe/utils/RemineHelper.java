package com.fanwe.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RemineHelper extends SQLiteOpenHelper
{

	private static final int DB_VERSION = 1;

	public RemineHelper(Context context)
	{
		super(context, RemineContance.DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(RemineContance.CREAT_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}

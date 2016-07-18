package com.fanwe.utils;

import android.database.sqlite.SQLiteDatabase;

public class RemineContance
{
	public static final String IMPORT_DB_NAME = "remine_db";
	public static final String IMPORT_DB_TABLENAME = "remine";
	public static final String TAG_INFO = "import_db";
	public static final String DB_NAME = "xiaomi.db";
	public static final String HISTORY_TABLENAME = "xiaomi";
	public static final String CREAT_HISTORY = "create table " + HISTORY_TABLENAME
			+ "(_id integer primary key autoincrement, remine_id text not null, c_time text not null)";

	
	public static final SQLiteDatabase SQLITE = SQLiteDatabase
			.openOrCreateDatabase
			(
					"data/data/com.example.activity/files/remine_db", null);

}

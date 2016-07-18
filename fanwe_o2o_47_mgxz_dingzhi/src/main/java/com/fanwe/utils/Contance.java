package com.fanwe.utils;

import android.database.sqlite.SQLiteDatabase;


public class Contance {
	public static final String IMPORT_DB_NAME = "jingdian_db";
	public static final String IMPORT_DB_TABLENAME = "jingdian";
	public static final String TAG_INFO = "import_db";
	public static final String DB_NAME = "shiyong.db";
	public static final String HISTORY_TABLENAME = "history";
	public static final String CREAT_HISTORY = "create table " + HISTORY_TABLENAME
			+ "(_id integer primary key autoincrement, h_name text not null)";

	
	public static final SQLiteDatabase SQLITE = SQLiteDatabase
			.openOrCreateDatabase(
					"data/data/com.example.activity/files/jingdian_db", null);
}

package com.feicui.news.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {
	private static int version = 3;

	public Dbhelper(Context context, String name) {
		super(context, name, null, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table label(subid int,subgroup varchar(20))");
		db.execSQL("create table News(nid INTEGER PRIMARY KEY ,type INTEGER,"
				+ "stamp varchar(10),icon varchar(20),title varchar(80), summary varchar(80),link varchar(40))");
		db.execSQL("create table collect(nid INTEGER,flag int)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		

	}

}

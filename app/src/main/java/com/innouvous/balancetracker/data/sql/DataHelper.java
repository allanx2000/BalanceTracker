package com.innouvous.balancetracker.data.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Balance.db";
    public static final int DATABASE_VERSION = 1;

    public DataHelper(Context cxt) {
        super(cxt, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void createTables(SQLiteDatabase database) {
        database.execSQL(SQLScripts.SQL_CREATE_BALANCES);
    }

    public void onCreate(SQLiteDatabase database) {
        createTables(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

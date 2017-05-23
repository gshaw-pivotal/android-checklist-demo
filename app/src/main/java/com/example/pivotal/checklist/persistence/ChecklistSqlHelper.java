package com.example.pivotal.checklist.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChecklistSqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CHECKLIST_DATABASE";

    public ChecklistSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ChecklistTable.getCreateTableQuery());
        db.execSQL(ChecklistNoteTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDatabase(db);
    }

    private void recreateDatabase(SQLiteDatabase db) {
        db.execSQL(ChecklistTable.getDropTableQuery());
        db.execSQL(ChecklistNoteTable.getDropTableQuery());
        onCreate(db);
    }
}

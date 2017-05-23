package com.example.pivotal.checklist.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.pivotal.checklist.models.Checklist;

public class ChecklistTable {

    private static final String CHECKLIST_TABLE_NAME = "checklist";
    private static final String CHECKLIST_TABLE_CREATE;
    private static final String CHECKLIST_TABLE_DROP;
    private static final String[] CHECKLIST_TABLE_COLUMNS;

    static {
        SqlTableStatementBuilder builder = new SqlTableStatementBuilder(CHECKLIST_TABLE_NAME);
        builder.addColumn(Columns.ID, SqlTableStatementBuilder.ColumnType.INT, true, true);
        builder.addColumn(Columns.NAME, SqlTableStatementBuilder.ColumnType.TEXT);

        CHECKLIST_TABLE_CREATE = builder.buildCreateTableStatement();
        CHECKLIST_TABLE_DROP = builder.buildDropTableStatement();
        CHECKLIST_TABLE_COLUMNS = builder.getColumns();
    }

    public static String getTableName() {
        return CHECKLIST_TABLE_NAME;
    }

    public static String getCreateTableQuery() {
        return CHECKLIST_TABLE_CREATE;
    }

    public static String getDropTableQuery() {
        return CHECKLIST_TABLE_DROP;
    }

    public static ContentValues getChecklistContentValues(Checklist checklist) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Columns.NAME, checklist.getName());
        return contentValues;
    }

    public static Checklist getChecklist(Cursor cursor) {
        Checklist checklist = new Checklist();

        checklist.setId(cursor.getInt(cursor.getColumnIndex(Columns.ID)));
        checklist.setName(cursor.getString(cursor.getColumnIndex(Columns.NAME)));

        return checklist;
    }

    public static String[] getColumns() {
        return CHECKLIST_TABLE_COLUMNS;
    }

    public static class Columns {
        public static String ID = "id";
        public static String NAME = "name";
    }
}

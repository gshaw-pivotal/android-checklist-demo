package com.example.pivotal.checklist.persistence;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.pivotal.checklist.models.Checklist;
import com.example.pivotal.checklist.models.ChecklistItem;

public class ChecklistNoteTable {

    private static final String CHECKLIST_NOTE_TABLE_NAME = "checklist_note";
    private static final String CHECKLIST_NOTE_TABLE_CREATE;
    private static final String CHECKLIST_NOTE_TABLE_DROP;
    private static final String[] CHECKLIST_NOTE_TABLE_COLUMNS;

    static {
        SqlTableStatementBuilder builder = new SqlTableStatementBuilder(CHECKLIST_NOTE_TABLE_NAME);
        builder.addColumn(ChecklistNoteTable.Columns.ID, SqlTableStatementBuilder.ColumnType.INT, true, true);
        builder.addColumn(Columns.CHECKLIST_ID, SqlTableStatementBuilder.ColumnType.INT);
        builder.addColumn(ChecklistNoteTable.Columns.NOTE, SqlTableStatementBuilder.ColumnType.TEXT);
        builder.addColumn(ChecklistNoteTable.Columns.CHECKED, SqlTableStatementBuilder.ColumnType.INT);

        CHECKLIST_NOTE_TABLE_CREATE = builder.buildCreateTableStatement();
        CHECKLIST_NOTE_TABLE_DROP = builder.buildDropTableStatement();
        CHECKLIST_NOTE_TABLE_COLUMNS = builder.getColumns();
    }

    public static String getTableName() {
        return CHECKLIST_NOTE_TABLE_NAME;
    }

    public static String getCreateTableQuery() {
        return CHECKLIST_NOTE_TABLE_CREATE;
    }

    public static String getDropTableQuery() {
        return CHECKLIST_NOTE_TABLE_DROP;
    }

    public static ContentValues getChecklistNoteContentValues(Checklist checklist, ChecklistItem checklistItem) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Columns.CHECKLIST_ID, checklist.getId());
        contentValues.put(Columns.NOTE, checklistItem.getNote());
        contentValues.put(Columns.CHECKED, checklistItem.isChecked() ? 1 : 0);
        return contentValues;
    }

    public static ChecklistItem getChecklistItem(Cursor cursor) {
        ChecklistItem checklistItem = new ChecklistItem();

        checklistItem.setId(cursor.getInt(cursor.getColumnIndex(Columns.ID)));
        checklistItem.setChecklistId(cursor.getInt(cursor.getColumnIndex(Columns.CHECKLIST_ID)));
        checklistItem.setNote(cursor.getString(cursor.getColumnIndex(Columns.NOTE)));
        checklistItem.setChecked(cursor.getInt(cursor.getColumnIndex(Columns.CHECKED)) == 0 ? false : true);

        return checklistItem;
    }

    public static String[] getColumns() {
        return CHECKLIST_NOTE_TABLE_COLUMNS;
    }

    public static class Columns {
        public static String ID = "id";
        public static String CHECKLIST_ID = "checklist_id";
        public static String NOTE = "note";
        public static String CHECKED = "checked";
    }
}
